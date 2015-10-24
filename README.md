## Synopsis

A selenium automated test harness with an example test suite for the Wolfram Development Platform

## Motivation
In order to familiarize myself with Selenium automation, I have attempted to automate the following steps using the Selenium framework:

1. Go to Wolfram Cloud (www.wolframcloud.com).
2. After signing in successfully, you will be taken to what we call, the Homescreen.
3. From the Homescreen, users can create new notebooks, upload files, etc.</li>
4. On the right-hand side of the application, you will see a red "New" button.</li>
5. By clicking the down-arrow button, you can create different types of files (.nb, .html, .css, etc.)
6. Create a .nb notebook.
7. In the new notebook, in the file header, click the file name field, "(unnamed)" -- you will see that the extension, ".nb" is automatically present.

In doing so, this test harness was born. This project can be used as a model or skeleton to set-up automated multi-browser testing using Selenium and TestNG.

## Installation

Running the following command should retrieve a copy of the source code:
```git
git clone https://github.com/bodom0015/selenium-test-harness.git
```

## Setup
There are two .properties files for which the test harness will check.

NOTE: Most of these binaries will require you to install the browser to use its driver (headless browsers excluded)

The first file is `src/main/resources/environment.properties`, which tells the test harness where to look for 
the various pieces necessary for particular WebDrivers to execute.

You can download the various different binary versions here: http://www.seleniumhq.org/download/.

```ini
# ChromeDriver
webdriver.chrome.driver=C:/full/path/to/chromedriver.exe

# InternetExplorerDriver
webdriver.ie.driver=C:/full/path/to/IEDriverServer.exe

# PhantomJSDriver
phantomjs.binary.path=C:/full/path/to/phantomjs.exe

# OperaDriver
webdriver.opera.driver=C:/full/path/to/operadriver.exe
opera.binary=C:/full/path/to/Opera/launcher.exe

# Edge and Safari will probably require a similar setup
```

You must then supply the test harness with a set of valid Wolfram Development Platform credentials to use for the test.

To create a Wolfram Development Platform account:

1. Go to Wolfram Cloud (www.wolframcloud.com)
2. Click Wolfram Development Platform. Please create a new Wolfram ID, and you can subscribe to a Free plan

Place these new login credentials into the `src/main/resources/credentials.properties` file:
```ini
# Valid user credentials (must be registered for Wolfram Development Platform)
test.user.valid.email=INSERT_REAL_WOLFRAM_LOGIN_EMAIL_HERE
test.user.valid.password=INSERT_REAL_WOLFRAM_LOGIN_PASSWORD_HERE

# Invalid user credentials
test.user.invalid.email=fake@fake.com
test.user.invalid.password=123password
```

## Overview
After setting up the two .properties files as described above, our directory / package structure should now look like this:

```
src/main/java
+-- [com.wolfram]
|   +-- [core]
|   |   +-- BrowserType (an enum representing all possible web browsers)
|   |   +-- IWebDriverProvider (interface providing a standardway to initialize a WebDriver)
|   |   +-- WebPage (an abstract superclass for our PageObjects)
|   +-- [test]
|   |   +-- SeleniumTestBase (a primitive superclass for all Selenium tests)
|   |   +-- providers (instantiation logic for all IWebDriverProvider implementations)
|   |      +-- ChromeDriverProvider (Google Chrome environment / settings)
|   |      +-- EdgeDriverProvider (Microsoft Edge environment / settings)
|   |      +-- FirefoxDriverProvider (Mozilla Firefox environment / settings)
|   |      +-- HtmlUnitDriverProvider (HtmlUnit environment / settings)
|   |      +-- InternetExplorerDriverProvider (Microsoft Internet Explorer environment / settings)
|   |      +-- OperaDriverProvider (Opera environment / settings)
|   |      +-- PhantomJSDriverProvider (PhantomJS environment / settings)
|   |      +-- SafariDriverProvider (Apple Safari environment / settings)
|   +-- [cloud] (the actual pages we wish to test)
|      +-- CloudLandingPage
|      +-- SignInPage
|      +-- DevPlatformHomePage
|      +-- NotebookViewPage  
src/main/resources
+-- credentials.properties (contains valid / invalid test credentials)
+-- environment.properties (contains paths to binaries used by selenium drivers)
src/test/java
+-- [com.wolfram.cloud.test]
|   +-- ITWolframDevelopmentPlatform (Wolfram Cloud integration tests for Wolfram Development Platform)
src/test/resources
+-- testng-all-parallel.xml (Test Suite which attempts to run tests in all available browsers in parallel)
+-- testng-all.xml (Test Suite which attempts to run tests in all available browsers)
+-- testng-firefox.xml (TestNG Suite which only runs tests in Firefox)
+-- testng-chrome.xml (TestNG Suite which only runs tests in Chrome)
+-- testng-opera.xml (TestNG Suite which only runs tests in Opera)
+-- testng-htmlunit.xml (TestNG Suite which only runs tests in HtmlUnit)
+-- testng-ie.xml (Test Suite which only runs tests in InternetExplorer)
+-- testng-phantomjs.xml (Test Suite which only runs tests in PhantomJS)
+-- testng-safari.xml (Test Suite which only runs tests in Safari)
+-- testng-edge.xml (Test Suite which only runs tests in Edge)
pom.xml (Maven project configuration and dependency information)
README.md (this readme file)
```

## Usage
The test suites can be run by:

1. Eclipse (via the TestNG Eclipse plugin) - right click a testng-*.xml file in Eclipse and choose "Run As > TestNG Suite"
2. Maven command line - navigate to the root folder of the project (with pom.xml) and execute "mvn clean package integration-test"

## License

MIT
