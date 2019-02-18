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
import java.rmi.Remote;
import java.util.*;
import java.util.logging.Level;

import com.sun.glass.ui.View;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
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
	public static String PERFECTO_SECURITY_TOKEN = System.getProperty("testToken", "DEFAULT_TOKEN"); //for Perfecto
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

		ChromeOptions cOptions = new ChromeOptions();
		cOptions.addArguments("--disable-popup-blocking");
		cOptions.addArguments("--disable-default-apps");
		cOptions.addArguments("--start-maximized");
		cOptions.addArguments("--disable-infobars");
		cOptions.addArguments("–-disable-notifications");
		cOptions.addArguments("--dom-automation");

/*

		Could be used to connect to locker Docker grid but it is not very stable

		cOptions.setCapability("browserName", "chrome");
		cOptions.setCapability("browserVersion", "70.0.3538.110");

		String mUrl;
		mUrl = "http://localhost:4444/wd/hub";


		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, cOptions);
		RemoteWebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL(mUrl), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
*/

		ChromeDriver driver = new ChromeDriver(cOptions);

		return driver;
	}

	public static RemoteWebDriver getRemoteWebDriver(String testName, String platformName, String platformVersion, String browserName,
			String browserVersion, String screenResolution, String deviceName, String persona, String automation) throws MalformedURLException {

		if (drivers == null || !drivers.containsKey(testName)) {

			if (USE_PERFECTO.equals("1")) {


				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("securityToken", PERFECTO_SECURITY_TOKEN);
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
				driver = new RemoteWebDriver(new URL(cloud_url), capabilities);
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

	public static void scrollPage(RemoteWebDriver driver){

		Actions builder = new Actions(driver);
		Action seriesOfActions = builder
				.sendKeys(Keys.PAGE_DOWN)
				.build();

		Integer i=0;
		for(i=0;i<10;i++) {
			seriesOfActions.perform();
			Utils.suspend(250);
		}
		seriesOfActions = builder
				.sendKeys(Keys.HOME)
				.build();
		seriesOfActions.perform();
	}


	public static void suspend(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}



}
