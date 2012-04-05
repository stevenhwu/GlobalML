package bmde.core.par;

public abstract class AbstractSavePar implements SavePar {

	protected int[] state;
	protected int count;
	protected double[][] allPar;

	@Override
	public void addPar(Parameter gp, int ite) {

		state[count] = ite;
		allPar[count] = gp.getPar();
		count++;

	}

	@Override
	public void resetCount() {
		count = 0;
	}

	@Override
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
}