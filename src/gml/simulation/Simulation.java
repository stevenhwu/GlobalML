package gml.simulation;

import gml.math.Transformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.util.MathUtils;

public class Simulation {

	final static String FILE_SEP = System.getProperty("file.separator");
	private static String simdata = "";
	private static String simresult = "";
	// private static double sd = 0.65;
	private static double sd = 0.5;
	private static double halfSD = sd / 2;
	private static double empiricalMean = -3;// -3.58;
	private static int totalSpot = 1000;
	private static int noSpotPerGroup = 12;

	private static RandomDataImpl rd = new RandomDataImpl();

	public static void setNoSpotPerGroup(int noSpotPerGroup) {
		Simulation.noSpotPerGroup = noSpotPerGroup;
	}

	public static void createTest100() {
		String fName = "sim1000";
		totalSpot = 1000;
		noSpotPerGroup = 12;
		sd = 2;
		double limDet = -999;

		// rnorm(20,-3,2)
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];

		for (int i = 0; i < setupMean.length; i++) {
			// setupMean[i] = rd.nextUniform(-6, -2);
			// setupDelta[i] = rd.nextUniform(-2, 2);
			//
			// setupPi[i] = rd.nextUniform(-0.5, 0.5);
			// setupRho[i] = rd.nextUniform(-2, 2);

			setupMean[i] = rd.nextGaussian(-1, sd);
			if (i < 50) {
				setupDelta[i] = rd.nextExponential(2);
			} else {
				setupDelta[i] = -rd.nextExponential(2);
			}

			setupPi[i] = rd.nextGaussian(1, 0.2);
			setupRho[i] = rd.nextGaussian(0, 1);
		}
		Arrays.sort(setupMean);
		Arrays.sort(setupDelta);
		Arrays.sort(setupPi);
		Arrays.sort(setupRho);

		try {
			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							fName);// ;.append(FILE_SEP);//
			// append(
			System.out.println(sb.toString());
			checkDir(sb, false);

			sb.append(fName);
			String simLog = sb.toString() + ".log";
			PrintWriter outLog = new PrintWriter(new BufferedWriter(
					new FileWriter(simLog)));
			outLog.println(Arrays.toString(setupMean));
			outLog.println(Arrays.toString(setupDelta));
			outLog.println(Arrays.toString(setupPi));
			outLog.println(Arrays.toString(setupRho));
			outLog.close();

			String simFile = sb.append(".csv").toString();

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));
			GelSetting g = new GelSetting(noSpotPerGroup);
			out.println(g.createLabel());

			double spotSd = sd * Math.sqrt(totalSpot) * 0.05;
			// for (int i = 0; i < totalSpot; i++) {
			for (int i = 0; i < totalSpot; i++) {

				// double meanDiff = diffRange[f][i];
				// double halfMeanDiff = MathUtils.round(meanDiff / 2, 3);
				// double smallMean = empiricalMean - halfMeanDiff;

				System.out.println(setupMean[i] + "\t" + setupDelta[i] + "\t"
						+ sb.toString());
				g = new GelSetting(noSpotPerGroup, setupMean[i], setupDelta[i],
						setupPi[i], setupPi[i] + setupRho[i], spotSd);

				g.setLimDet(limDet);

				out.println(GelSetting.printGel(i, g.generateSpot()));
				// for (int j = 0; j < 1; j++) {
				// out.println(GelSetting.printGel(i * 5 + j + 1, g
				// .generateSpot()));
				// }

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// }
		// }

	}

	// setupMean[i] = rd.nextUniform(-6, -2);
	// setupDelta[i] = rd.nextUniform(-2, 2);
	//
	// setupPi[i] = rd.nextUniform(-0.5, 0.5);
	// setupRho[i] = rd.nextUniform(-2, 2);

	public static void createSim() {

		String fName = "simFalsePos12_5";
		totalSpot = 100;
		noSpotPerGroup = 12;
		sd = 1;

		double limDet = -999;
		double spotScale = 1; //0.7; //sd
		double spotSd = sd *  spotScale; 
		double oneOverLambda = spotSd*1.5;
		
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];


		for (int i = 0; i < totalSpot; i++) {

			setupMean[i] = rd.nextGaussian(-5, sd);
			setupDelta[i] = 0;
//			rd.nextExponential(oneOverLambda);
			
			setupPi[i] = rd.nextGaussian(1, 1);
			setupRho[i] = 0;
		}
		
