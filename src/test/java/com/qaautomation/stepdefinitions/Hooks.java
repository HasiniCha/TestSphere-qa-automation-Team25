package com.qaautomation.stepdefinitions;

import com.qaautomation.utils.DriverFactory;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

   @Before("not @API")
public void setUp() {
    DriverFactory.initDriver();
    try {
        DriverFactory.getDriver().get(ConfigReader.get("app.url"));
    } catch (Exception e) {
        System.err.println("Initial page load timed out, attempting to continue anyway...");
    }
}

    @After("not @API")
    public void tearDown(Scenario scenario) {
        if (DriverFactory.getDriver() != null) {
            if (scenario.isFailed()) {
                try {
                    byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                            .getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", scenario.getName());
                } catch (Exception e) {
                    System.err.println("Failed to capture screenshot: " + e.getMessage());
                }
            }
            DriverFactory.quitDriver();
        }
    }
}