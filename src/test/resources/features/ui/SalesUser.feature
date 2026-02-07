@UI @User @Sales
Feature: Sales User UI

  Background: 
    Given Sale User is logged in and on Dashboard

  @TC-001_User @Positive
  Scenario: TC-UI-SALES-USER-001 View sales list with user restrictions
    When User clicks View Sales
    Then User should be on the Sales page
    And Sales records should be displayed with columns
    And "Sell Plant" button should NOT be visible
    And "Delete" icon should NOT be visible on any sales record

  @TC-002_User @Positive
  Scenario: TC-UI-SALES-User-005 Sort sales records by Plant Name and Quantity
    When User clicks View Sales
    Then User should be on the Sales page
    When User clicks on "Plant" column header
    Then Sales records should be sorted by "Plant" in ascending order
    When User clicks on "Plant" column header again
    Then Sales records should be sorted by "Plant" in descending order
    When User clicks on "Quantity" column header
    Then Sales records should be sorted by "Quantity" in ascending order
    When User clicks on "Quantity" column header again
    Then Sales records should be sorted by "Quantity" in descending order
    When User clicks on "Total Price" column header
    Then Sales records should be sorted by "Total Price" in ascending order
    When User clicks on "Total Price" column header again
    Then Sales records should be sorted by "Total Price" in descending order
    When User clicks on "Sold At" column header
    Then Sales records should be sorted by "Sold At" in ascending order
    When User clicks on "Sold At" column header again
    Then Sales records should be sorted by "Sold At" in descending order

  

  @TC-003_User @Positive
  Scenario: TC-UI-SALES-USER-003 Pagination Verify that Users can navigate through multiple pages
    When User clicks View Sales
    Then User should be on the Sales page
    When User scrolls to the bottom of the page
    And User clicks on "Next" page button
    Then The next set of sales records should be displayed