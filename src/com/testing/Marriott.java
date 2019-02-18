package com.testing;

import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;

import com.applitools.eyes.visualGridClient.model.RenderingConfiguration;
import com.applitools.eyes.visualGridClient.model.TestResultSummary;
import com.applitools.eyes.visualGridClient.services.VisualGridManager;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.selenium.StitchMode;
import com.perfecto.reportium.client.ReportiumClient;
import com.testing.pages.home;
import com.testing.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Marriott {


	protected RemoteWebDriver driver;
	protected ReportiumClient reportiumClient;
	protected home objHome;


	protected Target target;

	private VisualGridManager VisualGrid = new VisualGridManager(10);
	private RenderingConfiguration renderConfig = new RenderingConfiguration();
	private Eyes eyes = new Eyes();


	private static final String SOURCE_FILE_ROOT_PATH = "src";
	private static final String BATCH_NAME = "Marriott Demo 1";
	private static final String BATCH_ID = "Marriott1";  //optional or null, keep all tests in the same batch
	private static final String APP_NAME = "Marriott";


	@Test(priority = 1, alwaysRun = true, enabled = true)
	public void CheckURLs_1() {
		Integer i=0;
		String testName = "Marriott Web";
		long before;

		eyes.setMatchLevel(MatchLevel.EXACT);
		eyes.setStitchMode(StitchMode.CSS);
		eyes.setForceFullPageScreenshot(true);
		eyes.setSendDom(true);
		eyes.setMatchTimeout(500);

		//Set the environment name in the test batch results
		eyes.setEnvName(driver.getCapabilities().getBrowserName() + " " + driver.getCapabilities().getVersion());
		eyes.open(driver, APP_NAME, testName);

		String[] arr = new String[0];
		try {
			Scanner sc = new Scanner(new File("resources/urls.csv"));
			List<String> lines = new ArrayList<String>();
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			arr = lines.toArray(new String[0]);
			System.out.println("URL's to check: " + arr.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for(i=1;i<arr.length;i++){
			before = System.currentTimeMillis();
			System.out.println("Checking URL " + i + ": " + arr[i]);
			try {
				driver.get(arr[i]);
				Utils.scrollPage(driver);
				eyes.checkWindow(arr[1]);
			} catch (Exception e) {
				System.out.println("FAILED URL " + i + " in " + (System.currentTimeMillis() - before) + "ms");
				e.printStackTrace();
				eyes.abortIfNotClosed();
				if (driver != null) {
					driver.quit();
				}
				driver = Utils.getLocalWebDriver();
				driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
				eyes.open(driver, APP_NAME, testName);
			}
		}
	}


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
	@BeforeClass(alwaysRun = true)
	public void baseBeforeClass(String platformName ,String platformVersion, String browserName, String browserVersion,
								String screenResolution,  String location, String deviceName, String persona) throws MalformedURLException {


		Long threadid = Thread.currentThread().getId();
		String uniqueKey = threadid.toString() + " " + deviceName;
		long before = System.currentTimeMillis();
		driver = Utils.getLocalWebDriver();
		browserName = "Local Chrome";
		browserVersion = "Local Version";
		//driver.setLogLevel(Level.ALL);

		// Using Visual Grid Implementation eyes = Utils.getEyes(uniqueKey);
		eyes.setApiKey(Utils.EYES_KEY);

		BatchInfo batchInfo = new BatchInfo(BATCH_NAME);
		if(BATCH_ID!=null) batchInfo.setId(BATCH_ID);
		eyes.setBatch(batchInfo);

		//Allows for filtering dashboard view
		//not yet implemented in VG SDK eyes.addProperty("SANDBOX", "YES");

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
