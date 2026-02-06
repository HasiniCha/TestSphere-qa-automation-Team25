package com.qaautomation.utils;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

  private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

  public static WebDriver initDriver() {
    String browser = ConfigReader.get("browser");
    boolean headless = ConfigReader.getBoolean("headless");

    WebDriver webDriver = null;

    switch (browser.toLowerCase()) {
      case "chrome":
        ChromeOptions chromeOptions = new ChromeOptions();
        if (headless) {
          chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.addArguments("--disable-dev-shm-usage"); 
        chromeOptions.addArguments("--no-sandbox"); 
        chromeOptions.addArguments("--remote-allow-origins=*"); 

        System.setProperty("webdriver.chrome.silentOutput", "true");

        webDriver = new ChromeDriver(chromeOptions);
        break;
      case "firefox":
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (headless) {
          firefoxOptions.addArguments("--headless");
        }
        webDriver = new FirefoxDriver(firefoxOptions);
        break;
      case "edge":
        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
          edgeOptions.addArguments("--headless");
        }
        webDriver = new EdgeDriver(edgeOptions);
        break;
      default:
        throw new IllegalArgumentException("Browser not supported: " + browser);
    }

    webDriver
      .manage()
      .timeouts()
      .implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));

    webDriver
      .manage()
      .timeouts()
      .pageLoadTimeout(
        Duration.ofSeconds(ConfigReader.getInt("page.load.timeout"))
      );

    if (ConfigReader.getBoolean("browser.window.maximize")) {
      webDriver.manage().window().maximize();
    }

    driver.set(webDriver);
    return webDriver;
  }

  public static WebDriver getDriver() {
    return driver.get();
  }

  public static void quitDriver() {
    if (driver.get() != null) {
      driver.get().quit();
      driver.remove();
    }
  }
}
