package test.automation.selenium.core.providers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import test.automation.selenium.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Microsoft Edge.</p>
 * <p>All versions require adding Edge to your PATH</p>
 * <p><strong>NOTE:</strong> Edge is only available on Windows 10</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/EdgeDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class EdgeDriverProvider implements IWebDriverProvider {
	/** The name of the .properties file from which to read driver binary paths */
	//private static final String ENV_BUNDLE_NAME = "environment";

	/** The key in the above .properties file for the edge driver binary */
	//private static final String EDGE_BINARY_PATH = "webdriver.edge.driver";

	/** The current singleton instance */
	private static EdgeDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static EdgeDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static EdgeDriverProvider getCurrent() {
        if (instance == null) {
            instance = new EdgeDriverProvider();
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
		DesiredCapabilities capabilities = DesiredCapabilities.edge();

		// Cache this driver for later
		driver = new EdgeDriver(capabilities);
		return driver;
	}

}
