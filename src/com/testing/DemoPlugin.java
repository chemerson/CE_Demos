package com.testing;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.CustomField;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import com.testing.utils.Utils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.Method;

import com.testing.utils.VcsUtils;

public class DemoPlugin {

	protected RemoteWebDriver driver;
	protected ReportiumClient reportiumClient;

	private static final String SOURCE_FILE_ROOT_PATH = "src";



	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void Demo_FireStick() throws Exception {

		try {
			switchToContext(driver,"NATIVE_APP");

			Integer r;


			for (r=0;r<3;r++) {


				Utils.reportStepStart(reportiumClient, "@@@@@ RUN " + r + " @@@@@");

				Utils.reportStepStart(reportiumClient, "Click Home button");
				Utils.clickHomeButton(driver);

				Utils.reportStepStart(reportiumClient, "Open WSBTV App");
				Utils.closeApp(driver,"WSB-TV Channel 2 Action News");
				Utils.openApp(driver,"WSB-TV Channel 2 Action News");

				Utils.reportStepStart(reportiumClient, "Make sure app started");
				Utils.TextCheckpoint(driver,"WSB");

				Utils.reportStepStart(reportiumClient, "Click Watch Live");
				Utils.suspend(3000);
				Utils.clickButton(driver,"RIGHT");
				Utils.suspend(2000);
				Utils.clickButton(driver,"DOWN");
				Utils.suspend(2000);
				Utils.clickButton(driver,"ENTER");


				Utils.reportStepStart(reportiumClient, "Make sure video has started");
				Utils.suspend(2000);
				Integer i;
				Object result;
				Boolean hasVideo;
				Boolean resultBool;
				Map<String, Object> params = new HashMap<>();
				params.put("content", "GROUP:ChrisE/firestick/blacksquare.png");
				params.put("context", "all");
				params.put("match", "identical");
				params.put("source", "primary");
				params.put("timeout", "1");
				params.put("threshold", "90");
				hasVideo = false;
				for(i=0;i<5;i++) {
                    Utils.reportStepStart(reportiumClient, (i+1)+" poll for video");
                    result = driver.executeScript("mobile:checkpoint:image", params);
                    resultBool = Boolean.valueOf(result.toString());
                    if (!resultBool) {
                        hasVideo = true;
                        break;
                    }
                }
				if (!hasVideo) {
                    throw new Exception("Video did not start approx 10 seconds");
                }

				//Poll for error message

				//TODO: Also poll for frozen video
				Utils.reportStepStart(reportiumClient, "Start polling for an error message");
				params.clear();
				params.put("content", "GROUP:ChrisE/firestick/error.png");
				params.put("context", "all");
				params.put("match", "identical");
				params.put("timeout", "10");
				for(i=0;i<5;i++) {
                    Utils.reportStepStart(reportiumClient, (i+1)+" poll for error message");
                    result = driver.executeScript("mobile:checkpoint:image", params);
                    resultBool = Boolean.valueOf(result.toString());
                    if(resultBool) {throw new Exception("Error message found");}
                }


				//Test for frozen video
				//Grab screen
				//Save to cloud
				//Run image compa


				byte[] bytestss = driver.getScreenshotAs(OutputType.BYTES);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test(priority = 1, alwaysRun = true, enabled = true)
	public void Demo_TripAdvisor1() throws Exception {


		String label = "";
		String goBackxpath = "(//XCUIElementTypeNavigationBar/XCUIElementTypeButton[1]) | (//*[@resource-id=\"com.tripadvisor.tripadvisor:id/start_image\"]) | (//*[@label=\"Back\"]) | (//UIANavigationBar/UIAButton[1]/UIAImage[1]) | (//*[@content-desc=\"Navigate up\"])";

		Utils.startVNetwork(driver);

		Utils.clickHomeButton(driver);

		Utils.logs(driver, "start");

		Utils.closeApp(driver,"TripAdvisor");

		Utils.reportStepStart(reportiumClient, "Start app");
		Utils.openApp(driver, "TripAdvisor");

		switchToContext(driver,"NATIVE_APP");

		Utils.reportStepStart(reportiumClient, "Use OCR to verify app start");
		if(!Utils.TextCheckpoint(driver, "Things to Do")) {
			reportiumClient.reportiumAssert("App did not start", false);
			throw new Exception("App did not start");
		}

		Utils.reportStepStart(reportiumClient, "Search in Orlando");
		if(!Utils.TextCheckpoint(driver, "Orlando")) {
			Utils.clickElement(driver, "(//*[@label=\"TripAdvisor\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeCollectionView[1]/XCUIElementTypeCell[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeButton[1]) | (//*[@value=\"0\"]/UIAButton[2]) | (//*[@resource-id=\"com.tripadvisor.tripadvisor:id/expanded_pill\"])");
			if (!Utils.elementExists(driver, "(//*[@text=\"Orlando, Florida\"]) | (//*[@label=\"Orlando, Florida\"])")) {
				Utils.enterText(driver, "Orlando", "(//*[@resource-id=\"com.tripadvisor.tripadvisor:id/query_text\"]) | ((//*[@value=\"Where to?\"])[2])");
			}
			Utils.clickElement(driver, "(//*[@text=\"Orlando, Florida\"]) | (//*[@value=\"Orlando, Florida\"])");
		}
		if(!Utils.TextCheckpoint(driver, "Orlando")) {
			throw new Exception("Cannot set location to Orlando");
		}


		Utils.reportStepStart(reportiumClient, "See all hotels");
		Utils.clickElement(driver, "(//*[@value=\"See all\"]) | (//*[@text=\"See all\"])");

		Utils.reportStepStart(reportiumClient, "Go Back");
		Utils.clickElement(driver, goBackxpath);

		Utils.reportStepStart(reportiumClient, "View Restaurants");
		label = "Restaurants";
		Utils.clickElement(driver, "(//*[@text=\""+label+"\"]) | (//*[@label=\""+label+"\"])");

		Utils.reportStepStart(reportiumClient, "Go Back");
		Utils.clickElement(driver, goBackxpath);

		Utils.reportStepStart(reportiumClient, "View Things to do");
		label = "Things to Do";
		Utils.clickElement(driver, "(//*[@text=\""+label+"\"]) | (//*[@label=\""+label+"\"])");

		Utils.reportStepStart(reportiumClient, "Go Back");
		Utils.clickElement(driver, goBackxpath);

		Utils.reportStepStart(reportiumClient, "View Forums");
		label = "Forums";
		Utils.clickElement(driver, "(//*[@text=\""+label+"\"]) | (//*[@label=\""+label+"\"])");  //will fail
		if(!Utils.TextCheckpoint(driver, "Travel Forum")){
			throw new Exception("Did not open travel forum");
		}

		Utils.reportStepStart(reportiumClient, "Go Back");
		Utils.clickElement(driver, goBackxpath);
		if(!Utils.TextCheckpoint(driver, "Things to Do")) {
			throw new Exception("Did not return back to home screen");
		}

		byte[] bytestss = driver.getScreenshotAs(OutputType.BYTES);

	}



	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona", "automation"})
	@Test(priority = 1, alwaysRun = true, enabled = false)
	public void Demo_SpeedTest(String platformName ,String platformVersion, String browserName, String browserVersion,
						String screenResolution,  String location, String deviceName, String persona, String automation) throws Exception {

		String dutId = "";

		Map<String, Object> params = new HashMap<>();
		Object result;
		Long threadid = Thread.currentThread().getId();
		String testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		RemoteWebDriver driver = Utils.getRemoteWebDriver(threadid.toString() + " " + deviceName, platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, automation);

		dutId = "THREAD ID - " + threadid + " " + platformName + " " + platformVersion + " " + testName;
		//ReportiumClient reportiumClient = Utils.createReportiumClient(dutId, driver, "SpeedTest");
		//reportiumClient.testStart(TestConstants.TestData.SCRIPT_NAME + " " + platformName, new TestContext());
		//reportiumClient.testStart(this.getClass().getName(), new TestContext());
		System.out.println("THREAD ID - " + Thread.currentThread().getId() + " " + browserName + " " + browserVersion);
		//driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS); //brute force wait for tests to finish

		try {

			Utils.closeApp(driver, "Speedtest"); Utils.suspend(5000);
			Utils.openApp(driver, "Speedtest"); Utils.suspend(5000);

			Utils.reportStepStart(reportiumClient, "Simulate a 4G LTE Good network");
			params.clear();
			params.put("profile", "4g_lte_good");
			result = driver.executeScript("mobile:vnetwork:start", params);
			driver.findElementByXPath("//*[@label=\"Begin Test\" or @text=\"Begin Test\"]").click();

			Utils.reportStepStart(reportiumClient, "Simulate a 4G LTE Poor network");
			params.clear();
			params.put("profile", "4g_lte_poor");
			result = driver.executeScript("mobile:vnetwork:update", params);
			driver.findElementByXPath("//*[text()=\"Test Again\" or @label=\"Test Again\"]").click();


			Utils.reportStepStart(reportiumClient, "Simulate a 3G UMTS Average network");
			params.clear();
			params.put("profile", "3g_umts_average");
			result = driver.executeScript("mobile:vnetwork:update", params);
			driver.findElementByXPath("//*[text()=\"Test Again\" or @label=\"Test Again\"]").click();

			Utils.reportStepStart(reportiumClient, "End Test");
			reportiumClient.testStop(TestResultFactory.createSuccess());
			driver.close();
			driver.quit();
			Utils.killDriver(threadid.toString());


		} catch (Exception e) {
			System.out.println("EXCEPTION: " + e);
			reportiumClient.testStop(TestResultFactory.createFailure("Error",e));
			Utils.killDriver(threadid.toString());
		}
	}


	@Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona", "automation"})
	@BeforeClass(alwaysRun = true)
	public void baseBeforeClass(String platformName ,String platformVersion, String browserName, String browserVersion,
								String screenResolution,  String location, String deviceName, String persona, String automation) throws MalformedURLException {
		long before = System.currentTimeMillis();
		Long threadid = Thread.currentThread().getId();
		driver = Utils.getRemoteWebDriver(threadid.toString() + " " + deviceName, platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, automation);
		String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
		String dutId = "THREAD ID - " + threadid + " " + platformName + " " + platformVersion + " " + testName;
		reportiumClient = Utils.createReportiumClient(dutId, driver);

		System.out.println("START THREAD ID - " + Thread.currentThread().getId() + " " + browserName + " " + browserVersion);
		System.out.println("baseBeforeClass took " + (System.currentTimeMillis() - before) + "ms");
	}

	@AfterClass(alwaysRun = true)
	public void baseAfterClass() {
		System.out.println("Report url for " + this.getClass().getSimpleName() + ": " + reportiumClient.getReportUrl());
		if (driver != null) {
			long before = System.currentTimeMillis();
			driver.close();
			driver.quit();
			System.out.println("Driver quit took " + (System.currentTimeMillis() - before) + "ms");
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeTest(Method method) {
		try {
			Class testClass = method.getDeclaringClass();
			String testName = testClass.getSimpleName() + "." + method.getName();
			CustomField[] vcsFields = VcsUtils.createVcsFields(SOURCE_FILE_ROOT_PATH, testClass.getName());
			TestContext testContext = new TestContext.Builder().withCustomFields(vcsFields).build();
			reportiumClient.testStart(testName, testContext);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterTest(ITestResult testResult) {

		//Stop the network capture if there is one going on
		try {
			Map<String, Object> params = new HashMap<>();
			Object result = driver.executeScript("mobile:vnetwork:stop", params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int status = testResult.getStatus();
		switch (status) {
			case ITestResult.FAILURE:
				reportiumClient.testStop(TestResultFactory.createFailure("An error occurred", testResult.getThrowable()));
				break;
			case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
			case ITestResult.SUCCESS:
				reportiumClient.testStop(TestResultFactory.createSuccess());
				break;
			case ITestResult.SKIP:
				// Ignore
				break;
			default:
				throw new ReportiumException("Unexpected status: " + status);
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
