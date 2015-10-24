package com.wolfram.test.providers;

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
	private static final String OPERADRIVER_BINARY_PATH = "webdriver.opera.driver";
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
		
		ResourceBundle rb = ResourceBundle.getBundle("environment");
		System.setProperty(OPERADRIVER_BINARY_PATH, rb.getString(OPERADRIVER_BINARY_PATH));
		System.setProperty(OPERALAUNCHER_INSTALL_PATH, rb.getString(OPERALAUNCHER_INSTALL_PATH));
		System.out.println("Executing using: " + System.getProperty(OPERADRIVER_BINARY_PATH));
		System.out.println("Executing using: " + System.getProperty(OPERALAUNCHER_INSTALL_PATH));
		
		// Set any browser-specific settings here
		// XXX: Opera capabilities are deprecated, since opera is chromium we can use chrome instead
		// TODO: investigate operaBlink().. is this for mobile?
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		
		// Opera does not seem to check the PATH by default in all circumstances
		// See https://github.com/operasoftware/operachromiumdriver/issues/9
		ChromeOptions options = new ChromeOptions();
		options.setBinary(System.getProperty("opera.binary"));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		// Cache this driver for later
		driver = new OperaDriver(capabilities/*DesiredCapabilities.operaBlink()*/);
		return driver;
	}

}
