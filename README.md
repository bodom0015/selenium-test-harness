## Quick Setup
* Install [Apache Maven 3](https://maven.apache.org/index.html)
* Sign up for an account on the [Wolfram Development Platform]().
* Add a [credentials.properties](https://github.com/bodom0015/selenium-test-harness#credentials.properties) file with both valid and invalid test credentials.
  * Fore example, a finished `credentials.properties` file might look like this:
```ini
test.user.valid.email=INSERT_REAL_WDP_LOGIN_EMAIL_HERE
test.user.valid.password=INSERT_REAL_WDP_LOGIN_PASSWORD_HERE
test.user.invalid.email=fake@fake.com
test.user.invalid.password=123password
```
* Add an [environment.properties](https://github.com/bodom0015/selenium-test-harness#environment.properties) file with paths to the appropriate binaries.
  * The WebDriver binaries themselves can be downloaded [here](http://www.seleniumhq.org/download/). 
  * Point the properties described [here](https://github.com/bodom0015/selenium-test-harness#environment.properties) to the location of the binaries downloaded.
  * For example, a finished `environment.properties` file (for Windows 7) might look like this:
```ini
webdriver.chrome.driver=C:/full/path/to/chromedriver.exe
webdriver.ie.driver=C:/full/path/to/IEDriverServer.exe
phantomjs.binary.path=C:/full/path/to/phantomjs.exe
webdriver.opera.driver=C:/full/path/to/operadriver.exe
opera.binary=C:/full/path/to/Opera/launcher.exe
```
* Perform one of the following to begin test execution:
  1. Via Maven command line
    1. Open a terminal (cmd.exe / Cygwin)
    2. Navigate to the project root
    3. Execute `mvn clean package integration-test`, which will read the `pom.xml` and use the settings within to compile the project. This file also defines which `testng-*.xml` suite (or suites) will run during this phase
    4. After the `package` goal completes and the Java project is compiled, the tests will automatically begin execution (the `integration-test` goal)
  2. Via Eclipse IDE (Download Eclipse from [here](https://eclipse.org/downloads/))
    1. Install Eclipse m2e plugin from the Eclipse Marketplace
    2. Install Eclipse TestNG plugin from the Eclipse Marketplace
    3. Expand the `src/test/resources` folder and right-click one of the `testng-*.xml` suites there
    4. Choose `Run As > TestNG Suite` to begin running the tests
  
## Synopsis

A selenium automated test harness with a simple example test suite for the Wolfram Development Platform.

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

## Dependencies

Required:
* Install Maven: https://maven.apache.org/index.html

Recommended if using Eclipse IDE:
* Eclipse m2e plugin: http://www.eclipse.org/m2e/
* Eclipse TestNG plugin: http://testng.org/doc/eclipse.html

## Installation

Running the following command should retrieve a copy of the source code:
```git
git clone https://github.com/bodom0015/selenium-test-harness.git
```

You should then be able to import the project / `pom.xml` into a Maven-capable IDE of your choosing (for example, Eclipse).

## Setup
There are two `.properties` files for which the test harness will check.

#### credentials.properties
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

#### environment.properties
NOTE: Most of these binaries will require you to install the full browser in addition to the WebDriver (headless browsers excluded)

The first file is `src/main/resources/environment.properties`, which tells the test harness where to look for 
the various pieces necessary for a particular WebDriver to execute.

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

# Exceptions: Firefox and HtmlUnit require no additional setup
```

## Overview
After setting up the two .properties files as described above, our directory / package structure should now look like this:

```
src/main/java
+-- [com.wolfram]
|   +-- [core] (the core concepts of the automation harness)
|   |   +-- BrowserType (an enum representing all possible web browsers)
|   |   +-- IWebDriverProvider (interface providing a standardway to initialize a WebDriver)
|   |   +-- WebPage (an abstract superclass for our PageObjects)
|   +-- [test] (the core test classes of the automation harness)
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
|   +-- [cloud] (the actual PageObjects we wish to test)
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

The AUT's PageObjects can be found in `com.wolfram.cloud`. The PageObjects are each based on the abstract `WebPage` class, which uses the `PageFactory.initElements(driver, this)` method within its constructor to populate the PageObjects with WebElement references. Simple private methods call the lower-level functionality of the page (click a button, type into a field, etc.), while the public methods chain together the private methods to the desired overall action.

The `testng-*.xml` files describe which classes to run for each test suite. Each test class takes a single parameter named `browser` that will tell the test harness which driver to use while running that particular test class.

## Usage
The test suites can be run by:

1. Eclipse (via the TestNG Eclipse plugin) - right click a `testng-*.xml` file in Eclipse and choose `Run As > TestNG Suite`
2. Maven command line (via the maven-failsafe-plugin) - navigate to the root folder of the project, where the `pom.xml` file resides, and execute the following command:
```bash
mvn clean package integration-test
```

## FAQ
```java
java.lang.IllegalArgumentException: Cannot start SomeDriver: Driver binary not found. Verify correctness / existence of some.property.path in environment.properties
```
Ensure that the `environment.properties` file that you supply an entry at the indicated path

```java
java.lang.IllegalArgumentException: Cannot start OperaDriver: Launcher binary not found. Verify correctness / existence of opera.binary in environment.properties
```
Ensure that the `environment.properties` file that you supply an entry for `opera.binary` (Opera's `launcher.exe`) if you wish to test Opera

```java
org.testng.TestNGException: An error occurred while instantiating class com.wolfram.test.cloud.ITWolframDevelopmentPlatform: null
Caused by: java.lang.ExceptionInInitializerError
Caused by: java.util.MissingResourceException: Can't find bundle for base name credentials, locale en_US
```
Ensure that the `credentials.properties` file is supplied with the entries outlined above

## License

MIT
