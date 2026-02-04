package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class SalesPage extends BasePage {

    private By sellPlantButton = By.cssSelector("a.btn.btn-primary.btn-sm.mb-3");
    private By salesTable = By.cssSelector("table");
    private By salesTableRows = By.cssSelector("table tbody tr");
    private By deleteButton = By.cssSelector("button.btn-outline-danger");
    private By successMessage = By.xpath("//div[contains(@class,'alert-success')]");
    
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

        // Click delete icon on first row
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();

        // Handle confirmation alert
        try {
            Thread.sleep(500);
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // If no alert, continue
        }
    }

    public boolean isSaleDeleted(String saleName) {
        try {
            Thread.sleep(1000);
            
            List<WebElement> rows = driver.findElements(By.xpath(
                "//table/tbody/tr[td[contains(text(),'" + saleName + "')]]"
            ));
            return rows.isEmpty();
        } catch (Exception e) {
            return true; 
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return toast.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    
    public void clickColumnHeader(String columnName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));
        
        
        By columnLinkLocator = By.xpath("//th/a[contains(text(),'" + columnName + "')]");
        
        try {
            WebElement columnLink = wait.until(ExpectedConditions.elementToBeClickable(columnLinkLocator));
            columnLink.click();
            
            
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Could not find or click column header: " + columnName + ". Error: " + e.getMessage());
        }
    }

    public List<String> getColumnValues(String columnName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));
        
        
        int columnIndex = getColumnIndex(columnName);
        
        List<WebElement> rows = driver.findElements(salesTableRows);
        List<String> values = new ArrayList<>();
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > columnIndex) {
                String cellText = cells.get(columnIndex).getText().trim();
               
                if (!cellText.isEmpty()) {
                    values.add(cellText);
                }
            }
        }
        
        return values;
    }

    private int getColumnIndex(String columnName) {
        
        List<WebElement> headers = driver.findElements(By.xpath("//table/thead/tr/th"));
        
        for (int i = 0; i < headers.size(); i++) {
            try {
              
                WebElement link = headers.get(i).findElement(By.tagName("a"));
                String linkText = link.getText().trim();
                
                if (linkText.equalsIgnoreCase(columnName) || 
                    linkText.toLowerCase().contains(columnName.toLowerCase())) {
                    return i;
                }
            } catch (Exception e) {
               
                continue;
            }
        }
        
        // Default mapping
        switch (columnName.toLowerCase()) {
            case "plant": return 0;
            case "quantity": return 1;
            case "total price": return 2;
            case "sold at": return 3;
            default: return 0;
        }
    }

    public boolean isSortedAscending(List<String> values) {
        if (values.size() <= 1) {
            return true;
        }
        
        List<String> sorted = new ArrayList<>(values);
        sorted.sort(String::compareToIgnoreCase);
        
        System.out.println("Original: " + values);
        System.out.println("Expected (ASC): " + sorted);
        
        return values.equals(sorted);
    }

    public boolean isSortedDescending(List<String> values) {
        if (values.size() <= 1) {
            return true;
        }
        
        List<String> sorted = new ArrayList<>(values);
        sorted.sort((a, b) -> b.compareToIgnoreCase(a));
        
        System.out.println("Original: " + values);
        System.out.println("Expected (DESC): " + sorted);
        
        return values.equals(sorted);
    }

 

  
}