@API
Feature: Admin Category API Management

  @admin @api @admin_api1
  Scenario: Verify Admin can retrieve all sub-categories
    Given Admin is authenticated via API
    When Admin sends GET request to "/api/categories/sub-categories"
    Then Response status code should be 200
    And Response should contain sub-categories array

    @admin @api @update_category
    Scenario: Verify admin can successfully update an existing category with valid data
    Given Admin is authenticated via API
    When Admin sends PUT request to "/api/categories/10" with valid category data
    Then Response status code should be 200
    And Response body should contain updated category name "uiuiuo"

    @admin @api @update_category_negative
    Scenario: Verify error when admin updates a non-existent category
    Given Admin is authenticated via API
    When Admin sends PUT request to "/api/categories/1" with valid category data
    Then Response status code should be 404
    And Response body should contain error message for non-existent category

    @admin @api @get_categories_pagination
    Scenario: Verify category list retrieval with pagination
    Given Admin is authenticated via API
    When Admin sends GET request to "/api/categories/page?page=0&size=10"
    Then Response status code should be 200
    And Response body should contain paginated categories


    @admin @api @create_main_category
    Scenario: Verify admin can successfully create a new main (parent) category with valid data
    Given Admin is authenticated via API
    When Admin sends POST request to "/api/categories" with valid main category data
    Then Response status code should be 201
    And Response body should contain the newly created main category name "Option"


