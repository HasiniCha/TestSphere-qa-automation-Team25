Feature: Sales Admin UI

  Background:
    Given User is logged in and on Dashboard

  @TC-001_Admin_01  @validation
  Scenario: TC-UI-SALES-ADMIN-001 Plant selection field is required
    When Admin clicks View Sales 
    Then Admin should be on the Sales page
    When Admin clicks Sell Plant button
    Then Admin should be on the Sell Plant page
    When Admin leaves the plant dropdown empty
    And Admin enters quantity "5"
    And Admin clicks the "Sell" button
    Then Error message "Plant is required" is displayed