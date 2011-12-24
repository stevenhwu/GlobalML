package gml.core;
@SuppressWarnings("unused")
public class Setting {

	public static final String LOCAL = "LOCAL";
	public static final String GLOBAL = "GLOBAL";
	
	
	private static final double ZERO = 0;
	private static final double ONE = 1;
	private static final double TEN = 10;
	
	private static double priorMuMu;
	private static double priorMuSd;
	private static double priorMuShape;
	private static double priorMuScale;
	
	private static double priorLambdaShape; 
	private static double priorLambdaScale;
	private static double phiLower;
	private static double phiUpper;

	private static double gapLower = -2;
	private static double gapUpper = 2;
	
	
	private static double priorProbMu;
	private static double priorProbSd;
	private static double priorProbShape;
	private static double priorProbScale;
	
	
	private static double betaUpper = 10;
	private static double betaLower = 0;

	private static double probSdCap = 10;
	private static double sdCap = 10;

	private static double normalSd;
	private static double normalMean;
	


//	public static void useDefault(){
//		
//		
//		setupPriorMu(0, 5);
//		setupPriorSd(0.01, 0.01, 5);
//
//		setGap(2);
//		setupPriorExp(0.01, 100);
//		setupPhiRange(0.01, 0.99);
//		
//		setupProbMu(0, 5);
//		setupProbSd(0.01, 0.01);
//		
//	}
	
	public static void setGap(double gap) {
		gapLower = -gap;
		gapUpper = gap;
	}
		
	public static void setupNormal(double m, double sd) {
		normalMean = m;
		normalSd = sd;
		
	}

	public static void setupPriorMu(double m, double sd) {
		priorMuMu = m;
		priorMuSd = sd;
		
	}
	
	public static void setupPriorSd(double shape, double scale, double upper){
		priorMuShape = shape;
		priorMuScale = scale;
		sdCap = upper;
	}

	public static void setupPriorExp(double shape, double scale) {
		priorLambdaShape = shape;
		priorLambdaScale = scale;
		
	}

	
	public static void setupPhiRange(double lower, double upper){
		if(lower <=0 ){
			lower = 10E-2;
		}
		if(upper >=1 ){
			upper = 1-10E-2;
		}
		phiLower = lower;
		phiUpper = upper;
		
	}

	public static void setupProbMu(double m, double sd) {
		priorProbMu = m;
		priorProbSd = sd;
		
	}
	
	public static void setupProbSd(double shape, double scale) {
		priorProbShape = shape;
		priorProbScale = scale;
		
	}



	public static void setupBeta(double lower, double upper) {
		setBetaUpper(upper);
		setBetaLower(lower);
		
	}

	public static void setBetaLower(double lower) {
		betaLower = lower;
	}

	public static void setBetaUpper(double upper) {
		betaUpper = upper;
	}

	public static double getPriorMuMu() {
		return priorMuMu;
	}

	public static double getPriorMuSd() {
		return priorMuSd;
	}

	public static double getPriorMuShape() {
		return priorMuShape;
	}

	public static double getPriorMuScale() {
		return priorMuScale;
	}
	
	public static double getPriorLambdaShape() {
		return priorLambdaShape;
	}

	public static double getPriorLambdaScale() {
		return priorLambdaScale;
	}

	public static double getPhiLower() {
		return phiLower;
	}

	public static double getPhiUpper() {
		return phiUpper;
	}

	public static double getGap() {
		return gapUpper;
	}
	public static double getGapUpper() {
		return gapUpper;
	}

	public static double getGapLower() {
		return gapLower;
	}
	

	public static double getPriorProbMu() {
		return priorProbMu;
	}

	public static double getPriorProbSd() {
		return priorProbSd;
	}

	public static double getPriorProbShape() {
		return priorProbShape;
	}

	public static double getPriorProbScale() {
		return priorProbScale;
	}
	




	public static double getProbSdCap() {
		return probSdCap;
	}

	public static void setProbSdCap(double probSdCap) {
		Setting.probSdCap = probSdCap;
	}

	public static double getSdCap() {
		return sdCap;
	}

	public static void setSdCap(double sdCap) {
		Setting.sdCap = sdCap;
	}

	public static double getBetaUpper() {
		return betaUpper;
	}

	public static double getBetaLower() {
		return betaLower;
	}

	public static double[] getPriorSummary() {
		double[] priorSummary = {
		Setting.getPriorMuMu(),
		Setting.getPriorMuSd(),
		Setting.getPriorMuShape(),
		Setting.getPriorMuScale(),
		Setting.getSdCap(),
		Setting.getPriorLambdaShape(),
		Setting.getPriorLambdaScale(),
		Setting.getPhiLower(),
		Setting.getPhiUpper(),
		Setting.getPriorProbMu(),
		Setting.getPriorProbSd(),
		Setting.getPriorProbShape(),
		Setting.getPriorProbScale() };
		return priorSummary;
	}



	

}
