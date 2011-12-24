package gml.prior;

import gml.math.GammaDistribution;
public class PriorGamma implements PriorDist {

	
	private double shape;
	private double scale;

	public PriorGamma(double shape, double scale){
		
		this.shape = shape;
		this.scale = scale;
	
	}


	
	@Override
	public double getLogPrior(double x) {
		
		return GammaDistribution.logPdf(x, shape, scale);
	}
	
	

	@Override
	public String getPriorName() {
	
		return "Gamma";
	}



	@Override
	public double logPdf(double x) {
		return GammaDistribution.logPdf(x,  shape, scale);
	}



}
