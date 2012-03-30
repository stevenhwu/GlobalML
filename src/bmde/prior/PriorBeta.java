package bmde.prior;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.special.Gamma;

public class PriorBeta implements PriorDist {

	BetaDistribution bDist;

	/** First shape parameter. */
	private double alpha;

	/** Second shape parameter. */
	private double beta;

	/**
	 * Normalizing factor used in density computations. updated whenever alpha
	 * or beta are changed.
	 */
	private double z;

	/**
	 * Build a new instance.
	 * 
	 * @param alpha
	 *            first shape parameter (must be positive)
	 * @param beta
	 *            second shape parameter (must be positive)
	 */

	public PriorBeta(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
		z = Double.NaN;
		bDist = new BetaDistribution(alpha, beta);

	}

	@Override
	public double logPdf(double x) {
		return logPdf(x, alpha, beta);
	}

	public static double logPdf(double x, double alpha, double beta){
	
		double z = recomputeZ(alpha, beta);

		double logX = Math.log(x);
		double log1mX = Math.log1p(-x);
		return ((alpha - 1) * logX + (beta - 1) * log1mX - z);
	}

	/**
	 * Recompute the normalization factor.
	 */
	private void recomputeZ() {
		if (Double.isNaN(z)) {
			z = Gamma.logGamma(alpha) + Gamma.logGamma(beta)
					- Gamma.logGamma(alpha + beta);
		}
	}
	
	private static double recomputeZ(double alpha, double beta) {
        
        return ( Gamma.logGamma(alpha) + Gamma.logGamma(beta) - Gamma.logGamma(alpha + beta) );
        
    }

	public double pdf(double x) {
		return pdf(x,alpha,beta);

	}

	public static double pdf(double x, double alpha, double beta) {
		double z = recomputeZ(alpha, beta);

		double logX = Math.log(x);
		double log1mX = Math.log1p(-x);
		return Math.exp((alpha - 1) * logX + (beta - 1) * log1mX - z);

	}

	@Override
	public double getLogPrior(double x) {

		return logPdf(x);
	}

	@Override
	public String getPriorName() {

		return "Beta";
	}


}
