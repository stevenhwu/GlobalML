package gml.math;

import org.apache.commons.lang.math.RandomUtils;

public class MathLogDouble {

	
	public static double nextLogDouble(){
		
		return ( Math.log( RandomUtils.nextDouble() ) );
	}
}
