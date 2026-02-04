package com.qaautomation.stepdefinitions;

import com.qaautomation.pages.SalesPage;
import io.cucumber.java.en.*;
import java.util.List;
import org.junit.Assert;

public class SalescommonSteps {

  SalesPage salesPage = new SalesPage(Hooks.driver);

  // Sorting steps - shared by both Admin and User
  @When("Admin clicks on {string} column header")
  @When("User clicks on {string} column header")
  public void clicks_on_column_header(String columnName) {
    salesPage.clickColumnHeader(columnName);
  }

  @Then("Sales records should be sorted by {string} in ascending order")
  public void sales_records_should_be_sorted_in_ascending_order(
    String columnName
  ) {
    List<String> columnValues = salesPage.getColumnValues(columnName);

    Assert.assertTrue(
      "Sales records should be sorted by '" +
      columnName +
      "' in ascending order",
      salesPage.isSortedAscending(columnValues)
    );
  }

  @When("Admin clicks on {string} column header again")
  @When("User clicks on {string} column header again")
  public void clicks_on_column_header_again(String columnName) {
    salesPage.clickColumnHeader(columnName);
  }

  @Then("Sales records should be sorted by {string} in descending order")
  public void sales_records_should_be_sorted_in_descending_order(
    String columnName
  ) {
    List<String> columnValues = salesPage.getColumnValues(columnName);

    Assert.assertTrue(
      "Sales records should be sorted by '" +
      columnName +
      "' in descending order",
      salesPage.isSortedDescending(columnValues)
    );
  }
}
