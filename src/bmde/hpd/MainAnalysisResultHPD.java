package bmde.hpd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.common.primitives.Doubles;

public class MainAnalysisResultHPD {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String path = args[0];
//		String logFileName = args[1];
		
		
		
//		cwd = System.getProperty("user.dir")+File.separator;
		
//		analysisOne("/home/sw167/workspace_PhD/GlobalML/realData_0409/");
//		anasysisSim("/home/sw167/workspace_PhD/GlobalML/simData2/simData2_0330/");
//		anasysisSim("/home/sw167/workspace_PhD/GlobalML/simData2/simData2_0409/");
//		analysisOne("/home/sw167/workspace_PhD/GlobalML/simData2/simData2_0409/simMix2/");
//		analysisOne("/home/sw167/workspace_PhD/GlobalML/simData/simProbDown/");
//		analysisOne("/home/sw167/workspace_PhD/GlobalML/simData/simMix2/");
//		analysisOne("/home/sw167/workspace_PhD/GlobalML/simData/simDiffProb/");
		
		analysisOld("/home/sw167/workspace_PhD/GlobalML/simData/");
	}

	private static void analysisOld(String string) {
		String cwd = string;
		String logFileName;

		String[] sims = {
				"simProbDown/",
				"simDiffProb/",
				"simMix2/"};
		for (int i = 0; i < sims.length; i++) {
			String s = sims[i];
			logFileName = "Spot_pi.log";
			readLogFile(cwd, s+logFileName);
			logFileName = "Spot_likleihood.log";
					       
			readLogFile(cwd, s+logFileName);
		}
		
	}

	private static void anasysisSim(String string) {
		String cwd = string;
		String logFileName;

		String[] sims = {"simFalsePos12/",  
				"simFalsePos18/",  
				"simFalsePos24/",  
				"simFalsePos36/",
				"simFalsePos48/",
				"simProbDown/",
				"simDiffProb/",
				"simMix2/"};
		for (int i = 0; i < sims.length; i++) {
			String s = sims[i];
			logFileName = "Spot_d.log";
			readLogFile(cwd, s+logFileName);
			logFileName = "Spot_rho.log";
			readLogFile(cwd, s+logFileName);
		}
		
		
	}

	private static void analysisOne(String string) {
		String cwd = string;
		String logFileName;
		logFileName = "Spot_d.log";
		readLogFile(cwd, logFileName);
		logFileName = "Spot_rho.log";
		readLogFile(cwd, logFileName);

		
	}

	private static void readLogFile(String cwd, String logFileName) {

		String logFile = cwd+logFileName;
		System.out.println("Gel File:\t" + logFile);
		// ArrayList<Spot> allSpots = new ArrayList<Spot>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(logFile));
			FileWriter fout = new FileWriter(logFile+"_result.tab");
			String input = in.readLine();
//			String names = in.readLine();
			
//			String[] names = in.readLine().split("\t");
			String name = in.readLine();//.split("\t");
			String[] names = name.split("\t");
			
			StringTokenizer token = new StringTokenizer(name);

			int noSpot = token.countTokens();
			ArrayList<Double>[] dists = new ArrayList[noSpot];
			for (int i = 0; i < dists.length; i++) {
				dists[i] = new ArrayList<Double>();
			}
			
			System.out.print(dists.length+"\t");
			
			while ((input = in.readLine()) != null) {
				token = new StringTokenizer(input);
//				System.out.println(token.countTokens());
				token.nextToken(); //skip noIte
				for (int i = 0; i < noSpot; i++) {
					dists[i].add(Double.parseDouble(token.nextToken()))	;
				}

			}
			
			StringBuilder sb = new StringBuilder();
			int noSignificant = 0;
			for (int i = 0; i < dists.length; i++) {
				double[] values = Doubles.toArray( dists[i]);
				TraceDistribution td = new TraceDistribution(values, 0.975, 0.1);
				double hpdlower = td.getLowerHPD();
				double hpdupper = td.getUpperHPD();
				boolean isGood = hpdlower > 0 || hpdupper < 0; 
				if(isGood){
					noSignificant++;
				}
				sb.append(names[i]).append("\t").append(hpdlower).append("\t").append(hpdupper).
					append("\t").append(isGood).append("\n");
			
			}
//			System.out.println(sb.toString());
			System.out.println(noSignificant);
			
			fout.write(sb.toString());
			fout.write(""+noSignificant);
			
			in.close();
			fout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return allSpots;
	}

}
