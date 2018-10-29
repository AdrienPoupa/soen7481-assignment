package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.UnneededComputationInLoopsChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestUnneededComputationInLoops {

	@Test
	public void testNeededComputation() {
		List<BugPattern> bugPatterns = new UnneededComputationInLoopsChecker().check(new File("filesToParse/UnneededComputationInLoops/NeededComputation.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}
	
	@Test
	public void testUnneededComputation() {
		List<BugPattern> bugPatterns = new UnneededComputationInLoopsChecker().check(new File("filesToParse/UnneededComputationInLoops/UnneededComputationException.java"));

		Assert.assertEquals(5, bugPatterns.size());
		
		Assert.assertEquals("UnneededComputationException.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("forLoop", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(7, bugPatterns.get(0).getLine());
		
		Assert.assertEquals("UnneededComputationException.java", bugPatterns.get(1).getFilename());
		Assert.assertEquals("foreachLoop", bugPatterns.get(1).getFunctionName());
		Assert.assertEquals(24, bugPatterns.get(1).getLine());
		
		Assert.assertEquals("UnneededComputationException.java", bugPatterns.get(2).getFilename());
		Assert.assertEquals("foreachLoop", bugPatterns.get(2).getFunctionName());
		Assert.assertEquals(25, bugPatterns.get(2).getLine());
		
		Assert.assertEquals("UnneededComputationException.java", bugPatterns.get(3).getFilename());
		Assert.assertEquals("whileLoop", bugPatterns.get(3).getFunctionName());
		Assert.assertEquals(35, bugPatterns.get(3).getLine());
		
		Assert.assertEquals("UnneededComputationException.java", bugPatterns.get(4).getFilename());
		Assert.assertEquals("doWhileLoop", bugPatterns.get(4).getFunctionName());
		Assert.assertEquals(44, bugPatterns.get(4).getLine());
	}
	
}
