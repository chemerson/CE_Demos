package com.testing.utils;


import com.applitools.eyes.selenium.Eyes;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.w3c.tidy.Tidy;

import com.applitools.eyes.EyesBase;


public class Utils {

	//For driver factory
	static Map<String,RemoteWebDriver> drivers = new Hashtable<String,RemoteWebDriver>();

	//For Reportium Factory
	static Map<String,ReportiumClient> reports = new Hashtable<String,ReportiumClient>();

	//For Reportium Factory
	static Map<String,Eyes> eyes = new Hashtable<String,Eyes>();

//TODO: Make this more easy to switch from local, to localgrid, to Perfecto

	//public static RemoteWebDriver webdriver;
	// Set cloud host and credential values at runtime via Command Line Parameters or hardcode as the default parameter
	public static String USE_GRID = System.getProperty("useSeleniumGrid", "1");
	public static String GRID_HOST = System.getProperty("testHost", "DEFAULT_HOST_NAME");
	public static String USE_PERFECTO = System.getProperty("usePerfecto", "0");
	public static String SECURITY_TOKEN = System.getProperty("testToken", "DEFAULT_TOKEN"); //for Perfecto
	// FOR PERFECTO ONLY public static String CLOUD_URL = "https://" + GRID_HOST + "/nexperience/perfectomobile/wd/hub";
	public static String CLOUD_URL = "http://" + GRID_HOST + "/wd/hub";
	public static String FAST_WEB = System.getProperty("fastweb", "false");  //make TRUE for Perfecto
	public static String TEST_EYES = System.getProperty("testEyes", "1");
	public static String EYES_KEY = System.getProperty("eyesAPIKey", "DEFAULT_TOKEN");

	public static synchronized ReportiumClient createReportiumClient(String keyName, RemoteWebDriver driver) {

		if (reports == null || !reports.containsKey(keyName))
		{
			PerfectoExecutionContext perfectoExecutionContext =
					new PerfectoExecutionContext.PerfectoExecutionContextBuilder()

							.withProject(new Project("Sample Reportium project", "1.0"))
							.withJob(new Job("Daily Build", 2).withBranch("master"))
							.withProject(new Project("Demo_1", "1.0"))
							.withContextTags(TestConstants.TestData.BUILD_TAG, keyName)
							.withWebDriver(driver)
							.build();
			ReportiumClient report = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
			reports.put(keyName,report);
		}
		return reports.get(keyName);
	}

	public static void reportStepStart(ReportiumClient reportium, String stepDescription){
		reportium.stepStart(stepDescription);
		System.out.println("@@@ STEP: " + stepDescription);

	}

	public static void reportAssert(ReportiumClient reportium, String assertion, Boolean b){
		reportium.reportiumAssert(assertion, b);
		System.out.println("Assertion: "+assertion);
	}

	public static RemoteWebDriver getLocalWebDriver(){

		System.setProperty("webdriver.chrome.logfile", "log/chromedriver.log");
		System.setProperty("webdriver.chrome.verboseLogging", "true");

		//System.setProperty("webdriver.chrome.driver","/chromedriver");

		LoggingPreferences logs = new LoggingPreferences();
		logs.enable(LogType.BROWSER, Level.INFO);
		logs.enable(LogType.DRIVER, Level.INFO);
		//logs.enable(LogType.PERFORMANCE, Level.ALL);

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);

