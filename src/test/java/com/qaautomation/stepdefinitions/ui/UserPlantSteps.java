package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.PlantsPage;
import com.qaautomation.pages.DashboardPage;
import io.cucumber.java.en.*;
import org.junit.Assert;
import com.qaautomation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import com.qaautomation.utils.DriverFactory;

public class UserPlantSteps {

     private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage( driver);
    private PlantsPage plantsPage = new PlantsPage( driver);
    private DashboardPage dashboardPage = new DashboardPage( driver);
    
    private int initialPlantCount;
    private int filteredPlantCount;
    private int lowStockPlantCount; 
    private int actualLowStockCount;      
    private int dashboardLowStockCount;   
    
    //Background steps
    
    @Given("User is logged in_Plant")
    public void user_is_logged_in() {
         driver.get("http://localhost:8080/ui/login");
        loginPage.loginAsUser();
    
        try {
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Login completed. Current URL: " + currentUrl);
            
        
            Assert.assertFalse("Should not be on login page after login", 
                currentUrl.contains("/login"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Given("Multiple plants exist in different categories")
    public void multiple_plants_exist_in_different_categories() {
        plantsPage.navigateToPlants();
        int plantCount = plantsPage.getPlantCount();
        Assert.assertTrue("Multiple plants should exist (found: " + plantCount + ")", plantCount >= 2);
        System.out.println("Verified " + plantCount + " plants exist in the system");
    }

  
    @Given("User is on the Plants page")
    public void user_is_on_plants_page() {
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            
            Thread.currentThread().interrupt();
        }
        
        System.out.println("DEBUG: Navigating to Plants page...");
        System.out.println("DEBUG: Current URL before navigation: " + DriverFactory.getDriver().getCurrentUrl());
        
        plantsPage.navigateToPlants();
        
        System.out.println("DEBUG: Current URL after navigation: " + DriverFactory.getDriver().getCurrentUrl());
        
  
        int maxRetries = 3;
        boolean pageLoaded = false;
        
        for (int i = 0; i < maxRetries; i++) {
            if (plantsPage.isOnPlantsListPage()) {
                pageLoaded = true;
                System.out.println("Successfully loaded Plants page on attempt " + (i + 1));
                break;
            }
            
            System.out.println("Plants page not loaded, attempt " + (i + 1) + " of " + maxRetries);
            
            try {
                Thread.sleep(1500);
                if (i < maxRetries - 1) {
                    System.out.println("Retrying navigation...");
                    plantsPage.navigateToPlants();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        Assert.assertTrue("Should be on Plants page after " + maxRetries + " attempts", pageLoaded);
    }

    @Given("Plant list displays all plants")
    public void plant_list_displays_all_plants() {
        Assert.assertTrue("Plants table should be visible", plantsPage.isPlantsTableVisible());
        initialPlantCount = plantsPage.getPlantCount();
        
        if (initialPlantCount == 0) {
            System.out.println("WARNING: No plants found.");
        }
        
        System.out.println("✓ Initial plant count: " + initialPlantCount);
    }

    @When("User clicks on the category filter dropdown")
    public void user_clicks_on_category_filter_dropdown() {
        System.out.println("Category filter dropdown is ready");
    }

    @When("User selects category {string}")
    public void user_selects_category(String categoryName) {
        plantsPage.selectCategoryFilter(categoryName);
        System.out.println("Selected category: " + categoryName);
    }

    @When("User clicks the {string} button")
    public void user_clicks_the_button(String buttonText) {
        if (buttonText.equalsIgnoreCase("Search")) {
            plantsPage.clickSearchButton();
        } else if (buttonText.equalsIgnoreCase("Reset")) {
            plantsPage.clickResetButton();
        }
        System.out.println("Clicked " + buttonText + " button");
    }

    @Then("User plant list refreshes")
    public void plant_list_refreshes() {
        Assert.assertTrue("Plants table should be visible after filter", plantsPage.isPlantsTableVisible());
        filteredPlantCount = plantsPage.getPlantCount();
        System.out.println("Plant list refreshed, now showing " + filteredPlantCount + " plants");
    }

    @Then("Only plants from category {string} are displayed")
    public void only_plants_from_category_are_displayed(String categoryName) {
        boolean allFromCategory = plantsPage.areAllPlantsFromCategory(categoryName);
        Assert.assertTrue("All displayed plants should belong to category: " + categoryName, allFromCategory);
    }

    
    
    @When("User enters plant name {string} in search field")
    public void user_enters_plant_name_in_search_field(String plantName) {
        plantsPage.enterSearchName(plantName);
        System.out.println("Entered plant name: " + plantName);
    }

    @When("User selects category {string} from dropdown")
    public void user_selects_category_from_dropdown(String categoryName) {
        plantsPage.selectCategoryFilter(categoryName);
        System.out.println("Selected category from dropdown: " + categoryName);
    }

    @Then("Filtered results are displayed")
    public void filtered_results_are_displayed() {
        Assert.assertTrue("Plants table should be visible", plantsPage.isPlantsTableVisible());
        filteredPlantCount = plantsPage.getPlantCount();
        System.out.println("Filtered results showing " + filteredPlantCount + " plants");
    }

    @Then("Name search field is cleared")
    public void name_search_field_is_cleared() {
        boolean isEmpty = plantsPage.isSearchNameFieldEmpty();
        Assert.assertTrue("Name search field should be empty", isEmpty);
        System.out.println("Name search field is cleared");
    }

    @Then("Category dropdown is reset to default state")
    public void category_dropdown_is_reset_to_default_state() {
        boolean isReset = plantsPage.isCategoryFilterReset();
        Assert.assertTrue("Category dropdown should be reset to default", isReset);
        System.out.println("Category dropdown is reset");
    }

    @Then("Plant list displays all plants without filters")
    public void plant_list_displays_all_plants_without_filters() {
        int currentCount = plantsPage.getPlantCount();
        Assert.assertTrue("All plants should be displayed after reset", currentCount >= initialPlantCount);
        System.out.println("Plant list displays all " + currentCount + " plants");
    }

  
    @Given("At least one plant exists in the system")
    public void at_least_one_plant_exists_in_the_system() {
        int plantCount = plantsPage.getPlantCount();
        Assert.assertTrue("At least one plant should exist (found: " + plantCount + ")", plantCount > 0);
        System.out.println("Verified " + plantCount + " plants exist");
    }

    @When("User observes the Actions column")
    public void user_observes_actions_column() {
        System.out.println("Observing Actions column");
    }

    @Then("Delete button is not visible for any plant")
    public void delete_button_is_not_visible_for_any_plant() {
        boolean deleteButtonsVisible = plantsPage.areDeleteButtonsVisible();
        Assert.assertFalse("Delete buttons should NOT be visible for User role", deleteButtonsVisible);
        System.out.println("Delete buttons are hidden for User role");
    }

    @Then("User cannot access delete functionality")
    public void user_cannot_access_delete_functionality() {
        System.out.println("User cannot access delete functionality");
    }

    @Then("Edit button is not visible for any plant")
    public void edit_button_is_not_visible_for_any_plant() {
        boolean editButtonsVisible = plantsPage.areEditButtonsVisible();
        Assert.assertFalse("Edit buttons should NOT be visible for User role", editButtonsVisible);
        System.out.println("Edit buttons are hidden for User role");
    }

    @Then("User cannot access edit functionality")
    public void user_cannot_access_edit_functionality() {
        System.out.println("User cannot access edit functionality");
    }
 
   @Given("At least one plant with quantity less than {int} exists")
public void at_least_one_plant_with_quantity_less_than_exists(int threshold) {
   
    plantsPage.navigateToPlants();
    
    actualLowStockCount = plantsPage.getPlantCountWithQuantityLessThan(threshold);
    
    Assert.assertTrue(
        "Precondition failed: At least one plant with quantity < " + threshold + " must exist. Found: " + actualLowStockCount,
        actualLowStockCount > 0
    );
    
    System.out.println("Precondition validated: " + actualLowStockCount + " plant(s) with quantity < " + threshold + " exist");
}

    @When("User navigates to dashboard at {string}")
    public void user_navigates_to_dashboard_at(String dashboardPath) {
        dashboardPage.navigateToDashboard();
        Assert.assertTrue("Should be on Dashboard page", dashboardPage.isDashboardDisplayed());
        System.out.println("Navigated to dashboard");
    }

    @Then("Dashboard displays Plant summary card")
    public void dashboard_displays_plant_summary_card() {
        boolean cardDisplayed = dashboardPage.isPlantSummaryCardDisplayed();
        Assert.assertTrue("Plant summary card should be displayed", cardDisplayed);
        System.out.println("Plant summary card is displayed");
    }

    @Then("Low Stock Plants count shows correct number")
    public void low_stock_plants_count_shows_correct_number() {
        boolean sectionDisplayed = dashboardPage.isLowStockSectionDisplayed();
        Assert.assertTrue("Low Stock Plants section should be displayed", sectionDisplayed);
        
        dashboardLowStockCount = dashboardPage.getLowStockPlantCount();
        Assert.assertTrue("Low stock count should be displayed (found: " + dashboardLowStockCount + ")", 
                         dashboardLowStockCount >= 0);
        
        System.out.println("Dashboard shows Low Stock Plants count: " + dashboardLowStockCount);
    }

    @Then("Count matches plants with quantity less than {int}")
    public void count_matches_plants_with_quantity_less_than(int threshold) {
    
        Assert.assertEquals(
            "Dashboard low stock count should match actual plants with quantity < " + threshold,
            actualLowStockCount,
            dashboardLowStockCount
        );
        
    }
    
}