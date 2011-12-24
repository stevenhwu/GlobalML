package gml.prior;

import gml.math.NormalDistribution;

public class PriorNormal implements PriorDist{

	private double mean;
	private double sd;

	
	public PriorNormal(double mean, double sd){
		this.mean=mean;
		this.sd = sd;
		
	}
	
	public PriorNormal() {
		
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

	@Override
	public double getLogPrior(double x) {

		return NormalDistribution.logPdf(x, mean, sd);
	}


	public double pdf(double x) {
		return NormalDistribution.pdf(x, mean, sd);
	}



	@Override
	public String getPriorName() {
		
		return "Normal";
	}

	public double getMean() {
		return mean;
	}

	public double getSd() {
		return sd;
	}

	public static double getLogPrior(double x, double mean, double sd) {
		return NormalDistribution.logPdf(x, mean, sd);
	}

	@Override
	public double logPdf(double x) {
		return NormalDistribution.logPdf(x, mean, sd);
	}



	
	
}

