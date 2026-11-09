package stepDefinitions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.junit.Assert;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import factory.PlaywrightFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.ohrm.OrangeHRMLoginPage;
import utils.Library;

public class OktaSessionSteps {

    private static final Path STATE_FILE = Path.of(".playwright-auth", "okta_orangehrm.json");
    private static final Duration MANUAL_LOGIN_TIMEOUT = Duration.ofMinutes(5);

    @Given("I open {string} for manual OrangeHRM sign-in")
    public void openForManualSignIn(String loginUrl) {
        if (Boolean.parseBoolean(Library.getLibrary().getProperty("playwrightHeadless"))) {
            throw new IllegalStateException(
                    "Session capture requires a visible browser. Set -DplaywrightHeadless=false.");
        }
        System.out.println("Sign in to OrangeHRM in the browser using the demo credentials shown on the login page.");
        PlaywrightFactory.getPage().navigate(loginUrl);
    }

    @Then("I save the OrangeHRM session after signing in manually")
    public void saveSessionAfterManualSignIn() throws IOException {
        Page page = PlaywrightFactory.getPage();
        try {
            new OrangeHRMLoginPage(page).waitForDashboard(MANUAL_LOGIN_TIMEOUT.toMillis());
        } catch (TimeoutError e) {
            throw new AssertionError(
                    "OrangeHRM dashboard did not appear within five minutes. Rerun @OKTA_AUTH_CAPTURE and complete sign-in.",
                    e);
        }

        String storageState = page.context().storageState();
        Path directory = Files.createDirectories(STATE_FILE.getParent());
        Path pendingState = Files.createTempFile(directory, "okta_orangehrm-", ".tmp");
        try {
            Files.writeString(pendingState, storageState);
            Files.move(pendingState, STATE_FILE,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(pendingState);
        }
        System.out.println("Saved OrangeHRM session to " + STATE_FILE);
    }

    @Given("I open {string} using the saved OrangeHRM session")
    public void openWithSavedSession(String dashboardUrl) {
        Assert.assertTrue("Saved OrangeHRM session is missing or unreadable. Run @OKTA_AUTH_CAPTURE first.",
                Files.isRegularFile(STATE_FILE) && Files.isReadable(STATE_FILE));
        PlaywrightFactory.openWithStorageState(STATE_FILE).navigate(dashboardUrl);
    }

    @Then("I should see the OrangeHRM dashboard using the saved session")
    public void verifySavedSession() {
        OrangeHRMLoginPage loginPage = new OrangeHRMLoginPage(PlaywrightFactory.getPage());
        Assert.assertTrue("The saved session did not reach the OrangeHRM dashboard. Rerun @OKTA_AUTH_CAPTURE if the app redirected to login.",
                loginPage.isDashboardPageDisplayed());
    }
}
