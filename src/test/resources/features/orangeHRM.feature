Feature: OrangeHRM Login
  As a user
  I want to see an error message when invalid credentials are entered
  So that I know the login attempt was unsuccessful

  @OHRM
  Scenario: Login with invalid credentials
    Given I navigate to "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
    When I login to OrangeHRM with username "username" and password "password"
    Then I should see OrangeHRM invalid credentials message
