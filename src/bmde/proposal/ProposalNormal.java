package bmde.proposal;

import bmde.math.NormalDistribution;

public class ProposalNormal implements ProposalDist {

	/**
	 * @param x
	 * @param tune
	 * @return [0] new X [1] p(x | new X) at the top of the M-H ratio [2] p(new
	 *         X | x) at the bottom of the M-H ratio
	 * 
	 */
	public static double[] nextValue(double x, double tune) {

		double[] newX = { rNumber.nextGaussian(x, tune), 0, 0 };

		return newX;

	}

	public static double[] nextTruncatedValue(double x, double tune,
			double lower, double upper) {

		double[] newX = new double[3];
		// dist.setParam(x, tune);
		try {
			newX[0] = rNumber.nextGaussian(x, tune);
			while (newX[0] > upper || newX[0] < lower) {
				newX[0] = rNumber.nextGaussian(x, tune);
			}
			// p(x | newX)
			// dist.setMean(newX[0]);
			double limit = Math.log(NormalDistribution
					.cdf(upper, newX[0], tune)
					- NormalDistribution.cdf(lower, newX[0], tune));
			newX[1] = NormalDistribution.logPdf(x, newX[0], tune) - limit;

			// p(newX | x)
			limit = Math.log(NormalDistribution.cdf(upper, x, tune)
					- NormalDistribution.cdf(lower, x, tune));
			newX[2] = NormalDistribution.logPdf(newX[0], x, tune) - limit;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newX;

	}

}
