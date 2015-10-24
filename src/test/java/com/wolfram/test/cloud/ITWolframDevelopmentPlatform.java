package com.wolfram.test.cloud;

import static org.testng.Assert.*;

import java.util.ResourceBundle;

import org.testng.annotations.Test;

import com.wolfram.cloud.CloudLandingPage;
import com.wolfram.cloud.DevPlatformHomePage;
import com.wolfram.cloud.NotebookViewPage;
import com.wolfram.cloud.SignInPage;
import com.wolfram.test.SeleniumTestBase;


/**
 * This is a simple TestNG Test Suite to verify the workflow described below.
 * 
 * <ol>
 * <li>Go to Wolfram Cloud (www.wolframcloud.com).</li>
 * <li>Click Wolfram Development Platform. Please create a new Wolfram ID, and you can subscribe to a Free plan.</li> 
 * <li>After signing in successfully, you will be taken to what we call, the Homescreen.</li>
 * <li>From the Homescreen, users can create new notebooks, upload files, etc.</li>
 * <li>On the right-hand side of the application, you will see a red "New" button.</li>
 * <li>By clicking the down-arrow button, you can create different types of files (.nb, .html, .css, etc).</li>
 * <li>Create a .nb notebook.</li>
 * <li>In the new notebook, in the file header, click the file name field, "(unnamed)" -- you will see that the extension, ".nb" is automatically present.</li>
 * 
 * <p>Each test case represents passing through a page of functionality in the workflow. 
 * It could easily be expanded to include testing other portions of the Wolfram Cloud app as well.
 * If the test-case-per-page scheme gets out of hand, a better scheme might be to have a 
 * test-class-per-page. Given the current scope of tested material, this scheme seemed appropriate.</p>
 */
public class ITWolframDevelopmentPlatform extends SeleniumTestBase {
    /** Name for the group which tests the CloudLandingPage */
    private static final String TEST_GROUP_1 = "landing";

    /** Name for the group which tests the SignInPage, expecting failure  */
    private static final String TEST_GROUP_2 = "invalidSignin";

    /** Name for the group which tests the SignInPage, expecting success */
    private static final String TEST_GROUP_3 = "validSignin";

    /** Name for the group which tests the DevPlatformHomePage */
    private static final String TEST_GROUP_4 = "devPlatformHome";

    /** Name for the group which tests the NotebookViewPage */
    private static final String TEST_GROUP_5 = "notebook";
    
    // Read in credential info from credentials.properties
    // TODO: Assumes existence of this file... this could be a little more elegant
	private static final ResourceBundle rb = ResourceBundle.getBundle("credentials");
	
    // Test data for success cases:
    // TODO: Assumes existence of these properties... this could be a little more elegant
	private static String TEST_ACCOUNT_EMAIL = rb.getString("test.user.valid.email");
	private static String TEST_ACCOUNT_PASSWORD = rb.getString("test.user.valid.password");
	
	// Test data for failure cases:
    // TODO: Assumes existence of these properties... this could be a little more elegant
	private static String INVALID_TEST_EMAIL = rb.getString("test.user.invalid.email");
	private static String INVALID_TEST_PASSWORD = rb.getString("test.user.invalid.password");
	
	/** Our current instance of the sign-in page */
    private SignInPage signIn;

	/** Our current instance of the dev platform home page */
    private DevPlatformHomePage devPlatformHome;
    
	/** Our current instance of the ntoebook view page */
    private NotebookViewPage noteBookView;

    /** Our newly created notebook's hash (once we create it) */
	private String notebookHash;

	/**
	 * Ensure that the user is taken to the landing page, and that they
	 * can then navigate to the sign-in page.
	 */
	@Test(groups=TEST_GROUP_1)
	public void testCloudLandingPage() {
		 // Navigate to the start page
        driver.get(CloudLandingPage.START_URL);
        
        // Ensure that we have landed on the landing page
        CloudLandingPage landing = new CloudLandingPage(driver);
        assertEquals(driver.getTitle(), CloudLandingPage.PAGE_TITLE);
		
		// Click "Wolfram Development Platform" and ensure that we land on the signin page
        signIn = landing.chooseWolframDevelopmentPlatform();
        
        /* 
         * NOTE: the test framework will never get to this assertion in a failure case, 
         * as it will timeout while waiting for the page title before reaching this statement.
         * I include this assertion to be explicit about which page we end up on
         */
        assertEquals(driver.getTitle(), SignInPage.PAGE_TITLE);
	}
	
