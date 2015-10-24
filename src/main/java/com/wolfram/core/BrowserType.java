package com.wolfram.core;

import org.openqa.selenium.WebDriver;

import com.wolfram.test.providers.ChromeDriverProvider;
import com.wolfram.test.providers.EdgeDriverProvider;
import com.wolfram.test.providers.FirefoxDriverProvider;
import com.wolfram.test.providers.HtmlUnitDriverProvider;
import com.wolfram.test.providers.InternetExplorerDriverProvider;
import com.wolfram.test.providers.OperaDriverProvider;
import com.wolfram.test.providers.PhantomJSDriverProvider;
import com.wolfram.test.providers.SafariDriverProvider;

/**
 * An enum representing the different types of browsers. The intention is to keep any 
 * and all browser-specific configuration or setup enclosed within this class.
 * 
 * @author Mike Lambert
 *
 */
public enum BrowserType {
	/** Mozilla Firefox Browser */
	FIREFOX("firefox"),
	
	/** Google Chrome Browser */
	CHROME("chrome"),
	
	/** Microsoft Internet Explorer */
	IE("ie"),
	
	/** Opera Browser */
	OPERA("opera"),
	
	/** PhantomJS Headless Browser */
	PHANTOMJS("phantomjs"),
	
	/** HtmlUnit Headless Browser */
	HTMLUNIT("htmlunit"),
	
	/** Apple Safari Browser */
	SAFARI("safari"),
	
	/** Microsoft Edge Browser */
	EDGE("edge")
	;
	
	/**
	 * Internal enum string value
	 */
    private final String browserString;

    /** 
     * Internal constructor for enum values 
     */
    private BrowserType(final String browser) {
        this.browserString = browser.toLowerCase();
    }
    
    /** 
     * Given the current enum value, retrieve the driver representing that browser type.
     * This indirect access method allows us to lazy-load all drivers, while still keeping 
     * all of the browser string / driver initialization logic together. The test harness 
     * will not attempt to initialize a selenium driver unless we explicitly call the method
     * to do so.
     */
    public WebDriver getDriver() {    	
    	switch (this.browserString) {
    		case "firefox":
    			return FirefoxDriverProvider.getCurrent().getDriver();
    		case "chrome":
    			return ChromeDriverProvider.getCurrent().getDriver();
    		case "ie":
    			return InternetExplorerDriverProvider.getCurrent().getDriver();
    		case "opera":
    			return OperaDriverProvider.getCurrent().getDriver();
    		case "htmlunit":
    			return HtmlUnitDriverProvider.getCurrent().getDriver();
    		case "phantomjs":
    			return PhantomJSDriverProvider.getCurrent().getDriver();
    		case "edge":
    			return EdgeDriverProvider.getCurrent().getDriver();
    		case "safari":
    			return SafariDriverProvider.getCurrent().getDriver();
			default:
				throw new IllegalArgumentException("Unrecognized browser type: " + this.browserString);
    	}
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.browserString;
    }
}
