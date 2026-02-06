package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.PlantsPage;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import com.qaautomation.utils.DriverFactory;

public class AdminPlantSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private LoginPage loginPage = new LoginPage(driver);
    private PlantsPage plantsPage = new PlantsPage(driver);
    private String testPlantName = "Test Plant " + System.currentTimeMillis();


    @Given("Admin is logged in_Plant")
    public void admin_is_logged_in() {
        driver.get("http://localhost:8080/ui/login");
        loginPage.loginAsAdmin();
        plantsPage.navigateToPlants();
    }

    @Given("Admin is on the Plants page")
    public void admin_is_on_plants_page() {
        plantsPage.navigateToPlants();
        Assert.assertTrue(plantsPage.isPlantsTableVisible());
    }

    @Given("A plant exists in the system")
    public void a_plant_exists_in_system() {
        Assert.assertTrue(plantsPage.getPlantCount() > 0);
    }


    @When("Admin clicks edit icon on an existing plant")
    public void admin_clicks_edit_icon() {
        plantsPage.clickEditOnFirstPlant();
    }

    @When("Admin updates plant name to {string}")
    public void admin_updates_plant_name(String name) {
        testPlantName = name;
        plantsPage.enterPlantName(name);
    }

    @When("Admin changes category to {string}")
    public void admin_changes_category(String category) {
        plantsPage.selectCategory(category);
    }

    @When("Admin updates price to {string}")
    public void admin_updates_price(String price) {
        plantsPage.enterPrice(price);
    }

    @When("Admin updates quantity to {string}")
    public void admin_updates_quantity(String quantity) {
        plantsPage.enterQuantity(quantity);
    }

    @When("Admin clicks the {string} button_Plant")
    public void admin_clicks_button(String buttonName) {
        if (buttonName.equalsIgnoreCase("Save")) {
            plantsPage.clickSave();
        }
    }

    @Then("Success message is displayed")
    public void success_message_is_displayed() {
        Assert.assertTrue(plantsPage.isSuccessMessageDisplayed());
    }

    @Then("Admin is redirected to plants page")
    public void admin_redirected_to_plants_page() {
        Assert.assertTrue(plantsPage.isPlantsTableVisible());
    }

    @Then("All fields are updated in plant list")
    public void all_fields_updated_in_list() {
        Assert.assertTrue(plantsPage.isPlantUpdatedInList(testPlantName));
    }

    @Then("Changes are saved to database")
    public void changes_saved_to_database() {
        DriverFactory.getDriver().navigate().refresh();
        Assert.assertTrue(plantsPage.isPlantUpdatedInList(testPlantName));
    }


    @When("Admin navigates to add plant page")
    public void admin_navigates_to_add_plant_page() {
        plantsPage.clickAddPlant();
    }

    @When("Admin enters plant name {string}")
    public void admin_enters_plant_name(String name) {
        testPlantName = name;
        plantsPage.enterPlantName(name);
    }

    @When("Admin selects category {string}")
    public void admin_selects_category(String category) {
        plantsPage.selectCategory(category);
    }

    @When("Admin enters price {string}")
    public void admin_enters_price(String price) {
        plantsPage.enterPrice(price);
    }

    @When("Admin enters quantity {string}_Plant")
    public void admin_enters_quantity(String quantity) {
        plantsPage.enterQuantity(quantity);
    }

    @Then("Error message is displayed")
    public void error_message_is_displayed() {
        Assert.assertTrue("Error message should be visible", plantsPage.isErrorMessageDisplayed());
    }

    @Then("Admin remains on same page")
    public void admin_remains_on_same_page() {
        Assert.assertTrue(plantsPage.isOnSamePage());
    }

    @Then("Plant is not saved")
    public void plant_is_not_saved() {
        plantsPage.navigateToPlants();
        Assert.assertFalse(plantsPage.isPlantPresentInList(testPlantName));
    }


    @When("Admin clicks delete icon on a plant")
    public void admin_clicks_delete_icon() {
        plantsPage.clickDeleteOnFirstPlant();
    }

    @When("Admin clicks {string} on confirmation prompt")
    public void admin_clicks_on_confirmation(String action) {
        if (action.equalsIgnoreCase("Ok")) {
            plantsPage.confirmAlert();
        } else {
            plantsPage.dismissAlert();
        }
    }

    @Then("Plant is deleted from list")
    public void plant_is_deleted_from_list() {
        Assert.assertTrue(plantsPage.isPlantDeletedFromList(testPlantName));
    }

    @Then("Plant list refreshes")
    public void plant_list_refreshes() {
        Assert.assertTrue(plantsPage.isPlantsTableVisible());
    }


    @When("Admin navigates to plants list page")
    public void admin_navigates_to_plants_list_page() {
        plantsPage.navigateToPlants();
    }

  @Then("Plant is saved successfully")
public void plant_is_saved_successfully() {

    Assert.assertTrue("Plant added successfully message should be displayed", 
        plantsPage.isPlantAddedSuccessMessageDisplayed());
}

   @Then("{string} badge is displayed for the plant")
public void badge_is_displayed(String badgeType) {
    Assert.assertTrue(plantsPage.isLowStockBadgeDisplayed(testPlantName));
}

}