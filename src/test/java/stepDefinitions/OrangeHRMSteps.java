package stepDefinitions;

import org.junit.Assert;

import Pages.OHRM.OrangeHRM;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrangeHRMSteps {

    private OrangeHRM orangeHRM;

    public OrangeHRMSteps() {
    }

    @When("I login to OrangeHRM with username {string} and password {string}")
    public void iLoginToOrangeHRMWithUsernameAndPassword(
            String username,
            String password) {

        orangeHRM = new OrangeHRM();
        orangeHRM.login(username, password);
    }

    @Then("I should see OrangeHRM invalid credentials message")
    public void iShouldSeeOrangeHRMInvalidCredentialsMessage() {
        String actualMessage = orangeHRM.getInvalidCredentialsMessageText();

        Assert.assertTrue(
                "Expected invalid credentials message, but found: " + actualMessage,
                actualMessage.contains("Invalid credentials"));
    }
}
