package bmde.prior;

import bmde.math.InverseGammaDistribution;

public class PriorInvGamma implements PriorDist {

	public double shape;
	public double scale;

	
	public PriorInvGamma(double shape, double scale){
		this.shape = shape;
		this.scale = scale;
		
	}
	
	public PriorInvGamma() {
		
	}

	public void setShape(double shape) {
		 this.shape = shape;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public double getLogPrior(double x) {
		return InverseGammaDistribution.logPdf(x, shape, scale);
	}


	public double pdf(double x) {
		return InverseGammaDistribution.pdf(x, shape, scale);
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
