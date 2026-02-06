package com.qaautomation.stepdefinitions.ui;

import java.time.Duration;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qaautomation.pages.DashboardPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SidebarNavigation;
import com.qaautomation.pages.sales.PlantsPage;
import com.qaautomation.pages.sales.SalesPage;
import com.qaautomation.pages.sales.SellPlantPage;
import com.qaautomation.utils.ConfigReader;
import com.qaautomation.utils.DriverFactory;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SalesAdminSteps {

  private LoginPage loginPage;
  private DashboardPage dashboardPage;
  private SalesPage salesPage;
  private SellPlantPage sellPlantPage;
  private SidebarNavigation sidenavBar;
  private PlantsPage plantsPage;
  private WebElement rowToDelete;
  private int rowCountBefore; 
  private int initialStock;


  
  private LoginPage getLoginPage() {
    if (loginPage == null) {
      loginPage = new LoginPage(DriverFactory.getDriver());
    }
    return loginPage;
  }

  private DashboardPage getDashboardPage() {
    if (dashboardPage == null) {
      dashboardPage = new DashboardPage(DriverFactory.getDriver());
    }
    return dashboardPage;
  }

  private SalesPage getSalesPage() {
    if (salesPage == null) {
      salesPage = new SalesPage(DriverFactory.getDriver());
    }
    return salesPage;
  }

  private SellPlantPage getSellPlantPage() {
    if (sellPlantPage == null) {
      sellPlantPage = new SellPlantPage(DriverFactory.getDriver());
    }
    return sellPlantPage;
  }

  private SidebarNavigation getSidenavBar() {
    if (sidenavBar == null) {
      sidenavBar = new SidebarNavigation(DriverFactory.getDriver());
    }
    return sidenavBar;
  }

  private PlantsPage getPlantsPage() {
    if (plantsPage == null) {
      plantsPage = new PlantsPage(DriverFactory.getDriver());
    }
    return plantsPage;
  }

  // Background step
  @Given("Admin is logged in and on Dashboard")
  public void admin_is_logged_in_and_on_dashboard() {
    DriverFactory.getDriver().get(ConfigReader.get("app.url"));
    getLoginPage().loginAsAdmin();
    Assert.assertTrue(
      "Should be on Dashboard",
      getDashboardPage().isOnDashboard()
    );
  }

  @When("Admin clicks View Sales")
  public void admin_clicks_view_sales() {
    getDashboardPage().clickViewSalesButton();
  }

  @Then("Admin should be on the Sales page")
  public void admin_should_be_on_the_sales_page() {
    String currentUrl = DriverFactory.getDriver().getCurrentUrl();
    Assert.assertTrue("Should be on Sales page", currentUrl.contains("/sales"));
  }

  @When("Admin clicks Sell Plant button")
  public void admin_clicks_sell_plant_button() {
    getSalesPage().clickSellPlantButton();
  }

  @Then("Admin should be on the Sell Plant page")
  public void admin_should_be_on_sell_plant_page() {
    Assert.assertTrue(
      "Should be on Sell Plant page",
      getSellPlantPage().isOnSellPlantPage()
    );
  }

  @When("Admin leaves the plant dropdown empty")
  public void admin_leaves_plant_empty() {}

  @When("Admin enters quantity {string}")
  public void admin_enters_quantity(String qty) {
    getSellPlantPage().enterQuantity(qty);
  }

  @When("Admin clicks the {string} button")
  public void admin_clicks_button(String btn) {
    getSellPlantPage().clickSell();
  }

  // TC-001: validation error
  @Then("Error message {string} is displayed")
  public void error_message_displayed(String expected) {
    String actualMessage = getSellPlantPage().getErrorMessage();
    Assert.assertEquals("Error message text mismatch", expected, actualMessage);
  }

  @When("Admin selects plant {string} from dropdown")
public void admin_selects_plant(String dropdownText) {
    
    getSellPlantPage().selectPlant(dropdownText);
    String plantNameOnly = dropdownText.split(" \\(Stock:")[0];
    this.initialStock = getSellPlantPage().getPlantStockFromDropdown(dropdownText);
    
    System.out.println("Parsed Name: " + plantNameOnly);
    System.out.println("Parsed Stock: " + initialStock);
}

  @Then("Quantity validation message is displayed")
  public void quantity_validation_message_displayed() {
    Assert.assertTrue(
      "HTML5 quantity validation message should be displayed",
      getSellPlantPage().hasQuantityValidationMessage()
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
      getSalesPage().isOnSalesPage()
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
      getSalesPage().verifySaleExists(plantName, quantity)
    );
  }

  @When("Admin navigates to Plants page")
  public void admin_navigates_to_plants_page() {
    getSidenavBar().clickPlants();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


 @Then("Stock of plant {string} should be reduced by {int}")
  public void stock_of_plant_should_be_reduced_by(String plantName, int qtySold) {
    int actualStock = getPlantsPage().getPlantStockAsInt(plantName);
    int expectedStock = initialStock - qtySold;

    System.out.println("Expected stock after sale: " + expectedStock);
    System.out.println("Actual stock after sale: " + actualStock);

    Assert.assertEquals(
      "Stock for " + plantName + " did not reduce correctly!",
      expectedStock,
      actualStock
    );
  }
  

  //delete
  @Given("at least one sales record exists")
  public void at_least_one_sales_record_exists() {
    rowCountBefore = getSalesPage().getSalesRowCount();
    System.out.println("Initial row count BEFORE deletion: " + rowCountBefore);

    Assert.assertTrue(
      "Pre-condition failed: No sales records found to delete!",
      rowCountBefore > 0
    );
  }

  @When("Admin clicks Delete icon on action column")
  public void admin_clicks_delete_icon_on_action_column() {
    rowToDelete = getSalesPage().deleteSale();
  }

   @Then("success message is displayed")
  public void success_message_is_displayed() {
    Assert.assertTrue(
      "Success message was not visible after deletion!",
      getSalesPage().isSuccessMessageDisplayed()
    );
  }

  @And("record is deleted from the list")
  public void record_is_deleted_from_the_list() {
    new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
      .until(ExpectedConditions.stalenessOf(rowToDelete));

    System.out.println(
      "Verified: The specific row element has been removed from the page."
    );
  }

 
}
