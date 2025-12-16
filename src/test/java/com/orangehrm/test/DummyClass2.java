package com.orangehrm.test;

import static org.testng.Assert.assertEquals;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass{

	
	
	@Test
	public void dummyTest2() {
		//ExtentManager.startTest("DummyTest2 Test"); -->this has been implemented in TestListener
		String title = getDriver().getTitle();
		
		ExtentManager.logStep("verifying the title");
		assertEquals(title, "OrangeHRM", "Title didn't match");
		System.out.println("Test Passed from dummy2 - title matched");
		//ExtentManager.logStep("Validation Successfull");
		//throw new SkipException("Skipping the test as part of Testing");
	}
}
