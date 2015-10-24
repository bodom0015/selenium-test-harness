package com.wolfram.test.cloud;

import static org.testng.Assert.*;

import java.util.ResourceBundle;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
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
    private SignInPage signIn;
    private DevPlatformHomePage devPlatformHome;
    private NotebookViewPage noteBookView;
    
    // Read in credential info in credentials.properties
	private static final ResourceBundle rb = ResourceBundle.getBundle("credentials");
	
    // Test data for success cases:
	private static final String TEST_ACCOUNT_EMAIL = rb.getString("test.user.valid.email");
	private final String TEST_ACCOUNT_PASSWORD = rb.getString("test.user.valid.password");
	
	// Test data for failure cases:
	private final String INVALID_TEST_EMAIL = rb.getString("test.user.invalid.email");
	private final String INVALID_TEST_PASSWORD = rb.getString("test.user.invalid.password");
	
	// This hash is saved between tests
	private String notebookHash;
			
	/**
	 * Sets up our testing environment by instantiating a new driver to use.
	 * The "browser" parameter is specified in the different testng-*.xml found
	 * in src/test/resources. 
	 */
	@Parameters({ "browser" })
	@BeforeClass
	public void initializeBrowser(String browser) {
		// Call initializeDriver for the given browser type
		this.driver = this.initializeDriver(browser);
				
		/*
		 * Any other options that we may want to
		 * include for ONLY this test class could go here 
		 */
	}

	/**
	 * Tears down our test environment, which should close out all open browser windows.
	 * 
	 * NOTE: this is currently broken for Opera.
	 */
	@AfterClass
	public void closeBrowserWindows() {
		// FIXME: this does not seem to work for Opera...
		this.driver.quit();
	}

	/**
	 * Ensure that the user is taken to the landing page, and that they
	 * can then navigate to the sign-in page.
	 */
	@Test(groups="landing")
	public void testCloudLandingPage() {
		 // Navigate to the start page
        driver.get(CloudLandingPage.START_URL);
        
        // Ensure that we have landed on the landing page
        CloudLandingPage landing = new CloudLandingPage(driver);
        assertEquals(driver.getTitle(), CloudLandingPage.PAGE_TITLE);
		
		// Click "Wolfram Development Platform" and ensure that we land on the signin page
        landing.chooseWolframDevelopmentPlatform();
        assertEquals(driver.getTitle(), SignInPage.PAGE_TITLE);
	}
	
	/**
	 * Ensure that attempting to sign-in with an incorrect email address
	 * does not allow the user access to the home page.
	 */
	@Test(groups="invalidSignin", dependsOnGroups="landing")
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
	@Test(groups="invalidSignin", dependsOnGroups="landing")
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
	@Test(groups="invalidSignin", dependsOnGroups="landing")
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
	@Test(groups="validSignin", dependsOnGroups="invalidSignin")
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
	@Test(groups="devPlatformHome", dependsOnGroups="validSignin")
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
	@Test(groups="notebook", dependsOnGroups="devPlatformHome")
	public void testNotebookViewPage() {
		driver.get(NotebookViewPage.generateFullUrl(notebookHash));
        noteBookView = noteBookView.readNotebookDefaultName();
        assertEquals(driver.getTitle(), NotebookViewPage.PAGE_TITLE);
        
        // Verify that the default notebook ends with ".nb"
        assertTrue(noteBookView.verifyNewNotebookDefaultName());
	}
}
