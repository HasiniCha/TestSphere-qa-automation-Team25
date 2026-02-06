package com.qaautomation.stepdefinitions;

import com.qaautomation.utils.DriverFactory;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    public static WebDriver driver;

    
    @Before("not @API")
    public void setUp() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
              
            }
            driver = null;
        }
        
        driver = DriverFactory.initDriver();
        driver.get(ConfigReader.get("app.url"));
    }

    
    @After("not @API")
    public void tearDown() {
        if (driver != null) {
            DriverFactory.quitDriver();
            driver = null;
        }
    }
}