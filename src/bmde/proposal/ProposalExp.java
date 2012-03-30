/**
 * 
 */
package bmde.proposal;

import bmde.math.ExponentialDistribution;

/**
 * @author steven
 * 
 */
public class ProposalExp implements ProposalDist {

	// private static ExponentialDistribution dist = new
	// ExponentialDistribution();

	/**
	 * @param x
	 * @param tune
	 * @return [0] new X [1] p(x | new X) at the top of the M-H ratio [2] p(new
	 *         X | x) at the bottom of the M-H ratio
	 * 
	 */
	public static double[] nextValue(double x) {

		double[] newX = new double[3];
		newX[0] = rNumber.nextExponential(1 / x);

		// p(x | newX)
		newX[1] = ExponentialDistribution.logPdf(x, newX[0]);

		// p(newX | x)
		newX[2] = ExponentialDistribution.logPdf(newX[0], x);

		return newX;

	}

}
