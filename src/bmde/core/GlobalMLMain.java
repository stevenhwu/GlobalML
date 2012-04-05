package bmde.core;

/**
 * @author Steven Wu
 * Thanks beast-mcmc project for some very useful source code
 * 
 * Required library:
 * Apache commons-lang-2.6
 * Apache commons-math-2.2
 * 
 * Usage: java -jar bmde.jar config.conf
 * Note: with large file -Xmx switch might be required
 * 
 */

public class GlobalMLMain {


	public static void main(String[] args) {

		/*		
		 * config file format
		 * 
		 * dataFileName
		 * TotalChainLength LogInterval
		 * GlobalParameterOutputFile LocalParameterOutputFile
		 * 
		 * e.g.
		 * example.csv
		 * 10000000 1000
		 * SpotGlobal.log SpotLocal.log
		 */

		String configFile = args[0];

		MCMC mcmcChain = new MCMC(configFile);
		mcmcChain.run();
	}


}
