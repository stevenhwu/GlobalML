package test.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bmde.math.ExpUniExpDistribution;
import bmde.math.ExponentialDistribution;
import bmde.math.UniformDistribution;

public class ExpUniExpDistributionTest {

	ExpUniExpDistribution t = new ExpUniExpDistribution();

	@Test
	public void testGetPhi() {
//		fail("Not yet implemented");
		t.setPhi(10);
		assertEquals(10,t.getPhi(),0);
	}
	
	
	@Test
	public void testExpUniExpDistributionDoubleDoubleDoubleDouble() {
//		fail("Not yet implemented");
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(1,2,3,4);
		assertEquals("Down", 1, testClass.getlambdaDown(), 0);
		assertEquals("Up", 2, testClass.getLambdaUp(), 0);
		assertEquals("Phi", 3, testClass.getPhi(), 0);
//		assertEquals("Phi", 3, testClass.phi, 0);
//		assertEquals("Gap", 4, testClass.gapUpper, 0);
//		assertEquals("Gap", -4, testClass.gapLower, 0);
		
	}

	
	
	@Test
	public void testPdf() {
		
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(2,2,1.0/3,2);
		
		double proportionDE = (1 - 1.0/3)/2;
		double expected = proportionDE*ExponentialDistribution.pdf(1, 2);
		assertEquals("Up",expected, testClass.pdf(3), 0);
		assertEquals("Down", expected, testClass.pdf(-3), 0);
		expected = 1.0/3*UniformDistribution.pdf(1,-2,2);
		assertEquals("Same", expected, testClass.pdf(1), 0);
	}

	@Test
	public void testLogPdf() {
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(2,2,1.0/3,2);
		
		double proportionDE = (1 - 1.0/3)/2;
		double expected = Math.log( proportionDE*ExponentialDistribution.pdf(1, 2) );
		assertEquals("Up",expected, testClass.logPdf(3), 0);
		assertEquals("Down", expected, testClass.logPdf(-3), 0);
		expected = Math.log( 1.0/3*UniformDistribution.pdf(1,-2,2) );
		assertEquals("Same", expected, testClass.logPdf(1), 0);
		assertEquals("Static", expected, ExpUniExpDistribution.logPdf(1, 2,2,1.0/3,2),0);
	}

}
