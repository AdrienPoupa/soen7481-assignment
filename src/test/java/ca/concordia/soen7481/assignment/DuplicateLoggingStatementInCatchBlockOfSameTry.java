package ca.concordia.soen7481.assignment;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.DuplicateLoggingStatementInCatchBlockOfSameTryChecker;
import ca.concordia.soen7481.assignment.checkers.TodoFixMeCatchBlockChecker;

public class DuplicateLoggingStatementInCatchBlockOfSameTry {

	@Test
	public void testDuplicateLoggingStatementInCatchBlockOfSameTrywithadequateinformation() {
		List<BugPattern> bugPatterns = new DuplicateLoggingStatementInCatchBlockOfSameTryChecker().check(new File("filesToParse/InadequateLoggingInformationInCatchBlocks/AdequateLoggingInformationInCatchBlocks.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testDuplicateLoggingStatementInCatchBlockOfSameTryinadequateinformation() {
		List<BugPattern> bugPatterns = new DuplicateLoggingStatementInCatchBlockOfSameTryChecker().check(new File("filesToParse/InadequateLoggingInformationInCatchBlocks/InadequateLoggingInformationInCatchBlocks.java"));

		Assert.assertEquals(2, bugPatterns.size());
		Assert.assertEquals("InadequateLoggingInformationInCatchBlocks.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("anotherMain", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(17, bugPatterns.get(0).getLine());
		Assert.assertEquals("InadequateLoggingInformationInCatchBlocks.java", bugPatterns.get(1).getFilename());
		Assert.assertEquals("another", bugPatterns.get(1).getFunctionName());
		Assert.assertEquals(26, bugPatterns.get(1).getLine());
	}
}
