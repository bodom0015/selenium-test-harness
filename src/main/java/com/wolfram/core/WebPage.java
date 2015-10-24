package com.wolfram.core;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * An abstract class to represent each PageObject in the Web Application
 * that we wish to include in automated testing. The constructor will the use PageFactory
 * 
 * @author Mike Lambert
 * 
 */
public abstract class WebPage {
	/** 
	 * Number of seconds deemed appropriate 
	 * to explicitly wait for page / element load 
	 */
	protected static final int waitSeconds = 15;

	/**
	 * A reusable construct allowing us to wait the above amount of seconds.
	 */
    protected final WebDriver driver;

	/**
	 * A reusable construct allowing us to wait the above amount of seconds.
	 */
	protected final WebDriverWait wait;

	/**
	 * Returns the title to expect for this page
	 */
	protected abstract String getExpectedPageTitle();
	
	/**
	 * Returns the starting point for testing this page
	 */
	protected abstract String getStartUrl();
	
	/** 
	 * Initializes the page using the driver provided and the default wait time. 
	 * This method will return only after our page title matches the expected title.
	 */
	protected WebPage(WebDriver driver) {
		this(driver, new WebDriverWait(driver, waitSeconds));
	}

	/** 
	 * Additional constructor for control freaks. Initializes the page using 
	 * the driver and wait provided. This method will return 
	 * only after our page title matches the expected title.
	 */
	protected WebPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		
        PageFactory.initElements(this.driver, this);
        
        // Wait for some expectations
        wait.until(ExpectedConditions.urlContains(getStartUrl()));
		wait.until(ExpectedConditions.titleContains(getExpectedPageTitle()));
	}

	/**
	 * Verify that the current page title matches our expectation
	 * @return true iff our current page title matches our expectation
	 */
	public boolean verifyPageTitle() {
		String expected = getExpectedPageTitle();
		String actual = this.driver.getTitle();
		return expected.equals(actual);
	}

	/**
	 * Switches to a newly-opened window and returns the previous window handle
	 * @return the previous window handle
	 */
	public String switchToNewWindowOrTab() {
		String previousHandle = driver.getWindowHandle();

		wait.until(new Predicate<WebDriver>() {
		    public boolean apply(final WebDriver d) {
	        	return driver.getWindowHandles().size() > 1;
		    }
		});
		
		// -------------------------------------------------------------------
		// FIXME: Opera has issues switching tabs / windows
		// See https://github.com/operasoftware/operachromiumdriver/issues/15
		// -------------------------------------------------------------------

		// Hacky workaround just for Opera
		wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("browser://startpage/")));
		wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("Speed Dial")));
		
		printOpenWindows();
		//wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs.get(1));
	    //driver.close();
		printOpenWindows();
	    //driver.switchTo().window(tabs.get(0));
	    
	    if ("browser://startpage/".equals(driver.getCurrentUrl())) {
			//wait.until(ExpectedConditions.numberOfWindowsToBe(2));
	    	ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(1));
			printOpenWindows();
	    }
		
		// Wait for the new window to open (Chrome / Opera seem to need this)
		/*wait.until(new Predicate<WebDriver>() {
	        public boolean apply(final WebDriver d) {
	    		// Now switch to the new window

	    		List<String> allWindows = new ArrayList<String>(driver.getWindowHandles());
	            int numWindows = allWindows.size();
	    		String newWindow = allWindows.get(numWindows - 1);
	    		driver.switchTo().window(newWindow);
	    		
	    		// Hacky workaround just for Opera
	    		// (switch windows until we pass "Speed Dial", Opera's start page)
	            return !driver.getTitle().contains("Speed Dial");
	        }
	    });*/
		 
		return previousHandle;
	}
	
	public void printOpenWindows() {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		for (String handle : tabs) {
			driver.switchTo().window(handle);
			System.out.println("New Window Handle: " + handle);
			//System.out.println("New Window URL: " + driver.getCurrentUrl());
			//System.out.println("New Window Title: " + driver.getTitle());
		}
	}
}
