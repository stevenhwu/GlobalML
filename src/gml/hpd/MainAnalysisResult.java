package gml.hpd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import gml.simulation.Simulation;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.MathException;

public class MainAnalysisResult {

	
	
	final static String simdata = "simData";
	final static String simresult = "simResult";
	final static String FILE_SEP = System.getProperty("file.separator");
	final static char FILE_SEP_CHAR = FILE_SEP.charAt(0);

	
	private static String path = "/home/steven/workspace_NOBackup/sim1SD/";
	
	
	public static void main(String[] args) throws MathException {

		Simulation.setupDir(simdata, simresult);
		readLogFile("Spot1.log");
		
	
	}
	

	private ArrayList<double[]> readConfig(String configFile) {

		path = configFile.substring(0, configFile.lastIndexOf(FILE_SEP) + 1);
		System.out.println("coinfig file:\t" + configFile);
		// System.out.println(path);
		// System.out.println(FILE_SEP);
		ArrayList<double[]> config = new ArrayList<double[]>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(configFile));

			readLogFile(in.readLine());

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	/**
	 * Read input data
	 * 
	 * @param gelFile
	 * @return
	 */
	private static void readLogFile(String logFileName) {

		String logFile = path.concat(logFileName);
		System.out.println("Gel File:\t" + logFile);
		// ArrayList<Spot> allSpots = new ArrayList<Spot>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(logFile));

			String input = in.readLine();
//			String names = in.readLine();
			ArrayList<Trace> dists = new ArrayList<Trace>();
			
//			String[] names = in.readLine().split("\t");
			String names = in.readLine();//.split("\t");
			
			StringTokenizer token = new StringTokenizer(names);
//			for (int i = 0; i < .length; i++) {
//				
//			}
			System.out.println(ArrayUtils.toString(names));
			
			ArrayList[] dists2 = new ArrayList[names.length()];//
			for (ArrayList arrayList : dists2) {
				arrayList = new ArrayList<Double>();
				
			}
			
			
			while ((input = in.readLine()) != null) {
				input.split("\t");
			}
			

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return allSpots;
	}

	
}
