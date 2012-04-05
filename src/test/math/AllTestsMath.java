package test.math;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TransformationTest.class, 
	ExpUniExpDistributionTest.class
	})
public class AllTestsMath {
	
}