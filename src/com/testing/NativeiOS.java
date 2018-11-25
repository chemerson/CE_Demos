package com.testing;


import com.testing.utils.Utils;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NativeiOS {

    Long threadid = Thread.currentThread().getId();
    RemoteWebDriver driver;

    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona", "automation"})
    @BeforeClass
    public void before(String platformName,String platformVersion, String browserName, String browserVersion,
                       String screenResolution,  String location, String deviceName, String persona, String automation) throws Exception{
        driver = Utils.getRemoteWebDriver(threadid.toString() + "iOS", platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, automation);
    }

    @AfterClass
    public void after() {
        driver.close();
        driver.quit();
        Utils.killDriver(threadid.toString() + "iOS");
    }

    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
    @Test(alwaysRun = true, enabled = true)
    public void TC_01(String platformName,String platformVersion, String browserName, String browserVersion,
                      String screenResolution,  String location, String deviceName, String persona, ITestContext context) throws Exception {

        Map<String, Object> params = new HashMap<>();
        Object result;
        String dutId = "";

        String testName = Thread.currentThread().getStackTrace()[1].getMethodName();

        dutId = "THREAD ID - " + Thread.currentThread().getId() + " " + platformName + " " + deviceName + " " + testName;
        //ReportiumClient reportiumClient = Utils.createReportium(dutId, driver, testName);
        //reportiumClient.testStart(TestConstants.TestData.SCRIPT_NAME + " " + platformName, new TestContext());

        try {

            /*
            System.out.println(dutId);
            Utils.reportStepStart(reportiumClient, "Start iOS Test");
            Utils.clickHomeButton(driver);

            Utils.reportStepStart(reportiumClient, "Step 1 - Open app");

            Map<String, Object> params2 = new HashMap<>();
            params2.put("identifier", "com.perfectomobile.FingerprintTest");
            Object result2 = driver.executeScript("mobile:application:uninstall", params2);


            Map<String, Object> params1 = new HashMap<>();
            params1.put("file", "PRIVATE:apps/FingerprintTest.ipa");
            params1.put("sensorInstrument", "sensor");
            Object result1 = driver.executeScript("mobile:application:install", params1);

            switchToContext(driver, "NATIVE_APP");

            Utils.openApp(driver, "FingerprintTest");

            Utils.reportStepStart(reportiumClient, "Step 2 - Pass authentication");
            driver.findElementByXPath("//*[@label=\"AUTH\"]").click();
            params.clear();
            params.put("name", "FingerprintTest");
            params.put("resultAuth", "success");
            result = driver.executeScript("mobile:fingerprint:set", params);
            Utils.reportAssert(reportiumClient, "Success", true);


            Utils.reportStepStart(reportiumClient, "Step 3 - Fail authentication");
            driver.findElementByXPath("//*[@label=\"AUTH\"]").click();
            params.clear();
            params.put("name", "FingerprintTest");
            params.put("resultAuth", "fail");
            params.put("errorType", "authFailed");
            result = driver.executeScript("mobile:fingerprint:set", params);
            Utils.reportAssert(reportiumClient, "Failed authentication", true);

            Utils.reportStepStart(reportiumClient, "Step 4 - User cancels");
            driver.findElementByXPath("//*[@label=\"AUTH\"]").click();
            params.clear();
            params.put("name", "FingerprintTest");
            params.put("resultAuth", "fail");
            params.put("errorType", "userCancel");
            result = driver.executeScript("mobile:fingerprint:set", params);
            Utils.reportAssert(reportiumClient, "User cancel", true);


            reportiumClient.testStop(TestResultFactory.createSuccess());

*/

        } catch (Exception e) {
            System.out.println(dutId + " EXCEPTION: " + e);

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

    private static List<String> getContextHandles(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }


}
