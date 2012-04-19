package bmde.hpd;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import dr.stats.DiscreteStatistics;

/**
 * A class that stores the distribution statistics for a trace
 * 
 * @author Andrew Rambaut
 * @author Alexei Drummond
 * @version $Id: TraceDistribution.java,v 1.1.1.2 2006/04/25 23:00:09 rambaut
 *          Exp $
 */
public class TraceDistribution {

	public TraceDistribution(double[] values, double hpdValue, double burnin) {
		analyseDistribution(values, hpdValue, burnin);

	}

	public boolean isValid() {
		return isValid;
	}

	public double getMean() {
		return mean;
	}

	public double getMedian() {
		return median;
	}

	public double getLowerHPD() {
		return hpdLower;
	}

	public double getUpperHPD() {
		return hpdUpper;
	}

	public double getLowerCPD() {
		return cpdLower;
	}

	public double getUpperCPD() {
		return cpdUpper;
	}


	public double getMinimum() {
		return minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	/**
	 * Analyze trace
	 * @param hpdValue TODO
	 */
	private void analyseDistribution(double[] values, double hpdValue, double burnin) {

		int start = (int) (values.length * burnin);
		values = ArrayUtils.subarray(values, start, values.length);
//		mean = DiscreteStatistics.mean(values);
		mean = StatUtils.mean(values);
		minimum = Double.POSITIVE_INFINITY;
		maximum = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < values.length; i++) {
			if (values[i] < minimum)
				minimum = values[i];
			if (values[i] > maximum)
				maximum = values[i];
		}

		if (maximum == minimum) {
			isValid = false;
			return;
		}

		int[] indices = new int[values.length];
		HeapSort.sort(values, indices);
		median = statQuantile(0.5, values, indices);
		cpdLower = statQuantile(0.025, values, indices);
		cpdUpper = statQuantile(0.975, values, indices);
		calculateHPDInterval(hpdValue, values, indices);

		isValid = true;
	}

	public static double[] HPDInterval(double proportion, double[] x, int[] indices) {

        double minRange = Double.MAX_VALUE;
        int hpdIndex = 0;

        final int diff = (int) Math.round(proportion * (double) x.length);
        for (int i = 0; i <= (x.length - diff); i++) {
            final double minValue = x[indices[i]];
            final double maxValue = x[indices[i + diff - 1]];
            final double range = Math.abs(maxValue - minValue);
            if (range < minRange) {
                minRange = range;
                hpdIndex = i;
            }
        }

        return new double[]{x[indices[hpdIndex]], x[indices[hpdIndex + diff - 1]]};
    }
    private void calculateHPDInterval(double proportion, double[] array, int[] indices) {
        final double[] hpd = DiscreteStatistics.HPDInterval(proportion, array, indices);
        hpdLower = hpd[0];
        hpdUpper = hpd[1];
    }


	// ************************************************************************
	// private methods
	// ************************************************************************

	protected boolean isValid = false;

	protected double minimum, maximum;
	protected double mean, median;
	protected double cpdLower, cpdUpper;
	protected double hpdLower, hpdUpper;
	
	public static double statQuantile(double q, double[] x, int[] indices) {
		if (q < 0.0 || q > 1.0)
			throw new IllegalArgumentException("Quantile out of range");

		if (q == 0.0) {
			// for q==0 we have to "invent" an entry smaller than the smallest x

			return x[indices[0]] - 1.0;
		}

		return x[indices[(int) Math.ceil(q * indices.length) - 1]];
	}
}