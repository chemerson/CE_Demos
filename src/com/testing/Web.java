package com.testing;


import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.Region;
import com.applitools.eyes.images.EyesImagesScreenshot;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;

import com.perfecto.reportium.client.ReportiumClient;
import com.testing.pages.home;
import com.testing.utils.Utils;
import org.openqa.selenium.*;


import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;


public class Web {


	protected RemoteWebDriver driver;
	protected ReportiumClient reportiumClient;
	protected home objHome;

	protected Eyes eyes;
	protected Target target;

	private static final String SOURCE_FILE_ROOT_PATH = "src";
	private static final String BATCH_NAME = "Marriott Web";
	private static final String BATCH_ID = null;  //optional - setting will keep all tests in the same batch
	private static final String APP_NAME = "Marriott";






	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void LocalWebDemo_1(String platformName ,String platformVersion, String browserName, String browserVersion,
							   String screenResolution,  String location, String deviceName, String persona) {



		//Force to check against specific baseline branch
		eyes.setBaselineBranchName("LLFireFox");
		//Force to check witht he forced baselines corresponding environment
		eyes.setBaselineEnvName("firefox 63.0.3");

		//Set the environment name in the test batch results
		eyes.setEnvName(driver.getCapabilities().getBrowserName() + " " + driver.getCapabilities().getVersion());


		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);

		eyes.open(driver, APP_NAME, "Check Links");

		try {

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get("http://Christophers-Applitools-MacBook-Pro.local:3000/catalog");

			eyes.check("TEST", target.region(By.cssSelector("body > div > div > div.col-sm-10 > img:nth-child(1)")).fully());

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
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void LocalWebDemo_2(String platformName ,String platformVersion, String browserName, String browserVersion,
							   String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");

		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.open(driver, APP_NAME, "Check Books");


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
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void LocalWebDemo_3(String platformName ,String platformVersion, String browserName, String browserVersion,
							   String screenResolution,  String location, String deviceName, String persona) {

		//eyes.setBaselineBranchName("New Branch");
		//eyes.setEnvName("Chrome (New Branch)");

		eyes.setMatchLevel(MatchLevel.CONTENT);
		eyes.setStitchMode(StitchMode.SCROLL);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.setBaselineEnvName(driver.getCapabilities().getBrowserName() + " " + driver.getCapabilities().getVersion());
		eyes.open(driver, APP_NAME, "Check Genre");


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
	@BeforeClass(alwaysRun = true)
	public void baseBeforeClass(String platformName ,String platformVersion, String browserName, String browserVersion,
								String screenResolution,  String location, String deviceName, String persona) throws MalformedURLException {

	    Long threadid = Thread.currentThread().getId();
		String uniqueKey = threadid.toString() + " " + deviceName;
		long before = System.currentTimeMillis();
		if(Utils.USE_GRID.equals("1")){
 //           driver = Utils.getRemoteWebDriver(uniqueKey, platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, "");
            String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
            String dutId = "THREAD ID - " + threadid + " " + platformName + " " + platformVersion + " " + testName;
            // For Perfecto ONLY reportiumClient = Utils.createReportiumClient(dutId, driver);
        } else {
		//    driver = Utils.getLocalWebDriver();
		    browserName = "Local Chrome";
		    browserVersion = "Local Version";
		    //driver.setLogLevel(Level.ALL);
        }

		eyes = Utils.getEyes(uniqueKey);

		BatchInfo batchInfo = new BatchInfo(BATCH_NAME);
		if(BATCH_ID!=null) batchInfo.setId(BATCH_ID);
		eyes.setBatch(batchInfo);

		//Allows for filtering dashboard view
		eyes.addProperty("SANDBOX", "YES");

		System.out.println("START THREAD ID - " + Thread.currentThread().getId() + " " + browserName + " " + browserVersion);
		System.out.println("baseBeforeClass took " + (System.currentTimeMillis() - before) + "ms");
	}

	@AfterClass(alwaysRun = true)
	public void baseAfterClass() {

		//Logs logs = driver.manage().logs();
		//LogEntries logEntries = logs.get(LogType.DRIVER);

		//for (LogEntry logEntry : logEntries) {
		//	System.out.println(logEntry.getMessage());
		//}

		if (driver != null) {
			long before = System.currentTimeMillis();
			eyes.abortIfNotClosed();
			driver.quit();
			System.out.println("Driver quit took " + (System.currentTimeMillis() - before) + "ms");
		}


	}
}
