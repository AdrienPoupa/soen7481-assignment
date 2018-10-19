package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.TodoFixMeCatchBlockChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestTodoFixMeCatchBlock {

	@Test
	public void testTodoFixMeCatchBlock() {
		List<BugPattern> bugPatterns = new TodoFixMeCatchBlockChecker().check(new File("filesToParse/TodoFixMeCatchBlockBugPattern/TodoFixMeCatchBlockBugPattern.java"));
	}

}
