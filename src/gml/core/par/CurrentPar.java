package gml.core.par;

import gml.core.Likelihood;

import java.util.ArrayList;
import org.apache.commons.math.random.RandomDataImpl;

public class CurrentPar {

	private static int[] globalOrder = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
	private static RandomDataImpl rgen = new RandomDataImpl();

	private double limDet;

	private GlobalPar gp;
	private SpotPar[] spArray;
	private ArrayList<Spot> allSpots;
	private Likelihood li;

	// private Spot thisSpot;
	// private double lambda1;
	// private double lambda2;
	// private double phi;
	//	
	// private double alpha1;
	// private double beta1;
	// private double alpha2;
	// private double beta2;
	private int noSpot;

	// private double mu1;
	// private double p;
	// private double sd;

	// private double likelihood;
	// private double posterior;

	/**
	 * @param totalIte
	 * @param limDet
	 */
	public CurrentPar(ArrayList<Spot> allSpots, double limDet) {

		this.allSpots = allSpots;
		this.noSpot = allSpots.size();
		this.limDet = limDet;

		gp = new GlobalPar(noSpot, limDet);
		li = new Likelihood(noSpot, limDet);
		init();
		// spArray = SpotPar.init(limDet, allSpots, gp, li);
		// gp.calcLikeli(spArray, li);

	}

	// public void setAllSpots(ArrayList<Spot> allSpots) {
	// this.allSpots = allSpots;
	// li.setGlikelihood(li.calGlobalLogLikelihood(allSpots, sp));
	// }

	public void setPrior(double... config) {
		int i = 0;
		// double mean, double sd,
		// double shape, double scale, double sdUpper,
		// double gShape, double gScale,
		// double phiLower, double phiUpper,
		// double probMu, double probSd,
		// double invGShape, double invGScale

		gp.setPriorMu(config[i++], config[i++]);
		gp.setPriorSd(config[i++], config[i++], config[i++]);

		gp.setPriorExp(config[i++], config[i++]);
		gp.setPriorPhi(config[i++], config[i++]);

		gp.setPriorProbMu(config[i++], config[i++]);
		gp.setPriorProbSd(config[i++], config[i++]);

		// Setting.setGap(2);
		init();

	}

	public void init() {
		// li.init();

		spArray = SpotPar.init(limDet, allSpots, gp, li);

		gp.initCalcLikeli(spArray, li);

	}

	// public void resetSpot(AllSpot<String> allSpot){
	//		
	// this.allSpot = allSpot;
	// // chooseRandomStart();
	// setSpot(thisSpot);
	// updateLikelihood();
	// }

	// private void setSpot(Spot spot) {
	//
	// this.thisSpot = spot;
	// // updateLikelihood();
	// }

	// public void updateLikelihood(){
	//		
	// // RealPar rp = new RealPar(mu, d, k, g, sd);
	// likelihood =
	// ll.calLogLikeli(thisSpot.getExpressControlSpot(),thisSpot.getNumberOfSpot(),rp.getU1(),rp.getP1(),rp.getSd())+
	// ll.calLogLikeli(thisSpot.getExpressCaseSpot(),thisSpot.getNumberOfSpot(),rp.getU2(),rp.getP2(),rp.getSd());
	// posterior = likelihood+calculatePrior();
	//		
	//
	// }
	public void updateParamLikelihood() {
		
		gp.calcParamLikelihood(spArray, li);

	}
	public void updateGlobal(double[] tune) {


		globalOrder = shuffle(globalOrder);
		 for (int i = 0; i < globalOrder.length; i++) {

			// switch (globalOrder[i]) {
			switch (i) {
			case 0:
				gp.updateMean(spArray, li, tune[0]);
				
//				li.calGlobalLogLikelihood(spArray, gp);
				break;
			case 1:
				gp.updateDelta(spArray, li, tune[1]);

				break;
			case 2:
				gp.updateProb1(spArray, li, tune[2]);
				break;
			case 3:
				gp.updateProb2(spArray, li, tune[3]);
				break;
			case 4:
				gp.updateSpotScale(spArray, li, tune[9]);
				break;
			case 5:
				gp.updateAlphaMu(spArray, li, tune[8]);
				break;
			case 6:
				gp.updateAlphaPi(spArray, li, tune[6]);
			case 7:
				gp.updateAlphaRho(spArray, li, tune[7]);
			default:
				break;
			}

		}

		// final String GMUSD = "GMUSD";
		// final String GDELTA = "GDELTA";
		// final String GPI = "GPI";
		// final String GRHO = "GRHO";
		//
		// double zzz = li.getParamLikelihood(GMUSD)
		// + li.getPriorProb(GMUSD)+ li.getParamLikelihood(GDELTA)
		// + li.getParamLikelihood(GPI) + li.getParamLikelihood(GRHO)
		// + li.getPriorProb(GDELTA) + li.getPriorProb(GPI)
		// + li.getPriorProb(GRHO);
		// if(zzz - (li.getPosterior() - StatUtils.sum(li.getEachLikelihood()) )
		// >1E-10)
		// {
		// System.out.println( zzz -(li.getPosterior() -
		// StatUtils.sum(li.getEachLikelihood()) ) );
		// ;
		// }
		//		

	}

	private int[] shuffle(int[] srcArray) {
		// Collections.shuffle(gList);
		// List<Integer> gList2 = new List();

		// Collections.shuffle();
		// Collections.shuffle(Arrays.asList(globalOrder));

		for (int i = 0; i < srcArray.length; i++) {

			int randomPosition = rgen.nextInt(0, srcArray.length - 1);
			int temp = srcArray[i];
			srcArray[i] = srcArray[randomPosition];
			srcArray[randomPosition] = temp;
		}
			return srcArray;
	}

