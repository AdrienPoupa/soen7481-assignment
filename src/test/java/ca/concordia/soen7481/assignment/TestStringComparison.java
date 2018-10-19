package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.StringComparisonChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestStringComparison {

	@Test
	public void testStringEqualsComparison() {
		List<BugPattern> bugPatterns = new StringComparisonChecker().check(new File("filesToParse/StringComparison/StringEqualsComparison.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}
	
	@Test
	public void testStringEqualComparison() {
		List<BugPattern> bugPatterns = new StringComparisonChecker().check(new File("filesToParse/StringComparison/StringEqualComparison.java"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("StringEqualComparison.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("compareString", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(4, bugPatterns.get(0).getLine());
	}

}
