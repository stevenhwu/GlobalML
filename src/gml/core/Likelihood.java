package gml.core;

import gml.core.par.GlobalPar;
import gml.core.par.Spot;
import gml.core.par.SpotPar;
import gml.math.Constant;
import gml.math.NormalDistribution;
import gml.math.TwoExpDistribution;
import gml.math.modelDist.GlobalDeltaDistribution;
import gml.math.modelDist.GlobalMeanDistribution;
import gml.math.modelDist.ProbabilityOfExpression;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.math.stat.StatUtils;

/**
 * @author steven
 * 
 *         P(theta| spots) ~ P(spots | gp, sp)P(sp | gp)P(gp)
 */

public class Likelihood {

	
	private double gapLower;
	private double gapUpper;
	private double upperLim;
	private double lowerLim;
	private double glikelihood;
	private double posterior;
	private double[] eachLikelihood;

	// private ProbabilityOfExpression probExp = new ProbabilityOfExpression();
	// private NormalDistribution nd = new NormalDistribution();
	// private ExpUniExpDistribution eueDist = new ExpUniExpDistribution();
	// private InverseGammaDistribution igd = new InverseGammaDistribution();

	private HashMap<String, Double> priorProb = new HashMap<String, Double>();
	private HashMap<String, Double> paramLikelihood = new HashMap<String, Double>();
	private String[] fixLists;

	private int noSpot;

	private double gScale;
	private double scale;

	public Likelihood(int n, double limDet) {

		this.noSpot = n;
		eachLikelihood = new double[n];
		setUpperLim(Constant.GEL_MAX);
		setLowerLim(limDet);
		init();

		// TODO change scale factor for hot/cold chain

		scale = 1;
		gScale = 1 ;
	}

	public void init() {

		setGlikelihood(0);
		setPosterior(0);
		this.gapLower = Setting.getGapLower();
		this.gapUpper = Setting.getGapUpper();
		// eueDist.setLimit(gapLower, gapUpper);

		fixLists = new String[] { GlobalPar.GMUSD, GlobalPar.GDELTA,
				GlobalPar.GPI, GlobalPar.GRHO, GlobalPar.GSD };
		for (String string : fixLists) {
			priorProb.put(string, 0.0);
			paramLikelihood.put(string, 0.0);
		}

	}

	public void setLowerLim(double lowerLim) {
		this.lowerLim = lowerLim;
	}

	public void setUpperLim(double upperLim) {
		this.upperLim = upperLim;
	}

	public double calLogLikeli(SpotPar eachSp) {

		Spot eachSpot = eachSp.getSpot();
		double l = calLogLikeli(eachSpot, eachSp);

		return l;
	}

	public double calLogLikeli(Spot eachSpot, double... pars) {

		int i = 0;
		int noSpot = eachSpot.getNumberOfSpot();
		double l = calLogLikeli(eachSpot.getExpressControlSpot(), noSpot,
				pars[i++], pars[i++], pars[i++])
				+ calLogLikeli(eachSpot.getExpressCaseSpot(), noSpot,
						pars[i++], pars[i++], pars[i++]);
		return l;

	}

	private double calLogLikeli(Spot eachSpot, SpotPar eachSp) {

		int noSpot = eachSpot.getNumberOfSpot();
		double l = calLogLikeli(eachSpot.getExpressControlSpot(), noSpot,
				eachSp.getMu1(), eachSp.getProb1(), eachSp.getSd())
				+ calLogLikeli(eachSpot.getExpressCaseSpot(), noSpot, eachSp
						.getMu2(), eachSp.getProb2(), eachSp.getSd());
		return l;

	}

