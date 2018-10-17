package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.EqualsHashcode;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestEqualHashcode {

	@Test
	public void testEqualHashcode() {
		Assert.assertTrue(new EqualsHashcode().check(new File("filesToParse/EqualHashcode")));
	}

}
