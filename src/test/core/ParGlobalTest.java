package test.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.primitives.Doubles;

import bmde.core.Likelihood;
import bmde.core.par.ParGlobal;
import bmde.core.par.ParSpot;
import bmde.core.par.Spot;
import bmde.math.ExponentialDistribution;
import bmde.math.GammaDistribution;
import bmde.math.InverseGammaDistribution;
import bmde.math.NormalDistribution;
import bmde.math.UniformDistribution;
import bmde.prior.PriorBeta;
import bmde.prior.PriorExp;

public class ParGlobalTest {

	private ParGlobal gp;
	private ParSpot[] sp;
	private Likelihood li;
	private double expected;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}



	@Before
	public void setUp() throws Exception {
		int n = 10;
		gp = new ParGlobal(n, -10);
		li = new Likelihood(n, -10);
		ArrayList<Spot> allSpot = Spot.generateList(n, 6);

		sp = ParSpot.init(-10, allSpot, gp, li);
//		gp.setDefaultPrior();

		// li.init();
	}

	@Test
	public void testSetAllParGlobal() {

		double[] setup = {0.1, 5.1, 1.1, 2.1, 0.3, 1.1, 2.1, -1.1, 1.1};
		gp.setAllGlobalPar(0.1, 5.1, 1.1, 2.1, 0.3, 1.1, 2.1, -1.1, 1.1);
		
		assertEquals(0.1, gp.getMeanMuAlpha(), 0);
		assertEquals(5.1, gp.getMeanSdAlpha(), 0);
		assertEquals(1.1, gp.getLambda(), 0);
		assertEquals(0.3, gp.getPhi(), 0);
		assertEquals(1.1, gp.getPiMuAlpha(), 0);
		assertEquals(2.1, gp.getPiSdAlpha(), 0);
		assertEquals(-1.1, gp.getRhoMuAlpha(), 0);
		assertEquals(1.1, gp.getRhoSdAlpha(), 0);
		
	}

	@Test
	public void testCalcLikeli() {
		// System.out.println(gp.toString());
		gp.initCalcLikeli(sp, li);
		// System.out.println("after init");
		// System.out.println(li.getGlikelihood());
		// System.out.println(li.getParamLikelihood("GMEDU"));

	}

	@Test
	public void testSetPriorMuSetPriorSd() {
		double[] par = new double[9];
		Arrays.fill(par, 0.0);
		par[1] = 1;
		gp.setAllGlobalPar(par);
		
		gp.setPriorMu(2, 2);
		gp.setPriorSd(0.1, 0.1, 100);

		gp.initCalcLikeli(sp, li);
		expected = NormalDistribution.logPdf(0, 2, 2)
				+ InverseGammaDistribution.logPdf(1, 0.1, 0.1);

		assertEquals("prior mean", expected, li.getPriorProb("GMUSD"), 0);
	}

	@Test
	public void testSetPriorExpSetPriorPhi() {
		gp.setPriorExp(1);
		gp.setPriorPhi(0.2, 0.8);
		
		
		double[] par = new double[9];
		Arrays.fill(par, 0.0);
		par[2] = 1.5;
		par[4] = 0.4;
		gp.setAllGlobalPar(par);
		

		gp.initCalcLikeli(sp, li);

		expected = ExponentialDistribution.logPdf(1.5, 1)
//				+ GammaDistribution.logPdf(2.5, 0.1, 1)
				+ PriorBeta.logPdf(0.4, 0.2, 0.8);
		assertEquals("prior delta", expected, li.getPriorProb("GDELTA"), 0);
	}

	@Test
	public void testSetPriorProbMuSetPriorProbSd() {
		gp.setPriorProbMu(1, 2);
		gp.setPriorProbSd(0.1, 0.1);
		
		double[] par = new double[9];
		Arrays.fill(par, 0.0);
		par[5] = 5;
		par[6] = 2;
		par[7] = -2;
		par[8] = 0.3;
		gp.setAllGlobalPar(par);
		
		gp.initCalcLikeli(sp, li);

		expected = NormalDistribution.logPdf(5, 1, 2)
				+ InverseGammaDistribution.logPdf(2*2, 0.1, 0.1);
		assertEquals("prior delta", expected, li.getPriorProb("GPI"), 0);
		expected = NormalDistribution.logPdf(-2, 1, 2)
				+ InverseGammaDistribution.logPdf(0.3*0.3, 0.1, 0.1);
		assertEquals("prior delta", expected, li.getPriorProb("GRHO"), 0);
	}
	
//	@Ignore("Do it later")
	@Test
	public void testUpdate() {
		
		double[] allPar = gp.getTunePar();
		gp.setDefaultPrior();
		gp.initCalcLikeli(sp, li);
		double glikeli = li.getGlikelihood();
		double tune = 0.1;
		for (int i = 0; i < 1000; i++) {
			gp.updateDelta(sp, li, tune);
			gp.updateMean(sp, li, tune);
			gp.updateProb1(sp, li, tune);
			gp.updateProb2(sp, li, tune);
			gp.updateAlphaMu(sp, li, tune);
			gp.updateAlphaPi(sp, li, tune);
			gp.updateAlphaRho(sp, li, tune);
			gp.updateSpotScale(sp, li, tune);
		}
		double[] allPar2 = gp.getTunePar();
		System.out.println(Arrays.toString(allPar)+"\n"+ Arrays.toString(allPar2));
		for (int i = 4; i < allPar2.length; i++) {
//			boolean isF = allPar[i]==allPar2[i] ;
			assertFalse(i +"\t"+ allPar[i] +"\t"+ allPar2[i] +"\t"+ (allPar[i] == allPar2[i]), allPar[i] == allPar2[i] );	
		}
		assertNotSame((Object) allPar, (Object) allPar2);
		assertTrue("Likelihood", li.getGlikelihood() > glikeli);
	}
	
	@Ignore("Do it later")
	@Test
	public void testUpdateDelta() {
		fail("Not yet implemented");
	}
	@Ignore("Do it later")
	@Test
	public void testUpdateProb1() {
		fail("Not yet implemented");
	}
	@Ignore("Do it later")
	@Test
	public void testUpdateProb2() {
		fail("Not yet implemented");
		
	}



}
