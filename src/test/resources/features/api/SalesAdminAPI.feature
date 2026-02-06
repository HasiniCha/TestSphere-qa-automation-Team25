@API @Admin @Sales
Feature: Sales API Functionality

  @TC-API-SALES-ADMIN-001
  Scenario: Sell plant with valid request
    Given Admin is authenticated for API
    And a plant with ID 5 exists and has valid stock
    When Admin sends a POST request to "/sales/plant/{plantId}" for plant 5 with quantity 5
    Then the API returns 201 status code
    And the response contains sale details

  @TC-API-SALES-ADMIN-002 @Negative
  Scenario: Sell plant with invalid Plant ID
    Given Admin is authenticated for API
    When Admin sends a POST request to "/sales/plant/{plantId}" for invalid plant 9999 with quantity 5
    Then the API returns 404 status code
    And the response error message should be "Plant not found"

  @TC-API-SALES-ADMIN-003 @Negative
  Scenario Outline: Sell plant with invalid quantity
    Given Admin is authenticated for API
    When Admin sends a POST request to "/sales/plant/{plantId}" for plant 3 with quantity <qty>
    Then the API returns 400 status code
    And the response error message should contain "<error_msg>"

    Examples: 
      | qty  | error_msg              |
      | 0    | must be greater than 0 |
      | -1   | must be greater than 0 |
      | 9999 | available in stock     |

 @TC-API-SALES-ADMIN-004 @Delete
 Scenario: Delete sale by ID
  Given Admin is authenticated for API
  And a sale with ID 75 exists in the system
  When Admin sends a DELETE request to "/sales/{id}" for sale ID 75
  Then the API returns 204 status code

  @TC-API-SALES-ADMIN-005 @Negative @Delete
  Scenario: Delete sale - Non-existent sale ID
    Given Admin is authenticated for API
    When Admin sends a DELETE request to "/sales/{id}" for sale ID 99999
    Then the API returns 404 status code
    And the response error message should be "Sale not found"