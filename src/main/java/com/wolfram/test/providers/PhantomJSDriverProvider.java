package com.wolfram.test.providers;

import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wolfram.core.IWebDriverProvider;

/**
 * <p>A provider for the Selenium WebDriver for PhantomJS.</p>
 * <p>All versions require downloading the external PhantomJS.</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/PhantomJSDriver">Selenium Documentation</a>
 * @see <a href="https://github.com/detro/ghostdriver">Repository / Documentation</a>
 * @author Mike Lambert
 *
 */
public final class PhantomJSDriverProvider implements IWebDriverProvider {
	private static final String PHANTOMJS_BINARY_PATH = "phantomjs.binary.path";

	/** The current singleton instance */
	private static PhantomJSDriverProvider instance;
	
	/** The instance of this browser driver to use to run tests */
	private static PhantomJSDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static PhantomJSDriverProvider getCurrent() {
        if (instance == null) {
            instance = new PhantomJSDriverProvider();
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
		
		// Set the system property to point to the correct binary
		ResourceBundle rb = ResourceBundle.getBundle("environment");
		System.setProperty(PHANTOMJS_BINARY_PATH, rb.getString(PHANTOMJS_BINARY_PATH));
		System.out.println("Executing using: " + System.getProperty(PHANTOMJS_BINARY_PATH));
		
		// Set any browser-specific settings here
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});
		driver = new PhantomJSDriver(capabilities);
		return driver;
	}

}
