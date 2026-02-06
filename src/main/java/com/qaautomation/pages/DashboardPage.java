package com.qaautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    private By dashboardHeading = By.xpath("//h3[contains(text(), 'Dashboard')]");

    private By plantSummaryCard = By.xpath("//div[contains(@class, 'card')]//h6[contains(text(), 'Plants')]");

    private By lowStockCountValue = By.xpath("//h6[contains(text(), 'Plants')]/ancestor::div[contains(@class, 'card')]//div[text()='Low Stock']/preceding-sibling::div[@class='fw-bold fs-5']");

    private By lowStockNumber = By.xpath("//div[@class='small text-muted' and text()='Low Stock']/preceding-sibling::div[@class='fw-bold fs-5']");

    private By totalPlantsCount = By.xpath("//h6[contains(text(), 'Plants')]/ancestor::div[contains(@class, 'card')]//div[text()='Total']/preceding-sibling::div[@class='fw-bold fs-5']");
    private By manageCategoriesButton = By.xpath("//a[contains(normalize-space(), 'Manage Categories')]");
    private By managePlantButton = By.xpath("//a[contains(normalize-space(), 'Manage Plants')]");
    private By viewSalesButton = By.xpath("//a[contains(normalize-space(), 'View Sales')]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToDashboard() {
        driver.get("http://localhost:8080/ui/dashboard");
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading));
            Thread.sleep(500); 
        } catch (Exception e) {
            System.out.println("Dashboard page load exception: " + e.getMessage());
        }
    }

    
    public boolean isDashboardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPlantSummaryCardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(plantSummaryCard)).isDisplayed();
        } catch (Exception e) {
            System.out.println("Plant Summary card not found");
            return false;
        }
    }
    
   
    public int getLowStockPlantCount() {
        try {
            
            try {
                WebElement countElement = driver.findElement(lowStockNumber);
                String countText = countElement.getText().trim();
                return extractNumber(countText);
            } catch (Exception e1) {
                System.out.println("Strategy 1 (direct path) failed, trying alternative...");
            }
            
          
            try {
                WebElement numberElement = driver.findElement(lowStockCountValue);
                String numberText = numberElement.getText().trim();
                return extractNumber(numberText);
            } catch (Exception e2) {
                System.out.println("Strategy 2 (from card) failed");
            }
            
            System.out.println("Could not find low stock count with any strategy");
            return -1;
            
        } catch (Exception e) {
            System.out.println("Error getting low stock count: " + e.getMessage());
            return -1;
        }
    }
   
    public boolean verifyLowStockCount(int expectedCount) {
        int actualCount = getLowStockPlantCount();
        
        if (actualCount == -1) {
            System.out.println("Could not retrieve low stock count from dashboard");
            return false;
        }
        
        boolean matches = actualCount == expectedCount;
        
        if (matches) {
            System.out.println("Low stock count matches: " + actualCount + " (expected: " + expectedCount + ")");
        } else {
            System.out.println("Low stock count mismatch: " + actualCount + " (expected: " + expectedCount + ")");
        }
        
        return matches;
    }
    
   
    public boolean isLowStockSectionDisplayed() {
        try {
          
            By lowStockLabel = By.xpath("//div[@class='small text-muted' and text()='Low Stock']");
            WebElement section = wait.until(ExpectedConditions.presenceOfElementLocated(lowStockLabel));
            return section.isDisplayed();
        } catch (Exception e) {
            System.out.println("Low Stock section not found on dashboard");
            return false;
        }
    }
    
    
    public int getTotalPlantsCount() {
        try {
            WebElement countElement = driver.findElement(totalPlantsCount);
            String countText = countElement.getText().trim();
            return extractNumber(countText);
        } catch (Exception e) {
            System.out.println("Could not get total plants count: " + e.getMessage());
            return -1;
        }
    }
    
    private int extractNumber(String text) {
        try {
            String digits = text.replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                return Integer.parseInt(digits);
            }
        } catch (Exception e) {
            System.out.println(" Could not extract number from: '" + text + "'");
        }
        return -1;
    }
   
    public void printDashboardStructure() {
        try {
     
            WebElement card = driver.findElement(plantSummaryCard);
            System.out.println("Plant Summary Card HTML:");
            System.out.println(card.getAttribute("outerHTML"));

        } catch (Exception e) {
            System.out.println("Could not print dashboard structure: " + e.getMessage());
        }
    }
    public void clickManageCategoriesButton() {
        click(manageCategoriesButton);
    }

    public void clickManagePlantButton() {
        click(managePlantButton);
    }

   public void clickViewSalesButton() {
    wait.until(ExpectedConditions.elementToBeClickable(viewSalesButton)).click();
}



public boolean isOnDashboard() {
    try {
        // Wait up to 10 seconds for the URL to change
        return wait.until(ExpectedConditions.urlContains("/dashboard"));
    } catch (Exception e) {
        // If it times out, it means we never reached the dashboard
        return false;
    }
}

    
}