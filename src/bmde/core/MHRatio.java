package bmde.core;

import bmde.math.MathLogDouble;

/**
 * @author steven calculate the Metropolis-Hastings accepting ratio
 */
public class MHRatio {

	/**
	 * @param theta0
	 *            P(theta | theta_proposed)
	 * @param theta1
	 *            p(theta_proposed | theta)
	 * @param prob0
	 *            P(theta | data)
	 * @param prob1
	 *            p(theta_proposed | data)
	 * @return
	 */


	public static boolean acceptTemp(double theta0, double theta1, double post0,
			double post1, double temperature) {

		double alpha = post1 - post0 + theta0 - theta1;
		alpha = alpha * temperature;

		if (alpha > 0)
			alpha = 0.0;
		boolean accept = MathLogDouble.nextLogDouble() < alpha;
	
		return accept;

	}
	
	public static boolean accept(double theta0, double theta1, double post0,
			double post1) {

		double alpha = post1 - post0 + theta0 - theta1;
		if (alpha > 0)
			alpha = 0.0;
		boolean accept = MathLogDouble.nextLogDouble() < alpha;
	
		return accept;

	}
	public static boolean accept(double post0,	double post1) {

	
		return accept(0, 0 ,post0, post1);

	}
}
