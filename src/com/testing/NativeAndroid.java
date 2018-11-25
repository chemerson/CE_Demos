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

public class NativeAndroid {

    Long threadid = Thread.currentThread().getId();
    RemoteWebDriver driver;


    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona","automation"})
    @BeforeClass
    public void before(String platformName,String platformVersion, String browserName, String browserVersion,
                       String screenResolution,  String location, String deviceName, String persona, String automation) throws Exception{
        driver = Utils.getRemoteWebDriver(threadid.toString() + "Android", platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona, automation);
    }

    @AfterClass
    public void after() {
        driver.close();
        driver.quit();
        Utils.killDriver(threadid.toString() + "Android");
    }

    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
    @Test(alwaysRun = true)
    public void TC_01(String platformName,String platformVersion, String browserName, String browserVersion,
                      String screenResolution,  String location, String deviceName, String persona) throws Exception {

    }

    @Parameters({"platformName", "platformVersion", "browserName", "browserVersion", "screenResolution", "location", "deviceName", "persona"})
    @Test(alwaysRun = true)
    public void TC_02(String platformName,String platformVersion, String browserName, String browserVersion,
                      String screenResolution,  String location, String deviceName, String persona, ITestContext context) throws Exception {


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



