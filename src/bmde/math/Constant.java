package bmde.math;

public class Constant {

	public static final double GEL_MAX = Math.log(100) / Math.log(2);
	public static final double LOG_E_2 = Math.log(2);
	public static final int MAX_INIT = 2;
	public static final int MIN_INIT = -2;
	

	
	public static final int MAX_VAR = 3;
	
	public static final double MIN_SD = 0.01;
	public static final double MIN_VAR = 0.01;
	public static final int ZERO = 0;
	public static final double ZEROPLUS = 0.1;
	
	public static final double ALPHA_MIN = 0.1;
	public static final double ALPHA_MAX = 3;
	
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int FIVE = 5;

	public static final double IG_PARAM = 0.01;

	public static final int UPDATE_GROUP = 3;
	public static final int[][] UPDATE_INDEX = { { 0, 1, 2 }, { 0, 2, 1 },
			{ 1, 0, 2 }, { 1, 2, 0 }, { 2, 0, 1 }, { 2, 1, 0 } };
	public static final int UPDATE_PERM = 5;

	// tuning related
	public static final double OPTIMRATE = 0.234;
	public static final double ACCTOL=0.05;
	public static final int TUNESIZE = 500 ;
	public static final int TUNEGROUP = 6;
	public static final int TUNEGROUP1 = TUNEGROUP - 1;
	public static final double TUNESTEPSIZE = 0.1;
	public static final double TUNEINITSIZE = 2;
	public static final int UPDATE_GLOBAL_COUNT = 50;
	public static final double UPDATE_GLOBAL_PROB = 0.2;
	
	public static final double PHI_MIN = 0.05;
	public static final double PHI_MAX = 0.95;
	public static final double LOGIT_PHI_MIN = Transformation.logit(PHI_MIN);
	public static final double LOGIT_PHI_MAX = Transformation.logit(PHI_MAX);

}