//		for (int i = 0; i < totalSpot; i++) {
//
//			setupMean[i] = rd.nextGaussian(-5, sd);
//			if (i < 50) {
//				setupDelta[i] = rd.nextExponential(oneOverLambda);
//				setupRho[i] = rd.nextGaussian(-3, 0.25);
//			} else {
//				setupDelta[i] = -rd.nextExponential(oneOverLambda);
//				setupRho[i] = rd.nextGaussian(2, 0.25);
//			}
//
//			setupPi[i] = rd.nextGaussian(1, 0.25);
////			setupRho[i] = rd.nextGaussian(-4, 0.5);
//		}
		
//		for (int i = 0; i < totalSpot; i++) {
////
////			setupMean[i] = rd.nextUniform(-6, -2);
////			setupDelta[i] = rd.nextUniform(-3, 3);
//			setupPi[i] = rd.nextUniform(-1, 3);
//			setupRho[i] = rd.nextUniform(-2, 2);
//		}
		
//		Arrays.sort(setupMean);
//		Arrays.sort(setupDelta);
//		Arrays.sort(setupPi);
//		Arrays.sort(setupRho);
//		ArrayUtils.reverse(setupRho);
		
		try {
			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							fName);

			System.out.println(sb.toString());
			checkDir(sb, false);

			
			File setup = new File( sb.toString()+"setupCode.txt");
			setup.createNewFile();
			
			sb.append(fName);
			String simLog = sb.toString() + ".log";
			PrintWriter outLog = new PrintWriter(new BufferedWriter(
					new FileWriter(simLog)));

