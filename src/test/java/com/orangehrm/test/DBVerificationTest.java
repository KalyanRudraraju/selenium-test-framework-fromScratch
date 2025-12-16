package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.openqa.selenium.remote.tracing.Propagator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	/*
	 * To run the below test case you need to change the url to local one in Config.properties file then you need to start the apache and 
	 * mySQL in the xampp one then once it was working fine then change the username and password in the config file to local user 
	 * then start it only from testng.xml file as we are using ITestListners local execution wont work try to comment other tests while
	 * doing so*/
	@Test(dataProvider = "empVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String empId,String empName) throws InterruptedException {
		ExtentManager.logStep("Logging with Amin Credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtentManager.logStep("Click on PIM Tab");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);
		Thread.sleep(2);
		
		ExtentManager.logStep("Get the Employee Name From DB");
		String employee_id = empId;
		
		//Fetch the data into map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		
		String employeeFirstName = employeeDetails.get("firstName");
		String employeeMiddleName = employeeDetails.get("middleName");
		String employeeLastName = employeeDetails.get("lastName");
		
		String empFirstAndMiddleName = (employeeFirstName+" "+employeeMiddleName).trim();
		
		ExtentManager.logStep("Verify the employee first and Middle Name");
		assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName),"First and Middle name are not matching");

		ExtentManager.logStep("Verify the employee Last Name");
		assertTrue(homePage.verifyEmployeeLastName(employeeLastName),"Last name is not matching");

		ExtentManager.logStep("DB Validation Completed");
	}
}
