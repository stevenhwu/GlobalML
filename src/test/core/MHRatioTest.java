package test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bmde.core.MHRatio;

public class MHRatioTest {

	@Test
	public void testAcceptDoubleDoubleDoubleDouble() {
		for (int i = 0; i < 1000; i++) {
			assertTrue(MHRatio.accept(0, 0, 0, 1));
		}

		for (int i = 0; i < 1000; i++) {
			assertFalse(MHRatio.accept(0, 0, 0, -1000));
		}
		boolean[] count = new boolean[1000];
		double half = Math.log(0.5);
		for (int i = 0; i < 1000; i++) {
			count[i]=MHRatio.accept(0, 0, 0, half);
		}
		int T = 0;
		int F = 0;
		for (int i = 0; i < count.length; i++) {
			int z = count[i] ? T++: F++;
		}
		assertEquals("T1: "+T+" and "+F, T, F, 50);
		
		double halfhalf = half/2;
		double expected = Math.log(0.5);
			
//		assertEquals("T2: ", MHRatio.calculateRatio( halfhalf, 0, 0, halfhalf), expected, 0);

		
		
	}

}
