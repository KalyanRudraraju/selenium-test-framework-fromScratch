package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actionDriver;
	
	//Initialize the actionDriver object by passing webDriver instance
	/*
	 * public HomePage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); }
	 */
	
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Define locators for HomePage
	
	private By adminTab = By.xpath("//span[text() ='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-tab");
	private By logOutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']/img");
	
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By lastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	
	
	//Validate AdminTab is present or not 
	public boolean isAdminTabDisplayed()
	{
		return actionDriver.isDisplayed(adminTab);
	}
	
	// validate OrangeHRMLogo displayed
	public boolean isOrangeHRMLogoDisplayed() {
		return actionDriver.isDisplayed(orangeHRMLogo);
	}
	
	//Method to Navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	//employee search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}

	//verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String employeeFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, employeeFirstAndMiddleNameFromDB);
	}
	
	//verify employee lastName
	public boolean verifyEmployeeLastName(String employeeLastNameFromDB) {
		return actionDriver.compareText(lastName, employeeLastNameFromDB);
	}
	
	
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(logOutButton);
	}
}
