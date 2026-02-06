package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    private By manageCategoriesButton = By.xpath("//a[contains(normalize-space(), 'Manage Categories')]");
    private By managePlantButton = By.xpath("//a[contains(normalize-space(), 'Manage Plants')]");
    private By viewSalesButton = By.xpath("//a[contains(normalize-space(), 'View Sales')]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void clickManageCategoriesButton() {
        click(manageCategoriesButton);
    }

    public void clickManagePlantButton() {
        click(managePlantButton);
    }

   public void clickViewSalesButton() {
    wait.until(ExpectedConditions.elementToBeClickable(viewSalesButton)).click();
}



public boolean isOnDashboard() {
    try {
        // Wait up to 10 seconds for the URL to change
        return wait.until(ExpectedConditions.urlContains("/dashboard"));
    } catch (Exception e) {
        // If it times out, it means we never reached the dashboard
        return false;
    }
}

    
}