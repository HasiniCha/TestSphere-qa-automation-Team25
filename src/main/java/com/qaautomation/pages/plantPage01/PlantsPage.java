package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.List;

public class PlantsPage extends BasePage {

    private By plantsNavLink = By.xpath("//a[@href='/ui/plants']");
    
    private By addPlantButton = By.xpath("//a[@href='/ui/plants/add']");
    private By plantsTable = By.xpath("//table[contains(@class, 'table')]");
    private By plantsPageHeading = By.xpath("//h3[contains(text(), 'Plants')]");

    private By firstEditButton = By.xpath("//table//tbody/tr[1]//a[contains(@href, '/ui/plants/edit/')]");
    private By firstDeleteButton = By.xpath("//table//tbody/tr[1]//form[contains(@action, '/ui/plants/delete/')]//button");

    private By allEditButtons = By.xpath("//table//tbody//a[contains(@href, '/ui/plants/edit/')]");
    private By allDeleteButtons = By.xpath("//table//tbody//form[contains(@action, '/ui/plants/delete/')]//button");

    private By searchNameField = By.xpath("//input[@name='name']");
    private By categoryFilter = By.xpath("//select[@name='categoryId']");
    private By searchButton = By.xpath("//button[contains(text(), 'Search')]");
    private By resetButton = By.xpath("//a[contains(text(), 'Reset')]");
 
    private By plantNameField = By.id("name");
    private By categoryDropdown = By.id("categoryId");
    private By priceField = By.id("price");
    private By quantityField = By.id("quantity");
    private By saveButton = By.xpath("//button[contains(text(), 'Save')]");
    private By cancelButton = By.xpath("//a[contains(text(), 'Cancel')]");

    private By successMessage = By.xpath("//div[contains(@class, 'alert-success')]");
    private By errorMessage = By.xpath("//div[contains(@class,'text-danger')]");
   
    private By lowStockBadge = By.xpath("//span[contains(@class, 'badge') and contains(text(), 'Low')]");

    private By allTableRows = By.xpath("//table//tbody//tr[td[1][string-length(text())>0]]");

