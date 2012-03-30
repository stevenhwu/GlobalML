package bmde.core.par;

import java.util.ArrayList;

import org.apache.commons.math3.random.RandomDataImpl;

import bmde.core.Likelihood;
import bmde.core.MHRatio;
import bmde.math.Constant;
import bmde.math.NormalDistribution;
import bmde.math.Transformation;
import bmde.math.TwoExpDistribution;

public class SpotPar {

	static RandomDataImpl r = new RandomDataImpl();

	public static final String[] SPOT_LABELS = { "Ite", "mu", "d", "pi", "rho",
			"likleihood" };
	public static final String[] SPOT_LABELS_SUM = { "Ite", "d", "rho",
			"likleihood" };

	public static final int INDEX_MU = 0;
	public static final int INDEX_PROB = 2;

	private static int noLocalPar = 4;

	private double mu1;
	private double d;
	private double mu2;
	private double pi;
	private double rho;
	private double sd;
	private double prob1;
	private double prob2;

	private int spotIndex;

	private double tuneSd = 1;
	private double temperature = 1;

	// private double limit;
	private Spot thisSpot;
	// private double spotLikelihood;
	private double spotPosterLi;

	// private double spotPriorLi;

	public SpotPar(double limDet, Spot s, GlobalPar gp, Likelihood li, int index) {
		// limit = limDet;
		this.thisSpot = s;
		this.spotIndex = index;
		reset(limDet);
		setupLikeli(gp, li);
	}

	public SpotPar(double mu1, double d, double pi, double rho, double sd) {

		this.mu1 = mu1;
		this.d = d;
		// this.mu2 = mu1 + d;
		this.pi = pi;
		this.rho = rho;
		this.sd = sd;
		reCalcProb();
	}

	public static SpotPar[] init(double limDet, ArrayList<Spot> allSpots,
			GlobalPar gp, Likelihood li) {

		int n = allSpots.size();
		SpotPar[] allSp = new SpotPar[n];

		for (int i = 0; i < allSp.length; i++) {
			allSp[i] = new SpotPar(limDet, allSpots.get(i), gp, li, i);

		}

		return allSp;
	}

	public void setupLikeli(GlobalPar gp, Likelihood li) {
		setSd(gp);

		double tmpLikeli = li.calLogLikeli(this);
		li.setEachLikelihood(spotIndex, tmpLikeli);
		spotPosterLi = tmpLikeli + calculatePrior(gp);

	}

	/**
	 * For testing only
	 */
	public static SpotPar[] init(int n, double limDet) {

		SpotPar[] allSp = new SpotPar[n];
		for (int i = 0; i < allSp.length; i++) {
			allSp[i] = new SpotPar(limDet);

		}
		return allSp;
	}

	private SpotPar(double limDet) {
		reset(limDet);
	}

	public void reset(double limDet) {

		// mu1 = r.nextUniform(limDet, Constant.GEL_MAX);
		mu1 = r.nextUniform(limDet / 4, 0);
		d = r.nextUniform(-1, 1);
		pi = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		rho = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		sd = r.nextUniform(Constant.MIN_SD, Constant.MAX_SD);
		mu2 = getMu2();
		// d = mu2 - mu1;
		reCalcProb();

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		return (sb.append("Mu1:\t").append(mu1).append("\tMu2:\t").append(
				getMu2()).append("\tProb1:\t").append(prob1).append(
				"\tProb2:\t").append(prob2).append("\tsd:\t").append(sd)
				.append("\n").append(thisSpot.toString()).toString());
	}

	public void updateLocalPar(GlobalPar gp, Likelihood li, double[] eachTune) {

		setSd(gp);
		updateMuD(gp, li, eachTune[0]);
		updatePiRho(gp, li, eachTune[1]);

	}

	public void updateMuD(GlobalPar gp, Likelihood li, double tune) {

		double limDet = gp.getLimDet();

		double newMu = NormalDistribution.randomDist(mu1, tune);
		double newD = NormalDistribution.randomDist(d, tune * tuneSd);
		double temp = newMu + newD;

		while (temp > Constant.GEL_MAX || temp < limDet
				|| newMu > Constant.GEL_MAX || newMu < limDet) {

			newMu = NormalDistribution.randomDist(mu1, tune);
			newD = NormalDistribution.randomDist(d, tune * tuneSd);
			temp = newMu + newD;
		}

		double newXGivenX = NormalDistribution.logPdfBT(newMu, mu1, tune,
				limDet, Constant.GEL_MAX)
				+ NormalDistribution.logPdfBT(newD, d, tune * tuneSd, (limDet - mu1),
						(Constant.GEL_MAX - mu1));

		double xGivenNewX = NormalDistribution.logPdfBT(mu1, newMu, tune,
				limDet, Constant.GEL_MAX)
				+ NormalDistribution.logPdfBT(d, newD, tune * tuneSd, (limDet - mu1),
						(Constant.GEL_MAX - mu1));

		double newLikeli = li.calLogLikeli(thisSpot, tempParMuDLikeli(newMu, newD));
		double newPrior = calculatePrior(tempParMuDPrior(newMu, newD), gp);
		double newPost = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				spotPosterLi, newPost, temperature);

