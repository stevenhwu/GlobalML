/**
 * 
 */
package bmde.core.par;

import java.util.Formatter;

/**
 * @author steven
 * 
 */
public class SaveParLocal extends AbstractSavePar {

	private static final int NO_TUNE = 2;
	private static final int NO_PAR = ParSpot.NO_LOCAL_PAR;

	private int tunesize;

	public SaveParLocal(int n) {

		tunesize = ++n;
		state = new int[tunesize];

		init();
	}

	@Override
	public void init() {
		allPar = new double[tunesize][NO_PAR];
		resetCount();
	}



	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("state\tmu\td\tpi\trho\tsd\tlikelihood\n");
		Formatter f = new Formatter(sb);

		// NumberFormat formatter = NumberFormat.getNumberInstance();
		for (int i = 0; i < allPar.length; i++) {
			f.format("%d\t", state[i]);
			for (int j = 0; j < allPar[i].length; j++) {
				f.format("%.4f\t", allPar[i][j]);
			}
			f.format("\n", "");

		}
		return sb.toString();

	}

	/**
	 * calculate the acceptance rate
	 * 
	 * @return
	 */
	@Override
	public double[] calAccRate(int size) {

		double[] accRate = new double[NO_TUNE];
		size -= 1;
		for (int i = 0; i < size; i++) {
			if (allPar[i][0] != allPar[i + 1][0]) {
				accRate[0]++;
			}
			if (allPar[i][2] != allPar[i + 1][2]) {
				accRate[1]++;
			}

		}
		for (int i = 0; i < accRate.length; i++) {
			accRate[i] /= size;
		}
		resetCount();
		return accRate;
	}





	public static int getNotune() {
		return NO_TUNE;
	}

}
