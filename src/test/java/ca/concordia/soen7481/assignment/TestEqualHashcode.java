package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.EqualsHashcodeChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestEqualHashcode {

	@Test
	public void testEqualWithHashcode() {
        List<BugPattern> bugPatterns = new EqualsHashcodeChecker().check(new File("filesToParse/EqualsHashcode/EqualsWithHashcode.java"));

		Assert.assertEquals(0, bugPatterns.size());
	}

	@Test
	public void testEqualWithoutHashcode() {
		List<BugPattern> bugPatterns = new EqualsHashcodeChecker().check(new File("filesToParse/EqualsHashcode/EqualsWithoutHashcode.java"));

		Assert.assertEquals(1, bugPatterns.size());
		Assert.assertEquals("EqualsWithoutHashcode.java", bugPatterns.get(0).getFilename());
		Assert.assertEquals("equals", bugPatterns.get(0).getFunctionName());
		Assert.assertEquals(2, bugPatterns.get(0).getLine());
	}

}
