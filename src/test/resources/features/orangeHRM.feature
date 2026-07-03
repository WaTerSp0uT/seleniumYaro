Feature: OrangeHRM Login
  As a user
  I want to validate OrangeHRM login behavior
  So that I know users can and cannot access the application correctly

  @OHRM @Negative
  Scenario: Login with invalid credentials
    Given I navigate to "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
    When I login to OrangeHRM with username key "OHRM_INVALID_USERNAME" and password key "OHRM_INVALID_PASSWORD"
    Then I should see OrangeHRM invalid credentials message

  @OHRM @Positive
  Scenario: Login with valid credentials
    Given I navigate to "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
    When I login to OrangeHRM with username key "OHRM_VALID_USERNAME" and password key "OHRM_VALID_PASSWORD"
    Then I should see OrangeHRM dashboard page
