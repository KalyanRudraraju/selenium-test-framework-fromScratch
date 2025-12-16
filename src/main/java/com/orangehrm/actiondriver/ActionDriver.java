package com.orangehrm.actiondriver;

import java.awt.GridBagConstraints;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Green;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v140.audits.model.BlockedByResponseIssueDetails;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

import groovy.transform.builder.InitializerStrategy.SET;
import groovyjarjarantlr4.v4.gui.TestRig;
import io.opentelemetry.api.common.Value;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("Web Driver instance is Created");
	}

	// method to click on an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");

			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element " + elementDescription);
			logger.info("Clicked an element -->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to Click the element :" + e.getMessage());
			ExtentManager.logFailure(driver, "Unable to click the elemt ", elementDescription + "_Unable to click");
			logger.error("Unable to click element");
		}
	}

	// Method to enter text into the text field -- Avoid code Duplication - fix the
	// multiple call
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter the value : " + e.getMessage());
		}
	}

	// method to get text from input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text :" + e.getMessage());
			return "";
		}
	}

	// Compare Text -- Change the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info(" Text are matching :" + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text ",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching : " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Test Comparision Failed! ",
						"Test Comparision Failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare Text : " + e.getMessage());
		}
		return false;
	}

	// Method to check if an element is displayed
	/*
	 * public boolean isDisplayed(By by) { try { waitForElementToBeVisible(by);
	 * boolean isDisplayed = driver.findElement(by).isDisplayed(); if(isDisplayed) {
	 * System.out.println("Element is visible  "); return isDisplayed; } else {
	 * return isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not Displayed : "+e.getMessage()); return
	 * false; } }
	 */

	// Simplified the above method and remove redundant conditions
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtentManager.logStep("Element is Displayed " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is Dispalyed",
					"Element is Displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not Displayed " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed ",
					"Element is not Displayed: " + getElementDescription(by));
			return false;
		}
	}

	// wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded Successfully");
		} catch (Exception e) {
			logger.error("Page didn't load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Locate to the element " + e.getMessage());
		}
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable : " + e.getMessage());
		}
	}

	// Element to be Visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not Visible " + e.getMessage());
		}
	}

	// Method to get the description of an Element using By Locator
	public String getElementDescription(By locator) {
		// CHECK FOR NULL DRIVER/LOCATOR to avoid nullPointerException
		if (BaseClass.getDriver() == null)
			return "driver is null";
		if (locator == null)
			return "locator is null";

		try {
			// find the element using locator
			WebElement element = BaseClass.getDriver().findElement(locator);
			// System.out.println("*****************"+element+"***********");

			// get element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// return description based on attribute
			if (isNotEmpty(name)) {
				return "Element with name :" + name;
			} else if (isNotEmpty(id)) {
				return "Element with id : " + id;
			} else if (isNotEmpty(text)) {
				return " Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with className: " + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeHolder : " + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element " + e.getMessage());
		}
		return "Unable to describe the element ";
	}

	// utility method to check string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility Method to truncate long String
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// Apply the boarder
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element" + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element : " + getElementDescription(by), e.getMessage());
		}
	}

	// ================ Select Methods =============

	// Method to select a dropdown by visible Text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown Value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Select dropdown Value: " + value, e.getMessage());
		}
	}

	// Method to get all options from Dropdown
	public List<String> getDropdownOptions(By by) {

		List<String> optionsList = new ArrayList<>();
		try {
			WebElement element = driver.findElement(by);
			Select select = new Select(element);
			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrived dropDown Options from  " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get dropdown options: ", e.getMessage());
		}
		return optionsList;
	}

	// ============= JavaScript Utility Methods==============

	// Method to click using JavaScript
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			applyBorder(by, "green");
			logger.info("Clicked element using JS  " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click using JS ", e.getMessage());
		}
	}

	// Method to scroll to Bottom
	public void scrollToButtom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
		logger.info("Scrolled to the bottom of the page");
	}

	// Method to highlight an element using JavaScript
	public void highLightElementJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
			logger.info("HighLighted the element using JS  " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to HighLight element using JS ", e);
		}
	}

	// ================ Window and Frame Handling==================

	// Method to switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switch to window : " + window);
					return;
				}
			}
			logger.warn("Window with Title :" + windowTitle + " not found");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Unable to switch window : ", e);
		}
	}

	// Method to switch to an IFrame
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switch to iFrame: " + getElementDescription(by));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Unable to switch to Iframe: ", e);
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content");
	}

	// ================ Alert Handling ==============

	// Method to accept an allert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert Accepted---");
		} catch (Exception e) {
			logger.error("No Alert found to accept. ", e);
		}
	}

	// Method to dismiss an alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed---");
		} catch (Exception e) {
			logger.error("No Alert found to dismiss. ", e);
		}
	}

	// Method to get alert text
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.error("No Alert text found. ", e);
			return "";
		}
	}

	// ===================== Browser Actions=================

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page Refreshed Successfully---");
			logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error("Unable to refresh Page ", e);
		}
	}
	
	
	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			logger.info("Current URL Fetched Successfully---"+url);
			return url;
		} catch (Exception e) {
			logger.error("Unable to Fetch URL ", e);
			return null;
		}
	}
	
	
	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximized Successfully---");
			logger.info("Browser window maximized Sccessfully---");
		} catch (Exception e) {
			logger.error("Unable to Maximise browser window ", e);
		}
	}
	
	
	
	//================== Advanced WebElement Actions==============
	
	public void moveToElement(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Moved to element ---"+elementDescription);
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error("Unable to refresh Page ", e);
		}
	}

	
	public void dragAndDrop(By source, By target) {
		String sourceDes = getElementDescription(source);
		String targetDes = getElementDescription(target);
		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target) ).perform();
			ExtentManager.logStep("Dragged element "+sourceDes+" and dropped to "+targetDes);
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error(" ", e);
		}
	}
	
	
	public void doubleClick(By by) {
		String elementDes = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Double clicked on the element"+ elementDes+" Successfully---");
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error(" ", e);
		}
	}
	
	public void rightClick(By by) {
		String elementDes = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right clicked on the element"+ elementDes+" Successfully---");
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error(" ", e);
		}
	}
	
	
	public void sendKeysWithActions(By by, String value) {
		String elementDes = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			ExtentManager.logStep("Send keys to the element "+ elementDes+" Successfully---");
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error(" ", e);
		}
	}
	
	
	public void clearText(By by) {
		String elementDes = getElementDescription(by);
		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in the element"+ elementDes+" Successfully---");
			//logger.info("Page Refreshed Successfully---");
		} catch (Exception e) {
			logger.error(" ", e);
		}
	}
	
	
	//Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			//ExtentManager.logStep("Double clicked on the element"+ elementDes+" Successfully---");
			logger.info("Uplocaded file Successfully---");
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error(" ", e);
		}
	}
}
