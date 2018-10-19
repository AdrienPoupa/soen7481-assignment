package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.EqualsHashcodeChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestEqualHashcode {

	@Test
	public void testEqualHashcode() {
        List<BugPattern> bugPatterns = new EqualsHashcodeChecker().check(new File("filesToParse/EqualsHashcodeBugPattern/EqualsHashcodeBugPattern.java"));
	}

}
