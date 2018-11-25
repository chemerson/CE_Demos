package com.testing.pages;


import com.applitools.eyes.MatchLevel;
import com.testing.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.applitools.eyes.selenium.Eyes;


public class home {

    WebDriver driver;
    Eyes eyes;

    public home(WebDriver driver, Eyes eyes){
        this.driver = driver;
        this.eyes = eyes;
    }

    public boolean openHomePage() {

        driver.get("https://www.wikipedia.com");
        Utils.checkMyWindow(eyes, "Home Page");

        return true;
    }

    public boolean clickEnglish() {

        driver.findElement(By.id("js-link-box-en")).click();
        Utils.checkMyWindow(eyes, "English Page");

        return true;


    }


}
