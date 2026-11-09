@OKTA_AUTH_REUSE
Feature: Reuse a saved browser session

  Scenario: Open the OrangeHRM dashboard in a fresh browser context
    Given I open "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index" using the saved OrangeHRM session
    Then I should see the OrangeHRM dashboard using the saved session
