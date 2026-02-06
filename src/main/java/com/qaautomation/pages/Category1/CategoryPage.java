package com.qaautomation.pages.Category1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    private By addCategoryButton = By.cssSelector("a.btn.btn-primary[href='/ui/categories/add']");
    private By categoryNameInput = By.id("name");
    private By parentCategoryDropdown = By.id("parentId");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By successMessage = By.cssSelector(".alert-success");
    // Try multiple possible locators for validation errors
    private By validationErrorRequired = By.cssSelector("div.invalid-feedback");
    private By validationErrorLength   = By.cssSelector("div.invalid-feedback");

    // ============ EXISTING METHODS ============
    
    // Check visibility
    public boolean isAddCategoryButtonVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addCategoryButton));
        return driver.findElement(addCategoryButton).isDisplayed();
    }

    // Check clickability
    public boolean isAddCategoryButtonClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(addCategoryButton));
        return driver.findElement(addCategoryButton).isEnabled();
    }

    // Click the Add Category button
    public void clickAddCategoryButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCategoryButton));
        WebElement button = driver.findElement(addCategoryButton);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", button);
    }
    
    // ============ METHODS FOR CREATING CATEGORY ============
    
    // Enter category name
    public void enterCategoryName(String categoryName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
        WebElement nameField = driver.findElement(categoryNameInput);
        nameField.clear();
        nameField.sendKeys(categoryName);
    }
    
    // Leave category name empty (just clear the field)
    public void leaveCategoryNameEmpty() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
        WebElement nameField = driver.findElement(categoryNameInput);
        nameField.clear();
    }
    
    // Click Save button
    public void clickSaveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        driver.findElement(saveButton).click();
    }
    
    // Check if category exists in the list by name
    public boolean isCategoryVisibleInList(String categoryName) {
        By categoryInList = By.xpath("//td[text()='" + categoryName + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryInList));
        return driver.findElement(categoryInList).isDisplayed();
    }
    
    // Get parent value for a specific category
    public String getParentValueForCategory(String categoryName) {
        By parentCell = By.xpath("//td[text()='" + categoryName + "']/following-sibling::td[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(parentCell));
        return driver.findElement(parentCell).getText();
    }
    
    // Check if success message is displayed
    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return driver.findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // ============ VALIDATION METHODS ============
    
    // Check if required validation error is displayed
    public boolean isRequiredValidationErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(validationErrorRequired));
            return driver.findElement(validationErrorRequired).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Check if length validation error is displayed
    public boolean isLengthValidationErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(validationErrorLength));
            return driver.findElement(validationErrorLength).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Get the text of required validation error
    public String getRequiredValidationErrorText() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(validationErrorRequired));
        return driver.findElement(validationErrorRequired).getText();
    }
    
    // Check if validation error is displayed in red color
    public boolean isValidationErrorInRed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(validationErrorRequired));
        WebElement errorElement = driver.findElement(validationErrorRequired);
        String color = errorElement.getCssValue("color");
        // Red color in RGB format is typically "rgba(220, 53, 69, 1)" or similar
        return color.contains("220") || color.contains("red");
    }
    
    // ============ METHODS FOR DELETING CATEGORY ============
    
    // Debug method - prints all elements in the category row
    public void debugCategoryRow(String categoryName) {
        try {
            // Find the row
            By categoryRow = By.xpath("//td[text()='" + categoryName + "']/parent::tr");
            WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(categoryRow));
            
            System.out.println(">>> ====== DEBUG INFO FOR: " + categoryName + " ======");
            System.out.println(">>> Full Row HTML:");
            System.out.println(row.getAttribute("outerHTML"));
            
            // Try to find all buttons in this row
            List<WebElement> buttons = row.findElements(By.tagName("button"));
            System.out.println(">>> Total buttons found in row: " + buttons.size());
            
            for (int i = 0; i < buttons.size(); i++) {
                WebElement btn = buttons.get(i);
                System.out.println(">>> Button " + (i+1) + ":");
                System.out.println("    - Class: " + btn.getAttribute("class"));
                System.out.println("    - Text: " + btn.getText());
                System.out.println("    - HTML: " + btn.getAttribute("outerHTML"));
            }
            
            System.out.println(">>> ====== END DEBUG INFO ======");
            
        } catch (Exception e) {
            System.out.println(">>> ERROR in debug: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Locator for delete button - finds the outlined danger button
    private By deleteButtonForCategory(String categoryName) {
        return By.xpath("//td[text()='" + categoryName + "']/following-sibling::td//button[contains(@class, 'btn-outline-danger')]");
    }

    // Alternative locator - exact class match
    private By deleteButtonAlternative(String categoryName) {
        return By.xpath("//td[text()='" + categoryName + "']/following-sibling::td//button[@class='btn btn-sm btn-outline-danger']");
    }

    // Click delete button for a specific category
    public void clickDeleteButtonForCategory(String categoryName) {
        try {
            By deleteBtn = deleteButtonForCategory(categoryName);
            wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
            WebElement button = driver.findElement(deleteBtn);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", button);
        } catch (Exception e) {
            // Try alternative locator
            System.out.println(">>> Trying alternative delete button locator...");
            By deleteBtn = deleteButtonAlternative(categoryName);
            wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
            WebElement button = driver.findElement(deleteBtn);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", button);
        }
    }

    // Check if delete button is visible for a category
    public boolean isDeleteButtonVisibleForCategory(String categoryName) {
        try {
            By deleteBtn = deleteButtonForCategory(categoryName);
            wait.until(ExpectedConditions.visibilityOfElementLocated(deleteBtn));
            return driver.findElement(deleteBtn).isDisplayed();
        } catch (Exception e) {
            // Try alternative locator
            try {
                System.out.println(">>> Trying alternative delete button locator for visibility check...");
                By deleteBtn = deleteButtonAlternative(categoryName);
                wait.until(ExpectedConditions.visibilityOfElementLocated(deleteBtn));
                return driver.findElement(deleteBtn).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // Click confirm button in delete confirmation dialog (handles JavaScript alert)
    public void clickConfirmDeleteButton() {
        try {
            // Wait for alert to be present
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            alertWait.until(ExpectedConditions.alertIsPresent());
            
            // Switch to alert and accept it
            Alert alert = driver.switchTo().alert();
            System.out.println(">>> Alert text: " + alert.getText());
            alert.accept(); // Click OK
            
        } catch (Exception e) {
            System.out.println(">>> Error handling alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if category is NOT visible in the list (for deletion verification)
    public boolean isCategoryNotVisibleInList(String categoryName) {
        try {
            By categoryInList = By.xpath("//td[text()='" + categoryName + "']");
            // Wait a bit to see if element disappears
            Thread.sleep(1000);
            return driver.findElements(categoryInList).isEmpty();
        } catch (Exception e) {
            return true; // If exception, category likely doesn't exist
        }
    }

    // Get success message text
    public String getSuccessMessageText() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return driver.findElement(successMessage).getText();
    }
}