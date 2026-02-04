package com.qaautomation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SalesListPage {
    WebDriver driver;
    WebDriverWait wait;

    public SalesListPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void deleteSale(String saleName) {
        WebElement row = driver.findElement(By.xpath("//tr[td[text()='" + saleName + "']]"));
        row.findElement(By.cssSelector("button.btn-outline-danger")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    public boolean isSalePresent(String saleName) {
        List<WebElement> rows = driver.findElements(By.xpath("//tr[td[text()='" + saleName + "']]"));
        return !rows.isEmpty();
    }

    public void sortByColumn(String columnName) {
        driver.findElement(By.xpath("//th[text()='" + columnName + "']")).click();
        // Add wait or validation as needed
    }
}
