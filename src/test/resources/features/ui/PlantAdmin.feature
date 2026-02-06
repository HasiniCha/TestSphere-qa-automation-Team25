@AdminUI @Nethmi
Feature: Admin Plant Management
  As an admin
  I want to manage plants in the system
  So that I can maintain the plant inventory

  Background:
    Given User is logged in as Admin
    And Dashboard is displayed

  # --- TC 1: REMAINS ON ADD PAGE ---
  @TC-UI-PLANT-ADMIN001 @positive @smoke
  Scenario: Verify Admin can add a plant with valid data
    Given User navigates to Plant Add page
    When User enters "Avacad0o03 Plant" in the Name field
    And User selects "mango-1" from the Category dropdown
    And User enters "105.00" in the Price field
    And User enters "20" in the Quantity field
    And User clicks the Save button
    Then User is redirected to the Plant List page
    And The new plant "Avacad0o03 Plant" appears in the list

  # --- TC 2: MOVED TO EDIT PAGE ---
  @TC-UI-PLANT-ADMIN002 @negative @validation
  Scenario Outline: Plant - Name Length Validation
    Given User navigates to Plant edit page
    When User enters "<name>" in the Name field
    And User selects "mango-1" from the Category dropdown
    And User enters "5.00" in the Price field
    And User enters "10" in the Quantity field
    And User clicks the Save button
    Then The plant is not saved
    And Error message "Plant name must be between 3 and 25 characters" is displayed below the Name field

    Examples:
      | name                       | description           |
      | AB                         | 2 characters          |
      | A                          | 1 character           |
      | ABCDEFGHIJKLMNOPQRSTUVWXYZ | 26 characters         |

  # --- TC 3: MOVED TO EDIT PAGE ---
  @TC-UI-PLANT-ADMIN003 @negative @validation
  Scenario: Validation - Decimal values in quantity
    Given User navigates to Plant edit page
    When User enters "Basil" in the Name field
    And User selects "mango-1" from the Category dropdown
    And User enters "5.00" in the Price field
    And User enters "10.5" in the Quantity field
    And User clicks the Save button
    Then Error message is displayed
    And User remains on the same page
    And The plant is not saved

 @AdminUI @TC-UI-PLANT-ADMIN004 @positive @validation
  Scenario: Verify Admin can update only the plant quantity(Valid) in Edit Mode    
    Given User is on the Plant List page    
    And User clicks the Edit button for the plant "Avacad0o03 Plant"
    When User enters "110" in the Quantity field
    And User clicks the Save button
    Then User is redirected to the Plant List page   
    And The plant "Avacad0o03 Plant" should show a quantity of "110" in the list
     
 
  # --- TC 5: MOVED TO EDIT PAGE ---
  @TC-UI-PLANT-ADMIN005 @negative @validation
  Scenario: Validation - Invalid plant price format
    Given User navigates to Plant edit page
    When User enters "Basil" in the Name field
    And User selects "mango-1" from the Category dropdown
    And User enters "abc" in the Price field
    And User enters "10" in the Quantity field
    And User clicks the Save button
    Then Error message is displayed
    And User remains on the same page
    And The plant is not saved