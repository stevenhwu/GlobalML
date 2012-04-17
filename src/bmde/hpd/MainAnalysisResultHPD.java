package bmde.hpd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.primitives.Doubles;

import dr.inference.trace.TraceFactory;

public class MainAnalysisResultHPD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String path = args[0];
//		String logFileName = args[1];
		
		String cwd = System.getProperty("user.dir")+File.separator;
		String logFileName = "Spot_d.log";
		readLogFile(cwd, logFileName);
		
	}

	private static void readLogFile(String cwd, String logFileName) {

		String logFile = cwd+logFileName;
		System.out.println("Gel File:\t" + logFile);
		// ArrayList<Spot> allSpots = new ArrayList<Spot>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(logFile));

			String input = in.readLine();
//			String names = in.readLine();
			
//			String[] names = in.readLine().split("\t");
			String name = in.readLine();//.split("\t");
			String[] names = name.split("\t");
			
			StringTokenizer token = new StringTokenizer(name);

			int noSpot = token.countTokens()-1;
			ArrayList<Double>[] dists = new ArrayList[noSpot];
			for (int i = 0; i < dists.length; i++) {
				dists[i] = new ArrayList<Double>();
			}
			
			System.out.println(dists.length);
			
			while ((input = in.readLine()) != null) {
				token = new StringTokenizer(input);
				System.out.println(token.nextToken());
				for (int i = 0; i < noSpot; i++) {
					dists[i].add(Double.parseDouble(token.nextToken()))	;
				}

			}
			
			StringBuilder sb = new StringBuilder();
			int noSignificant = 0;
			for (int i = 0; i < dists.length; i++) {
				double[] values = Doubles.toArray( dists[i]);
				TraceDistribution td = new TraceDistribution(values, 0.1);
				double hpdlower = td.getLowerHPD();
				double hpdupper = td.getUpperHPD();
				boolean isGood = hpdlower > 0 || hpdupper < 0; 
				if(isGood){
					noSignificant++;
				}
				sb.append(names[i+1]).append("\t").append(hpdlower).append("\t").append(hpdupper).
					append("\t").append(isGood).append("\n");
			
			}
			System.out.println(sb.toString());
			System.out.println(noSignificant);
			
			
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return allSpots;
	}

}
