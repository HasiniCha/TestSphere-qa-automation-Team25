@API @Admin @Nethmi
Feature: Plant Management API
  As an Admin
  I want to manage plants via API
  So that I can integrate with external systems and verify backend logic

  Background:
    Given Admin is authenticated via API for Plant

  @TC-API-PLANT-ADMIN001 @positive
  Scenario: Create Plant - Valid Data
    Given A valid sub-category exists with ID 16
    When I send a POST request to "/api/plants/category/16" with the following data:
      | name     | flower11|
      | price    | 150       |
      | quantity | 25        |
    Then The API response status code should be 200 or 201
    And The response body should contain a generated "id"
    And The response body should contain "name" with value "flower11"

    @TC-API-PLANT-ADMIN002 @positive
  Scenario: Update Plant - Valid Data    
    # Verify ID 110 exists and capture its current state if needed
    And A plant exists with ID 110
    When I send a PUT request to "/api/plants/110" with the following data:
      | name     | Updated Plant1 |
      | price    | 45000.00              |
      | quantity | 50                  |
    Then The API response status code should be 200
    And The response body should contain "name" with value "Updated Plant1"
    And The response body should contain "id" with value 110


  @TC-API-PLANT-ADMIN003 @negative
  Scenario: Create Plant - Invalid Category (Parent instead of Sub)
    Given A parent category exists with ID 22
    When I send a POST request to "/api/plants/category/22" with the following data:
      | name     | Invalid Cat Plant |
      | price    | 10.00             |
      | quantity | 10                |
    Then The API response status code should be 400
    And The response body should contain "message" with text "sub-categories"

 @TC-API-PLANT-ADMIN004 @negative
  Scenario: Create Plant - Negative Quantity
    Given A valid sub-category exists with ID 16
    When I send a POST request to "/api/plants/category/16" with the following data:
      | name     | Negative Qty Plant |
      | price    | 10.00              |
      | quantity | -5                 |
    Then The API response status code should be 400
    # FIX: Check 'details.quantity' instead of 'message'
    And The response body should contain "details.quantity" with text "Quantity cannot be negative"

  @TC-API-PLANT-ADMIN005 @negative
  Scenario: Update Plant - Non-existent ID
    When I send a PUT request to "/api/plants/999999" with the following data:
      | name     | Ghost Plant |
      | price    | 50.00       |
      # FIX: Add quantity to pass validation so we can hit the 404 Not Found error
      | quantity | 10          |
    Then The API response status code should be 404
    # Note: If the message is nested, use 'details' or just check 'message' contains 'not found'
    And The response body should contain "message" with text "not found"