package com.wolfram.cloud;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import test.automation.selenium.core.AbstractWebPage;

/**
 * PageObject representing the "Wolfram Cloud" landing page. This page presents the users
 * with several links, allowing them to access Wolfram's different cloud services.
 * 
 * @author Mike Lambert
 * 
 */
public class CloudLandingPage extends AbstractWebPage {
	/** The Constant expected title for this page. */
	public static final String PAGE_TITLE = "Wolfram Cloud";

	// Build up / parameterize all parts of the URL
	private static final String BASE_URL = "http://www.wolframcloud.com/";

	/** The URL at which to point to start testing this page */
	public static final String START_URL = BASE_URL;
    
    /** The Wolfram Development Platform link. */
	@FindBy(how = How.CLASS_NAME, using = "product-link")
	@CacheLookup
    private WebElement wdpLink;
    
    /** The iframe HTML tag containing the page contents. */
    @FindBy(how = How.TAG_NAME, using = "iframe")
	@CacheLookup
    private WebElement iframeTag;

	/* (non-Javadoc)
	 * @see wolfram.cloud.WebPage#getExpectedPageTitle()
	 */
	@Override
	protected String getExpectedPageTitle() {
		return PAGE_TITLE;
	}
	
	/* (non-Javadoc)
	 * @see wolfram.cloud.WebPage#getExpectedPageTitle()
	 */
	@Override
	protected String getStartUrl() {
		return START_URL;
	}
    
    /**
     * Instantiates a new cloud landing page.
     *
     * @param driver the driver
     */
    public CloudLandingPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.urlToBe(START_URL));
    }
    
    /**
     * Instantiates a new cloud landing page.
     *
     * @param driver the driver
     * @param wait the wait
     */
    public CloudLandingPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.urlToBe(START_URL));
    }
	
	/**
	 * Switches to the first iframe on the page.
	 *
	 * @return the cloud landing page
	 */
	private CloudLandingPage switchToIFrame() {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeTag));
		return this;
	}
	
	/**
	 * Clicks the WDP tile on the Wolfram Cloud landing page.
	 *
	 * @return the sign in page
	 */
	private SignInPage clickWolframDevPlatformTile() {
		wait.until(ExpectedConditions.visibilityOf(this.wdpLink));
		wait.until(ExpectedConditions.elementToBeClickable(this.wdpLink));
		this.wdpLink.click();
		return new SignInPage(this.driver, this.wait);
	}

	/**
	 * Simulates a user clicking on the "Wolfram Development Platform" link.
	 *
	 * @return the sign in page
	 */
    public SignInPage chooseWolframDevelopmentPlatform() {
    	this.switchToIFrame();
    	return this.clickWolframDevPlatformTile();
    }
}
