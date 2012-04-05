package bmde.core.par;


import java.util.Arrays;

import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.stat.StatUtils;

import bmde.core.Setting;
import bmde.math.Constant;
import bmde.math.NormalDistribution;

// TUNESIZE = average of tuneSize iterations
// TuneGroup = over tuenGroup
//		overall, averaging over TuneSize*tuneGroup iterations
// tuneStepSize = maximum step size of tuning, nextUniform(0,tuneStepSize);

public class TunePar {

	private double[] tunePar;
	private double[][] accept;
	private int[] tuneType;

	private final double INV_OPT_ACC = NormalDistribution.quantile(
			Constant.OPTIMRATE / 2, 0, 1);
	// private int count=0;

	private int tuneSize;
	private int noTunePar;
	private int tuneGroup;
	private int tuneGroup1;
	

	private double tuneStepSize = Constant.TUNESTEPSIZE;
	private double tuneInitSize = Constant.TUNEINITSIZE;
	// private double accTol = Constant.ACCTOL;
	private double accLower = Constant.OPTIMRATE - Constant.ACCTOL;
	private double accUpper = Constant.OPTIMRATE + Constant.ACCTOL;

	private static RandomDataImpl rd = new RandomDataImpl();

	public TunePar(String forLorG, int ite, int tuneSize, int tuneGroup, String[] type) {
		this(forLorG, ite, tuneSize, tuneGroup);
		setType(type);
	}
	public TunePar(String forLorG, int ite, int tuneSize, int tuneGroup, String type) {
		this(forLorG, ite, tuneSize, tuneGroup);
		setType(type);
	}
	

	public TunePar(String forLorG, int ite, int tuneSize, int tuneGroup) {

		
		tuneInitSize = 2.38;
		tuneStepSize = 0.001;

		if (forLorG == Setting.LOCAL) {
			this.noTunePar = SaveParLocal.getNotune();
		} else {
			this.noTunePar = SaveParGlobal.getNotune();
		}
		tunePar = new double[noTunePar];
		for (int i = 0; i < noTunePar; i++) {
			Arrays.fill(tunePar, tuneInitSize);
		}
		this.tuneSize = tuneSize;
		this.tuneGroup = tuneGroup;
//		minAccRate = 1.0 / tuneSize;

		tuneGroup1 = tuneGroup - 1;
		accept = new double[noTunePar][tuneGroup];

	}

	private void setType(String type) {
		
		int typeIndex = -1;
		if (type.equalsIgnoreCase("normal")) {
			typeIndex = 0;
		} else if (type.equalsIgnoreCase("normalbig")) {
			typeIndex = 1;
		} else if (type.equalsIgnoreCase("scale")) {
			typeIndex = 2;
		}
		
		tuneType = new int[noTunePar];
		Arrays.fill(tuneType, typeIndex);

	}
	
	public void setType(String[] type) {

		if (noTunePar != type.length) {
			System.out.println("incorrect tune type length"
					+ Arrays.toString(type));
			System.out.println("Number of tuning par: " + noTunePar);
			System.exit(-1);
			
		}

		tuneType = new int[noTunePar];
		for (int i = 0; i < type.length; i++) {
			if (type[i].equalsIgnoreCase("normal")) {
				tuneType[i] = 0;
			} else if (type[i].equalsIgnoreCase("normalbig")) {
				tuneType[i] = 1;
			} else if (type[i].equalsIgnoreCase("scale")) {
				tuneType[i] = 2;
			}
		}
//		 System.out.println("Tune type:\t"+Arrays.toString(tuneType));
	}



	

	public void update(SavePar all, int ite) {

		int index = ite / tuneSize;

		double[] newAcc = all.calAccRate(tuneSize);

		double[] accRate = new double[newAcc.length];
		if (index >= tuneGroup) {

			for (int i = 0; i < accRate.length; i++) {
				System.arraycopy(accept[i], 1, accept[i], 0, tuneGroup1);
				accept[i][tuneGroup1] = newAcc[i];
				accRate[i] = StatUtils.mean(accept[i]);
			}
		} else {
			for (int i = 0; i < accRate.length; i++) {
				accept[i][index] = newAcc[i];
				accRate[i] = StatUtils.mean(accept[i]);
			}
		}
		for (int i = 0; i < tunePar.length; i++) {
			tunePar[i] = checkRate(tunePar[i], accRate[i], tuneType[i]);
		}
	}

	private double checkRate(double tp, double d, int type) {

		double newRate = tp;
		if (type == 0) {
			newRate = checkNormal(tp, d, 0.01);
		} else if (type == 1) {
			newRate = checkNormal(tp, d, 0.01);
		} else if (type == 2) {
			newRate = checkScale(tp, d);
		}
		return newRate;
	}

	private double checkScale(double tp, double d) {

		if (d >= accUpper) {
			tp -= rd.nextUniform(0, tuneStepSize);
		} else if (d < accLower) {
			tp += rd.nextUniform(0, tuneStepSize);

		}

		if (tp <= 0 | tp >= 1) {
			tp = rd.nextUniform(0.8, 0.9);
		}

		return tp;

	}

	private double checkNormal(double tp, double d, double reset) {

		double newTp = tp;
		if (d >= accUpper | d < accLower) {
			newTp = tp * INV_OPT_ACC / NormalDistribution.quantile(d / 2, 0, 1);
		}
		if (Double.isNaN(newTp)) {
			if(tp == reset){
				newTp = 2.38;
			}
			else{
				newTp = reset;
			}
		}
		if (tp< 0.001) {
			if(tp == reset){
				newTp = 2.38;
			}
			else{
				newTp = reset;
			}
		}
		if(newTp>10){
			newTp = 2.38;
		}
		return newTp;
	}
	
	@Override
	public String toString() {
		return "TunePar [getAveAccRate()=" + Arrays.toString(getAveAccRate())
				+ ", tunePar=" + Arrays.toString(tunePar) + "]";
	}

	public double[] getAveAccRate() {

		double[] r = new double[noTunePar];
		for (int i = 0; i < noTunePar; i++) {
			r[i] = StatUtils.mean(accept[i]);
		}
		return r;

	}

	public double[] getTunePar() {

		return tunePar;
	}

	public int[] getTuneType() {
		return tuneType;
	}

	public int getTuneSize() {
		return tuneSize;
	}

	public int getTuneGroup() {
		return tuneGroup;
	}

	public void setTuneSize(int tuneSize) {
		this.tuneSize = tuneSize;
	}

	public void setTuneGroup(int tuneGroup) {
		this.tuneGroup = tuneGroup;
	}



}
