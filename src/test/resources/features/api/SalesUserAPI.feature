@API @User @Sales
Feature: Sales User API Functionality

  @TC-API-SALES-USER-001 @Negative
  Scenario: Non-admin user cannot sell plant
    Given User is authenticated for API
    And a plant with ID 8 exists and has valid stock
    When User sends a POST request to "/sales/plant/{plantId}" for plant 8 with quantity 2
    Then the API returns 403 status code
    And the response error message should be "Access Denied"

  @TC-API-SALES-USER-002 @Negative
  Scenario: Non-admin user cannot delete a sale record
    Given User is authenticated for API
    And a sale with ID 2 exists in the system
    When User sends a DELETE request to "/sales/{id}" for sale ID 4
    Then the API returns 403 status code

  @TC-API-SALES-USER-003
  Scenario: Non-admin user retrieves all sales
    Given User is authenticated for API
    When User sends a GET request to "/sales"
    Then the API returns 200 status code
    And the response should contain a list of sales

  @TC-API-SALES-USER-004
  Scenario Outline: Non-admin user can view sales with pagination and sorting
    Given User is authenticated for API
    When User sends a GET request to "/sales/page" with page 0, size 1, and sort "<field>"
    Then the API returns 200 status code
    And the response should contain paginated results sorted by "<field>"

    Examples:
      | field      |
      | plantName  |
      | quantity   |
      | totalPrice |
      | soldAt     |

  @TC-API-SALES-USER-005
  Scenario: Non-admin user retrieves sale by ID
    Given User is authenticated for API
    When User sends a GET request for sales to "/api/sales/49"
    Then the API returns 200 status code
   