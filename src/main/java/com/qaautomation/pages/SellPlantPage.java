package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SellPlantPage extends BasePage {

    private By plantDropdown = By.name("plantId");
    private By quantityField = By.name("quantity");
    private By sellButton = By.cssSelector("button.btn.btn-primary");  
    private By errorMsg = By.cssSelector(".text-danger");

    public SellPlantPage(WebDriver driver) {
        super(driver);
    }

    public void selectPlant(String plantName) {
        Select select = new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(plantDropdown)));
        select.selectByVisibleText(plantName);
    }

    public void enterQuantity(String quantity) {
        type(quantityField, quantity);  
    }

    public void clickSell() {
        click(sellButton);  
    }

    public String getErrorMessage() {
        return getText(errorMsg);  
    }
}