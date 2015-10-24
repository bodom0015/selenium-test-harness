package com.wolfram.core;

import org.openqa.selenium.WebDriver;

/**
 * A helper method to initialize the desired browser type. 
 * This should localize most of our browser-specific setup. 
 * 
 * @author Mike Lambert
 *
 */
public interface IWebDriverProvider {
	/**
	 * Initializes and configures the desired WebDriver
	 */
	public WebDriver getDriver();
}
