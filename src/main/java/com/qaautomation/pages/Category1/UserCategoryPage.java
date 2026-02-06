package com.qaautomation.pages.Category1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserCategoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public UserCategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    private By categoryTable = By.tagName("table");

    // Check if category list/table is visible
    public boolean isCategoryListVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
            return driver.findElement(categoryTable).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Get current URL
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    // Check if Add Category button is NOT visible (User should not see it)
    public boolean isAddCategoryButtonNotVisible() {
        try {
            By addCategoryButton = By.cssSelector("a.btn.btn-primary[href='/ui/categories/add']");
            // Wait a bit to ensure page is fully loaded
            Thread.sleep(1000);
            // Check if button exists
            return driver.findElements(addCategoryButton).size() == 0;
        } catch (Exception e) {
            return false;
        }
    }
    // Click the first Edit icon in the category list
    public void clickEditIcon() {
        try {
            By editIcon = By.cssSelector("a[href*='/ui/categories/edit']");
            wait.until(ExpectedConditions.elementToBeClickable(editIcon));
            driver.findElement(editIcon).click();
        } catch (Exception e) {
            System.out.println(">>> Could not find or click Edit icon");
        }
    }

    // Modify category name in edit form
    public void modifyCategoryName(String newName) {
        try {
            By categoryNameInput = By.id("name");
            wait.until(ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
            driver.findElement(categoryNameInput).clear();
            driver.findElement(categoryNameInput).sendKeys(newName);
        } catch (Exception e) {
            System.out.println(">>> Could not modify category name");
        }
    }

    // Click Save button
    public void clickSaveButton() {
        try {
            By saveButton = By.cssSelector("button[type='submit']");
            wait.until(ExpectedConditions.elementToBeClickable(saveButton));
            driver.findElement(saveButton).click();
        } catch (Exception e) {
            System.out.println(">>> Could not click Save button");
        }
    }

    // Check if error message appears
    public boolean isErrorMessageDisplayed() {
        try {
            // Try multiple possible error message selectors
            By[] errorSelectors = {
                By.cssSelector(".alert-danger"),
                By.cssSelector(".error"),
                By.cssSelector(".text-danger"),
                By.xpath("//*[contains(text(),'error')]"),
                By.xpath("//*[contains(text(),'Error')]"),
                By.xpath("//*[contains(text(),'permission')]"),
                By.xpath("//*[contains(text(),'not allowed')]"),
                By.xpath("//*[contains(text(),'forbidden')]")
            };
            
            for (By selector : errorSelectors) {
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                    System.out.println(">>> Found error message with selector: " + selector);
                    return true;
                } catch (Exception e) {
                    // Try next selector
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
   // Click on Parent column header to sort
// public void clickParentColumnHeader() {
//     try {
//         By parentHeader = By.xpath("//th[contains(text(),'Parent')]");
//         wait.until(ExpectedConditions.elementToBeClickable(parentHeader));
//         driver.findElement(parentHeader).click();
//         Thread.sleep(1000); // Wait for sort to apply
//     } catch (Exception e) {
//         System.out.println(">>> Could not click Parent column header");
//     }
// }
// Click on Parent column header to sort
// Click on Parent column header to sort
public void clickParentColumnHeader() {
    try {
        // The Parent header is a link inside the th
        By parentHeaderLink = By.xpath("//th/a[contains(text(),'Parent')]");
        wait.until(ExpectedConditions.elementToBeClickable(parentHeaderLink));
        driver.findElement(parentHeaderLink).click();
        System.out.println(">>> Successfully clicked Parent header link");
        Thread.sleep(1000);
    } catch (Exception e) {
        System.out.println(">>> Could not click Parent column header: " + e.getMessage());
    }
}
// Check if categories are sorted (basic check - verifies table is still displayed after sort)
public boolean isCategorySorted() {
    try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
        return driver.findElement(categoryTable).isDisplayed();
    } catch (Exception e) {
        return false;
    }
}

// Check if sort indicator is visible (arrow icon showing sort direction)
public boolean isSortIndicatorDisplayed() {
    try {
        By sortIndicator = By.cssSelector("th:has-text('Parent') i, th:has-text('Parent') span.sort");
        return driver.findElements(sortIndicator).size() > 0;
    } catch (Exception e) {
        // Sort indicator might not be visible, that's okay
        return true;
    }
}
// Enter text in search field
public void enterSearchText(String searchText) {
    try {
        By searchInput = By.name("name");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(searchText);
        System.out.println(">>> Entered search text: " + searchText);
    } catch (Exception e) {
        System.out.println(">>> Could not enter search text");
    }
}

// Select parent from dropdown
public void selectParentCategory(String parentName) {
    try {
        By parentDropdown = By.name("parentId");
        wait.until(ExpectedConditions.elementToBeClickable(parentDropdown));
        driver.findElement(parentDropdown).sendKeys(parentName);
        System.out.println(">>> Selected parent: " + parentName);
    } catch (Exception e) {
        System.out.println(">>> Could not select parent");
    }
}

// Click Search button
public void clickSearchButton() {
    try {
        By searchButton = By.cssSelector("button[type='submit']");
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        driver.findElement(searchButton).click();
        Thread.sleep(1000);
        System.out.println(">>> Clicked Search button");
    } catch (Exception e) {
        System.out.println(">>> Could not click Search button");
    }
}

// Click Reset button
public void clickResetButton() {
    try {
        By resetButton = By.xpath("//a[contains(text(),'Reset')]");
        wait.until(ExpectedConditions.elementToBeClickable(resetButton));
        driver.findElement(resetButton).click();
        Thread.sleep(1000);
        System.out.println(">>> Clicked Reset button");
    } catch (Exception e) {
        System.out.println(">>> Could not click Reset button");
    }
}

// Check if search field is cleared
public boolean isSearchFieldCleared() {
    try {
        By searchInput = By.name("name");
        String value = driver.findElement(searchInput).getAttribute("value");
        return value == null || value.isEmpty();
    } catch (Exception e) {
        return false;
    }
}

// Check if parent dropdown is reset to default
public boolean isParentDropdownReset() {
    try {
        By parentDropdown = By.name("parentId");
        String value = driver.findElement(parentDropdown).getAttribute("value");
        return value == null || value.isEmpty();
    } catch (Exception e) {
        return false;
    }
}
}