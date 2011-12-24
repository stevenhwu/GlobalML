package gml.core;

import gml.core.par.CurrentPar;
import gml.core.par.GlobalPar;
import gml.core.par.SavePar;
import gml.core.par.SaveParGlobal;
import gml.core.par.SaveParLocal;
import gml.core.par.Spot;
import gml.core.par.SpotPar;
import gml.core.par.TunePar;
import gml.logger.NumberColumn;
import gml.logger.PerformanceLogger;
import gml.logger.TabFormatter;
import gml.math.Constant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

//import classes.org.jdom.

/**
 * @author steven
 * 
 */
public class MCMC {

	private final static String FILE_SEP = System.getProperty("file.separator");

	private static final String[] SPOT_LABELS = SpotPar.SPOT_LABELS;
	private static final String[] SPOT_LABELS_SUM = SpotPar.SPOT_LABELS_SUM;
	private static final String[] GLOBAL_LABELS = GlobalPar.GLOBAL_LABELS;

	private int totalIte;
	private int thinning;
	private int noSpot;
	private double limDet;
	private ArrayList<double[]> allConfig;

	private String saveFile;
	private String fileName;
	private ArrayList<Spot> allSpots = new ArrayList<Spot>();
	private CurrentPar cp;
	private SavePar ap;
	private String[] outfiles;
	private String globalOutFile;
	private String localOutFile;
	private String path;

	public MCMC(String configFile) {

		// private static void MCMCAnalysis(String[] args) {
		// String configFile = args[0];

		//
		// StringBuilder sb = new StringBuilder(System.getProperty("user.dir"));
		// sb.append(FILE_SEP_CHAR).append(simdata).append(FILE_SEP_CHAR).append(
		// args[0]);
		// String gelFile = sb.toString();
		// String configFile =
		// + args[1];
		//
		// // MCMC mcmc =
		// new MCMC(gelFile, configFile);
		// // mcmc.run(1);
		//
		// }

		// this.fileName = fileName;
		// allSpots = readGelData(fileName);
		allConfig = readConfig(configFile);

		limDet = findMin(allSpots);
		System.out.println("LimDet: "+limDet);
		// System.out.println(noSpot + "\t" + limDet);
		cp = new CurrentPar(allSpots, limDet);
		//
		// cp.setPrior(allConfig.get(0));
		System.out.println(Arrays.toString(Setting.getPriorSummary()));

		// outfiles = allConfig.get(2);;
		// run();
	}

	public void saveFile(String s) {

		saveFile = s;

	}

