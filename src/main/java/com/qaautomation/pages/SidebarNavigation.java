package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SidebarNavigation extends BasePage {

    private By dashboardLink = By.linkText("Dashboard");
    private By categoryLink = By.linkText("Categories");
    private By plantsLink = By.linkText("Plants");
    private By salesLink = By.linkText("Sales");
  
    public SidebarNavigation(WebDriver driver) {
        super(driver);
    }

    public void clickDashboard() {
        click(dashboardLink);
    }

     public void clickCategories() {
        click(categoryLink);
    }

    public void clickPlants() {
        click(plantsLink);
    }

    public void clickSales() {
        click(salesLink);
    }
}