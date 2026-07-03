package Pages.OHRM;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import Factory.DriverFactory2;
import utils.CommonUtils;
import utils.ElementUtils;

public class OrangeHRMLoginPage {

    private WebDriver driver;
    private ElementUtils elementUtils;
    private WebDriverWait wait;

    public OrangeHRMLoginPage() {
        this.driver = DriverFactory2.getDriver();
        PageFactory.initElements(driver, this);
        elementUtils = new ElementUtils(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @FindBy(xpath = "//h5[normalize-space()='Login']")
    public WebElement loginHeader;

    @FindBy(name = "username")
    public WebElement usernameField;

    @FindBy(name = "password")
    public WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement button_Login;

    @FindBy(xpath = "//p[contains(@class,'orangehrm-login-forgot-header')]")
    public WebElement forgotPasswordLink;

    @FindBy(xpath = "//p[contains(@class,'oxd-alert-content-text')]")
    public WebElement invalidCredentialsMessage;

    @FindBy(xpath = "//div[contains(@class,'orangehrm-login-logo')]")
    public WebElement orangeHrmLogo;

    @FindBy(xpath = "//span[contains(@class,'oxd-userdropdown-tab')]")
    public WebElement userDropdown;

    @FindBy(xpath = "//h6[normalize-space()='Dashboard']")
    public WebElement dashboardHeader;

    public void enterUsername(String username) {
        elementUtils.typeTextIntoElement(
                usernameField,
                username,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void enterPassword(String password) {
        elementUtils.typeTextIntoElement(
                passwordField,
                password,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnLoginButton() {
        elementUtils.clickOnElement(
                button_Login,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickOnLoginButton();
    }

    public String getLoginHeaderText() {
        return elementUtils.getTextFromElement(
                loginHeader,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public String getInvalidCredentialsMessageText() {
        return elementUtils.getTextFromElement(
                invalidCredentialsMessage,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnForgotPasswordLink() {
        elementUtils.clickOnElement(
                forgotPasswordLink,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public boolean isLoginPageDisplayed() {
        return elementUtils.displayStatusOfElement(
                loginHeader,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public boolean isUserDropdownDisplayed() {
        return elementUtils.displayStatusOfElement(
                userDropdown,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public boolean isDashboardPageDisplayed() {
        return elementUtils.displayStatusOfElement(
                dashboardHeader,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }
}
