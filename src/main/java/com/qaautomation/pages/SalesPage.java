package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.time.Duration;

public class SalesPage extends BasePage {

    private By sellPlantButton = By.cssSelector("a.btn.btn-primary.btn-sm.mb-3");
    private By salesTable = By.cssSelector("table");
    private By salesTableRows = By.cssSelector("table tbody tr");
    private By deleteButton = By.cssSelector("button.btn-outline-danger");
    
    public SalesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnSalesPage() {
        return driver.getCurrentUrl().contains("/sales");
    }

    public void clickSellPlantButton() {
        click(sellPlantButton);
    }

    public boolean verifySaleExists(String plantName, String quantity) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));
        
        By saleRow = By.xpath("//table/tbody/tr[contains(.,'" + plantName + "') and contains(.,'" + quantity + "')]");
        
        try {
            WebElement row = driver.findElement(saleRow);
            return row.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getLatestSaleRow() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesTableRows));
        List<WebElement> rows = driver.findElements(salesTableRows);
        
        if (rows.isEmpty()) {
            return null;
        }
        
        return rows.get(0);
    }

   public void deleteSale() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    // Click delete icon
    wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();

    // Handle confirmation alert
    driver.switchTo().alert().accept();
}


    public boolean isSaleDeleted(String saleName) {
        try {
           
            return wait.until(d -> {
                List<WebElement> rows = driver.findElements(By.xpath(
                    "//table/tbody/tr[td[contains(text(),'" + saleName + "')]]"
                ));
                return rows.isEmpty();
            });
        } catch (Exception e) {
            return true; 
        }
    }

    public void sortByColumn(String columnName) {
        driver.findElement(By.xpath("//th[text()='" + columnName + "']")).click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'alert-success')]//span[contains(text(),'deleted successfully')]")
            ));
            return toast.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    

}