package test.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bmde.math.Transformation;

public class TransformationTest {

	@Test
	public void testLogit() {
		
		assertEquals(0, Transformation.logit(0.5), 0);
		assertEquals(-2.1972246, Transformation.logit(0.1), 10E-7);
		assertEquals(-1.3862944, Transformation.logit(0.2), 10E-7);
		assertEquals(-0.8472979, Transformation.logit(0.3), 10E-7);
		assertEquals(-0.4054651, Transformation.logit(0.4), 10E-7);

		assertEquals(0.4054651, Transformation.logit(0.6), 10E-7);
		assertEquals(0.8472979, Transformation.logit(0.7), 10E-7);
		assertEquals(1.3862944, Transformation.logit(0.8), 10E-7);
		assertEquals(2.1972246, Transformation.logit(0.9), 10E-7);

	
		
	}

	@Test
	public void testInvLogit() {

		assertEquals(0.04742587, Transformation.invLogit(-3), 10E-7);
		assertEquals(0.11920292, Transformation.invLogit(-2), 10E-7);
		assertEquals(0.26894142, Transformation.invLogit(-1), 10E-7);
		assertEquals(0.5, 		 Transformation.invLogit(0), 10E-7);
		assertEquals(0.73105858, Transformation.invLogit(1), 10E-7);
		assertEquals(0.88079708, Transformation.invLogit(2), 10E-7);
		assertEquals(0.95257413, Transformation.invLogit(3), 10E-7);
		

	}

}