	/**
	 * Ensure that attempting to sign-in with an incorrect email address
	 * does not allow the user access to the home page.
	 */
	@Test(groups=TEST_GROUP_2, dependsOnGroups=TEST_GROUP_1)
	public void testSignInPageInvalidEmail() {
		//driver.get(SignInPage.getPageUrl(oAuthToken));
		driver.get(SignInPage.START_URL);
        signIn = new SignInPage(driver);
        signIn = signIn.failSignInAs(INVALID_TEST_EMAIL, TEST_ACCOUNT_PASSWORD);
        assertEquals(driver.getTitle(), SignInPage.PAGE_TITLE, "Failed login attempt should redirect back to Sign In Page.");
	}
	
	/**
	 * Ensure that attempting to sign-in with an incorrect password
	 * does not allow the user access to the home page.
	 */
	@Test(groups=TEST_GROUP_2, dependsOnGroups=TEST_GROUP_1)
	public void testSignInPageInvalidPassword() {
		//driver.get(SignInPage.getPageUrl(oAuthToken));
		driver.get(SignInPage.START_URL);
        signIn = new SignInPage(driver);
		signIn = signIn.failSignInAs(TEST_ACCOUNT_EMAIL, INVALID_TEST_PASSWORD);
        assertEquals(driver.getTitle(), SignInPage.PAGE_TITLE, "Failed login attempt should redirect back to Sign In Page.");
	}
	
	/** 
	 * Ensure that attempting to sign-in with an incorrect email address
	 * and an incorrect password does not allow the user access to the home page.
	 */
	@Test(groups=TEST_GROUP_2, dependsOnGroups=TEST_GROUP_1)
	public void testSignInPageInvalidEmailAndPassword() {
		//driver.get(SignInPage.getPageUrl(oAuthToken));
		driver.get(SignInPage.START_URL);
        signIn = new SignInPage(driver);
        signIn = signIn.failSignInAs(INVALID_TEST_EMAIL, INVALID_TEST_PASSWORD);
        assertEquals(driver.getTitle(), SignInPage.PAGE_TITLE, "Failed login attempt should redirect back to Sign In Page.");
	}
	
	/** 
	 * Ensure that signing-in with valid credentials allows
	 * the user access to the home page.
	 */
	@Test(groups=TEST_GROUP_3, dependsOnGroups=TEST_GROUP_2)
	public void testSignInPage() {
		//driver.get(SignInPage.getPageUrl(oAuthToken));
		driver.get(SignInPage.START_URL);
        signIn = new SignInPage(driver);
		devPlatformHome = signIn.signInAs(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
        assertEquals(driver.getTitle(), DevPlatformHomePage.PAGE_TITLE, "Valid login should redirect back to Wolfram Development Platform Home Page.");
	}

	/**
	 * Ensure that once signed into the WDP home page, 
	 * the user can create a new notebook.
	 */
	@Test(groups=TEST_GROUP_4, dependsOnGroups=TEST_GROUP_3)
	public void testDevPlatformHomePage() {
		driver.get(DevPlatformHomePage.START_URL);
        noteBookView = devPlatformHome.createNewNotebook();
        assertEquals(driver.getTitle(), NotebookViewPage.PAGE_TITLE);
        String[] urlParts = driver.getCurrentUrl().split("/");
        notebookHash = urlParts[urlParts.length - 1];
	}

	/**
	 * Ensure that if a new notebook is created, its default file extension is ".nb".
	 */
	@Test(groups=TEST_GROUP_5, dependsOnGroups=TEST_GROUP_4)
	public void testNotebookViewPage() {
		driver.get(NotebookViewPage.generateFullUrl(notebookHash));
        noteBookView = noteBookView.readNotebookDefaultName();
        assertEquals(driver.getTitle(), NotebookViewPage.PAGE_TITLE);
        
        // Verify that the default notebook ends with ".nb"
        assertTrue(noteBookView.verifyNewNotebookDefaultName());
	}
}
