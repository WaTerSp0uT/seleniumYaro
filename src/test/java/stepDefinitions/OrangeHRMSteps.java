package stepDefinitions;

import org.junit.Assert;

import Pages.OHRM.OrangeHRMLoginPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.SecretConfigReader;

public class OrangeHRMSteps {

    private OrangeHRMLoginPage orangeHRM;

    public OrangeHRMSteps() {
    }

    @When("I login to OrangeHRM with username {string} and password {string}")
    public void iLoginToOrangeHRMWithUsernameAndPassword(
            String username,
            String password) {

        orangeHRM = new OrangeHRMLoginPage();
        orangeHRM.login(username, password);
    }

    @When("I login to OrangeHRM with username key {string} and password key {string}")
    public void iLoginToOrangeHRMWithUsernameKeyAndPasswordKey(
            String usernameKey,
            String passwordKey) {

        orangeHRM = new OrangeHRMLoginPage();
        orangeHRM.login(
                SecretConfigReader.getSecret(usernameKey),
                SecretConfigReader.getSecret(passwordKey));
    }

    @Then("I should see OrangeHRM invalid credentials message")
    public void iShouldSeeOrangeHRMInvalidCredentialsMessage() {
        String actualMessage = orangeHRM.getInvalidCredentialsMessageText();

        Assert.assertTrue(
                "Expected invalid credentials message, but found: " + actualMessage,
                actualMessage.contains("Invalid credentials"));
    }

    @Then("I should see OrangeHRM dashboard page")
    public void iShouldSeeOrangeHRMDashboardPage() {
        Assert.assertTrue(
                "Expected OrangeHRM dashboard page to be displayed.",
                orangeHRM.isDashboardPageDisplayed());
    }
}
