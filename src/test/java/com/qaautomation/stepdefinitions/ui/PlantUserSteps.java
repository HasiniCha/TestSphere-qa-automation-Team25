package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.plant2.PlantUserPage;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import com.qaautomation.utils.DriverFactory;

public class PlantUserSteps {

    private WebDriver driver = DriverFactory.getDriver();
    private PlantUserPage userPage = new PlantUserPage(driver);
    private LoginPage loginPage = new LoginPage(driver);
    private String baseUrl = "http://localhost:8080/ui";

    @Given("User is logged in as {string}")
    public void user_is_logged_in_as(String userType) {
        driver.get("http://localhost:8080/ui/login");
        if (userType.equalsIgnoreCase("Admin")) {
            loginPage.loginAsAdmin();
        } else {
            loginPage.loginAsTestUser();
        }
    }

    @Given("User is logged in and on Dashboard")
    public void user_is_logged_in_and_on_dashboard() {
        driver.get("http://localhost:8080/ui/login");
        loginPage.loginAsTestUser();
    }

    @Given("User is on the Plant List page {string}")
    public void user_is_on_the_plant_list_page(String path) {
        // --- FIX: Handle the Race Condition ---
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        // 1. Wait briefly to see if the app redirects us to Dashboard first
        try {
            wait.until(ExpectedConditions.urlContains("dashboard"));
        } catch (Exception e) {
            // It's okay if we are not on dashboard, proceed
        }

        // 2. Now force navigation to the target path
        String cleanPath = path.startsWith("/") ? path : "/" + path;
        String targetUrl = baseUrl + cleanPath.replace("/ui", "");
        
        driver.get(targetUrl);
        
        // 3. Verify we actually got there
        try {
            wait.until(ExpectedConditions.urlContains("plants"));
        } catch (Exception e) {
            System.out.println("DEBUG: Forced navigation retrying...");
            driver.get(targetUrl); // Retry once if failed
        }
    }

    @When("User enters {string} in the search input field")
    public void user_enters_in_the_search_input_field(String plantName) {
        userPage.enterSearchText(plantName);
    }

    @When("User clicks the Search button")
    public void user_clicks_the_search_button() {
        userPage.clickSearch();
    }

    @Then("The list shows records matching {string}")
    public void the_list_shows_records_matching(String expectedName) {
        // Added wait to ensure results have time to render
        try { Thread.sleep(500); } catch (InterruptedException e) {} 
        
        String results = userPage.getResultsText();
        Assert.assertTrue("Expected plant '" + expectedName + "' not found. Page text was: " + results, 
            results.contains(expectedName));
    }

    @Then("Plant details are displayed")
    public void plant_details_are_displayed() {
        Assert.assertTrue("Plant table is not displayed", userPage.isTableDisplayed());
    }

    @Then("The plant list displays Name, Category, Price, and Quantity")
    public void the_plant_list_displays_name_category_price_and_quantity() {
        Assert.assertTrue("Table headers are incorrect.", userPage.areHeadersCorrect());
    }

    @Then("Stock status badges are visible")
    public void stock_status_badges_are_visible() {
        Assert.assertTrue("Stock badges not found", userPage.areBadgesVisible());
    }

    @Then("{string} and {string} buttons are not visible")
    public void and_buttons_are_not_visible(String btn1, String btn2) {
        Assert.assertFalse("Admin buttons (Edit/Delete) should NOT be visible", 
            userPage.areActionButtonsVisible());
    }

    @Then("The {string} button is not visible")
    public void the_button_is_not_visible(String btnName) {
        Assert.assertFalse("Add Plant button should NOT be visible for users", 
            userPage.isAddButtonPresent());
    }

    @When("User attempts to navigate to {string} directly")
    public void user_attempts_to_navigate_to_directly(String path) {
        String cleanPath = path.startsWith("/") ? path : "/" + path;
        driver.get(baseUrl + cleanPath.replace("/ui", ""));
    }

    @Then("User is redirected to the Plant List or shows {string}")
    public void user_is_redirected_to_the_plant_list_or_shows(String expectedMessage) {
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();

        boolean isLogin = currentUrl.contains("/login");
        boolean isList = currentUrl.contains("/plants") && !currentUrl.contains("/add");
        boolean isError = pageSource.contains(expectedMessage) || 
                          pageSource.contains("Access Denied") || 
                          pageSource.contains("403");

        Assert.assertTrue("Security check failed! URL: " + currentUrl, isLogin || isList || isError);
    }

    @Then("Only plants matching {string} and {string} are displayed")
    public void only_plants_matching_and_are_displayed(String name, String category) {
        Assert.assertTrue("Filter validation failed", 
            userPage.verifyFilteredResults(name, category));
    }   

    @When("User selects {string} from the category filter dropdown")
    public void user_selects_from_the_category_filter_dropdown(String categoryName) {
    userPage.selectCategory(categoryName);
    }
    
}