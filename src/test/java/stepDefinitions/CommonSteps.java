package stepDefinitions;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import Pages.cis.CISCommonPage;

import Pages.common.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.CommonUtils;
import utils.ElementUtils;
import utils.Library;

import utils.SecretConfigReader;

public class CommonSteps {

    private WebDriver driver;

    private CISCommonPage cisCommonPage;
    private CommonUtils commonUtils;
    private ElementUtils elementUtils;
    private LoginPage login;

    public CommonSteps() {
    }

    //Will add them to constants class after test
    private static final String EXPECTED_ROOT = "src/test/resources/visual-baselines";
    private static final String ACTUAL_ROOT = "target/visual-results/actual";
    private static final String DIFF_ROOT = "target/visual-results/diff";

    @Given("I navigate to {string}")
    public void iNavigateTo(String url) {
        Library.getLibrary().navigateTo(url);
    }

    @Given("I select the remote machine {string}")
    public void iSelectTheRemoteMachine(String machineNum) {

        Library.getLibrary().setProperty("executionType", "remote");
        System.out.println("Select Remote type : " + Library.getLibrary().getProperty("executionType"));
        Library.getLibrary().setProperty("targetMachine", machineNum);
        System.out.println("I select the Machine number to be used in feature file : " + Library.getLibrary().getProperty("targetMachine"));
    }

    @When("I enter valid userID {string} into userId field")
    public void i_enter_valid_user_id_into_user_id_field(String userId) {
        login = new LoginPage();
        login.enterUserID(SecretConfigReader.getSecret(userId));
    }

    @When("I enter valid password {string} into password field")
    public void i_enter_valid_password_into_password_field(String password) throws InterruptedException {

        Thread.sleep(2000);
        login = new LoginPage();
        login.enterPassword(SecretConfigReader.getSecret(password));
    }

    @When("I click on Login button")
    public void i_click_on_login_button() throws InterruptedException {
        login.clickOnLoginButton();
    }

    @Then("I should get successfully logged in")
    public void i_should_get_successfully_logged_in() {
        Assert.assertTrue(login.getLoginMessageText().contains("Click here to request new privileges."));
    }
}


