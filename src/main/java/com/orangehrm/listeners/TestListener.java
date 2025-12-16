package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;


public class TestListener implements ITestListener, IAnnotationTransformer {

	//the below method is used to apply the retry login to all the test cases mentioned in the testng.xml file
	//Using listeners class
	/*
	 * @Override public void transform(ITestAnnotation annotation, Class testClass,
	 * Constructor testConstructor, Method testMethod) {
	 * annotation.setRetryAnalyzer(RetryAnalyzer.class); }
	 */

	//Trigger when a test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		//start logging in extent report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started: "+testName);
	}

	//Trigger when test is succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully! ", "Test End: "+testName+" - ✅ Test Passed");
		}else {
			ExtentManager.logStepValidationForAPI("Test End: "+testName+" - ✅ Test Passed");
		}
	}
	
	
	//Triggered when Test Failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getTestName().toLowerCase().contains("api")) {
		ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed! ", "Test End: "+testName+" - ❌ Test Failed");
		}else {
			ExtentManager.logFailureAPI("Test End: "+testName+" - ❌ Test Failed");	
		}
	}

	
	//Triggered when Test Skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped: "+testName);
		
	}
	

	//Trigger  when a suite starts
	@Override
	public void onStart(ITestContext context) {
		//Initialize the extent reports
		ExtentManager.getReporter();
		
	}

	// Trigger when a suite ends
	@Override
	public void onFinish(ITestContext context) {
		//Flush the extent reports
		ExtentManager.endTest();
	}

	
}
