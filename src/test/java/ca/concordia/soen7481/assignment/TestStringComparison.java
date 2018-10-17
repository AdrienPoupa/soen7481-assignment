package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.StringComparison;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestStringComparison {
	
	@Test
	public void testStringComparison() {
		Assert.assertTrue(new StringComparison().check(new File("filesToParse/StringComparison")));
	}



}
