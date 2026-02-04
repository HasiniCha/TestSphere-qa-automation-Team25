package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SellPlantPage;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class SalesAdminSteps {

    LoginPage loginPage = new LoginPage(Hooks.driver);
    SellPlantPage sellPlantPage = new SellPlantPage(Hooks.driver);

    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        loginPage.loginAsAdmin();
    }

    @Given("Admin is on the Sell Plant page")
    public void admin_is_on_sell_plant_page() {
        Hooks.driver.get(ConfigReader.get("app.sell.plant.url") );
        
    }

    @When("Admin leaves the plant dropdown empty")
    public void admin_leaves_plant_empty() {
       
    }

    @When("Admin enters quantity {string}")
    public void admin_enters_quantity(String qty) {
        sellPlantPage.enterQuantity(qty);
    }

    @When("Admin clicks the {string} button")
    public void admin_clicks_button(String btn) {
        sellPlantPage.clickSell();
    }

    @Then("Error message {string} is displayed")
    public void error_message_displayed(String expected) {
        Assert.assertEquals(expected, sellPlantPage.getErrorMessage());
    }
}