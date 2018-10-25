package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.OpenStreamChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestOpenStream {

	@Test
	public void testOpenStreamClosed() {
		List<BugPattern> bugPatterns = new OpenStreamChecker().check(new File("filesToParse/OpenStream/OpenStreamClosed.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testOpenStreamNotClosed() {
		List<BugPattern> bugPatterns = new OpenStreamChecker().check(new File("filesToParse/OpenStream/OpenStreamNotClosed.java"));

		Assert.assertEquals(3, bugPatterns.size());

		Assert.assertEquals("OpenStreamNotClosed.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("testOpenStream", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(17, bugPatterns.get(0).getLine());

		Assert.assertEquals("OpenStreamNotClosed.java", bugPatterns.get(1).getFilename());
		Assert.assertEquals("testOpenStream", bugPatterns.get(1).getFunctionName());
		Assert.assertEquals(14, bugPatterns.get(1).getLine());

		Assert.assertEquals("OpenStreamNotClosed.java", bugPatterns.get(2).getFilename());
		Assert.assertEquals("testOpenStream", bugPatterns.get(2).getFunctionName());
		Assert.assertEquals(15, bugPatterns.get(2).getLine());
	}

}
