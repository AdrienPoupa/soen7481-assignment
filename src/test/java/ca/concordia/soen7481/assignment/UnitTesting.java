package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.EqualsHashcode;
import ca.concordia.soen7481.assignment.checkers.StringComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class UnitTesting {

	@Before
	public void setUp() {
		Main.initSetup();
	}

	@Test
	public void testEqualHashcode() {
		Assert.assertTrue(new EqualsHashcode().check(new File("filesToParse")));
	}
	
	@Test
	public void testStringComparison() {
		Assert.assertTrue(new StringComparison().check(new File("filesToParse")));
	}



}
