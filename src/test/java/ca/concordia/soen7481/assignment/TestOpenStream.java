package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.OpenStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class TestOpenStream {

	@Test
	public void testOpenStream() {
		Assert.assertTrue(new OpenStream().check(new File("filesToParse/OpenStream/OpenStream.java")));
	}

}
