package gml.math;

import flanagan.analysis.Stat;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.CauchyDistributionImpl;

public class CauchyDistribution implements Distribution {

	CauchyDistributionImpl cDist ;
	
    private double m = 0;
    private double s = 1;
    
	
	@Override
	public double logPdf(double x) {
	
		return logPdf(x, m, s);
	}

	@Override
	public double pdf(double x) {

		return pdf(x, m, s);
	}

	@Override
	public double mean() {
	
		return Double.NaN;
	}



	@Override
	public double variance() {

		return Double.NaN;
	}

    public CauchyDistribution(){
        
        this.m = 0;
        this.s = 1;
    }
    
    /**
     * Create a cauchy distribution using the given median and scale.
     * @param median median for this distribution
     * @param s scale parameter for this distribution
     */
    public CauchyDistribution(double median, double s){
        
        this.m = median;
        this.s = s;
    }

    /**
     * For this distribution, X, this method returns P(X &lt; <code>x</code>).
     * @param x the value at which the CDF is evaluated.
     * @return CDF evaluted at <code>x</code>. 
     */
    public double cumulativeProbability(double x) {
        return 0.5 + (Math.atan((x - m) / s) / Math.PI);
    }
    

    /**
     * For this distribution, X, this method returns the critical point x, such
     * that P(X &lt; x) = <code>p</code>.
     * <p>
     * Returns <code>Double.NEGATIVE_INFINITY</code> for p=0 and 
     * <code>Double.POSITIVE_INFINITY</code> for p=1.</p>
     *
     * @param p the desired probability
     * @return x, such that P(X &lt; x) = <code>p</code>
     * @throws IllegalArgumentException if <code>p</code> is not a valid
     *         probability.
     */
    
    public double inverseCumulativeProbability(double p) {
        double ret;
        if (p < 0.0 || p > 1.0) {
            throw MathRuntimeException.createIllegalArgumentException(
                  "{0} out of [{1}, {2}] range", p, 0.0, 1.0);
        } else if (p == 0) {
            ret = Double.NEGATIVE_INFINITY;
        } else  if (p == 1) {
            ret = Double.POSITIVE_INFINITY;
        } else {
            ret = m + s * Math.tan(Math.PI * (p - .5));
        }
        return ret;
    }
    
    public static double pdf (double x, double m, double s){
    	
    	return Stat.lorentzianPDF(m, s, x);
    }

    public static double logPdf(double x, double m, double s) {
	
		return Math.log(pdf(x,m,s));
	}
}
