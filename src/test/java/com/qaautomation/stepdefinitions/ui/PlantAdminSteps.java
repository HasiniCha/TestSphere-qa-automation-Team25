package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.plant2.PlantPageAdmin;
import com.qaautomation.utils.DriverFactory;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class PlantAdminSteps {
    
    private PlantPageAdmin plantPage = new PlantPageAdmin(DriverFactory.getDriver());
    private LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    private String testPlantName;

    // ================= BACKGROUND / LOGIN =================

    @Given("User is logged in as Admin")
    public void user_is_logged_in_as_admin() {
        DriverFactory.getDriver().get("http://localhost:8080/ui/login");
        loginPage.loginAsAdmin();
    }

    @Given("Dashboard is displayed")
    public void dashboard_is_displayed() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        String url = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue("Login failed. URL: " + url, 
            url.contains("/ui") || url.contains("/plants") || url.contains("/home"));
    }

    // ================= NAVIGATION =================

    @Given("User is on the Plant List page")
    @Given("User navigates to the Plant List page")
    public void user_navigates_to_the_plant_list_page() {
        plantPage.navigateToPlantList(); 
    }

    @Given("User navigates to Plant Add page")
    public void user_navigates_to_plant_add_page() {
        DriverFactory.getDriver().get("http://localhost:8080/ui/plants/add");
    }

    @Given("User navigates to Plant edit page")
    public void user_navigates_to_plant_edit_page() {
        DriverFactory.getDriver().get("http://localhost:8080/ui/plants/edit/110");
    }

    // ================= ACTIONS =================

    @Given("User clicks the Edit button for the plant {string}")
    public void user_clicks_the_edit_button_for_the_plant(String plantName) {
        plantPage.clickEditForPlant(plantName);
    }

    @When("User clicks the {string} button")
    public void user_clicks_the_button(String button) {
        if (button.equalsIgnoreCase("Add a Plant")) {
            plantPage.clickAddPlant();
        }
    }

    @When("User enters {string} in the Name field")
    public void user_enters_name(String name) {
        testPlantName = name;
        plantPage.enterPlantName(name);
    }

    @When("User selects {string} from the Category dropdown")
    public void user_selects_category(String category) {
        plantPage.selectCategory(category);
    }

    @When("User enters {string} in the Price field")
    public void user_enters_price(String price) {
        plantPage.enterPrice(price);
    }

    @When("User enters {string} in the Quantity field")
    public void user_enters_quantity(String quantity) {
        plantPage.enterQuantity(quantity);
    }

    @When("User clicks the Save button")
    public void user_clicks_save_button() {
        plantPage.clickSave();
    }

    // ================= ASSERTIONS =================

    @Then("User is redirected to the Plant List page")
    public void user_is_redirected_to_the_plant_list_page() {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        Assert.assertTrue("Not redirected to list page. Current: " + DriverFactory.getDriver().getCurrentUrl(),
                DriverFactory.getDriver().getCurrentUrl().contains("/ui/plants"));
    }

    @Then("The new plant {string} appears in the list")
    public void the_new_plant_appears_in_the_list(String plantName) {
        Assert.assertTrue("Plant '" + plantName + "' NOT found in table.", 
                plantPage.isPlantPresentInList(plantName));
    }

    @Then("The plant {string} should show a quantity of {string} in the list")
    public void the_plant_should_show_a_quantity_of_in_the_list(String plantName, String expectedQty) {
        String actualQty = plantPage.getPlantQuantityFromList(plantName);
        Assert.assertEquals("Quantity mismatch in the list!", expectedQty, actualQty);
    }

    @Then("The plant is not saved")
    @Then("User remains on the same page")
    public void user_remains_on_the_same_page() {
        String url = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue("User unexpectedly navigated away. Current: " + url,
                url.contains("/add") || url.contains("/edit"));
    }

    @Then("Error message {string} is displayed below the Name field")
    public void error_message_is_displayed_below_the_name_field(String message) {
        Assert.assertTrue("Expected error '" + message + "' not displayed.", 
                plantPage.isNameValidationErrorDisplayed(message));
    }

    @Then("Error message is displayed")
    public void error_message_is_displayed() {
        Assert.assertTrue("General error message not displayed.", 
                plantPage.isValidationErrorDisplayed());
    }

    @Then("The plant {string} does not show a {string} badge")
    public void the_plant_does_not_show_a_badge(String name, String badgeType) {
        boolean isBadgeVisible = plantPage.isLowStockBadgeDisplayed(name);
        Assert.assertFalse("Plant '" + name + "' incorrectly shows a Low Stock badge!", isBadgeVisible);
    }
}