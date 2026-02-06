package com.qaautomation.pages.plant2;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class PlantPageAdmin {

    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---
    private By addPlantBtn = By.xpath("//button[contains(text(),'Add a Plant')]");
    
    
    private By name = By.id("name");
    private By category = By.id("categoryId"); 
    private By price = By.id("price");
    private By quantity = By.id("quantity");
    private By saveBtn = By.xpath("//button[normalize-space()='Save']");
    
    // Error Message Locators
    private By successMsg = By.cssSelector(".alert-success");
    private By nameErrorId = By.id("nameError"); 
    private By globalErrorMsg = By.cssSelector(".alert-danger");
    
    private By fieldErrorMsg = By.cssSelector(".invalid-feedback, .text-danger, .error-message");

    // Search input for Admin List
    private By anySearchInput = By.cssSelector("input[type='search'], input[name='name']");

    public PlantPageAdmin(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // --- ACTIONS ---

    public void clickAddPlant() {
        wait.until(ExpectedConditions.elementToBeClickable(addPlantBtn)).click();
    }

    public void enterPlantName(String value) {
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(name));
        e.clear();
        e.sendKeys(value);
    }

    public void selectCategory(String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(category));
            new Select(dropdown).selectByVisibleText(value);
        } catch (Exception e) {
            
        }
    }

    public void enterPrice(String value) {
        driver.findElement(price).clear();
        driver.findElement(price).sendKeys(value);
    }

    public void enterQuantity(String value) {
        driver.findElement(quantity).clear();
        driver.findElement(quantity).sendKeys(value);
    }

    public void clickSave() {
        WebElement btn = driver.findElement(saveBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    // --- VALIDATIONS ---

    public boolean isNameValidationErrorDisplayed(String msg) {
        // 1. Check for explicit text in the page
        if (driver.getPageSource().contains(msg)) return true;
        
        // 2. Check for HTML5 'invalid' state
        if (isHtml5ValidationActive(name)) return true;

        // 3. Check for CSS 'is-invalid' class
        try {
            return driver.findElement(name).getAttribute("class").contains("is-invalid");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        // 1. Check for visible global alerts
        boolean hasAlert = !driver.findElements(globalErrorMsg).isEmpty();
        boolean hasNameError = !driver.findElements(nameErrorId).isEmpty();
        
        // --- NEW FIX: Check for field-level error messages (e.g. under Price) ---
        boolean hasFieldError = false;
        List<WebElement> fieldErrors = driver.findElements(fieldErrorMsg);
        for(WebElement err : fieldErrors) {
            if(err.isDisplayed() && !err.getText().isEmpty()) {
                hasFieldError = true;
                break;
            }
        }
        
        // 2. Check for HTML5 validation on Price or Quantity fields
        boolean priceInvalid = isHtml5ValidationActive(price);
        boolean qtyInvalid = isHtml5ValidationActive(quantity);
        
        return hasAlert || hasNameError || hasFieldError || priceInvalid || qtyInvalid;
    }

    public boolean isSuccessMessageDisplayed(String msg) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg)).getText().contains(msg);
        } catch (Exception e) {
            return false;
        }
    }

    // --- LIST VERIFICATION ---

    public boolean isPlantPresentInList(String plantName) {
        try {
            wait.until(ExpectedConditions.urlContains("/plants"));

            if (driver.getPageSource().contains(plantName)) return true;

            List<WebElement> searchInputs = driver.findElements(anySearchInput);
            if (!searchInputs.isEmpty() && searchInputs.get(0).isDisplayed()) {
                WebElement search = searchInputs.get(0);
                search.clear();
                search.sendKeys(plantName);
                search.sendKeys(Keys.ENTER);
                try { Thread.sleep(1000); } catch (InterruptedException e) {} 
                return driver.getPageSource().contains(plantName);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantPresentWithQuantity(String plantName, String qty) {
        return driver.getPageSource().contains(plantName) && driver.getPageSource().contains(qty);
    }

    public boolean isLowStockBadgeDisplayed(String plantName) {
        return !driver.findElements(By.cssSelector(".badge-warning, .bg-warning")).isEmpty();
    }

    // --- HELPER METHODS ---

    private boolean isHtml5ValidationActive(By fieldLocator) {
        try {
            WebElement element = driver.findElement(fieldLocator);
            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid === false", element);
        } catch (Exception e) {
            return false;
        }
    }

    // Add to PlantPageAdmin.java if missing
public void clickFirstEditButton() {
    // Finds the first "Edit" link/button in the table
    WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='edit']")));
    editBtn.click();
}

public void clickEditForPlant(String plantName) {
    // This XPath is more flexible with spaces and table structure
    String xpath = "//tr[td[contains(., '" + plantName + "')]]//a[contains(@href, 'edit')]";
    
    // Give the table a moment to render the data
    try { Thread.sleep(2000); } catch (InterruptedException e) {}
    
    driver.findElement(By.xpath(xpath)).click();
}

public String getPlantQuantityFromList(String plantName) {
    // Finds the quantity column for the specific plant row
    String xpath = "//td[contains(text(), '" + plantName + "')]/following-sibling::td[3]"; // Adjust index based on your table
    return driver.findElement(By.xpath(xpath)).getText().trim();
}
public void navigateToPlantList() {
    // This assumes your local environment follows this URL pattern
    driver.get("http://localhost:8080/ui/plants"); 
}
}