		RemoteWebDriver driver = new ChromeDriver(desiredCapabilities);
		return driver;
	}

	public static RemoteWebDriver getRemoteWebDriver(String testName, String platformName, String platformVersion, String browserName,
			String browserVersion, String screenResolution, String deviceName, String persona, String automation) throws MalformedURLException {

		if (drivers == null || !drivers.containsKey(testName)) {

			if (USE_PERFECTO.equals("1")) {


				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("securityToken", SECURITY_TOKEN);
				capabilities.setCapability("platformName", platformName);
				capabilities.setCapability("platformVersion", platformVersion);
				capabilities.setCapability("browserName", browserName);
				capabilities.setCapability("browserVersion", browserVersion);

				//CME test out Perfecto Connect
				//capabilities.setCapability("tunnelId", "a0a71af6-106a-432b-b215-66ce824abb63");

				//Set cloud device open timeout in MINUTES
				//capabilities.setCapability("openDeviceTimeout", 1);

				if (!screenResolution.isEmpty()) {
					capabilities.setCapability("resolution", screenResolution);
					System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion + ", " + browserName + " " + browserVersion + ", " + screenResolution);
				} else {
					if (!platformName.isEmpty())
						System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion);
					else
						System.out.println("Creating Remote WebDriver on: " + browserName);
				}

				// Instantiate platform specific drivers with some platform specific capabilities
				if (capabilities.getCapability("platformName").toString().equals("iOS")) {
					capabilities.setCapability("deviceName", deviceName);
					capabilities.setCapability("model", "");
					capabilities.setCapability("windTunnelPersona", persona);
					if (automation.matches("XCUITest")) {
						capabilities.setCapability("automationName", automation);
					}
					//capabilities.setCapability("automationName","");
					//capabilities.setCapability("bundleId", "com.apple.mobilesafari");

					// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
					try {
						PerfectoLabUtils.setExecutionIdCapability(capabilities, GRID_HOST);
					} catch (IOException e) {
						System.out.println("Exception setting shared session capability");
						//e.printStackTrace();
					}

				} else if (capabilities.getCapability("platformName").toString().equals("Android")) {
					capabilities.setCapability("deviceName", deviceName);
					capabilities.setCapability("model", "");
					capabilities.setCapability("windTunnelPersona", persona);
					//capabilities.setCapability("appPackage", "com.android.chrome");

					// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
					try {
						PerfectoLabUtils.setExecutionIdCapability(capabilities, GRID_HOST);
					} catch (IOException e) {
						System.out.println("Exception setting shared session capability");
						//e.printStackTrace();
					}
				}

				RemoteWebDriver driver = createDriver(capabilities, CLOUD_URL, screenResolution);
				drivers.put(testName, driver);
			}
			else {

				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("browserName", browserName);
				capabilities.setCapability("version", browserVersion);
				capabilities.setCapability("platformName", "linux");
				RemoteWebDriver driver = createDriver(capabilities, CLOUD_URL, screenResolution);
				drivers.put(testName, driver);
			}

		}

		return drivers.get(testName);

	}

	private static RemoteWebDriver createDriver(DesiredCapabilities capabilities, String cloud_url, String screenResolution)
			throws MalformedURLException  {

		RemoteWebDriver driver;

		System.out.println("Driver URL: " + cloud_url);


			if(FAST_WEB.equals("true")) {
				Long stime = System.currentTimeMillis();
				driver = new RemoteWebDriver(new URL(cloud_url + "/fast"), capabilities);
				System.out.println("Remote Web Driver create time (sec): " + (System.currentTimeMillis()-stime)/1000);
			} else {
				driver = new RemoteWebDriver(new URL(cloud_url), capabilities);
			}

		// Maximize browser window on Desktop
		if (!screenResolution.isEmpty()) {
			driver.manage().window().maximize();
		}
		return driver;
	}

	public static RemoteWebDriver getRemoteWebDriverBasic(String platformName, String platformVersion, String browserName,
													 String browserVersion, String screenResolution, String deviceName, String persona) throws MalformedURLException {

		RemoteWebDriver webdriver;

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", platformName);
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("browserName", browserName);
		capabilities.setCapability("browserVersion", browserVersion);

		// Define test name
		capabilities.setCapability("scriptName", TestConstants.TestData.SCRIPT_NAME);

		// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
		try {
			PerfectoLabUtils.setExecutionIdCapability(capabilities, GRID_HOST);
		} catch (IOException e) {
			System.out.println("Exception setting shared session capability");
			//e.printStackTrace();
		}

		if (!screenResolution.isEmpty()) {
			capabilities.setCapability("resolution", screenResolution);
			System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion + ", " + browserName + " " + browserVersion + ", " + screenResolution);
		}
		else {
			if (!platformName.isEmpty())
				System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion);
			else
				System.out.println("Creating Remote WebDriver on: " + browserName);
		}

		// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
		//PerfectoLabUtils.setExecutionIdCapability(capabilities, GRID_HOST);

		// Instantiate platform specific drivers with some platform specific capabilities
		if(capabilities.getCapability("platformName").toString().equals("iOS")) {
			capabilities.setCapability("deviceName", deviceName);
			capabilities.setCapability("model", "");
			capabilities.setCapability("windTunnelPersona", persona);
			//webdriver = new IOSDriver(new URL(CLOUD_URL), capabilities);
			webdriver = new RemoteWebDriver(new URL(CLOUD_URL), capabilities);

		} else if (capabilities.getCapability("platformName").toString().equals("Android")){
			capabilities.setCapability("deviceName", deviceName);
			capabilities.setCapability("model", "");
			//capabilities.setCapability("windTunnelPersona", persona);
			//webdriver = new AndroidDriver(new URL(CLOUD_URL), capabilities);
			webdriver = new RemoteWebDriver(new URL(CLOUD_URL), capabilities);
		} else {
			webdriver = new RemoteWebDriver(new URL(CLOUD_URL), capabilities);
		}

		// Define RemoteWebDriver timeouts
		//webdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//webdriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		//CME timeouts


		// Maximize browser window on Desktop
		if (!screenResolution.isEmpty()) {
			webdriver.manage().window().maximize();
		}

		return webdriver;
	}

	public static synchronized void killDriver(String testName)
	{
		drivers.remove(testName);
	}

	public static Eyes getEyes(String eyeKey){

		if (eyes == null || !eyes.containsKey(eyeKey)) {
			Eyes eye = new Eyes();
			eye.setApiKey(EYES_KEY);
			eyes.put(eyeKey, eye);
		}
		return eyes.get(eyeKey);
	}

	public static void checkMyWindow(Eyes eyes, String tag){

		if (TEST_EYES.equals("1")) {
			eyes.checkWindow(tag);
			System.out.println("(*) Eyes (*) " + tag);
		}

	}

	public static void clickHomeButton(RemoteWebDriver webdriver) {
		String command = "mobile:presskey";
		Map<String, Object> params = new HashMap<>();
		params.put("keySequence", "HOME");
		try {
			webdriver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	public static void clickButton(RemoteWebDriver webdriver, String button) {
		String command = "mobile:presskey";
		Map<String, Object> params = new HashMap<>();
		params.put("keySequence", button);
		try {
			webdriver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	public static void openApp(RemoteWebDriver webdriver,String appName) {
		String command = "mobile:application:open";
		Map<String, Object> params = new HashMap<>();
		params.put("name", appName);
		params.put("timeout", "5");
		try{
			webdriver.executeScript(command, params);
			params.clear();
		} catch (WebDriverException e) { }
	}

	public static void closeApp(RemoteWebDriver webdriver,String appName) {
		String command = "mobile:application:close";
		Map<String, Object> params = new HashMap<>();
		params.put("name", appName);
		try{
			Object res = webdriver.executeScript(command, params);
			params.clear();
		} catch (WebDriverException e) {
			System.out.println("Warning: Cannot close app");
		}
	}

	public static void clickButtonText(RemoteWebDriver webdriver,String buttonText) throws Exception{
		String command = "mobile:button-text:click";
		Map<String, Object> params = new HashMap<>();
		suspend(500);
		params.put("label", buttonText);
		params.put("timeout", "5");
		params.put("source", "camera");
		try{
			webdriver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	public static Boolean TextCheckpoint(RemoteWebDriver webdriver,String checkpointText) {
		String command = "mobile:checkpoint:text";
		Map<String, Object> params = new HashMap<>();
		params.put("content", checkpointText);
		params.put("timeout", "30");
		params.put("source", "camera");
		params.put("ignorecase", "case");  //this means do not ignore case
 		try{
			Object result = webdriver.executeScript(command, params);
			Boolean resultBool = Boolean.valueOf(result.toString());
			return resultBool;
		} catch (WebDriverException e) { return false; }
	}

	public static void SetText(RemoteWebDriver webdriver,String objectLabel, String textToSet) {
		Map<String, Object> params = new HashMap<>();
		params.put("label", objectLabel);
		params.put("text", textToSet);
		params.put("timeout", "15");
		params.put("source", "camera");
		try {
			webdriver.executeScript("mobile:edit-text:set", params);
			params.clear();
		} catch (WebDriverException e) { }
	}

	public static void SetTextWithNarrowView(RemoteWebDriver webdriver,String objectLabel, String textToSet, String top, String height, String left, String width) {
		Map<String, Object> params = new HashMap<>();
		params.put("label", objectLabel);
		params.put("text", textToSet);
		params.put("screen.top", top + "%");
		params.put("screen.height", height + "%");
		params.put("screen.left", left + "%");
		params.put("screen.width", width + "%");
		params.put("timeout", "15");
		params.put("source", "camera");
		try {
			webdriver.executeScript("mobile:edit-text:set", params);
			params.clear();
		} catch (WebDriverException e) { }
	}

	public static void suspend(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	public static void HoverClick(RemoteWebDriver driver, String MainMenuItemText, String SubMenuItem){
		//Hover over MainMenuItem and click on SubMenuItem that is revealed

		try {
			WebElement MainMenuItemLink = driver.findElementByLinkText(MainMenuItemText);
			WebElement SubMenuItemLink ; //= driver.findElementByXPath(SubMenuItem);
			Actions builder = new Actions(driver);
			builder.moveToElement(MainMenuItemLink).build().perform();

			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.ignoring(NoSuchElementException.class)
					.ignoring(NoSuchMethodError.class)
					.ignoring(StaleElementReferenceException.class);

			SubMenuItemLink = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(SubMenuItem)));
			SubMenuItemLink = wait.until(ExpectedConditions.elementToBeClickable(By
					.xpath(SubMenuItem)));
			SubMenuItemLink.click();


		} catch (WebDriverException e) {

		}

	}

    public static boolean swipeScreen(RemoteWebDriver driver, final String direction) {

	    String start = "";
	    String end = "";
	    switch(direction){
            case "up":
                start = "50%,80%";
                end = "50%,20%";
                break;
            case "down" :
                start = "50%,20%";
                end = "50%,80%";
                break;
            case "left" :
                start = "80%,50%";
                end = "20%,50%";
                break;
            case "right" :
                start = "20%,50%";
                end = "80%,50%";
                break;
        }

	    try{

            Map<String, Object> params = new HashMap<>();
            params.put("start", start);
            params.put("end", end);
            Object result = driver.executeScript("mobile:touch:swipe", params);
			Utils.suspend(1000);

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~ swipeScreen " + direction);
            return true;
        } catch (WebDriverException e) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR swipeScreen " + direction);
	        return false;
        }

    }

    public static boolean clickElement(RemoteWebDriver driver, final String xpath) {

		Integer t=0;
		Integer max=5;
		for (t=1;t<=max;t++) {
			try {
				driver.findElementByXPath(xpath).click();
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ SUCCESS clickElement - # tries - " + t);
				return true;
			} catch (WebDriverException e) {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR clickElement - # tries - " + t);
				if(t>max){
					return false; }
			}
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR clickElement FAILED");
		return false;
    }

    public static boolean elementExists(RemoteWebDriver driver, final String xpath) {

		Integer t=0;
		Integer max=5;
		for (t=1;t<=max;t++) {
			try {
				WebElement element;
				WebDriverWait wait = new WebDriverWait(driver, 1);
				wait.ignoring(NoSuchElementException.class)
						.ignoring(NoSuchMethodError.class)
						.ignoring(StaleElementReferenceException.class);
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				//element.click();
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ SUCCESS elementExists - # tries - " + t);
				return true;
			} catch (WebDriverException e) {
				//System.out.println("**** ERROR clickElement try " + t + " " + e.getMessage());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR elementExists - # tries - " + t);
				if(t>max){
					return false; }
			}
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR elementExists FAILED");
		return false;

    }

	public static boolean enterText(RemoteWebDriver driver, String text, final String xpath) {

		Integer t=0;
		Integer max=5;
		for (t=1;t<=max;t++) {
			try {
				WebElement element;
				WebDriverWait wait = new WebDriverWait(driver, 1);
				wait.ignoring(NoSuchElementException.class)
						.ignoring(NoSuchMethodError.class)
						.ignoring(StaleElementReferenceException.class);
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				element.sendKeys(text);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ SUCCESS enterText - # tries - " + t);
				return true;
			} catch (WebDriverException e) {
				//System.out.println("**** ERROR clickElement try " + t + " " + e.getMessage());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR enterText - # tries - " + t);
				if(t>max){
					return false; }
			}
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~ ERROR enterText FAILED");
		return false;
	}

    public static boolean elementIsVisible(RemoteWebDriver driver, final String xpath) {
        try{
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.ignoring(NoSuchElementException.class)
                    .ignoring(NoSuchMethodError.class)
                    .ignoring(StaleElementReferenceException.class);
            //WebElement e = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

            WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            //element.click();
            return true;
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public static void logs(RemoteWebDriver driver, String action){
		try {
			Map<String, Object> params1 = new HashMap<>();
			if(action.matches("start")){
                Object result1 = driver.executeScript("mobile:logs:start", params1);
            }
			if(action.matches("stop")){
                Object result2 = driver.executeScript("mobile:logs:stop", params1);
            }
		} catch (Exception e) {
			System.out.println("ERROR: Could not capture logs");
			e.printStackTrace();
		}
	}

	public static void startVNetwork(RemoteWebDriver driver){
		try {
			Map<String, Object> params10 = new HashMap<>();
			params10.put("generateHarFile", "true");
			Object result10 = driver.executeScript("mobile:vnetwork:start", params10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> getContextHandles(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}

	public static String getTableRow(RemoteWebDriver driver, String srxml){

		/* TODO use html cleaner instead from Allen

		// XPATH Search DOM *****************************
		switchToContext(driver,"NATIVE");
		String pgsource = driver.getPageSource();
		pgsource = cleanXML(pgsource);
		pgsource += "\0";
		pgsource = cleanXML(pgsource);

		System.out.println("*********************************************");
		System.out.println(pgsource);
		System.out.println("*********************************************");

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(new ByteArrayInputStream(pgsource.getBytes("utf-8")));


		// "(//UIATableCell)[1]"
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		XPathExpression expr = xpath.compile("(//UIATableCell)[1]");
		String xpathres =  (String) expr.evaluate(doc, XPathConstants.STRING);
		System.out.println("xpath result : " + xpathres);
		// XPATH Search DOM *****************************
		*/
		return "a";
	}

	public static String cleanXML(String data) throws Exception {
		Tidy tidy = new Tidy();

		String tags = "appiumaut, " +
				"appiumaut, " +
				"uiaapplication," +
				"uiaapplication, " +
				"uiawindow, " +
				"uiabutton, " +
				"uiaimage, " +
				"uiatableview," +
				"uiastatictext," +
				"uiatablecell," +
				"uiastatictext";

		Properties props = new Properties();
		props.setProperty("new-inline-tags", tags);
		props.setProperty("new-blocklevel-tags", tags);
		props.setProperty("new-empty-tags", tags);
		props.setProperty("new-pre-tags", tags);
		tidy.setConfigurationFromProps(props);

		//tidy.setXmlOut(true);


		/*
		tidy.setXHTML(true);


		tidy.setInputEncoding("UTF-8");
		tidy.setOutputEncoding("UTF-8");
		tidy.setWraplen(Integer.MAX_VALUE);
		tidy.setPrintBodyOnly(true);

		tidy.setSmartIndent(true);
		*/



		ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] XMLHeader="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n".getBytes();
		outputStream.write(XMLHeader,0,XMLHeader.length);
		tidy.parseDOM(inputStream, outputStream);
		return outputStream.toString("UTF-8");
	}

	public static boolean checkTableFormat(RemoteWebDriver driver){
		Map<String, Object> params = new HashMap<>();
		Object result;
		Boolean res = true;
		String regexAlpha, regexNumeric;

		//  regex to match -$999.99       \-?\$\d*.\d\d
		//  regex to match -999.99%       \-?\d*.\d\d%
		//  regex to match 99,999,999       \-?\d*.\d\d%
		//  regex to match a line of numbers \$\d{0,3},?\d{0,3},?\d{0,3}.\d\d \+?\-?\$\d{0,3}.\d\d \+?\-?\d{0,3}.\d\d% \d{0,3},?\d{0,3},?\d{0,3}
		//  regex to match a string  \D+
		regexAlpha = "\\D+";
		regexNumeric = "\\$\\d{0,3},?\\d{0,3},?\\d{0,3}.\\d\\d \\+?\\-?\\$\\d{0,3}.\\d\\d \\+?\\-?\\d{0,3}.\\d\\d% \\d{0,3},?\\d{0,3},?\\d{0,3}";

		//Function to check the Market Mover table on and iPad

		params.clear();
		params.put("context", "body");
		params.put("source", "native");
		params.put("screen.top", "21%");
		params.put("screen.height", "80%");
		params.put("screen.left", "20%");
		params.put("screen.width", "43%");
		result = driver.executeScript("mobile:screen:text", params);

		String[] tablecells = result.toString().split("\\r?\\n");  //split on new line chars

		Integer i = 0;
		for (String tc : tablecells){
			//match odd and even lines correctly
			if(i%2 == 0) {
				if (!tc.matches(regexAlpha)) {
					res = false;
				}
			} else {
				if (!tc.matches(regexNumeric)) {
					res = false;
				}
			}
			System.out.println(i + " " + res + ": " + tc);
			i++;
		}

		return res;
	}

	public static void setPickerWheel(RemoteWebDriver driver, String xpath, String setValue){
		String x, y, width, height, swipestart, swipeend, value;
		Integer i=0;
		Map<String, Object> params = new HashMap<>();
		Object result;

		x = driver.findElementByXPath(xpath).getAttribute("x");
		y = driver.findElementByXPath(xpath).getAttribute("y");
		width = driver.findElementByXPath(xpath).getAttribute("width");
		height = driver.findElementByXPath(xpath).getAttribute("height");

		//convert to the center of the box around the object
		x = Integer.toString((Integer.parseInt(width)/2) + Integer.parseInt(x));
		y = Integer.toString((Integer.parseInt(height)/2) + Integer.parseInt(y));
		swipestart = x + "," + y;
		swipeend = x + "," + (Integer.parseInt(y) + 50);
		value = driver.findElementByXPath(xpath).getAttribute("value");
		driver.findElementByXPath(xpath).click();

		while (!value.equals(setValue) && i<100) {
			params.put("location", swipeend);
			params.put("operation", "single");
			result = driver.executeScript("mobile:touch:tap", params);
			Utils.suspend(2000);
			value = driver.findElementByXPath(xpath).getAttribute("value");
			i++;
		}
	}

	public static void tapChart(RemoteWebDriver driver, String xpath){
		String x, y, centx, centy, width, height, taploc;
		Integer i=0;
		Map<String, Object> params = new HashMap<>();
		Object result;

		x = driver.findElementByXPath(xpath).getAttribute("x");
		y = driver.findElementByXPath(xpath).getAttribute("y");
		width = driver.findElementByXPath(xpath).getAttribute("width");
		height = driver.findElementByXPath(xpath).getAttribute("height");

		//convert to the center of the box around the object
		centx = Integer.toString((Integer.parseInt(width)/2) + Integer.parseInt(x));
		centy = Integer.toString((Integer.parseInt(height)/2) + Integer.parseInt(y));
		driver.findElementByXPath(xpath).click();

		//Cycle through tapping right, left, up, and down on chart
		taploc = (x + Integer.parseInt(width)/4) + "," + centy;

		params.put("location", taploc);
		params.put("operation", "single");
		result = driver.executeScript("mobile:touch:tap", params);
		Utils.suspend(2000);


	}

	public static void tapText(RemoteWebDriver driver, String findtext){
		Map<String, Object> params = new HashMap<>();
		Object result;
		Integer locx=0, locy=0;
		String currcontext;

		currcontext = getCurrentContextHandle(driver);
		switchToContext(driver, "VISUAL");
		locx = driver.findElementByLinkText(findtext).getLocation().getX();
		locy = driver.findElementByLinkText(findtext).getLocation().getY();
		if(locx!=0 && locy!=0) {
			params.put("location", locx + "," + locy);
			params.put("operation", "single");
			params.put("duration", "5");
			result = driver.executeScript("mobile:touch:tap", params);
		}
		switchToContext(driver, currcontext);
	}

	public static void tapObject(RemoteWebDriver driver, String xpath){
		Map<String, Object> params = new HashMap<>();
		Object result;
		String locx="", locy="";
		String currcontext;

		locx = driver.findElementByXPath(xpath).getAttribute("x");
		locy = driver.findElementByXPath(xpath).getAttribute("y");
		if(locx!="" && locy!="") {
			params.put("location", locx + "," + locy);
			params.put("operation", "single");
			params.put("duration", "5");
			result = driver.executeScript("mobile:touch:tap", params);
		}
	}

	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	private static String getCurrentContextHandle(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}


}
