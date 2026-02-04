package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.DashboardPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.PlantsPage;
import com.qaautomation.pages.SalesPage;
import com.qaautomation.pages.SellPlantPage;
import com.qaautomation.pages.SidebarNavigation;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SalesAdminSteps {

  LoginPage loginPage = new LoginPage(Hooks.driver);
  DashboardPage dashboardPage = new DashboardPage(Hooks.driver);
  SalesPage salesPage = new SalesPage(Hooks.driver);
  SellPlantPage sellPlantPage = new SellPlantPage(Hooks.driver);
  SidebarNavigation sidenavBar = new SidebarNavigation(Hooks.driver);
  PlantsPage plantsPage = new PlantsPage(Hooks.driver);
  private String deletedSaleName;

  // Background step
  @Given("User is logged in and on Dashboard")
  public void user_is_logged_in_and_on_dashboard() {
    Hooks.driver.get(ConfigReader.get("app.url"));
    loginPage.loginAsAdmin();
    Assert.assertTrue("Should be on Dashboard", dashboardPage.isOnDashboard());
  }

  //Sales admin steps
  @When("Admin clicks View Sales")
  public void admin_clicks_view_sales() {
    dashboardPage.clickViewSalesButton();
  }

  @Then("Admin should be on the Sales page")
  public void admin_should_be_on_the_sales_page() {
    String currentUrl = Hooks.driver.getCurrentUrl();
    Assert.assertTrue("Should be on Sales page", currentUrl.contains("/sales"));
  }

  @When("Admin clicks Sell Plant button")
  public void admin_clicks_sell_plant_button() {
    salesPage.clickSellPlantButton();
  }

  @Then("Admin should be on the Sell Plant page")
  public void admin_should_be_on_sell_plant_page() {
    Assert.assertTrue(
      "Should be on Sell Plant page",
      sellPlantPage.isOnSellPlantPage()
    );
  }

  @When("Admin leaves the plant dropdown empty")
  public void admin_leaves_plant_empty() {
    // Do nothing - dropdown stays empty
  }

  @When("Admin enters quantity {string}")
  public void admin_enters_quantity(String qty) {
    sellPlantPage.enterQuantity(qty);
  }

  @When("Admin clicks the {string} button")
  public void admin_clicks_button(String btn) {
    sellPlantPage.clickSell();
  }

  // TC-001: validation error
  @Then("Error message {string} is displayed")
  public void error_message_displayed(String expected) {
    String actualMessage = sellPlantPage.getErrorMessage();
    Assert.assertEquals("Error message text mismatch", expected, actualMessage);
  }

  // TC-002: validation error
  @When("Admin selects plant {string} from dropdown")
  public void admin_selects_plant(String plant) {
    sellPlantPage.selectPlant(plant);
  }

  @Then("Quantity validation message is displayed")
  public void quantity_validation_message_displayed() {
    Assert.assertTrue(
      "HTML5 quantity validation message should be displayed",
      sellPlantPage.hasQuantityValidationMessage()
    );
  }

  @Then("Admin should be redirected to the Sales page")
  public void admin_should_be_redirected_to_the_sales_page() {
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Assert.assertTrue(
      "Should be redirected to Sales page after successful sale",
      salesPage.isOnSalesPage()
    );
  }

  @Then(
    "New sale should appear in the sales list with plant {string} and quantity {string}"
  )
  public void new_sale_should_appear_in_sales_list_with_plant_and_quantity(
    String plantName,
    String quantity
  ) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Assert.assertTrue(
      "Sale with plant '" +
      plantName +
      "' and quantity '" +
      quantity +
      "' should appear in sales list",
      salesPage.verifySaleExists(plantName, quantity)
    );
  }

  @When("Admin navigates to Plants page")
  public void admin_navigates_to_plants_page() {
    sidenavBar.clickPlants();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Then("Stock of plant {string} should be reduced to {string}")
  public void stock_of_plant_should_be_reduced_to(
    String plantName,
    String expectedStock
  ) {
    Assert.assertTrue("Should be on Plants page", plantsPage.isOnPlantsPage());

    String actualStock = plantsPage.getPlantStock(plantName);
    
    // Extract numeric value only (remove " Low", " Medium", etc.)
    String numericStock = actualStock.split(" ")[0].trim();
    
    Assert.assertEquals(
      "Stock for plant '" + plantName + "' should be reduced correctly",
      expectedStock,
      numericStock
    );
  }

  //delete

  @Given("at least one sales record exists")
  public void at_least_one_sales_record_exists() {
    Hooks.driver.get(ConfigReader.get("app.url"));
    loginPage.loginAsAdmin();
    dashboardPage.clickViewSalesButton();
    Assert.assertTrue(
      "Sales page should have at least one record",
      salesPage.getLatestSaleRow() != null
    );
  }

@When("Admin clicks Delete icon on action column")
public void admin_clicks_delete_icon_on_action_column() {
    WebElement latestRow = salesPage.getLatestSaleRow();
    // Capture the name BEFORE deleting
    deletedSaleName = latestRow.findElement(By.xpath("./td[1]")).getText(); 
    salesPage.deleteSale();
}



@Then("success message is displayed")
  public void success_message_is_displayed() {
    Assert.assertTrue(
      "Success message should be displayed after deleting a sale",
      salesPage.isSuccessMessageDisplayed()
    );
  }

@And("record is deleted from the list")
public void record_is_deleted_from_the_list() {
    // Use the captured name from before deletion
    Assert.assertTrue(
        "Sale '" + deletedSaleName + "' should be deleted", 
        salesPage.isSaleDeleted(deletedSaleName)
    );
}

    // TC-005: Sorting test
    @When("Admin clicks on {string} column header")
    public void admin_clicks_on_column_header(String columnName) {
        salesPage.clickColumnHeader(columnName);
    }

    @Then("Sales records should be sorted by {string} in ascending order")
    public void sales_records_should_be_sorted_in_ascending_order(String columnName) {
        List<String> columnValues = salesPage.getColumnValues(columnName);
        
        Assert.assertTrue(
            "Sales records should be sorted by '" + columnName + "' in ascending order",
            salesPage.isSortedAscending(columnValues)
        );
    }

    @When("Admin clicks on {string} column header again")
    public void admin_clicks_on_column_header_again(String columnName) {
        salesPage.clickColumnHeader(columnName);
    }

    @Then("Sales records should be sorted by {string} in descending order")
    public void sales_records_should_be_sorted_in_descending_order(String columnName) {
        List<String> columnValues = salesPage.getColumnValues(columnName);
        
        Assert.assertTrue(
            "Sales records should be sorted by '" + columnName + "' in descending order",
            salesPage.isSortedDescending(columnValues)
        );
    }


  }

