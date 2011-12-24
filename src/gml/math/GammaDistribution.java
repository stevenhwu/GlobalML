package gml.math;

/**
 * gamma distribution.
 * 
 * (Parameters: shape, scale; mean: scale*shape; variance: scale^2*shape)
 * 
 * @version $Id: GammaDistribution.java,v 1.9 2006/03/30 11:12:47 rambaut Exp $
 * 
 * @author Korbinian Strimmer
 */
public class GammaDistribution implements Distribution{

	private double shape;
	private double scale;
	private RandomNumberGenerator rg = new RandomNumberGenerator();

	public GammaDistribution() {
	}

	public GammaDistribution(double shape, double scale) {
		this.shape = shape;
		this.scale = scale;
	}

	public double getShape() {
		return shape;
	}

	public void setShape(double value) {
		shape = value;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double value) {
		scale = value;
	}

	public void setParam(double mean, double sd) {

		double t = mean / sd;
		shape = t * t;
		scale = mean / shape;
	}

	public double mean(double shape, double scale) {
		return scale * shape;
	}

	@Override
	public double mean() {
		return mean(shape, scale);
	}

	public double variance(double shape, double scale) {
		return scale * scale * shape;
	}

	@Override
	public double variance() {
		return variance(shape, scale);
	}

	/**
	 * probability density function of the Gamma distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param scale
	 *            scale parameter
	 * 
	 * @return pdf value
	 */

	@Override
	public double pdf(double x) {

		return pdf(x, shape, scale);
	}

	public double pdf(double x, double shape, double scale) {
		// return Math.pow(scale,-shape)*Math.pow(x, shape-1.0)/
		// Math.exp(x/scale + GammaFunction.lnGamma(shape));
		if (x < 0)
			throw new IllegalArgumentException();
		if (x == 0) {
			if (shape == 1.0)
				return 1.0 / scale;
			else
				return 0.0;
		}
		if (shape == 1.0) {
			return Math.exp(-x / scale) / scale;
		}

		double a = Math.exp((shape - 1.0) * Math.log(x / scale) - x / scale
				- GammaFunction.lnGamma(shape));

		return a / scale;
	}

	/**
	 * the natural log of the probability density function of the distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param scale
	 *            scale parameter
	 * 
	 * @return log pdf value
	 */
	@Override
	public double logPdf(double x) {

		return logPdf(x, shape, scale);
	}

	public static double logPdf(double x, double shape, double scale) {
		// double a = Math.pow(scale,-shape) * Math.pow(x, shape-1.0);
		// double b = x/scale + GammaFunction.lnGamma(shape);
		// return Math.log(a) - b;

		// AR - changed this to return -ve inf instead of throwing an
		// exception... This makes things
		// much easier when using this to calculate log likelihoods.
		// if (x < 0) throw new IllegalArgumentException();
		if (x < 0)
			return Double.NEGATIVE_INFINITY;

		if (x == 0) {
			if (shape == 1.0)
				return Math.log(1.0 / scale);
			else
				return Double.NEGATIVE_INFINITY;
		}
		if (shape == 1.0) {
			return (-x / scale) - Math.log(scale);
		}

		return ((shape - 1.0) * Math.log(x / scale) - x / scale - GammaFunction
				.lnGamma(shape))
				- Math.log(scale);
	}

	/**
	 * cumulative density function of the Gamma distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param scale
	 *            scale parameter
	 * 
	 * @return cdf value
	 */
	public double cdf(double x) {

		return cdf(x, shape, scale);
	}

	public static double cdf(double x, double shape, double scale) {
		return GammaFunction.incompleteGammaP(shape, x / scale);
	}

	/**
	 * random gamma distribution
	 * 
	 * @return
	 */
	// TODO require proper testing
	public double randomDist() {

		return rg.nextGamma(shape, scale);
	}




}