	public double calLogLikeli(double[] expSpot, int noTotal, double u,
			double p, double sd) {

		int express = expSpot.length;
		int notExpress = noTotal - express;

		// nd.setParam(u, sd);
		// nd.setBothLim(lowerLim, upperLim);
		// TODO clean up

		double logP = (p == 0) ? 0 : Math.log(p) * express; // n*log(p)+

		double logNotExp = (1 - p) + p
				* NormalDistribution.cdf(lowerLim, u, sd);

		logNotExp = (logNotExp == 0) ? 0 : Math.log(logNotExp) * notExpress;
		// nbar*(log( pbar+p*pnorm(lowB,mean=mu,sd=sd) ))

		double likeli = StatUtils.sum(NormalDistribution.logPdfBT(expSpot, u,
				sd, lowerLim, upperLim))
				+ logP + logNotExp;

		// likeli = StatUtils.sum(nd.logPdfBT(expSpot))+ //
		// sum(dBTnorm(spot,mean=mu,sd=sd,log=T,lowB=lowB,upB=upB) ,na.rm=T)+
		// express * logP + //n*log(p)+
		// notExpress * logNotExp ; //nbar*(log(
		// pbar+p*pnorm(lowB,mean=mu,sd=sd) ))

		return likeli * scale;

	}

	// old method
	// public double calLogLikeli(double[] spot, double u, double p, double sd){
	//		
	// int notExpress = 0;
	// ArrayList<Double> expressSpot = new ArrayList<Double>();
	// for (int i = 0; i < spot.length; i++) {
	// if(Double.isNaN(spot[i])){
	// notExpress++;
	// }
	// else {
	// expressSpot.add(spot[i]);
	// }
	// }
	// int express = spot.length-notExpress;
	// double[] expSpot = ArrayUtils.toPrimitive(expressSpot.toArray(new
	// Double[express]));
	// nd.setParam(u,sd);
	// nd.setLowerLim(lowerLim);
	// nd.setUpperLim(upperLim);
	//
	// double likeli = StatUtils.sum(nd.logPdfBT(expSpot))+ //
	// sum(dBTnorm(spot,mean=mu,sd=sd,log=T,lowB=lowB,upB=upB) ,na.rm=T)+
	// express * Math.log(p) + //n*log(p)+
	// notExpress * Math.log( (1-p)+p * nd.cdf(lowerLim) );
	// //nbar*(log( pbar+p*pnorm(lowB,mean=mu,sd=sd) ))
	// if( Double.isNaN(likeli)){
	// likeli=0;
	// }
	// return likeli;
	//	
	// }
	public void calGlobalLogLikelihood(SpotPar[] sp) {

		for (int i = 0; i < sp.length; i++) {
			setEachLikelihood(i, calLogLikeli(sp[i]));
		}
	}

	public void calGlobalLogLikelihood(SpotPar[] sp, GlobalPar gp) {

		for (int i = 0; i < sp.length; i++) {
			sp[i].setSd(gp);
			setEachLikelihood(i, calLogLikeli(sp[i]));
		}
	}

	public double[] returnGlobalLogLikelihood(SpotPar[] sp, double newSd) {

//		double l = 0;
		double[] newEachLikeli = new double[sp.length];
//		double newSdSpot = newSd * Math.sqrt(sp.length);
		double newSdSpot = newSd ;
		for (int i = 0; i < sp.length; i++) {

			double[] newPar = { sp[i].getMu1(), sp[i].getProb1(), newSdSpot,
					sp[i].getMu2(), sp[i].getProb2(), newSdSpot };
			newEachLikeli[i] = calLogLikeli(sp[i].getSpot(), newPar);
//			l += newEachLikeli[i]; 
		}
		
		return newEachLikeli;
	}

	public double getGlikelihood() {
		return glikelihood;
	}

