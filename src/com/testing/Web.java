package com.testing;

import java.net.MalformedURLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.perfecto.reportium.client.ReportiumClient;
import com.testing.pages.home;
import com.testing.utils.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import static org.testng.Assert.*;


public class Web {


	protected RemoteWebDriver driver;
	protected ReportiumClient reportiumClient;
	protected home objHome;

	protected Eyes eyes;

	private static final String SOURCE_FILE_ROOT_PATH = "src";


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = true)
	public void LocalWebDemo_1(String platformName ,String platformVersion, String browserName, String browserVersion,
						String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");

		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.open(driver, "Local Library", "Check Links - Demo");

		try {

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get("http://Christophers-Applitools-MacBook-Pro.local:3000/catalog");
			eyes.checkWindow("Home Page");
			driver.findElement(By.linkText("All authors")).click();
			eyes.checkWindow("All authors");
			driver.findElement(By.linkText("All genres")).click();
			eyes.checkWindow("All genres");
			driver.findElement(By.linkText("All book-instances")).click();
			eyes.checkWindow("All book-instances");
			driver.findElement(By.linkText("Create new author")).click();
			eyes.checkWindow("Create new author");

			eyes.close();
		} catch (Exception e) {
			eyes.abortIfNotClosed();
			e.printStackTrace();
		}
	}


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = true)
	public void LocalWebDemo_2(String platformName ,String platformVersion, String browserName, String browserVersion,
							   String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");

		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.open(driver, "Local Library", "Check Books - Demo");

		try {

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get("http://Christophers-Applitools-MacBook-Pro.local:3000/catalog");
			eyes.checkWindow("Home Page");
			driver.findElement(By.linkText("All books")).click();
			eyes.checkWindow("All Books");
			driver.findElement(By.linkText("Apes and Angels")).click();
			eyes.checkWindow("Apes and Angels");
			driver.findElement(By.linkText("All books")).click();
			driver.findElement(By.linkText("Test Book 2")).click();
			eyes.checkWindow("Test Book 2");
			driver.findElement(By.linkText("All books")).click();
			driver.findElement(By.linkText("Death Wave")).click();
			eyes.checkWindow("Death Wave");
			driver.findElement(By.linkText("All books")).click();
			driver.findElement(By.linkText("Test Trade India sdfghn")).click();
			eyes.checkWindow("Test Trade India");
			driver.findElement(By.linkText("All books")).click();
			driver.findElement(By.linkText("Apes and Angels")).click();
			driver.findElement(By.linkText("5bc5cf054124410f842a42eb")).click();
			eyes.checkWindow("Edit Book");
			driver.findElement(By.linkText("Update BookInstance")).click();
			driver.findElement(By.id("status")).click();
			new Select(driver.findElement(By.id("status"))).selectByVisibleText("Loaned");
			driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Status:'])[1]/following::button[1]")).click();
			driver.findElement(By.linkText("Update BookInstance")).click();
			eyes.checkWindow("Update book - loaned");
			driver.findElement(By.id("status")).click();
			new Select(driver.findElement(By.id("status"))).selectByVisibleText("Available");
			driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Status:'])[1]/following::button[1]")).click();
			eyes.checkWindow("Update Book - Available");

			eyes.close();
		} catch (Exception e) {
			eyes.abortIfNotClosed();
			e.printStackTrace();
		}
	}


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = true)
	public void LocalWebDemo_3(String platformName ,String platformVersion, String browserName, String browserVersion,
							   String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");

		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.open(driver, "Local Library", "Check Genres - Demo");

		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get("http://Christophers-Applitools-MacBook-Pro.local:3000/catalog");
			eyes.checkWindow("Home Page");
			driver.findElement(By.linkText("All genres")).click();
			driver.findElement(By.linkText("Computer Programming")).click();
			eyes.checkWindow("Computer Programming");
			driver.findElement(By.linkText("All genres")).click();
			driver.findElement(By.linkText("Fantasy")).click();
			eyes.checkWindow("Fantasy");
			driver.findElement(By.linkText("All genres")).click();
			driver.findElement(By.linkText("French Poetry")).click();
			eyes.checkWindow("French Poetry");
			driver.findElement(By.linkText("All genres")).click();
			driver.findElement(By.linkText("Science")).click();
			eyes.checkWindow("Science");
			driver.findElement(By.linkText("All genres")).click();
			driver.findElement(By.linkText("Science Fiction")).click();
			eyes.checkWindow("Science Fiction");

			eyes.close();
		} catch (Exception e) {
			eyes.abortIfNotClosed();
			e.printStackTrace();
		}
	}

	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void WikipediaDemo(String platformName ,String platformVersion, String browserName, String browserVersion,
							  String screenResolution,  String location, String deviceName, String persona) {

		eyes.setBaselineBranchName("New Branch");
		eyes.setEnvName("Chrome (New Branch)");
		eyes.setMatchLevel(MatchLevel.LAYOUT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.open(driver, "Wikipedia", "Home Page RCA");

		try {
			objHome = new home(driver,eyes);

			objHome.openHomePage();
			objHome.clickEnglish();

			eyes.close();
		} catch (Exception e) {
			eyes.abortIfNotClosed();
			e.printStackTrace();
		}
	}

	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void SinglePageDemo(String platformName ,String platformVersion, String browserName, String browserVersion,
							 String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");
		eyes.setMatchLevel(MatchLevel.LAYOUT);
		eyes.setStitchMode(StitchMode.CSS);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.open(driver, "Web", "Popular Mechanics");

		try {

			driver.get("https://www.popularmechanics.com");
			eyes.checkWindow();

			eyes.close();
		} catch (Exception e) {
			eyes.abortIfNotClosed();
			e.printStackTrace();
		}
	}


	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@BeforeClass(alwaysRun = true)
	public void baseBeforeClass(String platformName ,String platformVersion, String browserName, String browserVersion,
								String screenResolution,  String location, String deviceName, String persona) throws MalformedURLException {

	    Long threadid = Thread.currentThread().getId();
		String uniqueKey = threadid.toString() + " " + deviceName;
		long before = System.currentTimeMillis();
		if(Utils.USE_GRID.equals("1")){
            driver = Utils.getRemoteWebDriver(uniqueKey, platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, "");
            String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
            String dutId = "THREAD ID - " + threadid + " " + platformName + " " + platformVersion + " " + testName;
            // For Perfecto ONLY reportiumClient = Utils.createReportiumClient(dutId, driver);
        } else {
		    driver = Utils.getLocalWebDriver();
		    browserName = "Local Chrome";
		    browserVersion = "Local Version";
		    //driver.setLogLevel(Level.ALL);
        }

		eyes = Utils.getEyes(uniqueKey);
		BatchInfo batchInfo = new BatchInfo("Library Tests - Demo");
		eyes.setBatch(batchInfo);

		System.out.println("START THREAD ID - " + Thread.currentThread().getId() + " " + browserName + " " + browserVersion);
		System.out.println("baseBeforeClass took " + (System.currentTimeMillis() - before) + "ms");
	}

	@AfterClass(alwaysRun = true)
	public void baseAfterClass() {

		Logs logs = driver.manage().logs();
		LogEntries logEntries = logs.get(LogType.DRIVER);

		for (LogEntry logEntry : logEntries) {
			System.out.println(logEntry.getMessage());
		}
		if (driver != null) {
			long before = System.currentTimeMillis();
			eyes.abortIfNotClosed();
			driver.close();
			driver.quit();
			System.out.println("Driver quit took " + (System.currentTimeMillis() - before) + "ms");
		}

	}
}
