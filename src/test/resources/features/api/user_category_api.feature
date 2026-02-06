@API
Feature: User Category API Management

  @user @api @get_sub_categories
  Scenario: Verify that user can retrieve all sub-categories across all parent categories
    Given User is authenticated via API
    When User sends GET request to "/api/categories/sub-categories"
    Then User response status code should be 200
    And User response should contain sub-categories array

   @user @api @get_specific_category
   Scenario: Verify that user can retrieve a specific category by providing a valid category ID
    Given User is authenticated via API
    When User sends GET request to "/api/categories/22"
    Then User response status code should be 200
    And User response body should contain category with ID 22
    And User response body should contain category details

    @user @api @get_category_invalid_id
    Scenario: Verify that appropriate error message is returned when user requests a category with non-existent ID
    Given User is authenticated via API
    When User sends GET request to "/api/categories/170"
    Then User response status code should be 404
    And User response body should contain error details
    And User error message should indicate category not found

    @user @api @update_category_unauthorized
    Scenario: Verify that regular user without admin privileges cannot update an existing category
    Given User is authenticated via API
    When User sends PUT request to "/api/categories/22" with body:
      """
      {
        "name": "hjjj",
        "parentId": ""
      }
      """
    Then User response status code should be 403
    And User response body should contain error details
    And User error message should indicate access denied

    @user @api @get_category_summary
    Scenario: Verify that user can retrieve summary statistics of categories
    Given User is authenticated via API
    When User sends GET request to "/api/categories/summary"
    Then User response status code should be 200
    And User response should contain mainCategories count
    And User response should contain subCategories count