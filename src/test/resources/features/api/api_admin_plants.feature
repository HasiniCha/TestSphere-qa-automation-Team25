@API @Admin @Plants
Feature: Plants Admin API Functionality

  Background:
    Given Admin user is authenticated for plants API

  @TC-API-PLANTS-ADMIN-001
  Scenario: Create Plant - Duplicate
    Given At least one plant exists in the category
    When Admin sends a POST request to "/api/plants/category/{categoryId}"
    And Include plant name that already exists in request body
    And Include valid price and quantity
    Then API should return 400 Bad Request status code
    And Error message should be displayed

  @TC-API-PLANTS-ADMIN-002
  Scenario: Delete Plant - Existing
    Given Plant with valid ID exists
    When Admin sends a DELETE request to "/api/plants/{id}" with valid plant ID
    Then API should return 204 No Content status code
    And Plant should be successfully deleted

  @TC-API-PLANTS-ADMIN-003
  Scenario Outline: Create Plant - Negative/Zero Price
    Given Admin has valid category ID
    When Admin sends a POST request to "/api/plants/category/{categoryId}"
    And Include valid plant name
    And Set price to <price> value
    And Include valid quantity
    Then API should return 400 Bad Request status code
    And Error message should be displayed

    Examples:
      | price |
      | 0     |
      | -1    |
      | -100  |

@TC-API-PLANTS-ADMIN-004
Scenario Outline: Create Plant - Missing Required Fields
  Given Admin has valid category ID
  When Admin sends a POST request to "/api/plants/category/{categoryId}"
  And Omit <field> from request body
  Then API should return 400 Bad Request status code
  And Validation error message should be displayed

  Examples:
    | field    |
    | name     |
    | price    |
    | quantity |
   
  @TC-API-PLANTS-ADMIN-005
  Scenario: Delete Plant - Non-existent ID
    When Admin sends a DELETE request to "/api/plants/{id}" with non-existent plant ID
    Then API should return 404 Not Found status code
    And Error message should be displayed
