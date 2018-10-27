package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.IneffectiveConditionChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestIneffectiveCondition {
	@Test
	public void testIneffectiveCondition() {
		List<BugPattern> bugPatterns = new IneffectiveConditionChecker().check(new File("filesToParse/IneffectiveCondition"));

		Assert.assertEquals(8, bugPatterns.size());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(12, bugPatterns.get(0).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(1).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(1).getFunctionName());
		Assert.assertEquals(16, bugPatterns.get(1).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(2).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(2).getFunctionName());
		Assert.assertEquals(20, bugPatterns.get(2).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(3).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(3).getFunctionName());
		Assert.assertEquals(24, bugPatterns.get(3).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(4).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(4).getFunctionName());
		Assert.assertEquals(28, bugPatterns.get(4).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(5).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(5).getFunctionName());
		Assert.assertEquals(32, bugPatterns.get(5).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(6).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(6).getFunctionName());
		Assert.assertEquals(36, bugPatterns.get(6).getLine());
		
		Assert.assertEquals("IneffectiveCondition.java", bugPatterns.get(7).getFilename());
		Assert.assertEquals("methodWithSeveralUneffectiveCondition", bugPatterns.get(7).getFunctionName());
		Assert.assertEquals(40, bugPatterns.get(7).getLine());
	}
}
