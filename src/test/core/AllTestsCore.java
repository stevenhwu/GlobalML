package test.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CurrentParTest.class,
	LikelihoodTest.class,
	MHRatioTest.class,
	ParGlobalTest.class, 
	ParSpotTest.class,
	SaveParLocalTest.class,
	})
public class AllTestsCore {
	
}