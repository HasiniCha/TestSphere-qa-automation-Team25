Feature: Sales User UI

  Background:
    Given User is logged in and on Dashboard

  @TC-001_User @validation
  Scenario: TC-UI-SALES-User-001 Plant selection field is required
    When User clicks View Sales 
    Then User should be on the Sales page
    When User clicks Sell Plant button
    Then User should be on the Sell Plant page
    When User leaves the plant dropdown empty
    And User enters quantity "5"
    And User clicks the "Sell" button
    Then Error message "Plant is required" is displayed


  @TC-002_User @validation
  Scenario: TC-UI-SALES-User-002 Quantity must be greater than 0
    When User clicks View Sales
    Then User should be on the Sales page
    When User clicks Sell Plant button
    Then User should be on the Sell Plant page
    When User selects plant "plant 10 (Stock: 7)" from dropdown
    And User enters quantity "0"
    And User clicks the "Sell" button
    Then Quantity validation message is displayed

  @TC-003_User @Positive
  Scenario: TC-UI-SALES-User-003 Successfully sell plant and verify stock reduction
    When User clicks View Sales
    Then User should be on the Sales page
    When User clicks Sell Plant button
    Then User should be on the Sell Plant page
    When User selects plant "abcd (Stock: 22)" from dropdown
    And User enters quantity "2"
    And User clicks the "Sell" button
    Then User should be redirected to the Sales page
    And New sale should appear in the sales list with plant "abcd" and quantity "2"
    When User navigates to Plants page
    Then Stock of plant "abcd" should be reduced to "20"

  @TC-004_User @Positive
  Scenario: TC-UI-SALES-User-004 Successfully delete a sales record
    Give at least one sales record exists
    When User clicks View Sales
    Then User should be on the Sales page
    When User clicks Delete icon on action column
    Then success message is displayed
    And record is deleted from the list

  @TC-005_User @Positive
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
    When User clicks on "Quantity" column header again
    Then Sales records should be sorted by "Sold At" in descending order

  