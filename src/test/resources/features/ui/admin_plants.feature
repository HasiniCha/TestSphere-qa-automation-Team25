@UI
Feature: Admin Plant Management

  Background:
    Given Admin is logged in_Plant
    And Admin is on the Plants page

  @TC-UI-PLANTS-ADMIN-001
  Scenario: Edit Plant with Valid Data
    Given A plant exists in the system
    When Admin clicks edit icon on an existing plant
    And Admin updates plant name to "Updated Rose"
    And Admin changes category to "Rose"
    And Admin updates price to "150"
    And Admin updates quantity to "10"
    And Admin clicks the "Save" button
    Then Success message is displayed
    And Admin is redirected to plants page
    And All fields are updated in plant list
    And Changes are saved to database

  @TC-UI-PLANTS-ADMIN-002
  Scenario: Plant - Quantity Negative Validation
    When Admin navigates to add plant page
    And Admin enters plant name "Tulip Test"
    And Admin selects category "Tulip"
    And Admin enters price "100"
    And Admin enters quantity "-5"
    And Admin clicks the "Save" button
    Then Error message is displayed
    And Admin remains on same page
    And Plant is not saved

  @TC-UI-PLANTS-ADMIN-003
  Scenario: Delete Plant - Confirm Deletion
    Given A plant exists in the system
    When Admin clicks delete icon on a plant
    And Admin clicks "Ok" on confirmation prompt
    Then Success message is displayed
    And Plant is deleted from list
    And Plant list refreshes

  @TC-UI-PLANTS-ADMIN-004
  Scenario: Plant - Low Stock Badge Display
    When Admin navigates to add plant page
    And Admin enters plant name "Low Stock"
    And Admin selects category "Rose"
    And Admin enters price "50"
    And Admin enters quantity "3"
    And Admin clicks the "Save" button
    And Admin navigates to plants list page
    And "Low" badge is displayed for the plant

  @TC-UI-PLANTS-ADMIN-005
 Scenario Outline: Plant Price Zero or Negative Values
  When Admin navigates to add plant page
  And Admin enters plant name "Orchid Test"
  And Admin selects category "Rose"
  And Admin enters price "<price>"
  And Admin enters quantity "10"
  And Admin clicks the "Save" button
  Then Error message is displayed
  And Plant is not saved

  Examples:
    | price |
    | 0     |
    | -1    |
    | -100  |