package com.wolfram.test.providers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wolfram.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for HtmlUnit.</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/HtmlUnitDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class HtmlUnitDriverProvider implements IWebDriverProvider {
	/** The current singleton instance */
	private static HtmlUnitDriverProvider instance;

	/** The instance of this browser driver to use to run tests */
	private static HtmlUnitDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static HtmlUnitDriverProvider getCurrent() {
        if (instance == null) {
            instance = new HtmlUnitDriverProvider();
        }
      
        return instance;
    }
    
	/* (non-Javadoc)
	 * @see com.wolfram.core.IWebDriverProvider#getDriver()
	 */
	@Override
	public WebDriver getDriver() {
		if (driver != null) {
			return driver;
		}
		
		// Set any browser-specific settings here
		DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
		//capabilities.setJavascriptEnabled(true); // Optionally enable JavaScript

		// Cache this driver for later
		driver = new HtmlUnitDriver(capabilities);
		return driver;
	}
}
