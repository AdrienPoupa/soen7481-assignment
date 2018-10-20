package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.EmptyCatchClauseChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestEmptyCatch {

	@Test
	public void testEmptyCatchNotEmpty() {
		List<BugPattern> bugPatterns = new EmptyCatchClauseChecker().check(new File("filesToParse/EmptyCatchClause/NotEmptyCatch.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testEmptyCatchEmpty() {
		List<BugPattern> bugPatterns = new EmptyCatchClauseChecker().check(new File("filesToParse/EmptyCatchClause/EmptyCatch.java"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("EmptyCatch.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("testCatchBlock", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(9, bugPatterns.get(0).getLine());
	}

}
