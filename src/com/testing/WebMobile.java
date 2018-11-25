package com.testing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebMobile {


/*

    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona", "automation"})
    @Test(priority = 1, alwaysRun = true, enabled = false)
    public void GoogleSignInString(String platformName ,String platformVersion, String browserName, String browserVersion,
            String screenResolution,  String location, String deviceName, String persona, String automation) throws Exception  {

        Long threadid = Thread.currentThread().getId();

        String dutId = "";

        Map<String, Object> params = new HashMap<>();
        Object result;
        String testName = Thread.currentThread().getStackTrace()[1].getMethodName();

        String googleUsername = "perfectoinstaller123@gmail.com";
        String googlePassword = "Install443210";
        String googleCode = "";
        String googleCode_old = "";

        RemoteWebDriver driverWeb = Utils.getRemoteWebDriverBasic(platformName, platformVersion, browserName, browserVersion, screenResolution, "", "");
        RemoteWebDriver driverMobile = Utils.getRemoteWebDriver("GoogleSignInMobile", "Android", "", "", "", "", deviceName, persona, automation);

        dutId = "THREAD ID - " + Thread.currentThread().getId() + " " + platformName + " " + platformVersion + " " + testName;
        ReportiumClient reportiumWebClient = Utils.createReportium("Web" + dutId, driverWeb, "GoogleSignIn Web");
        reportiumWebClient.testStart(TestConstants.TestData.SCRIPT_NAME + " " + platformName, new TestContext());
        reportiumWebClient.testStart(this.getClass().getName(), new TestContext());

        ReportiumClient reportiumMobileClient = Utils.createReportium("Mobile" + dutId, driverMobile, "GoogleSignIn Mobile");
        reportiumMobileClient.testStart(TestConstants.TestData.SCRIPT_NAME + " " + platformName, new TestContext());
        reportiumMobileClient.testStart(this.getClass().getName() + "MOBILE", new TestContext());

        try {

            System.out.println("THREAD ID - " + Thread.currentThread().getId() + " " + browserName + " " + browserVersion);
            Utils.reportStepStart(reportiumWebClient, "Start Test");
            Utils.reportStepStart(reportiumMobileClient, "Start Test");

            Utils.reportStepStart(reportiumMobileClient, "Step 1 - Get current code to confirm when new one arrives from Google");
            driverMobile.executeScript("mobile:handset:ready", params);
            Utils.closeApp(driverMobile,"com.android.mms");
            Utils.openApp(driverMobile,"com.android.mms");

            // block start




            // block end


            String attribute = driverMobile.findElementByXPath("(//*[@resource-id=\"com.android.mms:id/subject\"])[1]").getAttribute("text");
            googleCode_old = attribute.substring(2,8);
            System.out.println("googleCode_old:" + googleCode_old + ":");

            driverWeb.get("http://www.google.com");

            Utils.reportStepStart(reportiumWebClient,"Step 1 - Enter Credentials");
            driverWeb.findElementByXPath(".//*[@id='gb_70']").click();
            driverWeb.findElementByXPath(".//*[@id='Email']").sendKeys(googleUsername);
            driverWeb.findElementByXPath(".//*[@id='next']").click();

            driverWeb.findElementByXPath(".//*[@id='Passwd']").sendKeys(googlePassword);
            driverWeb.findElementByXPath(".//*[@id='signIn']").click();
            Utils.suspend(6000); //allow some time for the call to go through

            Utils.reportStepStart(reportiumWebClient,"Step 2 - Get code from mobile device");
            Utils.reportStepStart(reportiumMobileClient,"Step 2 - Get code from mobile device");
            Utils.closeApp(driverMobile,"com.android.mms");
            Utils.openApp(driverMobile,"com.android.mms");  //make sure display is refreshed with latest message
            attribute = driverMobile.findElementByXPath("(//*[@resource-id=\"com.android.mms:id/subject\"])[1]").getAttribute("text");
            googleCode = attribute.substring(2,8);
            System.out.println("googleCode:" + googleCode + ":");

            driverWeb.findElementByXPath(".//*[@id='idvPreregisteredPhonePin'] ").sendKeys(googleCode);
            driverWeb.findElementByXPath(".//*[@id='submit']").click();

            //make sure the code was correct and resend if not, check 2 items to be sure
            if(Utils.elementExists(driverWeb,".//*[@id='errorMsg']") || !Utils.elementExists(driverWeb, ".//*[@id='gs_htif0']")) {
                Utils.reportStepStart(reportiumMobileClient,"Step 2A - Resend code");
                driverWeb.findElementByXPath(".//*[@id='skipChallenge']").click();
                driverWeb.findElementByXPath(".//*[@id='challengePickerList']/li[1]/form/button").click();

                Utils.suspend(6000); //allow some time for the call to go through
                Utils.openApp(driverMobile,"com.android.mms");  //make sure display is refreshed with latest message
                attribute = driverMobile.findElementByXPath("(//*[@resource-id=\"com.android.mms:id/subject\"])[1]").getAttribute("text");
                googleCode = attribute.substring(2,8);
                System.out.println("googleCode:" + googleCode + ":");

                driverWeb.findElementByXPath(".//*[@id='idvPreregisteredPhonePin'] ").sendKeys(googleCode);
                driverWeb.findElementByXPath(".//*[@id='submit']").click();
            }


            Utils.reportStepStart(reportiumWebClient,"Step 3 - Verify that account is logged in");
            if(!Utils.elementExists(driverWeb,".//*[@id='errorMsg']")) {
                Utils.reportStepStart(reportiumWebClient, "End Test - Logged in");
                reportiumWebClient.testStop(TestResultFactory.createSuccess());
                reportiumMobileClient.testStop(TestResultFactory.createSuccess());
            } else {
                try {
                    throw new Exception("Login failure");
                } catch (Exception e) {
                    reportiumWebClient.testStop(TestResultFactory.createFailure("Not logged in",e));
                    reportiumMobileClient.testStop(TestResultFactory.createFailure("Not logged in",e));
                }
                Utils.reportStepStart(reportiumWebClient,"End Test - NOT Logged in");
            }

            System.out.println("WEB REPORT URL: " + reportiumWebClient.getReportUrl());
            System.out.println("MOBILE REPORT URL: " + reportiumMobileClient.getReportUrl());

            driverWeb.close();
            driverMobile.close();
            driverMobile.quit();
            driverWeb.quit();

        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e);
            System.out.println("WEB REPORT URL: " + reportiumWebClient.getReportUrl());
            System.out.println("MOBILE REPORT URL: " + reportiumMobileClient.getReportUrl());
            reportiumWebClient.testStop(TestResultFactory.createFailure("Error",e));
            reportiumMobileClient.testStop(TestResultFactory.createFailure("Error",e));
            driverWeb.close();
            driverMobile.close();
            driverMobile.quit();
            driverWeb.quit();
        }
    }

*/

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
