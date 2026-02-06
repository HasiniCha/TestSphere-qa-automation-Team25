package com.qaautomation.stepdefinitions.ui;

import com.qaautomation.pages.DashboardPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SidebarNavigation;
import com.qaautomation.pages.sales.SalesPage;
import com.qaautomation.utils.ConfigReader;
import com.qaautomation.utils.DriverFactory;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class SalesUserSteps {

  private LoginPage loginPage;
  private DashboardPage dashboardPage;
  private SalesPage salesPage;
  private SidebarNavigation sidenav;

  // Lazy initialization getters
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

  private SidebarNavigation getSidenav() {
    if (sidenav == null) {
      sidenav = new SidebarNavigation(DriverFactory.getDriver());
    }
    return sidenav;
  }

  @Given("Sale User is logged in and on Dashboard")
  public void user_is_logged_in_and_on_dashboard() {
    DriverFactory.getDriver().get(ConfigReader.get("app.url"));
    getLoginPage().loginAsUser();
    Assert.assertTrue("Should be on Dashboard", getDashboardPage().isOnDashboard());
  }

  @When("User clicks View Sales")
  public void user_clicks_view_sales() {
    getDashboardPage().clickViewSalesButton();
  }

  @Then("User should be on the Sales page")
  public void user_should_be_on_the_sales_page() {
    String currentUrl = DriverFactory.getDriver().getCurrentUrl();
    Assert.assertTrue("Should be on Sales page", currentUrl.contains("/sales"));
  }

  @Then("Sales records should be displayed with columns")
  public void sales_records_should_be_displayed_with_columns() {
    Assert.assertTrue(
      "Sales table should be visible",
      getSalesPage().isSalesTableDisplayed()
    );
  }

  @Then("{string} button should NOT be visible")
  public void button_should_not_be_visible(String buttonName) {
    Assert.assertFalse(
      "Sell Plant button should be hidden for standard users",
      getSalesPage().isSellPlantButtonVisible()
    );
  }

  @Then("{string} icon should NOT be visible on any sales record")
  public void icon_should_not_be_visible_on_any_sales_record(String iconType) {
    Assert.assertFalse(
      "Delete icons should be hidden for standard users",
      getSalesPage().isDeleteButtonVisible()
    );
  }

  @Given("User is on the login page")
  public void user_is_on_the_login_page() {
    DriverFactory.getDriver().get(ConfigReader.get("app.url")); 
  }

  @When("User enters valid non-admin username")
  public void user_enters_valid_non_admin_username() {
    getLoginPage().enterUsername(ConfigReader.get("app.user.username"));
  }

  @When("User enters valid password")
  public void user_enters_valid_password() {
    getLoginPage().enterPassword(ConfigReader.get("app.user.password"));
  }

  @When("User clicks Login button")
  public void user_clicks_login_button() {
    getLoginPage().clickLogin();
  }

  @Then("User should be redirected to the Dashboard")
  public void redirected_to_user_dashboard() {
    Assert.assertTrue(
      "Not on the Dashboard page!",
      getDashboardPage().isOnDashboard()
    );
  }

  @Then("Navigation menu highlights the active page")
  public void navigation_menu_highlights_the_active_page() {
    Assert.assertTrue(
      "The Dashboard menu item should have the 'active' class",
      getSidenav().isMenuItemActive("Dashboard")
    );
  }

  @When("User enters invalid non-admin username {string}")
  public void user_enters_invalid_username(String user) {
    getLoginPage().enterUsername(user);
  }

  @When("User enters invalid password {string}")
  public void user_enters_invalid_password(String pass) {
    getLoginPage().enterPassword(pass);
  }

  @Then("Login should fail")
  public void login_should_fail() {
    Assert.assertFalse("Login should have failed, but dashboard was reached!", 
        getDashboardPage().isOnDashboard());
  }

  @Then("Error message {string} is displayed in red")
  public void error_message_is_displayed_in_red(String expectedMessage) {
    String actualMessage = getLoginPage().getErrorMessageText();
    String color = getLoginPage().getErrorMessageColor(); 
    
    Assert.assertTrue("Error text mismatch! Expected to contain: " + expectedMessage, 
        actualMessage.contains(expectedMessage));
    
    String numbersOnly = color.replace("rgba(", "").replace("rgb(", "").replace(")", "");
    String[] rbgValues = numbersOnly.split(",");
    
    int r = Integer.parseInt(rbgValues[0].trim());
    int g = Integer.parseInt(rbgValues[1].trim());
    int b = Integer.parseInt(rbgValues[2].trim());

    System.out.println("DEBUG: Computed Color - R:" + r + " G:" + g + " B:" + b);

    Assert.assertTrue("Error message color is not red! RGB found: " + color, 
        r > g && r > b);
  }

  @Then("User should remain on the login page")
  public void user_should_remain_on_the_login_page() {
    Assert.assertTrue("User was redirected away from login page!", 
        DriverFactory.getDriver().getCurrentUrl().contains("/login"));
  }

  @Given("User is on the Sales page")
  public void user_is_on_the_sales_page() {
    getDashboardPage().clickViewSalesButton();
    Assert.assertTrue("Not on Sales page", getSalesPage().isOnSalesPage());
  }

  @When("User scrolls to the bottom of the page")
  public void user_scrolls_to_the_bottom_of_the_page() {
    getSalesPage().scrollToBottom();
  }

  @When("User clicks on {string} page button")
  public void user_clicks_on_page_button(String buttonType) {
    if (buttonType.equalsIgnoreCase("Next")) {
      getSalesPage().clickNextPage();
    } else {
      getSalesPage().clickPageTwo();
    }
  }

  @Then("The next set of sales records should be displayed")
  public void the_next_set_of_sales_records_should_be_displayed() {
    String activePage = getSalesPage().getActivePageText();
    Assert.assertEquals("The active page should be 2", "2", activePage);
  }
}