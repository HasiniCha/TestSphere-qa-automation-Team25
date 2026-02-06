package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private By manageCategoriesButton = By.linkText("Manage Categories");
    private By managePlantButton = By.linkText("Manage Plant");
    private By viewSalesButton = By.linkText("View Sales");

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
        click(viewSalesButton);
    }

    public boolean isOnDashboard() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/dashboard");
    }
}
