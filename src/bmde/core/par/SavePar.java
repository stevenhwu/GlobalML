package bmde.core.par;

public interface SavePar {




	@Override
	public abstract String toString();

	public abstract int getCount();

	public abstract double[][] getAllPar();

	public abstract double[] calAccRate(int size);

	public abstract void init();

	public abstract void addPar(Parameter gp, int ite);

	public abstract int[] getState();

	public abstract void resetCount();;



}