	public void updateLocalLikeli() {
		
		for (int i = 0; i < spArray.length; i++) {
			spArray[i].setupLikeli(gp, li) ;
		}
	}

	public void updateLocal(ArrayList<TunePar> tune) {

		double[] eachTune;
		for (int i = 0; i < spArray.length; i++) {
			eachTune = tune.get(i).getTunePar();
			spArray[i].updateLocalPar(gp, li, eachTune);
		}

	}

	public double[] getGlobalOutput() {
		double[] par = gp.getPar();
		double[] out = new double[par.length + 9];

		System.arraycopy(par, 0, out, 0, par.length);
		out[par.length] = getGlobalLikelihood();
		
		out[par.length] = li.getSumPriorProb();
		out[par.length + 1] = li.getSumParamLikelihood();
		out[par.length + 2] = getPosterior();

		out[par.length + 3] = gp.getAlphaPi()*gp.getPiMu();
		out[par.length + 4] = gp.getAlphaPi()*gp.getPiSd();
		out[par.length + 5] = gp.getAlphaRho()*gp.getRhoMu();
		out[par.length + 6] = gp.getAlphaRho()*gp.getRhoSd();
		out[par.length + 7] = gp.getAlphaMu()*gp.getMeanMu();
		out[par.length + 8] = gp.getAlphaMu()*gp.getMeanSd();

		return out;
		
	}

	public double[] getLocalOutput() {

		double[] outD = new double[noSpot];
		double[] outRho = new double[noSpot];
		double[] outLikeli = new double[noSpot];

		for (int i = 0; i < noSpot; i++) {
			outD[i] = spArray[i].getD();
			outRho[i] = spArray[i].getRho();
			outLikeli[i] = li.getEachLikelihood(i);
		}

		double[] outAll = new double[outD.length + outRho.length
				+ outLikeli.length];
		System.arraycopy(outD, 0, outAll, 0, outD.length);
		System.arraycopy(outRho, 0, outAll, outD.length, outRho.length);
		System.arraycopy(outLikeli, 0, outAll, outD.length + outRho.length,
				outLikeli.length);

		return outAll;
	}

	public double[] getLocalOutputAll() {

		double[] outMu = new double[noSpot];
		double[] outD = new double[noSpot];
		double[] outPi = new double[noSpot];
		double[] outRho = new double[noSpot];
		double[] outLikeli = new double[noSpot];

		for (int i = 0; i < noSpot; i++) {
			outMu[i] = spArray[i].getMu1();
			outD[i] = spArray[i].getD();
			outPi[i] = spArray[i].getPi();
			outRho[i] = spArray[i].getRho();
			outLikeli[i] = li.getEachLikelihood(i);
		}

		double[] outAll = new double[outD.length * 5];
		System.arraycopy(outMu, 0, outAll, 0, noSpot);
		System.arraycopy(outD, 0, outAll, noSpot, noSpot);
		System.arraycopy(outPi, 0, outAll, noSpot * 2, noSpot);
		System.arraycopy(outRho, 0, outAll, noSpot * 3, noSpot);
		System.arraycopy(outLikeli, 0, outAll, noSpot * 4, noSpot);

		return outAll;
	}

	public double[] getLocalOutput(int j) {

		// TODO change output format for each spot
		// double[] out = {spArray[j].getD(), spArray[j].getRho(),
		// li.getEachLikelihood(j)};
		double[] out = new double[SpotPar.getNoPar() + 1];
		double[] temp = spArray[j].getPar();
		System.arraycopy(temp, 0, out, 0, 4);
		out[4] = li.getEachLikelihood(j);

		return out;
	}

	public GlobalPar getGlobalPar() {
		return gp;
	}

	public SpotPar[] getSpotPar() {
		return spArray;
	}

	public Likelihood getLikelihood() {
		return li;
	}

	public double getPosterior() {
		return li.getPosterior();
	}

	public double getGlobalLikelihood() {
		return li.getGlikelihood();
	}

	public double getLimDet() {
		return limDet;
	}

	// private GammaDistribution gd = new GammaDistribution();
	// private NormalDistribution nd = new NormalDistribution();
	// private NormalDistribution nd2 = new NormalDistribution();
	// private GammaDistribution gd = new GammaDistribution();

	public int getNoSpot() {
		return noSpot;
	}

	public SpotPar getEachSpotPar(int i) {

		return spArray[i];
	}

	public String[] getGlobalLables() {

		return GlobalPar.GLOBAL_LABELS;
	}

	public double[] getParamOutput(int j) {

		double[] out = new double[noSpot];
		// double[] outD = new double[noSpot];
		// double[] outPi = new double[noSpot];
		// double[] outRho = new double[noSpot];
		// double[] outLikeli = new double[noSpot];

		// for (int i = 0; i < noSpot; i++) {
		// out[i] = spArray[i].getMu1();
		//		
		//		
		// outD[i] = spArray[i].getD();
		// outPi[i] = spArray[i].getPi();
		// outRho[i] = spArray[i].getRho();
		// outLikeli[i] = li.getEachLikelihood(i);
		// }
		if (j == 0) {

			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getMu1();
			}

		} else if (j == 1) {

			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getD();
			}
		} else if (j == 2) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getPi();
			}
		} else if (j == 3) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getRho();
			}
		} else if (j == 4) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getLikelihood();
			}
		}
		return out;
	}



	// private double pro0, pro1, newMu, newD, newK, newG, newSD, newPost,
	// newLikeli, temp;

}
