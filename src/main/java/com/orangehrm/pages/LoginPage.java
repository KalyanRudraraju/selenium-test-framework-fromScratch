package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	
	//Initialize the actionDriver object by passing webDriver instance
	/*
	 * public LoginPage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); }
	 */
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	
	// Define all locators using By Class
	
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	
	//Method to perform Login 
	public void login(String username, String password)
	{
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check is error message is Displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	
	//Method to get the errorMessage text 
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	
	//Verify error message displayed correct or not 
	public boolean verifyErrorMessage(String expected) {
		return actionDriver.compareText(errorMessage, expected);
	}

}
