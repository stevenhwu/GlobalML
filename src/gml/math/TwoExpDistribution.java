package gml.math;



public class TwoExpDistribution implements Distribution {


	private double lambda;
	private double phi;
	private double oneMinusPhi;

	public TwoExpDistribution(double lambda, double phi) {

		updateDist(lambda, phi);
		// setLimit(gap);
	}

	//
	 public TwoExpDistribution() {
		 updateDist(1,0.5);
	 }

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPhi() {

		return phi;
	}

	public void setPhi(double newPhi) {
		updateDist(lambda, newPhi);
		// proportionDE = (1 - phi) / 2;

	}


	public void updateDist(double lambda, double phi) {

		this.lambda = lambda;
		this.phi = phi;
		oneMinusPhi = 1 - phi;
		// proportionDE = (1 - phi) / 2;
	}

	@Override
	public double pdf(double x) {

		double p = 0;
		if (x > 0) {
			p = phi * ExponentialDistribution.pdf(x, lambda);
		} else {

			p = oneMinusPhi * ExponentialDistribution.pdf(-x, lambda);
		}
		return p;
	}

	@Override
	public double logPdf(double x) {
		return logPdf(x, lambda, phi);
	}

	@Override
	public double mean() {
		
		return Double.NaN;
	}

	@Override
	public double variance() {
	
		return Double.NaN;
	}


	public static double logPdf(double x, double lambda, double phi) {

		if (phi < 0 | phi > 1) {
			return 0;
		} else {

			double p;
			if (x > 0) {
				p = Math.log(phi) + ExponentialDistribution.logPdf(x, lambda);
			} else {
				p = Math.log(1 - phi)
						+ ExponentialDistribution.logPdf(-x, lambda);
			}
			
			return p;
		}
	}

}
