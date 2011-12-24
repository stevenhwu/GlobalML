package gml.math;

import gml.core.Setting;

public class ExpUniExpDistribution implements Distribution {

	private double gapUpper;
	private double gapLower;

	private double lambdaDown;
	private double lambdaUp;
	private double phi;
	private double proportionDE;
	private double phiUpper;
	private double phiLower;

	public ExpUniExpDistribution(double lambdaDown, double lambdaUp,
			double phi, double gap) {

		updateDist(lambdaDown, lambdaUp, phi);
		setLimit(gap);
	}

	public ExpUniExpDistribution() {
		setLimit(Setting.getGapLower(), Setting.getGapUpper());
	}

	public double getLambdaUp() {
		return lambdaUp;
	}

	public void setLambdaUp(double lambdaUp) {
		this.lambdaUp = lambdaUp;
	}

	public double getlambdaDown() {
		return lambdaDown;
	}

	public void setlambdaDown(double lambdaDown) {
		this.lambdaDown = lambdaDown;
	}

	public double getPhi() {

		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
		proportionDE = (1 - phi) / 2;

	}

	public void setLimit(double gap) {
		gapUpper = gap;
		gapLower = -gap;
	}

	public void setLimit(double lower, double upper) {

		gapLower = lower;
		gapUpper = upper;
	}

	public void updateDist(double lambdaDown, double lambdaUp, double phi) {

		this.lambdaDown = lambdaDown;
		this.lambdaUp = lambdaUp;
		this.phi = phi;
		proportionDE = (1 - phi) / 2;
	}

	@Override
	public double pdf(double x) {

		double p = 0;
		if (x > gapUpper) {
			p = proportionDE
					* ExponentialDistribution.pdf(x - gapUpper, lambdaUp);
		} else if (x < gapLower) {
			p = proportionDE
					* ExponentialDistribution.pdf(gapLower - x, lambdaDown);
		} else {
			p = phi * UniformDistribution.pdf(x, gapLower, gapUpper);
		}
		return p;
	}

	@Override
	public double logPdf(double x) {
		return logPdf(x, lambdaDown, lambdaUp, phi, gapLower, gapUpper);
	}

	@Override
	public double mean() {
		
		return Double.NaN;
	}

	@Override
	public double variance() {
	
		return Double.NaN;
	}
//	public static double logPdf(double x, double lambdaDown, double lambdaUp,
//			double phi) {
//		return logPdf(x, lambdaDown, lambdaUp, phi, gapUpper);
//	}
	public static double logPdf(double x, double lambdaDown, double lambdaUp,
			double phi, double gap) {
		return logPdf(x, lambdaDown, lambdaUp, phi, -gap, gap);
		
	}
	
	public static double logPdf(double x, double lambdaDown, double lambdaUp,
			double phi, double gapLower, double gapUpper) {

		if (phi < 0 | phi > 1) {
			return 0;
		} else {
			double proportionDE = (1.0 - phi) / 2;
			double p;
			double logProportionDE = Math.log(proportionDE);
			if (x > gapUpper) {
				p = logProportionDE
						+ ExponentialDistribution
								.logPdf(x - gapUpper, lambdaUp);
			} else if (x < gapLower) {
				p = logProportionDE
						+ ExponentialDistribution.logPdf(gapLower - x,
								lambdaDown);
			} else {
				p = Math.log(phi)
						+ UniformDistribution.logPdf(x, gapLower, gapUpper);
			}
			return p;
		}
	}
	


}
