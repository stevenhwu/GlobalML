package gml.proposal;

public class ProposalUniform implements ProposalDist {

	static double lower;
	static double upper;

	/**
	 * @param x
	 * @param tune
	 * @return [0] new X [1] p(x | new X) at the top of the M-H ratio [2] p(new
	 *         X | x) at the bottom of the M-H ratio
	 * 
	 */
	public static double[] nextValue(double lower, double upper) {

		double[] newX = new double[3];
		newX[0] = rNumber.nextUniform(lower, upper);

		// p(x | newX)
		newX[1] = 0;// dist.logPdf(x);

		// p(newX | x)
		newX[2] = 0;// dist.logPdf(newX[1]);

		return newX;

	}

}
