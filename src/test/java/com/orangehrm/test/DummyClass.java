package com.orangehrm.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass{
	
	
	@Test
	public void dummyTest()
	{
		//ExtentManager.startTest("DummyTest1 Test");  -->this has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("verifying the title");
		assertEquals(title, "OrangeHRM", "Title didn't match");
		System.out.println("title matched");
		//ExtentManager.logSkip("This case is skipped");
	}

}
