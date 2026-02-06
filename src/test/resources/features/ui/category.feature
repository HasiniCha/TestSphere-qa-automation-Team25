Feature: Category Management

  @admin @ui @test1
  Scenario: Verify Add Category button is visible and clickable for Admin
    Given Admin is logged in
    And Admin navigates to the Categories page
    Then Add Category button should be visible
    And Add Category button should be clickable
    When Admin clicks the Add Category button
    Then Admin should be navigated to Add Category page

     @admin @ui @test2
  Scenario: Verify successful creation of a main category
    Given Admin is logged in
    And Admin navigates to Add Category page
    When Admin enters category name "Flowers"
    And Admin leaves Parent Category as default
    And Admin clicks Save button
    Then Main category should be created successfully
    And Admin should be redirected to category list page
    And New category "Flowers" should be visible in the list
    And Parent column should show "-" for category "Flowers"

     @admin @test3
  Scenario: Verify validation error for empty category name
    Given Admin is logged in
    And Admin navigates to Add Category page
    When Admin leaves the Category Name field empty
    And Admin clicks Save button
    Then Validation error message "Category name is required" should be displayed
    And Name length validation message should be displayed
    And Form should not submit
    And Admin should remain on Add Category page

    @admin @ui @test4
Scenario: Verify validation error for category name length
  Given Admin is logged in
  And Admin navigates to Add Category page
  When Admin enters category name "Ro"
  And Admin clicks Save button
  Then Validation error message "Category name must be between 3 and 10 characters" should be displayed
  And Form should not submit
  And Admin should remain on Add Category page

  @admin @ui @test5
Scenario: Delete Category - Confirm Deletion
  Given Admin is logged in
  And Admin navigates to the Categories page
  And Debug category row for "Flowers"
  And Delete button is visible for category "Flowers"
  When Admin clicks the Delete button for category "Flowers"
  And Admin clicks OK/Confirm in the confirmation dialog
  Then Category "Flowers" should be deleted successfully
  And Success message for deletion should be displayed
  And Category "Flowers" should no longer be visible in the category list

  