Feature: User Category Management

  @user @ui @user1
  Scenario: Verify User can access category list with view only access
    Given User is logged in
    When User navigates to the category list page
    Then User should be redirected to "/ui/categories" successfully
    And Category list should be visible to User

    @user @ui @user2
    Scenario: Verify Add Category button is not visible to User
    Given User is logged in
    When User navigates to the category list page
    Then Add Category button should not be visible to User

    @user @ui @user3
    Scenario: Verify User cannot edit categories
    Given User is logged in
    When User navigates to the category list page
    And User clicks Edit icon for a category
    And User modifies the category name
    And User clicks Save button
    Then An error message should appear

 @user @ui @user4
Scenario: Verify User can sort categories by Parent column
  Given User is logged in
  When User navigates to the category list page
  And User clicks on the Parent column header
  Then Categories should be sorted by parent category
  When User clicks on the Parent column header again
  Then Sort order should toggle to descending

  @user @ui @user5
Scenario: Verify Reset button clears search and filter fields
  Given User is logged in
  When User navigates to the category list page
  And User enters "test" in search field
  And User selects a parent category from dropdown
  And User clicks Search button
  And User clicks Reset button
  Then Search field should be cleared
  And Parent dropdown should reset to default
  And Full category list should be displayed