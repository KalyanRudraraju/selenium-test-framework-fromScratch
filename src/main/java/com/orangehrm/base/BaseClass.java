package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

import org.apache.logging.log4j.Logger;

public class BaseClass {

	protected static Properties prop; // here we made this static because if not after executing once
	// the other tests are getting skipped because prop was initialized only once
	// before suite and for
	// once and for later tests it will be null to fix this we made it static for
	// all the instances to have the
	// same variable load.
//private static WebDriver driver; // for driver we are closing each driver after test completed and
	// and opening a new browser again so no issues here.

	/*
	 * iN ORDER NOT to create more web driver instance each time for each test case
	 * like for Home and login pages for now 2 instances are getting created for the
	 * driver So making it to Singleton to create only one instance for the driver
	 */
//private static ActionDriver actionDriver;
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	/*
	 * public WebDriver getDriver() { return driver; }
	 * 
	 * public void setDriver(WebDriver driver) { this.driver = driver; }
	 */

	
	  public static Properties getProp() { return prop; }
	 

	// Getter method for WebDriver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			System.out.println("WebDriver is not Initialised");
			throw new IllegalStateException("WebDriver is not Initialised");
		}
		//System.out.println("WebDriver Instance is Created");
		return driver.get();
	}

	// Getter method for ActionDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not Initialised");
			throw new IllegalStateException("ActionDriver is not Initialised");
		}
		return actionDriver.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties File Loaded ");
		
		
		//Start the extentReport
		//ExtentManager.getReporter();  -->this has been implemented in TestListener
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting up webDriver for :" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is Trace Message");
		logger.error("this is Error Message");
		logger.debug("This is Debug Message");
		logger.fatal("This is Fatal Message");
		logger.warn("This is Warn Message");

		// Initialize the ActionDriver only once
		/*
		 * if (actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * logger.info("Action Driver instance is created "+Thread.currentThread().getId
		 * ()); }
		 */
		
		//Initialize actionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver Initialised for thread : "+Thread.currentThread().getId());
	}

	/*
	 * Initialize the webDriver based on the browser defined in config.properties
	 * file
	 */
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			
			/*
			 * ChromeOptions options = new ChromeOptions();
			 * options.addArguments("--headless"); //run chrome in headless mode
			 * options.addArguments("--disable-gpu"); //Disable GPU for headless mode
			 * //options.addArguments("--window-size=1920,1080"); //set window size
			 * options.addArguments("--disable-notifications"); //disable browser
			 * notifications options.addArguments("--no-sandbox"); //Required for some CI
			 * environments options.addArguments("--disable-dev-shm-usage"); //Resolve
			 * issues in resources
			 * 
			 * //driver = new ChromeDriver(); driver.set(new ChromeDriver(options)); // new
			 * changes as per ThreadLocal
			 */
			driver.set(new ChromeDriver());
			
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");  //run chrome in headless mode
			options.addArguments("--disable-gpu");  //Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); //set window size
			options.addArguments("--disable-notifications");  //disable browser notifications
			options.addArguments("--no-sandbox");  //Required for some CI environments
			options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resources
			
			
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FireFox instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless");  //run chrome in headless mode
			options.addArguments("--disable-gpu");  //Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); //set window size
			options.addArguments("--disable-notifications");  //disable browser notifications
			options.addArguments("--no-sandbox");  //Required for some CI environments
			options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resources
			
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created");
		} else {
			throw new IllegalArgumentException("Browser not supproted" + browser);
		}
	}

	/*
	 * configure the browser settings like wait, maximize and navigate
	 */
	private void configureBrowser() {
		// implicit wait
		int wait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

		// maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to the URL
		// String url = prop.getProperty("url");
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL " + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver Instance is Closed.");
		driver.remove();
		actionDriver.remove();
		//driver = null;
		//actionDriver = null;
		
		//ExtentManager.endTest();    -->this has been implemented in TestListener
	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
