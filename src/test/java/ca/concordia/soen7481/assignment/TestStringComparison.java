package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.StringComparisonChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestStringComparison {
	
	@Test
	public void testStringComparison() {
		List<BugPattern> bugPatterns = new StringComparisonChecker().check(new File("filesToParse/StringComparisonBugPattern/StringComparisonBugPattern.java"));
	}



}
