package gml.prior;




public interface PriorDist{

	public final static String PRIOR = "prior";
	public final static String UNIFORM = "uniform";
	public final static String TRUE = "true";
 
		/**
		 * @param 
		 * @return the log prior of some aspect of the given value
		 */
		public double getLogPrior(double x);

		/**
		 * Returns the logical name of this prior. 
		 * @return the logical name of this prior.
		 */
		public String getPriorName();
		
		public double logPdf(double x);
	
	
}