		if (accept) {

			mu1 = newMu;
			d = newD;
			li.setEachLikelihood(spotIndex, newLikeli);// = newLikeli;
			spotPosterLi = newPost;

		}
	}

	public double[] tempParMuDLikeli(double newMu, double newD) {

		return new double[] { newMu, getProb1(), sd, newMu + newD, getProb2(),
				sd };

	}

	public double[] tempParMuDPrior(double newMu, double newD) {

		return new double[] { newMu, newD, pi, rho };

	}

	public void updatePiRho(GlobalPar gp, Likelihood li, double tune) {

		double newPi = NormalDistribution.randomDist(pi, tune);
		double newRho = NormalDistribution.randomDist(rho, tune);

		double newLikeli = li.calLogLikeli(thisSpot, tempParPiRhoLikeli(newPi,
				newRho));

		double newPrior = calculatePrior(tempParPiRhoPrior(newPi, newRho), gp);
		double newPost = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(0, 0, spotPosterLi, newPost,
				temperature);


		if (accept) {
			setPiRho(newPi, newRho);
			li.setEachLikelihood(spotIndex, newLikeli);
			// spotLikelihood = newLikeli;
			spotPosterLi = newPost;

		}
	}

	public double[] tempParPiRhoLikeli(double newPi, double newRho) {
		double[] probs = calcProb(newPi, newRho);
		return new double[] { mu1, probs[0], sd, getMu2(), probs[1], sd };
	}

	public double[] tempParPiRhoPrior(double newPi, double newRho) {

		return new double[] { mu1, d, newPi, newRho };
	}

	public double calculatePrior(GlobalPar gp) {

		double[] tempPar = { mu1, d, pi, rho };
		return calculatePrior(tempPar, gp);
	}

	public double calculatePrior(double[] tempPar, GlobalPar gp) {

		double prior = NormalDistribution.logPdf(tempPar[0], gp.getMeanMu()
				* gp.getAlphaMu(), gp.getMeanSd() * gp.getAlphaMu())
				+ TwoExpDistribution.logPdf(tempPar[1], gp.getLambda(), gp
						.getPhi())
				+ NormalDistribution.logPdf(tempPar[2], gp.getPiMu()
						* gp.getAlphaPi(), gp.getPiSd() * gp.getAlphaPi())
				+ NormalDistribution.logPdf(tempPar[3], gp.getRhoMu()
						* gp.getAlphaRho(), gp.getRhoSd() * gp.getAlphaRho());

		return prior;
	}

	public void setSpot(Spot s) {
		this.thisSpot = s;
	}

	public void setMu1(double mu1) {
		this.mu1 = mu1;
	}

	public void setD(double d) {
		this.d = d;
		// mu2 = mu1 + d;
	}

	public void setPi(double pi) {
		this.pi = pi;
		reCalcProb();
	}

	public void setRho(double rho) {
		this.rho = rho;
		reCalcProb();
	}

	public void setPiRho(double pi, double rho) {
		this.pi = pi;
		this.rho = rho;
		reCalcProb();
	}


	public void setSd(GlobalPar gp) {
		sd = gp.getSpotSd();
	}

	public double[] getPar() {
		double[] allPar = { mu1, d, pi, rho };

		return allPar;
	}

	public double[] getParLikeli(Likelihood li) {

		double[] allPar = { mu1, d, pi, rho, li.getEachLikelihood(spotIndex) };
		return allPar;
	}

	public static int getNoPar() {

		return noLocalPar;
	}

	public Spot getSpot() {
		return thisSpot;
	}

	public double getLikelihood() {
		return spotPosterLi; 
	}

	public double getPosterior() {
		return spotPosterLi;
	}

	// public double getPriorLi() {
	// return spotPriorLi;
	// }

	public double getMu1() {
		return mu1;
	}

	public double getMu2() {
		mu2 = mu1 + d;
		return mu2;
	}

	// public void setMu2(double mu2) {
	// this.mu2 = mu2;
	// }

	public double getD() {
		return d;
	}

	public double getPi() {
		return pi;
	}

	public double getRho() {
		return rho;
	}

	public double getProb1() {

		return prob1;
	}

	public double getProb2() {

		return prob2;
	}

	public double getSd() {
		return sd;
	}

	public int getSpotIndex() {
		return spotIndex;
	}

	private void reCalcProb() {
		prob1 = Transformation.invLogit(pi);
		prob2 = Transformation.invLogit(pi + rho);
	}

	public static double[] calcProb(double pi, double rho) {
		double[] probs = { Transformation.invLogit(pi),
				Transformation.invLogit(pi + rho) };
		return probs;

	}

}