    public PlantsPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPlants() {
        driver.get("http://localhost:8080/ui/plants");
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(plantsTable),
                ExpectedConditions.visibilityOfElementLocated(plantsPageHeading)
            ));
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Page load wait exception: " + e.getMessage());
        }
    }

    public void clickAddPlant() {
        click(addPlantButton);
        waitForPageLoad();
    }

   
    
    public void enterSearchName(String plantName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(searchNameField));
        field.clear();
        field.sendKeys(plantName);
    }
    
    public void selectCategoryFilter(String categoryName) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryFilter));
        new Select(dropdown).selectByVisibleText(categoryName);
    }
    
    public void clickSearchButton() {
        click(searchButton);
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void clickResetButton() {
        click(resetButton);
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public boolean isSearchNameFieldEmpty() {
        try {
            WebElement field = driver.findElement(searchNameField);
            String value = field.getAttribute("value");
            return value == null || value.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isCategoryFilterReset() {
        try {
            WebElement dropdown = driver.findElement(categoryFilter);
            Select select = new Select(dropdown);
            WebElement selected = select.getFirstSelectedOption();
            String value = selected.getAttribute("value");
            return value == null || value.trim().isEmpty() || value.equals("");
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean areAllPlantsFromCategory(String categoryName) {
        try {

            List<WebElement> rows = driver.findElements(allTableRows);
            
            if (rows.isEmpty()) {
                System.out.println("No plants found in table");
                return false;
            }
            
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2) {
                    String category = cells.get(1).getText().trim();
                    if (!category.equalsIgnoreCase(categoryName)) {
                        System.out.println("Found plant with category: " + category + " (expected: " + categoryName + ")");
                        return false;
                    }
                }
            }
            
            System.out.println("All " + rows.size() + " plants belong to category: " + categoryName);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error checking plant categories: " + e.getMessage());
            return false;
        }
    }
    
    public int getDisplayedPlantCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(plantsTable));
            List<WebElement> rows = driver.findElements(allTableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean areEditButtonsVisible() {
        try {
            List<WebElement> editButtons = driver.findElements(allEditButtons);
            return !editButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean areDeleteButtonsVisible() {
        try {
            List<WebElement> deleteButtons = driver.findElements(allDeleteButtons);
            return !deleteButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickEditOnFirstPlant() {
        click(firstEditButton);
        waitForPageLoad();
    }

    public void enterPlantName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(plantNameField));
        field.clear();
        field.sendKeys(name);
    }

    public void selectCategory(String categoryName) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryDropdown));
        new Select(dropdown).selectByVisibleText(categoryName);
    }

    public void enterPrice(String price) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(priceField));
        field.clear();
        field.sendKeys(price);
    }

    public void enterQuantity(String quantity) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityField));
        field.clear();
        field.sendKeys(quantity);
    }

    public void clickSave() {
        click(saveButton);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
            );
            return error.isDisplayed();
        } catch (Exception e) {
            List<WebElement> errors = driver.findElements(errorMessage);
            return !errors.isEmpty();
        }
    }

    public boolean isOnPlantsListPage() {
        try {
            return wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(plantsTable),
                ExpectedConditions.visibilityOfElementLocated(plantsPageHeading)
            )) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantAddedSuccessMessageDisplayed() {
        By addSuccessMessage = By.xpath("//div[contains(@class, 'alert-success')]//span[contains(text(), 'added successfully')]");
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(addSuccessMessage));
            return message.isDisplayed();
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }
    
    public boolean isPlantsTableVisible() {
        return isOnPlantsListPage();
    }

    public boolean isPlantUpdatedInList(String plantName) {
        By plantInList = By.xpath("//table//tbody//td[contains(text(), '" + plantName + "')]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(plantInList)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantPresentInList(String plantName) {
        By plantInList = By.xpath("//table//tbody//td[contains(text(), '" + plantName + "')]");
        try {
            return driver.findElement(plantInList).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantDeletedFromList(String plantName) {
        return !isPlantPresentInList(plantName);
    }

    public boolean isOnSamePage() {
        try {
            return driver.findElement(saveButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    
    public boolean isLowStockBadgeDisplayed(String plantName) {
        By plantRow = By.xpath("//table//tbody//tr[td[contains(text(), '" + plantName + "')]]");
        By lowBadgeInRow = By.xpath(
            "//table//tbody//tr[td[contains(text(), '" + plantName + "')]]" +
            "//span[@class='badge bg-danger ms-2' and contains(text(), 'Low')]"
        );
        
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(plantRow));
            WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(lowBadgeInRow));
            String badgeText = badge.getText().trim();
            System.out.println("✓ Found badge with text: '" + badgeText + "' for plant: " + plantName);
            return badge.isDisplayed();
        } catch (Exception e) {
            System.out.println("Low badge not found for plant: " + plantName);
            System.out.println("   Reason: " + e.getMessage());
            try {
                List<WebElement> allBadges = driver.findElements(
                    By.xpath("//table//tbody//tr[td[contains(text(), '" + plantName + "')]]//span[contains(@class, 'badge')]")
                );
                System.out.println("   Found " + allBadges.size() + " badge(s) in this row");
                for (WebElement b : allBadges) {
                    System.out.println("   - Badge class: " + b.getAttribute("class") + ", text: '" + b.getText() + "'");
                }
            } catch (Exception debugEx) {
                System.out.println("   Could not retrieve debug info");
            }
            return false;
        }
    }


    public void clickDeleteOnFirstPlant() {
        click(firstDeleteButton);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void confirmAlert() {
        try {
            driver.switchTo().alert().accept();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(" No alert found: " + e.getMessage());
        }
    }

    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            System.out.println(" No alert found: " + e.getMessage());
        }
    }

  
    public int getPlantCountWithQuantityLessThan(int threshold) {
        try {
        
            wait.until(ExpectedConditions.presenceOfElementLocated(plantsTable));
            
           
            List<WebElement> rows = driver.findElements(allTableRows);
            
            int lowStockCount = 0;
            
            for (WebElement row : rows) {
                try {
            
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    
                    if (cells.size() >= 4) {
                    
                        WebElement stockCell = cells.get(3);
                    try {
                            WebElement quantitySpan = stockCell.findElement(By.tagName("span"));
                            String quantityText = quantitySpan.getText().trim();
                            
                            int quantity = Integer.parseInt(quantityText);
                            
                            if (quantity < threshold) {
                                lowStockCount++;
                                System.out.println("   Low stock plant found: " + cells.get(0).getText() + " - quantity = " + quantity);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping non-numeric quantity in row");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Could not parse quantity for a row: " + e.getMessage());
                }
            }
            
            System.out.println("   Found " + lowStockCount + " plants with quantity < " + threshold);
            return lowStockCount;
            
        } catch (Exception e) {
            System.out.println("Error counting low stock plants: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int getPlantCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(plantsTable));
            List<WebElement> rows = driver.findElements(allTableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}