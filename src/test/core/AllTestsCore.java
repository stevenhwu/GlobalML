package test.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CurrentParTest.class,
	ParGlobalTest.class, 
	LikelihoodTest.class,
	MHRatioTest.class,
	SaveParLocalTest.class,
	ParSpotTest.class
	})
public class AllTestsCore {
	
}