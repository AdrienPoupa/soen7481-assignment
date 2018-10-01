package ca.concordia.soen7481.assignment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UT {

	@Before
	public void setUp() throws Exception {
		MyAnalysis.init_setup();
	}

	@Test
	public void test_equal_hashcode() {
		assertTrue(MyAnalysis.check_for_equal_hashcode());
	}
	
	@Test
	public void test_string_comparison() {
		assertTrue(MyAnalysis.check_string_comparison());
	}

}
