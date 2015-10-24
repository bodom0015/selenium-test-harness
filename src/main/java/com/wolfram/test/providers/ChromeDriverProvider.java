package com.wolfram.test.providers;

import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wolfram.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Google Chrome.</p>
 * <p>All versions require downloading the external chromedriver</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/ChromeDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class ChromeDriverProvider implements IWebDriverProvider {
	/** The path to the chromedriver binary */
	private static final String CHROME_BINARY_PATH = "webdriver.chrome.driver";

	/** The current singleton instance */
	private static ChromeDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static ChromeDriver driver;
	
    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static ChromeDriverProvider getCurrent() {
        if (instance == null) {
            instance = new ChromeDriverProvider();
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
		
		ResourceBundle rb = ResourceBundle.getBundle("environment");
		System.setProperty(CHROME_BINARY_PATH, rb.getString(CHROME_BINARY_PATH));
		
		// Set any browser-specific settings here
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        return new ChromeDriver(capabilities);
	}

}
