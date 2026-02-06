@UserUI
Feature: Plant Management - User Role
  As a standard user
  I want to view and search plants
  So that I can see available inventory

  Background:
    Given User is logged in as "Test User"
    And User is on the Plant List page "/plants"

  @TC-UI-PLANT-USER-001 @positive
  Scenario: Filter plant by Name
    When User enters "Apple5" in the search input field
    And User clicks the Search button
    Then The list shows records matching "Apple5"
    And Plant details are displayed

  @TC-UI-PLANT-USER-002 @positive
  Scenario: View plant list (Read-Only)
    Then The plant list displays Name, Category, Price, and Quantity
    And Stock status badges are visible
    And "Edit" and "Delete" buttons are not visible

  @TC-UI-PLANT-USER-003 @negative @security
  Scenario: Restrict Add functionality
    Then The "Add a Plant" button is not visible
    When User attempts to navigate to "/plants/add" directly
    Then User is redirected to the Plant List or shows "Access Denied"

  @TC-UI-PLANT-USER-004 @positive
  Scenario: Filter by Name and Category    
    When User enters "Apple5" in the search input field
    And User clicks the Search button
    Then Only plants matching "Apple5" and "mango-1" are displayed

