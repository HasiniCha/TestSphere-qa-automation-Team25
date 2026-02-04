package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SellPlantPage extends BasePage {

    private By plantDropdown = By.name("plantId");
    private By quantityField = By.name("quantity");
    private By sellButton = By.cssSelector("button.btn.btn-primary");  
    private By plantErrorMsg = By.xpath("//div[@class='text-danger' and contains(text(),'Plant is required')]");

    public SellPlantPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnSellPlantPage() {
    return driver.getCurrentUrl().contains("/new");
}

    public void selectPlant(String plantName) {
        Select select = new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(plantDropdown)));
        select.selectByVisibleText(plantName);
    }

    public void enterQuantity(String quantity) {
    WebElement qtyField = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityField));
    qtyField.clear();
    qtyField.sendKeys(quantity);
}


    public void clickSell() {
        click(sellButton);  
    }

    public String getErrorMessage() {
        return getText(plantErrorMsg);  
    }
}