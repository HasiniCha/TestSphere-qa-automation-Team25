package com.qaautomation.stepdefinitions.ui;

import com.qaautomation.pages.sales.SalesPage;
import com.qaautomation.utils.DriverFactory;
import io.cucumber.java.en.*;

import java.time.Duration;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SalescommonSteps {

  private SalesPage salesPage;

  private SalesPage getSalesPage() {
    if (salesPage == null) {
      salesPage = new SalesPage(DriverFactory.getDriver());
    }
    return salesPage;
  }

  @When("Admin clicks on {string} column header")
  @When("User clicks on {string} column header")
  public void clicks_on_column_header(String columnName) {
    getSalesPage().clickColumnHeader(columnName);
    waitForTableRefresh();
  }

  @Then("Sales records should be sorted by {string} in ascending order")
  public void sales_records_should_be_sorted_in_ascending_order(
    String columnName
  ) {
    List<String> columnValues = getSalesPage().getColumnValues(columnName);
    System.out.println("Actual values in column '" + columnName + "': " + columnValues);
    

    Assert.assertTrue(
      "Sales records should be sorted by '" +
      columnName +
      "' in ascending order",
      getSalesPage().isSortedAscending(columnValues)
    );
  }

  @When("Admin clicks on {string} column header again")
  @When("User clicks on {string} column header again")
  public void clicks_on_column_header_again(String columnName) {
    getSalesPage().clickColumnHeader(columnName);
    waitForTableRefresh();
  }

  @Then("Sales records should be sorted by {string} in descending order")
  public void sales_records_should_be_sorted_in_descending_order(
    String columnName
  ) {
    List<String> columnValues = getSalesPage().getColumnValues(columnName);

    Assert.assertTrue(
      "Sales records should be sorted by '" +
      columnName +
      "' in descending order",
      getSalesPage().isSortedDescending(columnValues)
    );
  }


 private void waitForTableRefresh() {
    // 1. Give the UI a moment to start the sorting animation/request
    try {
        Thread.sleep(800); 
        // 2. Wait for the table body to be stable/visible
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr")));
    } catch (Exception e) {
        // Log error if needed
    }
}
}



