package gml.prior;

import gml.math.UniformDistribution;


public class PriorUniform implements PriorDist {

	private double lower;
	private double upper;
	
	public PriorUniform(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public PriorUniform(){
		lower = 0;
		upper = 1;
	}
	public double getLogPrior(double x, double y){
		return getLogPrior(x)+getLogPrior(y);
	}
	@Override
	public double getLogPrior(double x) {
		return UniformDistribution.logPdf(x, lower, upper);
	}

	@Override
	public String getPriorName() {
		return "Uniform";
	}
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}

	@Override
	public double logPdf(double x) {
		return getLogPrior(x);
	}



}


