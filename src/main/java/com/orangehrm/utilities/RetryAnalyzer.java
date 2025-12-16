package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int retryCount = 0; //No.Of retries
	public static final int maxRetryCount = 2;
	
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxRetryCount) {
			retryCount++;
			return true; //retry the test
		}
		return false;
	}

	
	
}
