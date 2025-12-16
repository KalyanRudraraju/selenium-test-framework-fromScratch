package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username,String password) {
		//ExtentManager.startTest("Valid Login test "); -->this has been implemented in TestListener
		ExtentManager.logStep("Navigate to Login Page entering user name and password ");
		loginPage.login(username, password);
		ExtentManager.logStep("Verify Admin Tab is visible or not ");
		assertTrue(homePage.isAdminTabDisplayed(),"Admin Tab should be visible after successful Login");
		ExtentManager.logStep("Validation Successfull");
		homePage.logout();
		ExtentManager.logStep("Logged out successfully!");
		staticWait(2);
	}
	
	
	@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
	public void verifyInValidLoginTest(String username,String password) {
		//ExtentManager.startTest("In valid Login test "); -->this has been implemented in TestListener
		ExtentManager.logStep("Navigate to Login Page entering user name and password ");
		loginPage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		ExtentManager.logStep("Validation Successfull");
		assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message displayed");
		//assertTrue(loginPage.isErrorMessageDisplayed(),"Error message is not displayed");
		ExtentManager.logStep("Logged out successfully!");
	}

}
