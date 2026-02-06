@API @User @Plants
Feature: Plants User API Functionality

  Background:
    Given User is authenticated for plants API

  @TC-API-PLANTS-USER-001
  Scenario: Get Plant by ID - Valid
    Given Plant with valid ID exists for retrieval
    When User sends a GET request to "/api/plants/{id}" with valid plant ID
    Then API should return 200 OK status code
    And Response contains plant details

  @TC-API-PLANTS-USER-002
  Scenario: Get Plant Summary
    Given At least one plant exists in the api system
    When User sends a GET request to "/api/plants/summary"
    Then API should return 200 OK status code
    And Response contains summary with total plants and low stock count

  @TC-API-PLANTS-USER-003
  Scenario: Get Paged Plants - Sorting
    Given Multiple plants exist in the system
    When User sends a GET request to "/api/plants/paged" with sort parameter "sort=price,desc"
    Then API should return 200 OK status code
    And Plants are sorted by price in descending order

  @TC-API-PLANTS-USER-004
  Scenario: Get Paged Plants - Category Filter
    Given Plants exist in a specific category
    When User sends a GET request to "/api/plants/paged" with category filter
    Then API should return 200 OK status code
    And Response contains only plants from the specified category

 Scenario: Get Plants by Category - Invalid Category
  Given User is authenticated for plants API
  When User sends a GET request to "/api/plants/category/{categoryId}" with non-existent category ID
  Then User API should return 404 Not Found status code
  And Response should contain category not found error message