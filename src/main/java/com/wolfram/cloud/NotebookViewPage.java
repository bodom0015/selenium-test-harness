package com.wolfram.cloud;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wolfram.core.WebPage;



/**
 * PageObject representing the Notebook Viewer page. This page 
 * allows the user to view, evaluate, format, or alter a Notebook.
 * 
 * @author Mike Lambert
 * 
 */
public class NotebookViewPage extends WebPage {
	/** The Constant expected title for this page. */
	public static final String PAGE_TITLE = "(unnamed) - Wolfram Development Platform";

	/** The base portion of the URL */
	private static final String BASE_URL = "https://dev.wolframcloud.com";
	
	/** The relative portion of the URL */
	private static final String RELATIVE_URL = "/app/objects/";

	/** The URL at which to point to start testing this page */
	public static final String START_URL = BASE_URL + RELATIVE_URL;
	public static final String START_URL_CREATE_NEW = "https://dev.wolframcloud.com/app/view/newNotebook?ext=nb";
	
	/** The Constant attribute in which to search for text input contents. */
	private static final String RENAME_INPUT_CONTENTS_ATTR = "value";

	/** The Constant expected default value of the "rename notebook" text input field. */
	private static final String DEFAULT_CONTENTS = ".nb";
		
	/** The "rename notebook" button. */
	@CacheLookup
	private WebElement renameButton;
    
    /** The "rename notebook" toolbar's name input field. */
	@CacheLookup
    private WebElement toolbarRenameInputField;

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
	 * Given a hash from a previously created notebook, build
	 * and return the full URL of that page
	 */
	public static String generateFullUrl(String notebookHash) {
		return BASE_URL + RELATIVE_URL + notebookHash;
	}
	
    /**
     * Instantiates a new notebook view page.
     *
     * @param driver the driver
     */
    public NotebookViewPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Instantiates a new notebook view page.
     *
     * @param driver the driver
     * @param wait the wait
     */
    public NotebookViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
	/**
	 * Clicks on the "rename notebook" button.
	 *
	 * @return the notebook view page
	 */
	private NotebookViewPage clickRenameNotebook() {
		wait.until(ExpectedConditions.visibilityOf(this.renameButton));
		wait.until(ExpectedConditions.elementToBeClickable(this.renameButton));
		this.renameButton.click();
		return this;
	}
    
	/**
	 * Verify that our DEFAULT_CONTENTS match the current value of the rename input text field.
	 *
	 * @return true, iff our default notebook name matches the expectation
	 */
	public boolean verifyNewNotebookDefaultName() {
		wait.until(ExpectedConditions.visibilityOf(this.toolbarRenameInputField));
		wait.until(ExpectedConditions.elementToBeClickable(this.toolbarRenameInputField));
        return DEFAULT_CONTENTS.equals(this.toolbarRenameInputField.getAttribute(RENAME_INPUT_CONTENTS_ATTR));
	}

    /**
     * Clicks the "rename notebook" button in order to make the rename input visible.
     *
     * @return the notebook view page
     */
    public NotebookViewPage readNotebookDefaultName() {
        return this.clickRenameNotebook();
    }
}
