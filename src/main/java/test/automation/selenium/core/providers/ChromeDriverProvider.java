package test.automation.selenium.core.providers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import test.automation.selenium.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Google Chrome.</p>
 * <p>All versions require downloading the external chromedriver</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/ChromeDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class ChromeDriverProvider implements IWebDriverProvider {
	/** The name of the .properties file from which to read driver binary paths */
	private static final String ENV_BUNDLE_NAME = "environment";
	
	/** The key in the above .properties file for the chrome driver binary */
	private static final String CHROME_BINARY_KEY = "webdriver.chrome.driver";

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
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle("environment");
			System.setProperty(CHROME_BINARY_KEY, rb.getString(CHROME_BINARY_KEY));
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("Cannot start ChromeDriver: Driver binary not found. Verify " 
					+ "correctness / existence of "+ CHROME_BINARY_KEY + " in " + ENV_BUNDLE_NAME + ".properties");
		}
		
		// Set any browser-specific settings here
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

		// Cache this driver for later
        driver = new ChromeDriver(capabilities);
        return driver;
	}

}
