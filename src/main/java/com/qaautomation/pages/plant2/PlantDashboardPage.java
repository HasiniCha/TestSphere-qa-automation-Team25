package com.qaautomation.pages.plant2;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class PlantDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---
    // Finds the card containing "Plant" or "Plants"
    private By plantCard = By.xpath("//div[contains(@class, 'card') or contains(@class, 'info-box')][contains(.,'Plant')]");    
    
    private By countText = By.xpath("//div[contains(@class, 'card')][contains(.,'Plant')]//h3 | //div[contains(@class, 'card')][contains(.,'Plant')]//span");

    public PlantDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // --- METHOD 1: Check visibility ---
    public boolean isPlantCardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(plantCard)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- METHOD 2: Get the count ---
    public int getDashboardCount() {
        try {
            // Wait for the card to be visible
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(plantCard));
            String text = element.getText();
            
            // Extract numbers from the text (e.g., "Total Plants: 25" -> "25")
            String numberOnly = text.replaceAll("[^0-9]", "");
            
            if(numberOnly.isEmpty()) return 0; 
            return Integer.parseInt(numberOnly);
        } catch (Exception e) {
            System.out.println("DEBUG: Could not read dashboard count.");
            return -1;
        }
    }
}