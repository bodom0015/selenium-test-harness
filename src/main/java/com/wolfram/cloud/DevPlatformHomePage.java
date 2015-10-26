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
 * PageObject representing the "Wolfram Development Platform" home page. This
 * page allows the user to view and share their existing media, as well as to 
 * create more files and visualizations.
 * 
 * @author Mike Lambert
 * 
 */
public class DevPlatformHomePage extends AbstractWebPage {
	/** The Constant expected title for this page. */
	public static final String PAGE_TITLE = "Home - Wolfram Development Platform";

	// Build up / parameterize all parts of the URL
	/** The base portion of the URL */
	private static final String BASE_URL = "https://dev.wolframcloud.com";

	/** The relative portion of the URL */
	private static final String RELATIVE_URL = "/app/";

	/** The URL at which to point to start testing this page */
	public static final String START_URL = BASE_URL + RELATIVE_URL;
        
    /** The "new file menu" dropdown. */
    @FindBy(how = How.CLASS_NAME, using = "newNotebookBtn-dropdown")
	@CacheLookup
    private WebElement newNotebookBtnDropdown;
    
    /** The new ".nb" button. */
    @FindBy(how = How.ID, using = "nb")
	@CacheLookup
    private WebElement newNBBtn;

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
     * Instantiates a new WDP home page.
     *
     * @param driver the driver
     */
    public DevPlatformHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Instantiates a new WDP home page.
     *
     * @param driver the driver
     * @param wait the wait
     */
    public DevPlatformHomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

	/**
	 * Open the "new file" dropdown menu.
	 *
	 * @return the dev platform home page
	 */
	// Open the dropdown menu
    private DevPlatformHomePage openNewFileDropdown() {
		wait.until(ExpectedConditions.elementToBeClickable(this.newNotebookBtnDropdown));
		this.newNotebookBtnDropdown.click();
		return this;
    }

	/**
	 * Click on the ".nb" / "new notebook" button on the dropdown.
	 *
	 * @return the notebook view page
	 */
	private NotebookViewPage clickNewNotebookButton() {
		wait.until(ExpectedConditions.elementToBeClickable(this.newNBBtn));
		this.newNBBtn.click();

		// Clicking this button opens a new tab
        this.switchToNewWindowOrTab();
        
		return new NotebookViewPage(this.driver, this.wait);
	}

    /**
     * Creates a new notebook by opening up the "New File" dropdown menu and choosing ".nb".
     *
     * @return the notebook view page
     */
    public NotebookViewPage createNewNotebook() {
    	// Navigating to this page opens a new tab
    	return this.openNewFileDropdown().clickNewNotebookButton();
    }
}
