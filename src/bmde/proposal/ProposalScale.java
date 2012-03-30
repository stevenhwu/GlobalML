package bmde.proposal;

public class ProposalScale implements ProposalDist {

	public static double[] nextValue(double x, double tune) {

		double scale = (tune + (rNumber.nextUniform(0, 1) * ((1.0 / tune) - tune)));
		double[] newX = { x * scale, 0, 0 };
		return newX;

	}

	public static double[] nextTruncatedValue(double x, double tune,
			double lower, double upper) {

		double[] newX = new double[3];

		do {

			double scale = (tune + (rNumber.nextUniform(0, 1) * ((1.0 / tune) - tune)));
			newX[0] = x * scale;

		} while (newX[0] > upper || newX[0] < lower);

		newX[1] = 0;
		newX[2] = 0;

		return newX;

	}

}
