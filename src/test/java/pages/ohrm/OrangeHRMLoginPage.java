package pages.ohrm;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

public class OrangeHRMLoginPage {

    private final Page page;

    private final Locator loginHeader;
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator forgotPasswordLink;
    private final Locator invalidCredentialsMessage;
    private final Locator orangeHrmLogo;
    private final Locator userDropdown;
    private final Locator dashboardHeader;

    public OrangeHRMLoginPage(Page page) {
        this.page = page;

        loginHeader = page.locator("//h5[normalize-space()='Login']");
        usernameField = page.locator("input[name='username']");
        passwordField = page.locator("input[name='password']");
        loginButton = page.locator("button[type='submit']");
        forgotPasswordLink = page.locator("//p[contains(@class,'orangehrm-login-forgot-header')]");
        invalidCredentialsMessage = page.locator("//p[contains(@class,'oxd-alert-content-text')]");
        orangeHrmLogo = page.locator("//div[contains(@class,'orangehrm-login-logo')]");
        userDropdown = page.locator("//span[contains(@class,'oxd-userdropdown-tab')]");
        dashboardHeader = page.locator("//h6[normalize-space()='Dashboard']");
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }

    public void enterUsername(String username) {
        usernameField.fill(username);
    }

    public void enterPassword(String password) {
        passwordField.fill(password);
    }

    public void clickOnLoginButton() {
        loginButton.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickOnLoginButton();
    }

    public String getLoginHeaderText() {
        return loginHeader.textContent().trim();
    }

    public String getInvalidCredentialsMessageText() {
        invalidCredentialsMessage.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        return invalidCredentialsMessage.textContent().trim();
    }

    public void clickOnForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public boolean isLoginPageDisplayed() {
        return isVisible(loginHeader);
    }

    public boolean isOrangeHrmLogoDisplayed() {
        return isVisible(orangeHrmLogo);
    }

    public boolean isUserDropdownDisplayed() {
        return isVisible(userDropdown);
    }

    public boolean isDashboardPageDisplayed() {
        return isVisible(dashboardHeader);
    }

    private boolean isVisible(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE));
            return locator.isVisible();
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
