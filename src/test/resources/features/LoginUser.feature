@UI @User @Login
Feature: Login Functionality

  Background:
    Given User is on the login page

  @TC-LOGIN-USER-001 @Positive
  Scenario: Valid User Login and Access Dashboard
    When User enters valid non-admin username
    And User enters valid password
    And User clicks Login button
    Then User should be redirected to the Dashboard
    And Navigation menu highlights the active page

  @TC-LOGIN-USER-002 @Negative
  Scenario: Invalid User Login
    When User enters invalid non-admin username "wrong_user"
    And User enters invalid password "wrong_password"
    And User clicks Login button
    Then Login should fail
    And Error message "Invalid username or password." is displayed in red
    And User should remain on the login page

