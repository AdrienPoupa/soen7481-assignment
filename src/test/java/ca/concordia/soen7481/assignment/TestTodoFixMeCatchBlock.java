package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.TodoFixMeCatchBlockChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestTodoFixMeCatchBlock {

	@Test
	public void testTodoFixMeCatchBlockNoComment() {
		List<BugPattern> bugPatterns = new TodoFixMeCatchBlockChecker().check(new File("filesToParse/TodoFixMeCatchBlock/TodoFixMeCatchBlockNoComment.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testTodoFixMeCatchBlockComment() {
		List<BugPattern> bugPatterns = new TodoFixMeCatchBlockChecker().check(new File("filesToParse/TodoFixMeCatchBlock/TodoFixMeCatchBlockComment.java"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("TodoFixMeCatchBlockComment.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("testCatchBlock", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(10, bugPatterns.get(0).getLine());
	}

}
