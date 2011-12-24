package gml.core.par;

public interface SavePar {




	@Override
	public abstract String toString();

	public abstract int getCount();

	public abstract double[][] getAllPar();

	public abstract double[] calAccRate(int size);

	public void init();;



}