package com.wolfram.test.providers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wolfram.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for Opera.</p>
 * <p>All versions require downloading the external operadriver</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/OperaDriver">Selenium Documentation</a>
 * @see <a href="https://github.com/operasoftware/operachromiumdriver">Repository / Documentation</a>
 * @author Mike Lambert
 *
 */
public final class OperaDriverProvider implements IWebDriverProvider {
	/** The name of the .properties file from which to read driver binary paths */
	private static final String ENV_BUNDLE_NAME = "environment";
	
	/** The key in the above .properties file for the opera driver binary */
	private static final String OPERADRIVER_BINARY_PATH = "webdriver.opera.driver";

	// Opera does not seem to check the PATH by default in all circumstances
	// See https://github.com/operasoftware/operachromiumdriver/issues/9
	/** The key in the above .properties file for the opera launcher binary */
	private static final String OPERALAUNCHER_INSTALL_PATH = "opera.binary";

	/** The current singleton instance */
	private static OperaDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static OperaDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static OperaDriverProvider getCurrent() {
        if (instance == null) {
            instance = new OperaDriverProvider();
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
			ResourceBundle rb = ResourceBundle.getBundle(ENV_BUNDLE_NAME);
			System.setProperty(OPERALAUNCHER_INSTALL_PATH, rb.getString(OPERALAUNCHER_INSTALL_PATH));
			System.out.println("Executing using: " + System.getProperty(OPERALAUNCHER_INSTALL_PATH));
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("Cannot start OperaDriver: Launcher binary not found. Verify " 
					+ "correctness / existence of "+ OPERALAUNCHER_INSTALL_PATH + " in " + ENV_BUNDLE_NAME + ".properties");
		}
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle(ENV_BUNDLE_NAME);
			System.setProperty(OPERADRIVER_BINARY_PATH, rb.getString(OPERADRIVER_BINARY_PATH));
			System.out.println("Executing using: " + System.getProperty(OPERADRIVER_BINARY_PATH));
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("Cannot start OperaDriver: Driver binary not found. Verify " 
					+ "correctness / existence of "+ OPERADRIVER_BINARY_PATH + " in " + ENV_BUNDLE_NAME + ".properties");
		}
		
		// Set any browser-specific settings here
		// XXX: Opera capabilities are deprecated, since opera is chromium we can use chrome instead
		// TODO: investigate operaBlink().. is this for mobile?
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		
		// Set the launcher.exe path we read in earlier
		options.setBinary(System.getProperty("opera.binary"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		// Cache this driver for later
		driver = new OperaDriver(capabilities/*DesiredCapabilities.operaBlink()*/);
		return driver;
	}

}
