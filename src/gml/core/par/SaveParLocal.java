/**
 * 
 */
package gml.core.par;

import java.util.Formatter;

/**
 * @author steven
 * 
 */
public class SaveParLocal implements SavePar {

	private static final int NOTUNE = 2;
	private static final int INDEX_MU = SpotPar.INDEX_MU;
	private static final int INDEX_PROB = SpotPar.INDEX_PROB;
	private static final int NOPAR = SpotPar.getNoPar();

	private int count;
	private int tunesize;

	private int[] state;

	private double[][] allPar;

	public SaveParLocal(int n) {

		tunesize = ++n;
		state = new int[tunesize];

		init();
	}

	public void resetCount() {
		count = 0;
	}

	@Override
	public void init() {
		allPar = new double[tunesize][NOPAR];
		resetCount();
	}

	public void add(SpotPar sp, int st) {

		allPar[count] = sp.getPar();
		count++;

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
			// f.format("%d\t%.5f\t%.5f\t%.5f\t%.5f\t%.5f\n",state[i],mu1[i],d[i],pi[i],rho[i],likelihood[i]);

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

		double[] accRate = new double[NOTUNE];
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


	public int[] getState() {
		return state;
	}


	@Override
	public int getCount() {
		return count;
	}

	@Override
	public double[][] getAllPar() {
		return allPar;
	}

	public static int getNotune() {
		return NOTUNE;
	}

}
