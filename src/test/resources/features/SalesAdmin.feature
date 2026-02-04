Feature: Sales Admin UI

 Scenario: TC-UI-SALES-ADMIN-001 Plant selection field is required
  Given Admin is logged in
  And Admin is on the Sales page
  When Admin clicks Sell Plant button
  And Admin leaves the plant dropdown empty
  And Admin enters quantity "5"
  And Admin clicks the "Sell" button
  Then Error message "Plant is required" is displayed

  