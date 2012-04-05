package test.prior;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bmde.prior.PriorBeta;

public class PriorBetaTest {

	BetaDistribution bdist;
	double expected;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLogPdfDoubleDoubleDouble()  {
		double alpha = 1;
		double beta = 2;
		bdist = new BetaDistribution(alpha, beta);
		expected = Math.log( bdist.density(0.7) );
		assertEquals("",expected, PriorBeta.logPdf(0.7, alpha, beta),1E-10);
		PriorBeta pb = new PriorBeta(alpha, beta);
		assertEquals("",expected, pb.logPdf(0.7, alpha, beta),1E-10);
	}

	@Test
	public void testPdfDoubleDoubleDouble()  {
		double alpha = 1;
		double beta = 2;
		bdist = new BetaDistribution(alpha, beta);
		expected = bdist.density(0.4);
		assertEquals("",expected, PriorBeta.pdf(0.4, alpha, beta),1E-10);
		PriorBeta pb = new PriorBeta(alpha, beta);
		assertEquals("",expected, pb.pdf(0.4, alpha, beta),1E-10);
	}

}
