package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.UnusedMethodChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestUnusedMethod {

	@Test
	public void testOneUnusedMethod() {
		List<BugPattern> bugPatterns = new UnusedMethodChecker().check(new File("filesToParse/UnusedMethod"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("UnusedMethodTestClass.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("anUnusedMethod", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(7, bugPatterns.get(0).getLine());
	}

}