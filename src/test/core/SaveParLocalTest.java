package test.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bmde.core.Likelihood;
import bmde.core.par.ParGlobal;
import bmde.core.par.ParSpot;
import bmde.core.par.SaveParLocal;
import bmde.core.par.Spot;

public class SaveParLocalTest {

	private double expected;
	private static SaveParLocal savePL;
	
	private ParGlobal gp;
	private Likelihood li;
	private ArrayList<Spot> allSpot;
	private ParSpot[] spArray;
	private ParSpot spTest;
	private double[] expecteds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		savePL = new SaveParLocal(10);
	}

	@Before
	public void setUp() throws Exception {
		int n = 10;
		gp = new ParGlobal(n, -10);
		li = new Likelihood(n, -10);
		ArrayList<Spot> allSpot = Spot.generateList(n, 6);
		spArray = ParSpot.init(-10, allSpot, gp, li);
		spTest = new ParSpot(1, 2, 0.3, 0.2, 0.5);
	}

	@Test
	public void testSaveParLocal() {

		double[][] temp = savePL.getAllPar();
		assertEquals(11, temp.length, 0);
		for (int i = 0; i < temp.length; i++) {
			assertEquals(ParSpot.NO_LOCAL_PAR, temp[i].length, 0);
		}

	}

	@Test
	public void testAdd() {
	
		savePL.addPar(spTest, 10);
		spTest = new ParSpot(2, 3, 1.3, 1.2, 1.5);
		savePL.addPar(spTest, 20);
		double[][] temp = savePL.getAllPar();
		
		expecteds = new double[] {1, 2, 0.3, 0.2};
		assertArrayEquals(expecteds, temp[0], 0);
		expecteds = new double[] {2, 3, 1.3, 1.2};
		assertArrayEquals(expecteds, temp[1], 0);
		
		int[] count = savePL.getState();
		int[] expectedInt = new int[] {10, 20, 0,0,0,0,0,0,0,0,0};
		assertArrayEquals(expectedInt, count);
		
	}


	@Test
	public void testToString() {

		System.out.println(savePL.toString());
	}

}
