package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.OvercatchExceptionTerminationChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestOvercatchExceptionTermination {

	@Test
	public void testOvercatchExceptionTerminationNoExit() {
		List<BugPattern> bugPatterns = new OvercatchExceptionTerminationChecker().check(
				new File("filesToParse/OvercatchExceptionTermination/OvercatchExceptionTerminationNoExit.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testOvercatchExceptionTerminationExit() {
		List<BugPattern> bugPatterns = new OvercatchExceptionTerminationChecker().check(
				new File("filesToParse/OvercatchExceptionTermination/OvercatchExceptionTerminationExit.java"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("OvercatchExceptionTerminationExit.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("testTermination", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(10, bugPatterns.get(0).getLine());
	}

}
