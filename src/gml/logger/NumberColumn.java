

package gml.logger;

import java.text.DecimalFormat;

public class NumberColumn {

	private int sf = -1;
	private int dp = -1;

	private double upperCutoff;
	private double[] cutoffTable;
	private final DecimalFormat decimalFormat = new DecimalFormat();
	private DecimalFormat scientificFormat = null;

	private String label;
	private int minimumWidth;

	// public NumberColumn(String label) { super(label); }

	public NumberColumn(String label, int sf) {

		setLabel(label);
		minimumWidth = -1;
		setSignificantFigures(sf);
	}

	public String[] formatValues(double[] value){
		String[] s = new String[value.length];
		for (int i = 0; i < value.length; i++) {
			s[i] = formatValue(value[i]);
		}
		return s;
	}
	
	public String formatValue(double value) {
		if (dp < 0 && sf < 0) {
			// return it at full precision
			return Double.toString(value);
		}

		int numFractionDigits = 0;

		if (dp < 0) {

			double absValue = Math.abs(value);

			if ((absValue > upperCutoff) || (absValue < 0.1)) {

				return scientificFormat.format(value);

			} else {

				numFractionDigits = getNumFractionDigits(value);
			}

		} else {

			numFractionDigits = dp;
		}

		decimalFormat.setMaximumFractionDigits(numFractionDigits);
		decimalFormat.setMinimumFractionDigits(numFractionDigits);
		return decimalFormat.format(value);
	}

	private int getNumFractionDigits(double value) {
		value = Math.abs(value);
		for (int i = 0; i < cutoffTable.length; i++) {
			if (value < cutoffTable[i])
				return sf - i - 1;
		}
		return sf - 1;
	}

	public void setLabel(String label) {
		if (label == null)
			throw new IllegalArgumentException("column label is null");
		this.label = label;
	}

	/**
	 * Set the number of significant figures to display when formatted. Setting
	 * this overrides the decimal places option.
	 */
	public void setSignificantFigures(int sf) {
		this.sf = sf;
		this.dp = -1;

		upperCutoff = Math.pow(10, sf - 1);
		cutoffTable = new double[sf];
		long num = 10;
		for (int i = 0; i < cutoffTable.length; i++) {
			cutoffTable[i] = num;
			num *= 10;
		}
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setMaximumFractionDigits(sf - 1);
		decimalFormat.setMinimumFractionDigits(sf - 1);
		scientificFormat = new DecimalFormat(getPattern(sf));
	}

	/**
	 * Set the number of decimal places to display when formatted. Setting this
	 * overrides the significant figures option.
	 */
	public void setDecimalPlaces(int dp) {
		this.dp = dp;
		this.sf = -1;
	}

	public String getLabel() {
		StringBuffer buffer = new StringBuffer(label);

		if (minimumWidth > 0) {
			while (buffer.length() < minimumWidth) {
				buffer.append(' ');
			}
		}

		return buffer.toString();
	}

	/**
	 * Get the number of significant figures to display when formatted. Returns
	 * -1 if maximum s.f. are to be used.
	 */
	public int getSignificantFigures() {
		return sf;
	}

	/**
	 * Get the number of decimal places to display when formatted. Returns -1 if
	 * maximum d.p. are to be used.
	 */
	public int getDecimalPlaces() {
		return dp;
	}

	private String getPattern(int sf) {
		String pattern = "0.";
		for (int i = 0; i < sf - 1; i++) {
			pattern += "#";
		}
		pattern += "E0";
		return pattern;
	}

}
