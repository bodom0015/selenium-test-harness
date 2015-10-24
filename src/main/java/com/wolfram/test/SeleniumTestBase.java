package com.wolfram.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.wolfram.core.BrowserType;

/**
 * A simple base class to represent a set of tests that
 * we plan to run against multiple browsers (WebDrivers).
 * 
 * @author Mike Lambert
 *
 */
public abstract class SeleniumTestBase {
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
	protected BrowserType initializeDriver(String browser) {
		this.browserType = BrowserType.valueOf(browser.toUpperCase());
		
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
		
		return this.browserType;
	}
	
	/**
	 * Sets up our testing environment by instantiating a new driver to use.
	 * The "browser" parameter is specified in the different testng-*.xml found
	 * in src/test/resources. 
	 */
	@Parameters({ "browser" })
	@BeforeClass
	public void initializeBrowser(String browser) {
		// Call initializeDriver for the given browser type
		this.initializeDriver(browser);
				
		/*
		 * Any other options that we may want to
		 * include for ONLY this test class could go here 
		 */
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
	 * After each test method runs, take a screenshot of the result of the test
	 * and save it to the directory defined by TEST_OUTPUT_DIR.
	 * 
	 * Screenshot titles will be formatted as: "TESTNAME_BROWSERTYPE.png"
	 * 
	 * @throws IOException if there is an error copying the file from the temp directory to TEST_OUTPUT_DIR
	 */
	@AfterMethod
	public void takeScreenshotOfTestResult() throws IOException {
		// Source: Save a screenshot to the temp folder (environment-specific)
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		// Destination: TEST_OUTPUT_DIR, following the formatting outlined above
		File destFile = new File(TEST_OUTPUT_DIR, this.currentTestName + "-" + this.browserType.toString() + ".png");
		
		// Perform the file copy
		FileUtils.copyFile(scrFile, destFile);
	}
}
