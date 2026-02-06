package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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


   public boolean isMenuItemActive(String menuName) {
    try {
        // We use contains(., 'text') because the text "Dashboard" is 
        // next to an <i> tag inside the <a> tag.
        WebElement menuItem = driver.findElement(By.xpath("//a[contains(.,'" + menuName + "')]"));
        
        String classAttribute = menuItem.getAttribute("class");
        
        // This returns true if "active" is present in the class string
        return classAttribute != null && classAttribute.contains("active");
    } catch (Exception e) {
        // If the element isn't found, it's definitely not active
        return false;
    }
}
}