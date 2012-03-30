package bmde.math;

import java.util.Random;

public class MathLogDouble {

	static Random rand = new Random();
	public static double nextLogDouble(){
		
		return ( Math.log( rand.nextDouble() ) );
	}
}
