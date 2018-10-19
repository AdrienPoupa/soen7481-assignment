package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.OpenStreamChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TestOpenStream {

	@Test
	public void testOpenStream() {
		List<BugPattern> bugPatterns = new OpenStreamChecker().check(new File("filesToParse/OpenStreamBugPattern/OpenStreamBugPattern.java"));
	}

}
