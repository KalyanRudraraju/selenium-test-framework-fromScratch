package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class APITest {

	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void verifyGetUserApi() {

		// Step :1 define API end-point
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";

		ExtentManager.logStep("API EndPoint: " + endPoint);

		// Step2: Send the get request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = APIUtility.sendGetRequest(endPoint);

		// Step3: Validate the status code
		ExtentManager.logStep("Validating API Response Status Code");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);
		assertTrue(isStatusCodeValid, "StatusCode is not as expected!");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Valikdation Passed");
		} else {
			ExtentManager.logFailureAPI("Status COde Validation Failed!");
		}

		// Step4: Validate UserName
		ExtentManager.logStep("Validating Response Body for UserName");
		String userName = APIUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		assertTrue(isUserNameValid, "UserName is not valid");

		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("UserName Validation Passed");
		} else {
			ExtentManager.logFailureAPI("UserName Validation Failed!");
		}

		// Step5: Validate email
		ExtentManager.logStep("Validating Response Body for Email");
		String email = APIUtility.getJsonValue(response, "email");
		boolean isValidEmail = "Sincere@april.biz".equals(email);
		assertTrue(isValidEmail, "Email is not valid");

		if (isValidEmail) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed");
		} else {
			ExtentManager.logFailureAPI("Email Validation Failed!");
		}
	}

}
