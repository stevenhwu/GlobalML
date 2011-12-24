package gml.prior;

import gml.math.ExponentialDistribution;

public class PriorExp implements PriorDist {

	private double lambda;

	public PriorExp(double lambda) {

		this.lambda = lambda;
	}

	public PriorExp() {
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPrior(double x) {
		return ExponentialDistribution.logPdf(x, lambda);
	}

	public double getLogPrior(double x, double y) {
		return getLogPrior(x) + getLogPrior(y);
	}

	@Override
	public double getLogPrior(double x) {
		return ExponentialDistribution.logPdf(x, lambda);
	}

	@Override
	public String getPriorName() {
		return "Exponential";
	}

	@Override
	public double logPdf(double x) {

		return logPdf(x);
	}

}
