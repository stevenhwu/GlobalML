package gml.math;

public class Transformation {

	/**
	 * transform probability to real number by log(p/(1-p))
	 * @param probability [0,1]
	 * @return real number
	 */
	public static double logit(double p) {
		
		double y = Math.log(p / (1.0-p) );
		return y;
		
	}
	
	/** 
	 * transform real number to probability [0, 1]
	 * @param real number
	 * @return probability [0,1]
	 */
	public static double invLogit(double y) {
		double t = Math.exp(y);
		double p = t/(1.0+t);
		return p;
	}
}
