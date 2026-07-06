package stepDefinitions;

import org.junit.Assert;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import factory.PlaywrightFactory;
import pages.ohrm.OrangeHRMLoginPage;
import utils.SecretConfigReader;

public class OrangeHRMPlaywrightSteps {

    private OrangeHRMLoginPage orangeHRMLoginPage;

    public OrangeHRMPlaywrightSteps() {
    }

    @When("I login to OrangeHRM with username {string} and password {string}")
    public void iLoginToOrangeHRMWithUsernameAndPassword(
            String username,
            String password) {

        orangeHRMLoginPage = new OrangeHRMLoginPage(PlaywrightFactory.getPage());
        orangeHRMLoginPage.login(username, password);
    }

    @When("I login to OrangeHRM with username key {string} and password key {string}")
    public void iLoginToOrangeHRMWithUsernameKeyAndPasswordKey(
            String usernameKey,
            String passwordKey) {

        orangeHRMLoginPage = new OrangeHRMLoginPage(PlaywrightFactory.getPage());
        orangeHRMLoginPage.login(
                SecretConfigReader.getSecret(usernameKey),
                SecretConfigReader.getSecret(passwordKey));
    }

    @Then("I should see OrangeHRM invalid credentials message")
    public void iShouldSeeOrangeHRMInvalidCredentialsMessage() {
        String actualMessage = orangeHRMLoginPage.getInvalidCredentialsMessageText();

        Assert.assertTrue(
                "Expected invalid credentials message, but found: " + actualMessage,
                actualMessage.contains("Invalid credentials"));
    }

    @Then("I should see OrangeHRM dashboard page")
    public void iShouldSeeOrangeHRMDashboardPage() {
        Assert.assertTrue(
                "Expected OrangeHRM dashboard page to be displayed.",
                orangeHRMLoginPage.isDashboardPageDisplayed());
    }
}
