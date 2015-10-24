package com.wolfram.test.providers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wolfram.core.IWebDriverProvider;

/** 
 * <p>A provider for the Selenium WebDriver for Microsoft Internet Explorer.</p>
 * 
 * <p>All versions require downloading the external IEServerDriver.exe (Windows only)</p>
 * <p>IE7+ requires that all zones share the same status for Protected Mode on Internet Options -> Security</p>
 * <p>IE10+ also requires that Enhanced Protected Mode be disabled on Internet Options -> Advanced</p>
 * <p>IE11+ further requires that a custom registry key to be added:
 * 	 <ul>
 *     <li>For 32-bit Windows installations, the key you must examine in the registry editor is 
 * 		 <pre>HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE</pre></li>
 *     <li>For 64-bit Windows installations, the key is 
 *   	 <pre>HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE</pre></li>
 *   </ul>
 * </p> 
 * <p>Please note that the FEATURE_BFCACHE subkey may or may not be present, and should be created if it is not present.</p>
 * <p><strong>Important:</strong> Inside this key, create a DWORD value named iexplore.exe with the value of 0.</p>
 * 
 * @see <a href="https://code.google.com/p/selenium/wiki/InternetExplorerDriver">Selenium Documentation</a>
 * @author Mike Lambert
 *
 */
public final class InternetExplorerDriverProvider implements IWebDriverProvider {
	/** The name of the .properties file from which to read driver binary paths */
	private static final String ENV_BUNDLE_NAME = "environment";
	
	/** The key in the above .properties file for the IEDriverServer.exe binary */
	private static final String IE_BINARY_KEY = "webdriver.ie.driver";

	/** The current singleton instance */
	private static InternetExplorerDriverProvider instance;

	/** The instance of this browser driver to use to run tests */
	private static InternetExplorerDriver driver;

    /**
     * Returns the current singleton instance for this class.
     *
     * @return the current instance
     */
    public static InternetExplorerDriverProvider getCurrent() {
        if (instance == null) {
            instance = new InternetExplorerDriverProvider();
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
			System.setProperty(IE_BINARY_KEY, rb.getString(IE_BINARY_KEY));
			System.out.println("Executing using: " + System.getProperty(IE_BINARY_KEY));
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("Cannot start IEDriverServer.exe: Driver binary not found. Verify " 
					+ "correctness / existence of "+ IE_BINARY_KEY + " in " + ENV_BUNDLE_NAME + ".properties");
		}

		// Set any browser-specific settings here
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        
        // Cache this driver
        driver = new InternetExplorerDriver(capabilities);
        return driver;
	}

}
