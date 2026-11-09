@OKTA_AUTH_CAPTURE
Feature: Capture a browser session after manual sign-in

  Scenario: Save an authenticated OrangeHRM demo session
    Given I open "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login" for manual OrangeHRM sign-in
    Then I save the OrangeHRM session after signing in manually
