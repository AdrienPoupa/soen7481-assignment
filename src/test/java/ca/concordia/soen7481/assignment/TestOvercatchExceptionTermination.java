package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.OvercatchExceptionTerminationChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestOvercatchExceptionTermination {

	@Test
	public void testOvercatchExceptionTermination() {
		List<BugPattern> bugPatterns = new OvercatchExceptionTerminationChecker().check(
				new File("filesToParse/OvercatchExceptionTerminationBugPattern/OvercatchExceptionTerminationBugPattern.java"));
	}

}
