@API @user
Feature: Plant Retrieval API (User)
  As a User
  I want to browse plants
  So that I can see what is available for purchase

  Background:
    Given User is authenticated via API

@TC-API-PLANT-USER-001 @positive
  Scenario: Get All Plants
    When User sends a GET request to "/api/plants"    
    Then The User receives status code 200
    And The response should contain a list of plants

  @TC-API-PLANT-USER-002 @positive
  Scenario: Get Plants by Category
    Given A valid category exists with ID 2
    When User sends a GET request to "/api/plants/category/2"    
    Then The User receives status code 200
    And The response should contain a list of plants

  @TC-API-PLANT-USER-003 @positive
  Scenario: Get Paged Plants - Pagination
    When User sends a GET request to "/api/plants/paged" with page 0 and size 10    
    Then The User receives status code 200    
    And The response body should contain "totalElements"
    And The response body should contain "content"
    And The response list "content" should have size less than or equal to 10

  @TC-API-PLANT-USER-004 @positive
  Scenario: Get Paged Plants - Plant Name Filter
    When User sends a GET request to search "/api/plants/paged" for name "Anthurium"    
    Then The User receives status code 200
    And The response body should contain "content"

 @TC-API-PLANT-USER-005 @negative
  Scenario: Get Plant by ID - Non-existent
    When User sends a GET request to "/api/plants/999999"
    Then The User receives status code 404   
    And The User response body should contain "message" with text "not found"