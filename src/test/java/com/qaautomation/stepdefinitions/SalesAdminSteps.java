package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SalesPage;
import com.qaautomation.pages.SellPlantPage;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class SalesAdminSteps {

    LoginPage loginPage = new LoginPage(Hooks.driver);
    SalesPage salesPage = new SalesPage(Hooks.driver);
    SellPlantPage sellPlantPage = new SellPlantPage(Hooks.driver);

    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        loginPage.loginAsAdmin();
    }

    @Given("Admin is on the Sales page")
    public void admin_is_on_sales_page() {
        Hooks.driver.get(ConfigReader.get("app.sales.url"));
    }

    @When("Admin clicks Sell Plant button")
    public void admin_clicks_sell_plant_button() {
        salesPage.clickSellPlantButton();
    }

    @When("Admin leaves the plant dropdown empty")
    public void admin_leaves_plant_empty() {
        // No action needed - just don't select anything
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