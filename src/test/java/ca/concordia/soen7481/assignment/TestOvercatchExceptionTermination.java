package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.OvercatchExceptionTermination;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestOvercatchExceptionTermination {

	@Test
	public void testOvercatchExceptionTermination() {
		Assert.assertTrue(new OvercatchExceptionTermination().check(new File("filesToParse/OvercatchExceptionTermination/OvercatchExceptionTermination.java")));
	}

}
