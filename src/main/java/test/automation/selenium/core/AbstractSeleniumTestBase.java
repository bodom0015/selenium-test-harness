package test.automation.selenium.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;


import test.automation.selenium.core.BrowserType;

/**
 * A simple base class to represent a set of tests that
 * we plan to run against multiple browsers (WebDrivers).
 * 
 * @author Mike Lambert
 *
 */
public abstract class AbstractSeleniumTestBase {
    /** The directory in which to save screenshots of test cases */
    private static final String TEST_OUTPUT_DIR = "./test-screenshots";
    
    /** The name of the currently executing test case */
    protected String currentTestName;
    
	/** 
	 * The browser to use to perform the set 
	 * of tests described in the subclass 
	 */
	protected BrowserType browserType;
	
	/** 
	 * The WebDriver to use to perform the set
	 *  of tests described in the subclass
	 */
	protected WebDriver driver;

	/**
	 * Initialize the desired browser type for the desired browser.
	 */
	protected WebDriver initializeDriver(String browser) {		
		this.browserType = BrowserType.valueOf(browser.toUpperCase());
		return this.browserType.getDriver();
	}
	
	/**
	 * Sets up our testing environment by instantiating a new driver to use.
	 * The "browser" parameter is specified in the different testng-*.xml found
	 * in src/test/resources. 
	 */
	@Parameters({ "browser" })
	@BeforeClass
	public void initializeTestBrowserWindow(String browser) {
		// Initialize a driver for the given browser type
		this.driver = this.initializeDriver(browser);

		// Initialize injected browser
		this.driver = this.browserType.getDriver();
		
		// Clear any stored cookies / state
		this.driver.manage().deleteAllCookies();
		
		/*
		 * Any other options that we may want to
		 * include for ALL tests could go here 
		 */
		
		// Maximize the window
		this.driver.manage().window().maximize();
	}
	
	/**
	 * Tears down our test environment, which should close out all open browser windows.
	 * 
	 * NOTE: this is currently broken for Opera.
	 */
	@AfterClass
	public void closeBrowserWindows() {
		// FIXME: this does not seem to work for Opera...
		this.driver.quit();
	}
	
	/** 
	 * Saves the name of the current test method. This allows us to easily
	 * save a screenshot named for each test case.
	 */
	@BeforeMethod
    public void saveTestMethodNameForScreenshot(Method method)
    {
		this.currentTestName = method.getName();
    }

	/**
	 * After each test method runs (pass or fail), take a screenshot of the result of the test
	 * and save it to the directory defined by TEST_OUTPUT_DIR. Screenshot titles will be 
	 * formatted as: "TESTNAME_BROWSERTYPE.png". If screenshots are not available for the
	 * current browser type, this method performs prints a warning to the console.
	 * 
	 * TODO: Investigate only saving failed test cases. Although passing cases
	 * would be nice to include as well, in case we wish to manually inspect elements
	 * on the page.
	 * 
	 * @throws IOException if there is an error copying the file from the temp directory to TEST_OUTPUT_DIR
	 */
	@AfterMethod
	public void takeScreenshotOfTestResult() throws IOException {
		// TODO: Use a real Logger (log4j?)
		System.out.println(String.format("Final page (source) of test is: %s", this.driver.getPageSource()));
		
		String destinationFileName = this.browserType.toString() + "-" + this.currentTestName  + ".png";
		this.takeScreenshotOfPage(destinationFileName);
	}
	
	protected void takeScreenshotOfPage(String destinationFileName) {
		if (this.driver instanceof TakesScreenshot) {
			try {
				// Source: Save a screenshot to the temp folder (environment-specific)
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				
				// Destination: TEST_OUTPUT_DIR, following the formatting outlined above
				File destFile = new File(TEST_OUTPUT_DIR, destinationFileName);
				
				// Copy from the temp directory to save the file permanently
				FileUtils.copyFile(scrFile, destFile);
			} catch (WebDriverException | IOException e) {
				// TODO: Use a real Logger (log4j?)
				System.out.println(String.format("ERROR: Failed to save screenshot (%s) on browser %s", destinationFileName, this.browserType.toString()));
			} 
		} else {
			// TODO: Use a real Logger (log4j?)
			System.out.println(String.format("WARNING: 'Take Screenshot' operation not available in browser type: %s", this.browserType.toString()));
		}
	}
}
