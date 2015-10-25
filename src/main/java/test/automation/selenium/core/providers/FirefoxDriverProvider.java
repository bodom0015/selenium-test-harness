package test.automation.selenium.core.providers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import test.automation.selenium.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Mozilla Firefox.</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/FirefoxDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class FirefoxDriverProvider implements IWebDriverProvider {
	/** The current singleton instance */
	private static FirefoxDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static FirefoxDriver driver; 

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static FirefoxDriverProvider getCurrent() {
        if (instance == null) {
            instance = new FirefoxDriverProvider();
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
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();

		// Cache this driver for later
		driver = new FirefoxDriver(capabilities);
		return driver;
	}
}
