package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SalesPage extends BasePage {

    private By sellPlantButton = By.cssSelector("a.btn.btn-primary.btn-sm.mb-3");
    
    public SalesPage(WebDriver driver) {
        super(driver);
    }

    public void clickSellPlantButton() {
        click(sellPlantButton);
    }
}