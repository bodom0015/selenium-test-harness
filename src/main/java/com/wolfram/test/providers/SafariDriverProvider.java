package com.wolfram.test.providers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.wolfram.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Apple Safari.</p>
 * <p>All versions require adding Safari to your PATH</p>
 * <p>SafariDriver now requires manual installation of the extension prior to automation.</p>
 * <p><strong>NOTE:</strong> Safari 6+ is no longer supported on Windows</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/SafariDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class SafariDriverProvider implements IWebDriverProvider {
	//private static final String SAFARI_BINARY_PATH = "webdriver.safari.driver";

	/** The current singleton instance */
	private static SafariDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static SafariDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static SafariDriverProvider getCurrent() {
        if (instance == null) {
            instance = new SafariDriverProvider();
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
		
		// Cache this driver for later
		driver = new SafariDriver(DesiredCapabilities.safari());
		return driver;
	}
}
