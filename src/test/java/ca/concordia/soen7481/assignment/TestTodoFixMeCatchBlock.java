package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.TodoFixMeCatchBlock;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestTodoFixMeCatchBlock {

	@Test
	public void testTodoFixMeCatchBlock() {
		Assert.assertTrue(new TodoFixMeCatchBlock().check(new File("filesToParse/TodoFixMeCatchBlock")));
	}

}
