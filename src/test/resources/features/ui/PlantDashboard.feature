Feature: Dashboard Analytics

  @TC-UI-PLANT-USER-005 @positive @dashboard
  Scenario: Verify total plant count displays on dashboard
    # These steps reuse the logic from your existing PlantUserSteps
    Given User is logged in as "Test User"
    And User is on the Plant List page "/plants"
    
    # These are the new steps we will define in the new Java file
    When User captures the total plant count from the list
    And User navigates to the Dashboard page "/dashboard"
    Then The Plant Summary card is displayed
    And The dashboard plant count matches the captured count