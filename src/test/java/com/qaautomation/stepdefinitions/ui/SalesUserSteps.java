package com.qaautomation.stepdefinitions.ui;

import com.qaautomation.pages.DashboardPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.pages.SalesPage;
import com.qaautomation.pages.SidebarNavigation;
import com.qaautomation.stepdefinitions.Hooks;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class SalesUserSteps {

  LoginPage loginPage = new LoginPage(Hooks.driver);
  DashboardPage dashboardPage = new DashboardPage(Hooks.driver);
  SalesPage salesPage = new SalesPage(Hooks.driver);
  SidebarNavigation sidenav = new SidebarNavigation(Hooks.driver);

  @Given("User is logged in and on Dashboard")
  public void user_is_logged_in_and_on_dashboard() {
    Hooks.driver.get(ConfigReader.get("app.url"));
    loginPage.loginAsUser();
    Assert.assertTrue("Should be on Dashboard", dashboardPage.isOnDashboard());
  }

  @When("User clicks View Sales")
  public void user_clicks_view_sales() {
    dashboardPage.clickViewSalesButton();
  }

  @Then("User should be on the Sales page")
  public void user_should_be_on_the_sales_page() {
    String currentUrl = Hooks.driver.getCurrentUrl();
    Assert.assertTrue("Should be on Sales page", currentUrl.contains("/sales"));
  }

  @Then("Sales records should be displayed with columns")
  public void sales_records_should_be_displayed_with_columns() {
    Assert.assertTrue(
      "Sales table should be visible",
      salesPage.isSalesTableDisplayed()
    );
  }

  @Then("{string} button should NOT be visible")
  public void button_should_not_be_visible(String buttonName) {
    Assert.assertFalse(
      "Sell Plant button should be hidden for standard users",
      salesPage.isSellPlantButtonVisible()
    );
  }

  @Then("{string} icon should NOT be visible on any sales record")
  public void icon_should_not_be_visible_on_any_sales_record(String iconType) {
    Assert.assertFalse(
      "Delete icons should be hidden for standard users",
      salesPage.isDeleteButtonVisible()
    );
  }

  @Given("User is on the login page")
public void user_is_on_the_login_page() {
    Hooks.driver.get(ConfigReader.get("app.url")); 
}

  @When("User enters valid non-admin username")
  public void user_enters_valid_non_admin_username() {
    loginPage.enterUsername(ConfigReader.get("app.user.username"));
  }

  @When("User enters valid password")
  public void user_enters_valid_password() {
    loginPage.enterPassword(ConfigReader.get("app.user.password"));
  }

  @When("User clicks Login button")
  public void user_clicks_login_button() {
    loginPage.clickLogin();
  }


  @Then("User should be redirected to the Dashboard")
  public void redirected_to_user_dashboard() {
    Assert.assertTrue(
      "Not on the Dashboard page!",
      dashboardPage.isOnDashboard()
    );
  }

  @Then("Navigation menu highlights the active page")
  public void navigation_menu_highlights_the_active_page() {
    
    Assert.assertTrue(
      "The Dashboard menu item should have the 'active' class",
      sidenav.isMenuItemActive("Dashboard")
    );
  }

  @When("User enters invalid non-admin username {string}")
  public void user_enters_invalid_username(String user) {
      loginPage.enterUsername(user);
  }

  @When("User enters invalid password {string}")
  public void user_enters_invalid_password(String pass) {
      loginPage.enterPassword(pass);
  }

  @Then("Login should fail")
  public void login_should_fail() {
   
      Assert.assertFalse("Login should have failed, but dashboard was reached!", 
          dashboardPage.isOnDashboard());
  }
@Then("Error message {string} is displayed in red")
public void error_message_is_displayed_in_red(String expectedMessage) {
    String actualMessage = loginPage.getErrorMessageText();
    String color = loginPage.getErrorMessageColor(); 
    
    
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
          Hooks.driver.getCurrentUrl().contains("/login"));
  }


  @Given("User is on the Sales page")
  public void user_is_on_the_sales_page() {
      dashboardPage.clickViewSalesButton();
      Assert.assertTrue("Not on Sales page", salesPage.isOnSalesPage());
  }

  @When("User scrolls to the bottom of the page")
  public void user_scrolls_to_the_bottom_of_the_page() {
      salesPage.scrollToBottom();
  }

 @When("User clicks on {string} page button")
  public void user_clicks_on_page_button(String buttonType) {
      if (buttonType.equalsIgnoreCase("Next")) {
          salesPage.clickNextPage();
      } else {
          salesPage.clickPageTwo();
      }
  }
 

  @Then("The next set of sales records should be displayed")
  public void the_next_set_of_sales_records_should_be_displayed() {
      // After clicking Next/Page 2, the 'active' class should move to the link with text "2"
      String activePage = salesPage.getActivePageText();
      Assert.assertEquals("The active page should be 2", "2", activePage);
  }
}
