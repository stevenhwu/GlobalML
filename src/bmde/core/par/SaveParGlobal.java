/**
 * 
 */
package bmde.core.par;

/**
 * @author steven
 * 
 */
public class SaveParGlobal implements SavePar {

	private static final int NOTUNE = 10;

	private static final int INDEX_MU = GlobalPar.INDEX_MU;
	private static final int INDEX_LAMBDA = GlobalPar.INDEX_LAMBDA;
	private static final int INDEX_PI = GlobalPar.INDEX_PI;
	private static final int INDEX_RHO = GlobalPar.INDEX_RHO;

	private static final int INDEX_PI_SD = GlobalPar.INDEX_PI_SD;
	private static final int INDEX_RHO_SD = GlobalPar.INDEX_RHO_SD;

	private static final int INDEX_PI_ALPHA = GlobalPar.INDEX_PI_ALPHA;
	private static final int INDEX_RHO_ALPHA = GlobalPar.INDEX_RHO_ALPHA;
	private static final int INDEX_MU_ALPHA = GlobalPar.INDEX_MU_ALPHA;
	private static final int INDEX_SPOT_SCALE = GlobalPar.INDEX_SPOT_SCALE;

	private static final int[] INDEX_PARAM = { GlobalPar.INDEX_MU,
			GlobalPar.INDEX_LAMBDA, GlobalPar.INDEX_PI, GlobalPar.INDEX_RHO,
			GlobalPar.INDEX_PI_SD, GlobalPar.INDEX_RHO_SD };

	private static final int NOPAR = GlobalPar.getNoPar();

	// private static final int INDEX_PHI = 4; // not used

	private int[] state;
	private int tunesize;

	private int count;

	private double[][] allPar;

	public SaveParGlobal(int n) {

		tunesize = ++n;
		state = new int[tunesize];

		init();

	}

	public void addPar(GlobalPar gp, int ite) {

		state[count] = ite;
		allPar[count] = gp.getPar();
		count++;

	}

	public void resetCount() {
		count = 0;
	}

	@Override
	public void init() {
		allPar = new double[tunesize][NOPAR];
		resetCount();
	}

	@Override
	public double[] calAccRate(int size) {

		double[] accRate = new double[NOTUNE];
		size -= 1;
		for (int i = 0; i < size; i++) {

			if (allPar[i][INDEX_MU] != allPar[i + 1][INDEX_MU]) {
				accRate[0]++;
			}
			if (allPar[i][INDEX_LAMBDA] != allPar[i + 1][INDEX_LAMBDA]) {
				accRate[1]++;
			}
			if (allPar[i][INDEX_PI] != allPar[i + 1][INDEX_PI]) {
				accRate[2]++;
			}
			if (allPar[i][INDEX_RHO] != allPar[i + 1][INDEX_RHO]) {
				accRate[3]++;
			}
			if (allPar[i][INDEX_PI_SD] != allPar[i + 1][INDEX_PI_SD]) {
				accRate[4]++;
			}
			if (allPar[i][INDEX_RHO_SD] != allPar[i + 1][INDEX_RHO_SD]) {
				accRate[5]++;
			}
			if (allPar[i][INDEX_PI_ALPHA] != allPar[i + 1][INDEX_PI_ALPHA]) {
				accRate[6]++;
			}
			if (allPar[i][INDEX_RHO_ALPHA] != allPar[i + 1][INDEX_RHO_ALPHA]) {
				accRate[7]++;
			}
			if (allPar[i][INDEX_MU_ALPHA] != allPar[i + 1][INDEX_MU_ALPHA]) {
				accRate[8]++;
			}
			if (allPar[i][INDEX_SPOT_SCALE] != allPar[i + 1][INDEX_SPOT_SCALE]) {
				accRate[9]++;
			}
						
		}

		for (int i = 0; i < accRate.length; i++) {
			accRate[i] /= size;
		}

		resetCount();
		return accRate;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public double[][] getAllPar() {
		return allPar;
	}

	public int[] getState() {
		return state;
	}

	public static int getNotune() {
		return NOTUNE;
	}
}
