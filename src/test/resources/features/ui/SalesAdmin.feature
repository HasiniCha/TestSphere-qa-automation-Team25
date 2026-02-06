@UI @Admin @Sales
Feature: Sales Admin UI

  Background:
    Given Admin is logged in and on Dashboard

  @TC-001_Admin @validation
  Scenario: TC-UI-SALES-ADMIN-001 Plant selection field is required
    When Admin clicks View Sales 
    Then Admin should be on the Sales page
    When Admin clicks Sell Plant button
    Then Admin should be on the Sell Plant page
    When Admin leaves the plant dropdown empty
    And Admin enters quantity "5"
    And Admin clicks the "Sell" button
    Then Error message "Plant is required" is displayed


  @TC-002_Admin @validation
  Scenario: TC-UI-SALES-ADMIN-002 Quantity must be greater than 0
    When Admin clicks View Sales
    Then Admin should be on the Sales page
    When Admin clicks Sell Plant button
    Then Admin should be on the Sell Plant page
    When Admin selects plant "<dropdownText>" from dropdown
    And Admin enters quantity "0"
    And Admin clicks the "Sell" button
    Then Quantity validation message is displayed
      Examples:
    | plantName | dropdownText         |
    | Plant A| Plant A (Stock: 100) |

  @TC-003_Admin @Positive
Scenario Outline: Successfully sell plant and verify stock reduction
    When Admin clicks View Sales
    Then Admin should be on the Sales page
    When Admin clicks Sell Plant button
    Then Admin should be on the Sell Plant page
    When Admin selects plant "<dropdownText>" from dropdown
    And Admin enters quantity "<qty>"
    And Admin clicks the "Sell" button
    Then Admin should be redirected to the Sales page
    And New sale should appear in the sales list with plant "<plantName>" and quantity "<qty>"
    When Admin navigates to Plants page
    Then Stock of plant "<plantName>" should be reduced by <qty>

  Examples:
    | plantName | dropdownText          | qty |
    | Plant A | Plant A (Stock: 100) | 5   |
    

  @TC-004_Admin @Positive
  Scenario: TC-UI-SALES-ADMIN-004 Successfully delete a sales record
    When Admin clicks View Sales
    Then Admin should be on the Sales page
    And at least one sales record exists
    When Admin clicks Delete icon on action column
    Then success message is displayed
    And record is deleted from the list
    
    

  @TC-005_Admin @Positive
  Scenario: TC-UI-SALES-ADMIN-005 Sort sales records by Plant Name and Quantity
    When Admin clicks View Sales
    Then Admin should be on the Sales page
    When Admin clicks on "Plant" column header
    Then Sales records should be sorted by "Plant" in ascending order
    When Admin clicks on "Plant" column header again
    Then Sales records should be sorted by "Plant" in descending order
    When Admin clicks on "Quantity" column header
    Then Sales records should be sorted by "Quantity" in ascending order
    When Admin clicks on "Quantity" column header again
    Then Sales records should be sorted by "Quantity" in descending order
    When Admin clicks on "Total Price" column header
    Then Sales records should be sorted by "Total Price" in ascending order
    When Admin clicks on "Total Price" column header again
    Then Sales records should be sorted by "Total Price" in descending order
    When Admin clicks on "Sold At" column header
    Then Sales records should be sorted by "Sold At" in ascending order
    When Admin clicks on "Sold At" column header again
    Then Sales records should be sorted by "Sold At" in descending order

  