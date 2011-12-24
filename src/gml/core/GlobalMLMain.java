package gml.core;

import gml.simulation.Simulation;
import org.apache.commons.math.MathException;

/**
 * @author steven
 * 
 */

public class GlobalMLMain {

	/**
	 * @param args
	 */
	final static String simdata = "simData";
	final static String simresult = "simResult";
	final static String FILE_SEP = System.getProperty("file.separator");
	final static char FILE_SEP_CHAR = FILE_SEP.charAt(0);

	double invGShape = 0.01;
	double invGScale = invGShape;
	double gShape = 0.01;
	double gScale = 1 / gShape;

	public static void main(String[] args) throws MathException {

		// int testSpot = Integer.parseInt(args[1]);
		// int ite = Integer.parseInt(args[2]);

		// double gap = 1.25;
		int noOfData = 10;
		Simulation.setupDir(simdata, simresult);
		Simulation.setNoSpotPerGroup(12);
//		Simulation.GlobalMean();
//		Simulation.createTest();
//		Simulation.createTest100();
		
		
		Simulation.createSim();
/*
 * old simulation
 * 		 Simulation.diffMean(noOfData);
		 // Simulation.diffProb(noOfData);
		 // Simulation.diffLim(gap,noOfData);
*/		//
		
//		
//		testXML(args);
//		
//		 MCMCAnalysis(args);


	}
	private static void testXML(String[] args) {

		new ReadConfig(args[1]);
		
	}
	
	private static void MCMCAnalysis(String[] args) {
		String configFile = args[0];

		MCMC mcmcChain = new MCMC(configFile);
		mcmcChain.run();
	}


}
