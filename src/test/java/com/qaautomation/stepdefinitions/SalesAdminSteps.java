package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.DashboardPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SalesPage;
import com.qaautomation.pages.SellPlantPage;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class SalesAdminSteps {

    LoginPage loginPage = new LoginPage(Hooks.driver);
    DashboardPage dashboardPage = new DashboardPage(Hooks.driver);
    SalesPage salesPage = new SalesPage(Hooks.driver);
    SellPlantPage sellPlantPage = new SellPlantPage(Hooks.driver);

    // Background step
    @Given("User is logged in and on Dashboard")
    public void user_is_logged_in_and_on_dashboard() {
        
        Hooks.driver.get(ConfigReader.get("app.url"));
        
        loginPage.loginAsAdmin();
       
        Assert.assertTrue("Should be on Dashboard", dashboardPage.isOnDashboard());
    }

    @When("Admin clicks View Sales")
    public void admin_clicks_view_sales() {
        dashboardPage.clickViewSalesButton();
    }

    @Then("Admin should be on the Sales page")
    public void admin_should_be_on_the_sales_page() {
        String currentUrl = Hooks.driver.getCurrentUrl();
        Assert.assertTrue("Should be on Sales page", 
                         currentUrl.contains("/sales"));
    }

    @When("Admin clicks Sell Plant button")
    public void admin_clicks_sell_plant_button() {
        salesPage.clickSellPlantButton();
    }

    @Then("Admin should be on the Sell Plant page")
    public void admin_should_be_on_sell_plant_page() {
        Assert.assertTrue("Should be on Sell Plant page", 
                         sellPlantPage.isOnSellPlantPage());
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
        Assert.assertEquals("Error message text mismatch", 
                           expected, sellPlantPage.getErrorMessage());
    }
}