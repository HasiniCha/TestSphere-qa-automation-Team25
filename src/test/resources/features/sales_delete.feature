Feature: Delete Sale

  Scenario: Admin deletes a sale successfully
    Given Admin is logged in
    When Admin navigates to the Sales List page
    And Admin deletes the sale with name "plant 10"
    Then The sale should be removed from the list
    And Dashboard revenue and item count should be updated
