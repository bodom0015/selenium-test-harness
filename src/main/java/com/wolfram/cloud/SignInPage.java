package com.wolfram.cloud;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wolfram.core.WebPage;



/**
 * PageObject representing the Sign In page. This page allows the user to authenticate 
 * themselves against Wolfram's user base. The page URL contains an OAuth token and the 
 * URL to which the user should be redirected upon successful login.  
 * 
 * @author Mike Lambert
 * 
 */
public class SignInPage extends WebPage {
	/** The Constant expected title for this page. */
	public static final String PAGE_TITLE = "Sign In - Wolfram Development Platform";
	
	// Build up / parameterize all parts of the URL
	private static final String BASE_URL = "https://user.wolfram.com";
	private static final String RELATIVE_URL = "/oauth/authorize/cloud";
	private static final String OAUTH_TOKEN_QUERY = "oauth_token=";
	private static final String OAUTH_WDP_CALLBACK_QUERY = "oauth_callback=https%3A%2F%2Fdev.wolframcloud.com%2Fapp%2Fj_spring_oauth_security_check"
											   + "%3Fproductname%3DWolfram%20Development%20Platform%26learnabout%3Dhttp%3A%2F%2Fwww.wolfram.com"
											   + "%2Fdevelopment-platform%26preview%3Dfalse%26logo%3Dwolfram_development_platform";

	// XXX: This is actually attempting to navigate to the dev platform home page, and signin intercepts it
	/** The URL at which to point to start testing this page */
	public static final String START_URL = DevPlatformHomePage.START_URL;
    
	/** The email input. */
	@CacheLookup
	private WebElement email;
    
    /** The password input. */
	@CacheLookup
    private WebElement password;
    
    /** The sign in button. */
	@FindBy(how = How.ID, using = "signIn")
	@CacheLookup
    private WebElement signIn;

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
		return BASE_URL + RELATIVE_URL;
	}
	
	/** 
	 * Given an oAuth token from a previous session's URL, build
	 * and return the full URL of that page
	 */
	public static String generateFullUrl(String token) {
		return BASE_URL + RELATIVE_URL + generateQueryStrings(token);
	}

	/** 
	 * A helper method to generate the query strings at the end of the full URL.
	 * We could easily expand this method to parameterize the oauth callback path
	 * if we wanted to test services other than the Wolfram Development Platform.
	 */
	private static String generateQueryStrings(String token) {
		return "?" + OAUTH_TOKEN_QUERY + token + "&" + OAUTH_WDP_CALLBACK_QUERY;
	}
    
    /**
     * Instantiates a new sign in page.
     *
     * @param driver the driver
     */
    public SignInPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Instantiates a new sign in page.
     *
     * @param driver the driver
     * @param wait the wait
     */
    public SignInPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
	
    /**
     * Clears the contents of the "email" input field and 
     * types the provided email address into it.
     *
     * @param emailAddress the email to input
     * @return the sign in page
     */
    private SignInPage typeEmail(String emailAddress) {
		wait.until(ExpectedConditions.visibilityOf(this.email));
		wait.until(ExpectedConditions.elementToBeClickable(this.email));
		this.email.clear();
        this.email.sendKeys(emailAddress);
		return this;
	}
	
	/**
     * Clears the contents of the "password" input field and 
     * types the provided password into it.
	 *
	 * @param passwordString the password to input
	 * @return the sign in page
	 */
	private SignInPage typePassword(String passwordString) {
		wait.until(ExpectedConditions.visibilityOf(this.password));
		wait.until(ExpectedConditions.elementToBeClickable(this.password));
		this.password.clear();
		this.password.sendKeys(passwordString);
		return this;
	}
		
    /**
     * Submit credentials for signin, expecting them to be correct and
     * allow the user access to the WDP home page.
     *
     * @return the sign in page
     */
	private DevPlatformHomePage submitValidSignIn() {
		wait.until(ExpectedConditions.visibilityOf(this.signIn));
		wait.until(ExpectedConditions.elementToBeClickable(this.signIn));
		this.password.sendKeys(Keys.ENTER);
		this.signIn.click();
		return new DevPlatformHomePage(this.driver, this.wait);
	}
	
    /**
     * Submit credentials for signin, expecting them to be invalid and 
     * prevent the user from accessing Wolfram services.
     *
     * @return the sign in page
     */
	private SignInPage submitInvalidSignIn() {
		wait.until(ExpectedConditions.visibilityOf(this.signIn));
		wait.until(ExpectedConditions.elementToBeClickable(this.signIn));
		this.password.sendKeys(Keys.ENTER);
		this.signIn.click();
		return new SignInPage(this.driver, this.wait);
	}

    /**
     * Sign into the Wolfram Cloud.
     *
     * @param email the email
     * @param password the password
     * @return the dev platform home page (successful login)
     */
    public DevPlatformHomePage signInAs(String email, String password) {
    	return this.typeEmail(email).typePassword(password).submitValidSignIn();
    }
    
    /**
     * Attempt to sign into the Wolfram Cloud, expecting failure.
     *
     * @param email the email
     * @param password the password
     * @return the sign in page (failed login)
     */
    public SignInPage failSignInAs(String email, String password) {
    	return this.typeEmail(email).typePassword(password).submitInvalidSignIn();
    }
}