	public void run() {

		try {
			runMCMC();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void runMCMC() throws IOException {

		NumberColumn nc = new NumberColumn("", 1);
		nc.setDecimalPlaces(5);

		// global
		TabFormatter tabOutGlobal = new TabFormatter(globalOutFile, nc);
		tabOutGlobal.logHeading("Global parameters");
		tabOutGlobal.logLabels(GLOBAL_LABELS);

		// local
		TabFormatter tabOutLocal = new TabFormatter(localOutFile, nc);
		tabOutLocal.logHeading("Local parameters");

		ArrayList<String> alString = new ArrayList<String>();
		alString.add("Ite");
		for (int j = 1; j < SPOT_LABELS.length; j++) {
			for (int i = 0; i < cp.getNoSpot(); i++) {
				alString.add("Spot" + i + "_" + SPOT_LABELS[j]);
			}
		}
		String[] sArray = new String[cp.getNoSpot() * 3];
		tabOutLocal.logLabels(alString.toArray(sArray));

		// local each param
		ArrayList<TabFormatter> logEachParam = new ArrayList<TabFormatter>();
		sArray = new String[cp.getNoSpot()];
		for (int j = 1; j < SPOT_LABELS.length; j++) {
			TabFormatter tf = new TabFormatter(path + "Spot_" + SPOT_LABELS[j]
					+ ".log", nc);
			tf.logHeading("Spot_" + SPOT_LABELS[j]);
			alString.clear();
			for (int i = 0; i < cp.getNoSpot(); i++) {
				alString.add("Spot" + i + "_" + SPOT_LABELS[j]);
			}
			tf.logLabels(alString.toArray(sArray));
			logEachParam.add(tf);
		}

		// local each spot
		ArrayList<TabFormatter> logEachLocal = new ArrayList<TabFormatter>();
		for (int i = 0; i < cp.getNoSpot(); i++) {
			TabFormatter tf = new TabFormatter(
					path + "Spot" + (i + 1) + ".log", nc);
			tf.logHeading("Spot" + (i + 1));
			tf.logLabels(SPOT_LABELS);
			logEachLocal.add(tf);
		}

		// performance log
		PerformanceLogger perfLog = new PerformanceLogger(path
				+ "Performance.log");
		PerformanceLogger perfScreen = new PerformanceLogger();

		int updateGlobalCount = 250;
		int tuneSize = Constant.TUNESIZE;
		int tuneGroup = Constant.TUNEGROUP;

		int saveLocalCount = thinning;
//		int saveGlobalCount = updateGlobalCount * thinning;

		int tuneLocalCount = tuneSize;
		int tuneGlobalCount = tuneSize;

		// SaveParGlobal saveGlobal = new SaveParGlobal(globalIte);
		// SaveParGlobal saveGlobal = new SaveParGlobal(1);
		int noSpot = cp.getNoSpot();

		// TunePar tpGlobal = new TunePar(Setting.GLOBAL, totalIte, tuneSize,
		// tuneGroup, new String[] { "Scale", "Scale", "Scale", "Scale" });
		TunePar tpGlobal = new TunePar(Setting.GLOBAL, totalIte, tuneSize,
				tuneGroup, new String[] { "Normal", "Normal", "Normal",
						"Normal", "Normal", "Normal", "Normal", "Normal",
						"Normal", "Normal" });
		
		SaveParGlobal saveTuneGlobal = new SaveParGlobal(tuneSize);

		ArrayList<TunePar> tpLocal = new ArrayList<TunePar>();
		ArrayList<SaveParLocal> saveTuneLocal = new ArrayList<SaveParLocal>();

		String[] localTuneType = new String[] { "Normal", "Normal" };
		for (int i = 0; i < noSpot; i++) {
			// saveLocal.add(new SaveParLocal(totalIte));
			// saveLocal.add(new SaveParLocal(1));
			saveTuneLocal.add(new SaveParLocal(tuneSize));
			tpLocal.add(new TunePar(Setting.LOCAL, totalIte, tuneSize,
					tuneGroup, localTuneType));
		}

		perfLog.startLogging();
		perfScreen.startLogging();
		// totalIte=1;

		int gCount = 0;
		System.out.println(ArrayUtils.toString(cp.getGlobalOutput()));
		for (int ite = 0; ite < totalIte; ite++) {

			cp.updateLocal(tpLocal);

			// Save par for tuning local
			for (int j = 0; j < noSpot; j++) {
				saveTuneLocal.get(j).add(cp.getEachSpotPar(j), ite);

			}

			// Update tuning local
			if (ite % tuneLocalCount == 0) {
				for (int j = 0; j < noSpot; j++) {
					tpLocal.get(j).update(saveTuneLocal.get(j), ite);

				}
			}

			if (ite % 10000 == 0) {
				perfScreen.log(ite);
				perfLog.log(ite);
				// output to file
				System.out.println(Arrays.toString(tpGlobal.getAveAccRate()));
//				System.out.println(Arrays.toString(tpGlobal.getTunePar()));
				// System.out.println(Arrays.toString(tpLocal.get(0).getAveAccRate()));
				// System.out.println(Arrays.toString(tpLocal.get(0).getTunePar()));
			}

			if (ite % updateGlobalCount == 0 ) {
				
				int eachGIte =  updateGlobalCount;
				// int gUpdate = eachGIte / 1;

				cp.updateParamLikelihood();

				for (int gIte = 0; gIte < eachGIte; gIte++) {

					gCount++;
					cp.updateGlobal(tpGlobal.getTunePar());
					saveTuneGlobal.addPar(cp.getGlobalPar(), gCount);
					
					if (gCount % tuneGlobalCount == 0) {
						tpGlobal.update(saveTuneGlobal, gCount);

					}
				}
				cp.updateLocalLikeli();
				

			}

			// output local
			if (ite % saveLocalCount == 0) {
				// tabOutLocal.logValues(ite, cp.getLocalOutput());
				tabOutLocal.logValues(ite, cp.getLocalOutputAll());
				for (int j = 0; j < cp.getNoSpot(); j++) {
					logEachLocal.get(j).logValues(ite, cp.getLocalOutput(j));

				}
				for (int j = 0; j < SPOT_LABELS.length-1; j++) {
					logEachParam.get(j).logValues(ite, cp.getParamOutput(j));
				}

				tabOutGlobal.logValues(ite, cp.getGlobalOutput());

			}
		}

		// tabOutLocal.logValues(ite, cp.getLocalOutput());
		tabOutLocal.logValues(totalIte, cp.getLocalOutputAll());
		for (int j = 0; j < cp.getNoSpot(); j++) {
			logEachLocal.get(j).logValues(totalIte, cp.getLocalOutput(j));

		}
		for (int j = 0; j < SPOT_LABELS.length-1; j++) {
			logEachParam.get(j).logValues(totalIte, cp.getParamOutput(j));
		}
		tabOutGlobal.logValues(totalIte, cp.getGlobalOutput());

	
		perfScreen.stopLogging();
		perfLog.stopLogging();

		// tabOutGlobal.logValues(tpGlobal.getTunePar());
		System.out.println(tpGlobal.toString());
		System.out.println(tpLocal.get(0).toString());
		System.out.println("\n=====END=====");

	}

	/**
	 * Read the configuration file, tab delimited 10 parameters at moment, all
	 * hyper prior
	 * 
	 * Gelfile total ite, thinning globalout localout prior
	 * 
	 * @param configFile
	 * @return
	 * 
	 * 
	 */
	private ArrayList<double[]> readConfig(String configFile) {

		path = configFile.substring(0, configFile.lastIndexOf(FILE_SEP) + 1);
		System.out.println("coinfig file:\t" + configFile);
		// System.out.println(path);
		// System.out.println(FILE_SEP);
		ArrayList<double[]> config = new ArrayList<double[]>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(configFile));

			readGelData(in.readLine());
			setIte(in.readLine());
			setOutfile(in.readLine());

			// double[] priorConfig =
			// stringToDouble(in.readLine().split("\\s+"));
			// config.add(priorConfig);

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	private void setOutfile(String s) {

		String[] outfile = s.split("\\s");
		this.globalOutFile = path + outfile[0];
		this.localOutFile = path + outfile[1];

	}

	private void setIte(String s) {

		int[] mcmcInfo = stringToInt(s.split("\\s"));
		this.totalIte = mcmcInfo[0];
		this.thinning = mcmcInfo[1];
	}

	/**
	 * Read input data
	 * 
	 * @param gelFile
	 * @return
	 */
	private void readGelData(String gelFileName) {

		String gelFile = path.concat(gelFileName);
		System.out.println("Gel File:\t" + gelFile);
		// ArrayList<Spot> allSpots = new ArrayList<Spot>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(gelFile));

			String input = in.readLine();

			while ((input = in.readLine()) != null) {
				Spot s = new Spot(input);
				allSpots.add(s);
			}
			noSpot = allSpots.size();

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return allSpots;
	}

	private static double[] stringToDouble(String[] fields) {

		double[] temp = new double[fields.length];
		for (int i = 0; i < fields.length; i++) {
			temp[i] = Double.parseDouble(fields[i]);
		}
		return temp;
	}

	private static int[] stringToInt(String[] fields) {

		int[] temp = new int[fields.length];
		for (int i = 0; i < fields.length; i++) {
			temp[i] = Integer.parseInt(fields[i]);
		}
		return temp;
	}

	/**
	 * find minimum value
	 * 
	 * @param allSpot
	 * @return
	 */
	private static double findMin(ArrayList<Spot> allSpot) {
		double min = 0;
		double newMin = 0;
		for (Spot spot : allSpot) {
			newMin = spot.findMin();
			min = (min < newMin) ? min : newMin;
		}
		return min;
	}

}