//			outLog.println(Arrays.toString(setupMean));
//			outLog.println(Arrays.toString(setupDelta));
//			outLog.println(Arrays.toString(setupPi));
//			outLog.println(Arrays.toString(setupRho));
			

			String simFile = sb.append(".csv").toString();

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));
			GelSetting g = new GelSetting(noSpotPerGroup);
			out.println(g.createLabel());

			for (int i = 0; i < totalSpot; i++) {
				String info = i+"\t"+setupMean[i] + "\t" + setupDelta[i] + "\t"
				+ Transformation.invLogit(setupPi[i]) +"\t"+ Transformation.invLogit(setupPi[i]+setupRho[i]);
				System.out.println(info);
				outLog.println(info);
				g = new GelSetting(noSpotPerGroup, setupMean[i], setupDelta[i],
						setupPi[i], setupRho[i], spotSd);

				g.setLimDet(limDet);
				out.println(GelSetting.printGel(i, g.generateSpot() ));

			}
			out.close();
			outLog.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("END");
	}

	public static void createTest() {
		String fName = "sim30";
		totalSpot = 10;
		noSpotPerGroup = 12;
		double limDet = -999;

		// rnorm(20,-3,2)
		double[] setupMean = // { -4.0, -3.8, -3.6, -3.4, -3.2, -3.0, -2.8,
		// -2.6, -2.4, -2.2 };
		// NormalDistribution.randomDist(-3,sd);
		// {-3.931244, -3.676252, -3.267163, -3.185495, -3.008657, -2.982609,
		// -2.845479, -2.727249, -2.421971, -2.194658};
		{ -4.3435574, -2.1669564, -3.3140539, -4.4712736, -2.7994992,
				-2.9001727, -1.7678443, -3.0935153, -3.5088050, -2.1480583,
				-2.2270892, -3.0598310, -2.6634057, -2.4979394, -2.8279055,
				-4.3122765, -3.9632533, -4.8586136, -0.8628994, -3.6556448 };
		// 5 runif 5 rexp+2
		double[] setupDelta =
		// { 0.1926531, 0.2071016, 0.6485506, 1.3666269, 1.6099387,
		// -0.1265382, -0.7419509, -1.2941278, -2.2160230, -3.7109849};
		// rexp(10) -rexp(10)
		{ 1.7575603, 1.9749193, 0.9164464, 2.1226964, 0.1437014, 0.3908779,
				1.7249010, 0.5938384, 0.2962534, 0.1489002, -0.01056629,
				-0.68633844, -0.21167337, -0.43839241, -0.56177363,
				-1.24472782, -2.38148142, -1.72705781, -1.94747866, -0.49970269 };
		// 3 files
		// for (int f = 0; f < 3; f++) {

		double[] setupPi = { 0.7751008, 0.7941787, 0.7604776, 0.7727293,
				0.8293569, 0.8034208, 0.7949960, 0.7866419, 0.8604927,
				0.8460542, 0.7674016, 0.7880800, 0.7363203, 0.7891760,
				0.8030470, 0.8883394, 0.7830407, 0.7926653, 0.8128097,
				0.8221606 };
		double[] setupRho = { -0.159807395, 0.090765697, -0.041775205,
				-0.189068531, -0.047745083, -0.146389028, 0.041348829,
				0.080260469, -0.053921037, -0.003537372, -0.130429228,
				-0.049334359, 0.005642451, -0.150883418, -0.185749411,
				-0.035446096, -0.056351056, -0.032736894, 0.074014899,
				0.034656555 };

		try {
			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							fName);// ;.append(FILE_SEP);//
			// append(
			System.out.println(sb.toString());
			checkDir(sb, false);

			sb.append(fName).append(".csv");
			String simFile = sb.toString();

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));
			GelSetting g = new GelSetting(noSpotPerGroup);
			out.println(g.createLabel());

			double spotSd = sd * Math.sqrt(setupMean.length);
			// for (int i = 0; i < totalSpot; i++) {
			for (int i = 0; i < setupMean.length; i++) {

				// double meanDiff = diffRange[f][i];
				// double halfMeanDiff = MathUtils.round(meanDiff / 2, 3);
				// double smallMean = empiricalMean - halfMeanDiff;

				System.out.println(setupMean[i] + "\t" + setupDelta[i] + "\t"
						+ sb.toString());
				g = new GelSetting(noSpotPerGroup, setupMean[i], setupDelta[i],
						setupPi[i], setupPi[i] + setupRho[i], spotSd, true);

				g.setLimDet(limDet);

				out.println(GelSetting.printGel(i, g.generateSpot()));
				// for (int j = 0; j < 1; j++) {
				// out.println(GelSetting.printGel(i * 5 + j + 1, g
				// .generateSpot()));
				// }

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// }
		// }

	}

	private static void checkDir(StringBuilder sb, boolean overWrite) {

		File f = new File(sb.toString());
		if (f.exists() & overWrite) {
			sb.append("_new");
			checkDir(sb, overWrite);
		} else {
			sb.append(FILE_SEP);
			f.mkdirs();
		}

	}

	public static void GlobalMean() {

		totalSpot = 5;
		noSpotPerGroup = 12;
		double limDet = -999;

		double[][] diffRange = { { 0, 2, 4, 6 }, { -4, -2, 2, 4 },
				{ -6, -1, 1, 6 } };

		// 3 files
		for (int f = 0; f < 3; f++) {
			try {
				StringBuilder sb = new StringBuilder(System
						.getProperty("user.dir")).append(FILE_SEP).append(
						simdata).append(FILE_SEP).append("Global_").append(
						f + 1).append(".csv");
				String simFile = sb.toString();

				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(simFile)));
				GelSetting g = new GelSetting(noSpotPerGroup);
				out.println(g.createLabel());
				// 3 files
				// 4 different groups, 5 spots each, 12 gels per groups(24 gels
				// per
				// spot)
				// all prob expression = 0.8
				// F1 [-2, -2] [-3, -1] [-4, 0] [-5,1]
				// F2 [0, -4] [-1, -3] [-3, -1] [-4, 0]
				// F3 [1, -5] [-1.5, -2.5] [-2.5, -1.5] [-5, 1]
				//

				for (int i = 0; i < 4; i++) {

					double meanDiff = diffRange[f][i];
					double halfMeanDiff = MathUtils.round(meanDiff / 2, 3);
					double smallMean = empiricalMean - halfMeanDiff;

					System.out.println(smallMean + "\t" + meanDiff + "\t"
							+ sb.toString());
					g = new GelSetting(noSpotPerGroup, smallMean, meanDiff, 50,
							0, sd);
					g.gelSetP(0.8, 0.8);
					g.setLimDet(limDet);

					for (int j = 0; j < totalSpot; j++) {
						out.println(GelSetting.printGel(i * 5 + j + 1, g
								.generateSpot()));
					}

				}
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// }

	}

	public static void diffMean(int noOfData) {

		for (int i = 0; i < 12; i++) {
			double meanDiff = MathUtils.round(sd * i / 4, 3);
			double halfMeanDiff = MathUtils.round(meanDiff / 2, 3);
			double smallMean = empiricalMean - halfMeanDiff;

			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							"DiffMean_").append(i).append(".csv");

			String simFile = sb.toString();
			System.out.println(meanDiff + "\t" + smallMean + "\t"
					+ sb.toString());

			double limDet = -999;
			GelSetting g = new GelSetting(noSpotPerGroup, smallMean, meanDiff,
					50, 0, sd);
			g.gelSetP(1, 1);
			g.setLimDet(limDet);
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(simFile)));
				out.println(g.createLabel());

				for (int j = 0; j < noOfData; j++) {
					out.println(j + "\t"
							+ GelSetting.printGel(g.generateSpot()));
				}
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void diffProb(int noOfData) {

		double limDet = -999;
		for (int p = 1; p < 11; p++) {
			double p1 = p / 10.0;
			for (int pp = 1; pp <= p; pp++) {
				double p2 = pp / 10.0;
				StringBuilder sb = new StringBuilder(System
						.getProperty("user.dir"));
				sb.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
						"MeanProb_").append(p1).append("_").append(p2).append(
						".csv");

				String simFile = sb.toString();
				System.out.println(p1 + "\t" + p2 + "\t" + simFile);

				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new FileWriter(simFile)));

					GelSetting g = new GelSetting(noSpotPerGroup,
							empiricalMean, 0, 50, 0, sd);
					out.println(g.createLabel());

					g.gelSetP(p1, p2);
					g.setLimDet(limDet);

					for (int j = 0; j < noOfData; j++) {
						out.println(j + "\t"
								+ GelSetting.printGel(g.generateSpot()));
					}
					out.close();
				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static void diffLim(double gap, int noOfData) {

		double limDet = -8.67;
		double meanDiff = MathUtils.round(sd * gap, 3);
		double halfMeanDiff = MathUtils.round(meanDiff / 2, 3);
		double smallMean = empiricalMean - halfMeanDiff;
		System.out.println(meanDiff + "\t" + smallMean + "\t");

		for (int i = 1; i < 12; i++) {

			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							"DiffLim_").append(i).append(".csv");

			String simFile = sb.toString();
			GelSetting g = new GelSetting(noSpotPerGroup, smallMean, meanDiff,
					50, 0, sd);
			g.gelSetP(1, 1);
			NormalDistributionImpl nd = new NormalDistributionImpl(smallMean,
					sd);

			System.out.println(sb.toString());
			try {
				limDet = nd.inverseCumulativeProbability(0.05 * (i - 1));
				System.out.println(simFile + "\t" + limDet);
				g.setLimDet(limDet);
				generateSimDataFile(simFile, g);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void generateSimDataFile(String simFile, GelSetting g) {
		try {

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));
			out.println(g.createLabel());
			for (int j = 0; j < totalSpot; j++) {
				out.println(j + "\t" + GelSetting.printGel(g.generateSpot()));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setupDir(String data, String result) {
		simdata = data;
		simresult = result;

	}

}
