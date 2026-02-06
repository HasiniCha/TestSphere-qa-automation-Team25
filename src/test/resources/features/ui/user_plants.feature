@UI
Feature: User Plant Management
  As a User
  I want to view and filter plants
  So that I can browse available plants without administrative capabilities

  @TC-UI-PLANTS-USER-001
  Scenario: Verify user can filter plant by category
    Given User is logged in
    And User is on the Plants page
    And Plant list displays all plants
    When User clicks on the category filter dropdown
    And User selects category "Rose"
    And User clicks the "Search" button
    Then User plant list refreshes
    And Only plants from category "Rose" are displayed

  @TC-UI-PLANTS-USER-002
  Scenario: Reset button clears all input fields
    Given User is logged in
    And User is on the Plants page
    When User enters plant name "Tulip" in search field
    And User selects category "Tulip" from dropdown
    And User clicks the "Search" button
    Then Filtered results are displayed
    When User clicks the "Reset" button
    Then Name search field is cleared
    And Category dropdown is reset to default state
    And Plant list displays all plants without filters

  @TC-UI-PLANTS-USER-003
  Scenario: Verify Delete button is hidden for User role
    Given User is logged in
    And User is on the Plants page
    When User observes the Actions column
    Then Delete button is not visible for any plant
    And User cannot access delete functionality

  @TC-UI-PLANTS-USER-004
  Scenario: Verify Edit button is hidden for User role
    Given User is logged in
    And User is on the Plants page
    When User observes the Actions column
    Then Edit button is not visible for any plant
    And User cannot access edit functionality

   @TC-UI-PLANTS-USER-005
  Scenario: Verify low stock plant count displays on dashboard
    Given User is logged in
    And At least one plant with quantity less than 5 exists
    When User navigates to dashboard at "/ui/dashboard"
    Then Dashboard displays Plant summary card
    And Low Stock Plants count shows correct number
    And Count matches plants with quantity less than 5