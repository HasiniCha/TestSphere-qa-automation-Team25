package com.qaautomation.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.qaautomation.pages.BasePage;

public class SellPlantPage extends BasePage {

    private By plantDropdown = By.name("plantId");
    private By quantityField = By.name("quantity");
    private By sellButton = By.cssSelector("button.btn.btn-primary");  
    private By errorMsg = By.xpath("//div[@class='text-danger']");

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
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sellButton));
    try {
        element.click();
    } catch (Exception e) {
        
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}

    
    public String getErrorMessage() {
        return getText(errorMsg);  
    }

    // quantity validation
    public String getQuantityValidationMessage() {
        WebElement qty = driver.findElement(quantityField);
        return qty.getAttribute("validationMessage");
    }
    
    public boolean hasQuantityValidationMessage() {
        WebElement qty = driver.findElement(quantityField);
        String validationMsg = qty.getAttribute("validationMessage");
        return validationMsg != null && !validationMsg.isEmpty();
    }

   public int getPlantStockFromDropdown(String fullText) {
    
    java.util.regex.Pattern p = java.util.regex.Pattern.compile("Stock: (\\d+)");
    java.util.regex.Matcher m = p.matcher(fullText);
    
    if (m.find()) {
        return Integer.parseInt(m.group(1)); // Returns the 75
    }
    return 0;
}
}