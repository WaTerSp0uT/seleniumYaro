package Pages.cis;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Factory.DriverFactory2;
import org.junit.*;

import utils.CommonUtils;
import utils.ElementUtils;
import utils.SoftAssert;

public class CISCommonPage {

    private WebDriver driver;
    private ElementUtils elementUtils;
    private WebDriverWait wait;

    public CISCommonPage() {
        this.driver = DriverFactory2.getDriver();
        PageFactory.initElements(driver, this);
        elementUtils = new ElementUtils(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @FindBy(xpath = "//input[@value='Save']")
    public WebElement btn_Save;

    @FindBy(xpath = "//input[@type='reset']")
    public WebElement btn_Reset;

    @FindBy(xpath = "//input[@value='Continue']")
    public WebElement btn_Continue;

    @FindBy(xpath = "//input[@value='Submit']")
    public WebElement button_Submit;

    @FindBy(xpath = "//input[@value='Reset']")
    public WebElement button_Reset;

    @FindBy(xpath = "//input[@value='New']")
    public WebElement button_New;

    @FindBy(xpath = "//input[@value='Suspend']")
    public WebElement button_Suspend;

    public WebElement returnDropdownElement(String selectName) {
        return driver.findElement(
                By.xpath("//b[contains(text(),'" + selectName + "')]/../following-sibling::td[1]//select")
        );
    }


    public void validateCellDataOnPreLoadedPage(String cellName, String expectedDataToValidate) {
        WebElement elementWithData = driver
                .findElement(By.xpath("//td[contains(text(),'" + cellName + "')]/following-sibling::td[1]"));

        String actualData = elementUtils.getTextFromElement(elementWithData, 5);
        Assert.assertEquals("Expected data to be equal", expectedDataToValidate, actualData);
    }

    public void validateCellDataOnCEOContactPage(String cellName, String expectedDataToValidate) {
        WebElement elementWithData = driver
                .findElement(By.xpath("//td[contains(text(),'" + cellName + "')]/following-sibling::td[1]/input[2]"));

        String actualData = elementUtils.getValueFromElement(elementWithData, 5);
        Assert.assertEquals("Expected data to be equal", expectedDataToValidate, actualData);
    }

    public void validateCellDataOnLoadedPage(String cellName, String expectedDataToValidate) {
        WebElement elementWithData = driver
                .findElement(By.xpath("//b[contains(text(),'" + cellName + "')]/../following-sibling::td[1]"));

        String actualData = elementUtils.getTextFromElement(elementWithData, 5);
        Assert.assertEquals("Expected data to be equal", expectedDataToValidate, actualData);
    }

    public void validateThatLinkIsNotPresentOnPage(String linkText) {
        List<WebElement> elements = driver.findElements(By.linkText(linkText));

        Assert.assertTrue(
                "Expected link with text " + linkText + " to be absent, but it was found.",
                elements.isEmpty());
    }

    public void clickButtonOfTheLinkNameOnCISPage(String buttonName, String linkText) {

        WebElement elementToClick = DriverFactory2.getDriver().findElement(
                By.xpath("//td/a[contains(text(),'" + linkText
                        + "')]/../../../../td//input[@title='" + buttonName + "']"));

        System.out.println("(//td//a[contains(text(),'" + linkText
                + "')])[2]/../../../td//input[@title='" + buttonName + "']");

        elementUtils.clickOnElement(elementToClick, 5);
    }

    public void enterDataToInputboxOnCisPage(String inputData, String elementName) {

        WebElement inputElement = DriverFactory2.getDriver()
                .findElement(By.xpath("//*[contains(text(),'" + elementName
                        + "')]/following-sibling::*/input"));

        elementUtils.typeTextIntoElement(inputElement, inputData, 5);
    }




    public void validateThatThePageIsLoadedWithTheFollowingElementsContains(List<String> reportDataList) {

        for (String data : reportDataList) {
            data = data.replace("\"", "");

            // Remaining code is cut off in the screenshot.
        }
    }



    public void validateThatThePageIsLoadedWithTheFollowingElementsExact(List<String> reportDataList) {
        for (String data : reportDataList) {
            data = data.replace("\"", "");
            elementUtils.validatePartialTextInElement(
                    driver.findElement(By.xpath("//*[.='" + data + "']")), data);
        }
    }


    public void validateThatThePageIsLoadedWithTheFollowingLinkElements(List<String> reportDataList) {
        for (String data : reportDataList) {
            data = data.replace("\"", "");
            elementUtils.validatePartialTextInElement(
                    driver.findElement(By.xpath("//a[contains(text(),'" + data + "')]")), data);
        }
    }

    public void validateThatSelectDropDownHasOptionSelected(String selectName, String expectedOptionToVerify) {

        WebElement dropDownElement = DriverFactory2.getDriver()
                .findElement(By.xpath("//select[@name='" + selectName + "']"));

        Select select = new Select(dropDownElement);

        String actualSelectOption = select.getFirstSelectedOption().getText().trim();

        Assert.assertEquals(
                "Select option does not match the expected value for ropdown" + expectedOptionToVerify,
                expectedOptionToVerify,
                actualSelectOption);
    }

    public void selectOptionFromDropDown(String optionToSelect, String selectName) {

        WebElement dropDownElement = DriverFactory2.getDriver()
                .findElement(By.xpath("//select[@name='" + selectName + "']"));

        Select select = new Select(dropDownElement);
        select.selectByContainsVisibleText(optionToSelect);
    }

    public void validateTableRowWithCellDataPresent(String cellData) {

        WebElement cellDataWebElement = DriverFactory2.getDriver()
                .findElement(By.xpath("(//td[contains(text(),'" + cellData + "')])[last()]"));

        Assert.assertTrue(
                "Expected celldata" + cellDataWebElement.getText() + " to be present, but it was not found.",
                cellDataWebElement.isDisplayed());
    }
}