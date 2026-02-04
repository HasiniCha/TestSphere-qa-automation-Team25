package com.qaautomation.stepdefinitions;

import com.qaautomation.utils.DriverFactory;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    public static WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverFactory.initDriver();
        driver.get(ConfigReader.get("app.url"));
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
