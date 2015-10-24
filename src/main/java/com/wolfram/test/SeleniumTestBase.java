package com.wolfram.test;

import org.openqa.selenium.WebDriver;

import com.wolfram.core.BrowserType;

/**
 * A simple base class to represent a set of tests that
 * we plan to run against multiple browsers (WebDrivers).
 * 
 * @author Mike Lambert
 *
 */
public abstract class SeleniumTestBase {
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
		
		return this.driver;
	}
}
