package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUp() {
		loginPage = new LoginPage(getDriver());
		homePage  = new HomePage(getDriver());
	}
	
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username,String password) {
		//ExtentManager.startTest("Home page logo test "); -->this has been implemented in TestListener
		ExtentManager.logStep("Navigate to Login Page entering user name and password ");
		loginPage.login(username,password);
		ExtentManager.logStep("Verifying Logo is Visible or not");
		assertTrue(homePage.isOrangeHRMLogoDisplayed(),"Orange HRM Logo is not displayed ");
		staticWait(2);
		ExtentManager.logStep("Logged out successfully!");
	}

}
