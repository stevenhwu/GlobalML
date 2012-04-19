package test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bmde.core.Likelihood;
import bmde.core.par.CurrentPar;
import bmde.core.par.ParGlobal;
import bmde.core.par.ParSpot;
import bmde.core.par.Spot;
import bmde.core.par.TunePar;

public class CurrentParTest {

	private static ParGlobal gp;
	private static ParSpot[] sp;
	private static Likelihood li;
	static CurrentPar cp;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		int n = 10;
		double limDet = -10;
		ArrayList<Spot> allSpots = Spot.generateList(n, 6);

		cp = new CurrentPar(allSpots, limDet);
		
	}

	@Before
	public void setUp() throws Exception {
	}

//	@Test
	public void testCurrentParIntDouble() {
		fail("Not yet implemented");
	}


	@Test
	public void testInit() {
		String lString = cp.getLikelihood().toString();
		cp.init();
		assertNotSame(lString,cp.getLikelihood().toString());
	}

	@Test
	public void testUpdateGlobal() {

		int ite = 1000;
		String lString = cp.getLikelihood().toString();
		TunePar gtp = new TunePar("", 100, 10, 10);
		double[] tp = new double[10];
		Arrays.fill(tp, 1);
		for (int i = 0; i < ite; i++) {
			cp.updateGlobal(gtp);		
		}
		assertFalse(  lString.equals(cp.getLikelihood().toString()) );

	}
	

}


