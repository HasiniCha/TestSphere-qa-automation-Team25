package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PlantsPage extends BasePage {

    private By plantsTable = By.cssSelector("table");

    public PlantsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnPlantsPage() {
        return driver.getCurrentUrl().contains("/plants");
    }

    
    public String getPlantStock(String plantName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(plantsTable));
        
        By plantStockLocator = By.xpath("//table/tbody/tr[td[contains(text(),'" + plantName + "')]]/td[4]"); 
        
        try {
            WebElement stockCell = wait.until(ExpectedConditions.visibilityOfElementLocated(plantStockLocator));
            return stockCell.getText();
        } catch (Exception e) {
            return "";
        }
    }
}