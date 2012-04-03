package bmde.math;

public class Constant {

	final public static double GEL_MAX = Math.log(100) / Math.log(2);
	final public static double LOG_E_2 = Math.log(2);
	final public static int MAX_INIT = 2;
	final public static int MIN_INIT = -2;
	

	final public static int MAX_SD = 2;
	final public static double MIN_SD = 0.01;
	final public static int ZERO = 0;
	final public static double ZEROPLUS = 0.1;
	
	final public static double ALPHA_MIN = 0.1;
	final public static double ALPHA_MAX = 10;
	
	final public static int ONE = 1;
	final public static int TWO = 2;
	final public static int FIVE = 5;

//	final public static int PRIOR_D_VAR = 2;
//	final public static int PRIOR_K_VAR = 2;
//	final public static int PRIOR_G_VAR = 2;

	final public static double IG_PARAM = 0.01;

	final public static int UPDATE_GROUP = 3;
	final public static int[][] UPDATE_INDEX = { { 0, 1, 2 }, { 0, 2, 1 },
			{ 1, 0, 2 }, { 1, 2, 0 }, { 2, 0, 1 }, { 2, 1, 0 } };
	final public static int UPDATE_PERM = 5;

	// tuning related
	final public static double OPTIMRATE = 0.234;
	final public static double ACCTOL=0.05;
	final public static int TUNESIZE = 500 ;
	final public static int TUNEGROUP = 6;
	final public static int TUNEGROUP1 = TUNEGROUP - 1;
	final public static double TUNESTEPSIZE = 0.1;
	final public static double TUNEINITSIZE = 2;
	public static final int UPDATE_GLOBAL_COUNT = 50;
	public static final double UPDATE_GLOBAL_PROB = 0.5;
	
	public static double PHI_MIN = 0.01;
	public static double PHI_MAX = 0.99;
	public static double getPHI_MIN() {
		return PHI_MIN;
	}
	public static void setPHI_MIN(double pHIMIN) {
		PHI_MIN = pHIMIN;
		
	}
	public static double getPHI_MAX() {
		return PHI_MAX;
	}
	public static void setPHI_MAX(double pHIMAX) {
		PHI_MAX = pHIMAX;
	}
}
