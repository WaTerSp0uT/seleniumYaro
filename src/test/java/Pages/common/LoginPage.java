package Pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Factory.DriverFactory2;
import utils.CommonUtils;
import utils.ElementUtils;

public class LoginPage {

    private WebDriver driver;
    private ElementUtils elementUtils;

    public LoginPage() {
        this.driver = DriverFactory2.getDriver();
        PageFactory.initElements(driver, this);
        elementUtils = new ElementUtils(driver);
    }

    @FindBy(id = "userid")
    private WebElement userIdField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(name = "btn_login")
    private WebElement btnLogin;

    @FindBy(linkText = "Click here to request new privileges.")
    private WebElement LoginMessage;

    @FindBy(linkText = "NDGrants AWS UAT External")
    // @FindBy(xpath = "//a[contains(text(),'NDGrants AWS UAT External')]")
    private WebElement AWSUAExternal_Link;

    @FindBy(linkText = "DFSC Internal")
    private WebElement DFSClink;

    @FindBy(linkText = "Logout")
    private WebElement btnLogout;

    @FindBy(xpath = "//table[3]/tbody/tr/td/table[1]/tbody/tr/td")
    private WebElement LogoutMessage;

    // @FindBy(xpath = "//a[contains(text(),'Click here to continue without any profile updating.')]")
    @FindBy(xpath = "//a[contains(text(),'Click here to continue')]")
    public WebElement clickToContinueLink;

    @FindBy(xpath = "//*[@id='topNav']/div[3]/div/ul/li/a")
    public WebElement usernameArrow;

    @FindBy(xpath = "//a[contains(text(),'Logout')]")
    public WebElement logoutLink;

    @FindBy(xpath = "//a[contains(text(),'Please click here to return to the login home page')]")
    public WebElement returnToLoginHomePageLink;

    @FindBy(xpath = "//a[contains(text(),'Please click here to return to the login home page')]")
    public WebElement returnHomepageText;

    public void enterUserID(String userId) {
        elementUtils.typeTextIntoElement(userIdField, userId, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void enterPassword(String passwordText) {
        elementUtils.typeTextIntoElement(passwordField, passwordText, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnLoginButton() {
        elementUtils.clickOnElement(btnLogin, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
        // btnLogin.click();
    }

    public String getLoginMessageText() {
        return elementUtils.getTextFromElement(LoginMessage, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnAWSUAExternal_Link() {
        elementUtils.clickOnElement(AWSUAExternal_Link, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnAWSUAInternal_Link() {
        elementUtils.clickOnElement(DFSClink, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnLogoutButton() {
        elementUtils.clickOnElement(btnLogout, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public String getLogoutMessageText() {
        return elementUtils.getTextFromElement(LogoutMessage, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnClickToContinueLink() {
        elementUtils.clickOnElement(clickToContinueLink, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public void clickOnUsernameArrow() {
        elementUtils.clickOnElement(usernameArrow, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    public String getreturnHomepageText() {
        return elementUtils.getTextFromElement(returnHomepageText, CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }

    @FindBy(linkText = "Mitigation eGrants Application")
    public WebElement mitigationegrantappLink;

    public void clickOnMitigationegrantappLink() {
        elementUtils.clickOnElement(
                mitigationegrantappLink,
                CommonUtils.EXPLICIT_WAIT_BASIC_TIME);
    }
}