	/**
	 * calculate sum( log( p(m_control_i | mu1) )) + log(p(mu1)) the likelihood
	 * for each parameter is independent
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliMu(SpotPar[] sp, double m, double sd) {
		// nd.setParam(m, sd);

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += NormalDistribution.logPdf(sp[i].getMu1(), m, sd);
			// System.out.println("mu:\t"+sp[i].getMu1()+"\tlogPdfMu:\t"+nd.logPdf(sp[i].getMu1()));
		}
//		l *= gScale;
		return l;

	}

	// use ExpUnifExp distribution
//	public double condLikeliDeltaOld(SpotPar[] sp, double lambdaDown,
//			double lambdaUp, double phi) {
//
//		// eueDist.updateDist(lambdaDown, lambdaUp, phi);
//		double l = 0;
//		for (int i = 0; i < sp.length; i++) {
//			l += ExpUniExpDistribution.logPdf(sp[i].getD(), lambdaDown,
//					lambdaUp, phi, gapLower, gapUpper);
//			// System.out.println("D:\t"+sp[i].getD()+"\t"+eueDist.logPdf(sp[i].getD()));
//		}
//		l *= gScale;
//		return l;
//	}

	public double condLikeliDelta(SpotPar[] sp, double lambda, double phi) {

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += TwoExpDistribution.logPdf(sp[i].getD(), lambda, phi);
			// System.out.println("D:\t"+sp[i].getD()+"\t"+eueDist.logPdf(sp[i].getD()));
		}
//		l *= gScale;
		return l;
	}

	/**
	 * Calculate sum ( log ( p1 | mu, sd) )
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliPi(SpotPar[] sp, double m, double sd) {

		// probExp.setParam(m, sd);
		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			
			l += NormalDistribution.logPdf(sp[i].getPi(), m, sd);
			}
//		l *= gScale;
		return l;
	}
	/**
	 * Calculate sum ( log ( p2 | mu, sd) )
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliRho(SpotPar[] sp, double m, double sd) {

		// probExp.setParam(m, sd);
		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += NormalDistribution.logPdf(sp[i].getRho(), m, sd);

		}
//		l *= gScale;
		return l;
	}

//	public double condLikeliSd(SpotPar[] sp, double shape, double scale) {
//
//		// igd.setParam(shape, scale);
//		double l = 0;
//		for (int i = 0; i < sp.length; i++) {
//			l += InverseGammaDistribution.logPdf(sp[i].getSd(), shape, scale);
//
//		}
//		l *= gScale;
//		return l;
//	}

	private void setPosterior(double n) {
		this.posterior = n;
	}

	private void setGlikelihood(double newGlikeli) {

		this.glikelihood = newGlikeli;
	}

	public double getPosterior() {
		return posterior;
	}

	public double getPriorProb(String key) {
		return priorProb.get(key);
	}

	public void putPriorProb(String key, double value) {

		posterior = posterior - priorProb.get(key) + value;
		priorProb.put(key, value);
	}

	public double getParamLikelihood(String key) {
		return paramLikelihood.get(key);
	}

	public void putParamLikelihood(String key, double value) {

		replaceLikelihood(paramLikelihood.get(key), value);
		paramLikelihood.put(key, value);

	}

	public double[] getEachLikelihood() {
		return eachLikelihood;
	}

	public double getEachLikelihood(int i) {
		return eachLikelihood[i];
	}

	public void setEachLikelihood(int i, double value) {

		replaceLikelihood(eachLikelihood[i], value);
		eachLikelihood[i] = value;
	}

	private void replaceLikelihood(double oldLikelihood, double newLikelihood) {

		double t =  newLikelihood - oldLikelihood;
		glikelihood = glikelihood + t;
		posterior = posterior +t;

		// glikelihood += eachLikelihood[n];
		// return glikelihood;

	}

	@Override
	public String toString() {
		return "Likelihood [eachLikelihood=" + Arrays.toString(eachLikelihood)
				+ "\n glikelihood=" + glikelihood + "\n paramLikelihood="
				+ paramLikelihood + "\n priorProb=" + priorProb
				+ "\n upperLim=" + upperLim + ", lowerLim=" + lowerLim
				+ "\n getGlikelihood()=" + getGlikelihood() + "]";
	}

	public String getParamLikelihood() {

		StringBuilder sb = new StringBuilder();
		for (String s : fixLists) {
			sb.append(s).append(": ").append(paramLikelihood.get(s)).append(
					"\t");
		}

		return sb.toString();
	}

	public String getPriorProb() {
		StringBuilder sb = new StringBuilder();
		for (String s : fixLists) {
			sb.append(s).append(": ").append(priorProb.get(s)).append("\t");
		}

		return sb.toString();

	}

	public void updateAllEachLikeli(double[] allNewEachLikeli) {
		for (int i = 0; i < allNewEachLikeli.length; i++) {
			setEachLikelihood(i,allNewEachLikeli[i]);
		}
		
	}

	public double getSumPriorProb() {
		double l = 0;
		for (String s : fixLists) {
			l += priorProb.get(s);
		}
		return l;
	}

	public double getSumParamLikelihood() {
		double l = 0;
		for (String s : fixLists) {
			l += paramLikelihood.get(s);
		}
		
		return l;
	}

}
