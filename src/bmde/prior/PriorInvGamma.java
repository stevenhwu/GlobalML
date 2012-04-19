package bmde.prior;

import bmde.math.GammaDistribution;

public class PriorInvGamma implements PriorDist {

	private double shape;
	private double rate;
	private double scale;
	
	public PriorInvGamma(double shape, double rate){
		this.shape = shape;
//		this.rate = rate;
		this.scale = 1/rate;
	}
	


	@Override
	public double getLogPrior(double x) {
//		org.apache.commons.math3.distribution.GammaDistribution g = new org.apache.commons.math3.distribution.GammaDistribution(0.001, 0.001);
//		System.out.println(x +"\t"+ 
//				(GammaDistribution.logPdf(1/x, 0.001, 1/0.001)) +"\t"+ Math.log(g.density(1/x)) +"\t"+ 
//				(InverseGammaDistribution.logPdf(x, 0.001, 0.001))); 
		
		return GammaDistribution.logPdf(1.0/x, shape, scale);
	}


	public double pdf(double x) {
		return GammaDistribution.pdf(1.0/x, shape, scale);
	}


	@Override
	public String getPriorName() {
		return "Inverse Gamma";
	}

	@Override
	public double logPdf(double x) {
		return logPdf(x);
	}

}
