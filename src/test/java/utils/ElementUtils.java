package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.javafaker.Faker;

import Factory.DriverFactory2;

import org.junit.Assert;

public class ElementUtils {

    static WebDriver driver;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
    }

    public static void clickOnElement(WebElement element, long durationInSeconds) {
        WebElement webElement = waitForElement(element, durationInSeconds);
        webElement.click();
    }

    public static void typeTextIntoElement(WebElement element,
                                           String textToBeTyped,
                                           long durationInSeconds) {

        WebElement webElement = waitForElement(element, durationInSeconds);
        webElement.click();
        webElement.clear();
        webElement.sendKeys(textToBeTyped);
    }

    public static WebElement waitForElement(WebElement element,
                                            long durationInSeconds) {

        WebElement webElement = null;

        try {
            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(durationInSeconds));

            webElement = wait.until(
                    ExpectedConditions.elementToBeClickable(element));

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return webElement;
    }

    public static void waitForPageLoad(int timeoutInSeconds) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        wait.until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete");
            }
        });
    }

    public void selectOptionInDropdown(WebElement element,
                                       String dropDownOption,
                                       long durationInSeconds) {

        WebElement webElement =
                waitForElement(element, durationInSeconds);

        Select select = new Select(webElement);
        select.selectByVisibleText(dropDownOption);
    }

    public void selectOptionInDropdown(String dropDownOption,
                                       String selectName) {

        WebElement webElementSelect =
                DriverFactory2.getDriver()
                        .findElement(By.xpath("//select[@name='" + selectName + "']"));

        Select select = new Select(webElementSelect);
        select.selectByVisibleText(dropDownOption);
    }

    public void acceptAlert(long durationInSeconds) {

        try {
            Alert alert = waitForAlert(durationInSeconds);
            alert.accept();

        } catch (Exception e) {
            // Log or ignore if the alert is not present
            System.out.println(
                    "No alert appeared within "
                            + durationInSeconds
                            + " seconds.");
        }
    }

    public void dismissAlert(long durationInSeconds) {

        Alert alert = waitForAlert(durationInSeconds);
        alert.dismiss();
    }

    public void verifyAlertMessage(String expectedAlertMessage) {

        Alert alert = waitForAlert(5);
        String actualAlertText = alert.getText();

        Assert.assertTrue(
                "Expected text to contain: '"
                        + expectedAlertMessage
                        + "', but found: '"
                        + actualAlertText
                        + "'",
                actualAlertText.contains(expectedAlertMessage));
    }

    public Alert waitForAlert(long durationInSeconds) {

        Alert alert = null;

        try {
            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(durationInSeconds));
            alert = wait.until(ExpectedConditions.alertIsPresent());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return alert;
    }

    public void mouseHoverAndClick(WebElement element, long durationInSeconds) {

        WebElement webElement = waitForVisibilityOfElement(element, durationInSeconds);
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
    }

    public WebElement waitForVisibilityOfElement(WebElement element,
                                                 long durationInSeconds) {

        WebElement webElement = null;

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(durationInSeconds));

            webElement = wait.until(ExpectedConditions.visibilityOf(element));

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return webElement;
    }

    public void javaScriptClick(WebElement element, long durationInSeconds) {

        WebElement webElement = waitForVisibilityOfElement(element, durationInSeconds);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click();", webElement);
    }

    public void javaScriptType(WebElement element,
                               long durationInSeconds,
                               String textToBeTyped) {

        WebElement webElement = waitForVisibilityOfElement(element, durationInSeconds);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].value='" + textToBeTyped + "'",
                webElement);
    }

    public static void scrollDownWithJavaScript() {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,200)", "");
    }

    public static void scrollToTheBottomOfPage() {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public String getTextFromElement(WebElement element,
                                     long durationInSeconds) {

        WebElement webElement = waitForElement(element, durationInSeconds);
        return webElement.getText();
    }

    public String getValueFromElement(WebElement element,
                                      long durationInSeconds) {

        WebElement webElement = waitForElement(element, durationInSeconds);
        return webElement.getAttribute("value");
    }

    public boolean displayStatusOfElement(WebElement element,
                                          long durationInSeconds) {

        try {
            WebElement webElement =
                    waitForVisibilityOfElement(element, durationInSeconds);
            return webElement.isDisplayed();

        } catch (Throwable e) {
            return false;
        }
    }

    public boolean isFieldNonEditableById(String fieldId,
                                          long durationInSeconds) {

        try {

            WebElement field = driver.findElement(By.id(fieldId));
            String tagName = field.getTagName();

            return !"input".equalsIgnoreCase(tagName)
                    && !"textarea".equalsIgnoreCase(tagName);

        } catch (Exception e) {
            return false;
        }
    }

    public void selectOptionFromDropdown(List<WebElement> dropDownOptions,
                                         String optionToSelect)
            throws InterruptedException, AWTException {

        Thread.sleep(1000);

        // System.out.println("The Options are : " + dropDownOptions.size());

        for (WebElement option : dropDownOptions) {

            System.out.println("The options are : " + option.getText());

            if (option.getText().contains(optionToSelect)) {

                Thread.sleep(1000);
                option.click();

                System.out.println("Option To select: " + optionToSelect);

                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                break;

                // Thread.sleep(1000);
            }
        }
    }

    public void switchToWindowWithExactTitle(String exactTitle,
                                             int timeoutSeconds)
            throws InterruptedException {

        // ***** NEED to implement WAIT TIME HERE!

        Thread.sleep(3000);

        int elapsed = 0;
        int pollInterval = 500; // in ms

        while (elapsed < timeoutSeconds * 1000) {

            Thread.sleep(3000);

            Set<String> windowHandles = driver.getWindowHandles();

            for (String handle : windowHandles) {

                driver.switchTo().window(handle);
                String currentTitle = driver.getTitle();
                System.out.println(currentTitle);

                if (currentTitle != null &&
                        currentTitle.equals(exactTitle)) {

                    System.out.println(
                            "Switched to window with title: "
                                    + currentTitle);
                    return;
                }
            }

            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(
                        "Thread was interrupted while waiting for window with title: "
                                + exactTitle);
            }

            elapsed += pollInterval;
        }

        throw new RuntimeException(
                "Timeout: No window found with title containing: "
                        + exactTitle);
    }

    public void validatePartialTextInElement(WebElement element,
                                             String expectedPartialText) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(element));

        String actualText = element.getText();

        Assert.assertTrue(
                "Expected text to contain: '" + expectedPartialText
                        + "', but found: '" + actualText + "'",
                actualText.contains(expectedPartialText));
    }

    public static void waitForPageToLoad() {

        new WebDriverWait(DriverFactory2.getDriver(),
                Duration.ofSeconds(30))
                .until(ExpectedConditions.jsReturnsValue(
                        "return document.readyState === 'complete';"));
    }

    public static boolean isInputFieldEmpty(WebElement element) {

        if (element == null) {
            throw new IllegalArgumentException("WebElement is null");
        }

        String value = element.getAttribute("value");
        return value == null || value.trim().isEmpty();
    }

    public void validateDropdownOptions(
            WebElement actual_select_Element,
            List<String> expectedOptions) {

        Select select = new Select(actual_select_Element);

        List<WebElement> optionElements = select.getOptions();
        List<String> actualOptions = new ArrayList<>();

        for (WebElement option : optionElements) {
            actualOptions.add(option.getText().trim());
        }

        List<String> missingOptions = new ArrayList<>();

        for (String expected : expectedOptions) {
            if (!actualOptions.contains(expected)) {
                missingOptions.add(expected);
            }
        }

        Assert.assertTrue(
                "Missing dropdown options: " + missingOptions,
                missingOptions.isEmpty());
    }

    public static void hardWait(Integer seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(
                    "Thread was interrupted during hard wait");
        }
    }

    public void clickLinkByText(String linkText) {
        try {
            By dynamicLink = By.xpath("//a[contains(text(),'" + linkText.trim() + "')]");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));
            link.click();
            System.out.println("Clicked on link: " + linkText);
        } catch (TimeoutException e) {
            throw new RuntimeException("Link with text '" + linkText + "' was not clickable in time", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click link with text: " + linkText, e);
        }
    }

    public void clickLinkByTextExact(String linkText) {
        try {
            By dynamicLink = By.xpath("//a[.='" + linkText.trim() + "']");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));
            link.click();
            System.out.println("Clicked on link: " + linkText);
        } catch (TimeoutException e) {
            throw new RuntimeException("Link with text '" + linkText + "' was not clickable in time", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click link with text: " + linkText, e);
        }
    }

    public void clickLinkByTextExact(String linkText, Integer indexElement) {
        try {
            By dynamicLink = By.xpath("(//a[.='" + linkText.trim() + "'])[" + indexElement + "]");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));
            link.click();
            System.out.println("Clicked on link: " + linkText);
        } catch (TimeoutException e) {
            throw new RuntimeException("Link with text '" + linkText + "' was not clickable in time", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click link with text: " + linkText, e);
        }
    }

    public void clickElementByTextExact(String elementText) {
        try {
            // By dynamicLink = By.xpath("//*[.='" + elementText.trim() + "']");
            By dynamicLink = By.xpath("//*[normalize-space(text())='" + elementText.trim() + "']");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));
            link.click();
            System.out.println("Clicked on link: " + elementText);
        } catch (TimeoutException e) {
            throw new RuntimeException("Link with text '" + elementText + "' was not clickable in time", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click link with text: " + elementText, e);
        }
    }

    public static void waitForPageToLoadWithTitle() {
        new WebDriverWait(DriverFactory2.getDriver(), Duration.ofSeconds(20))
                .until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));

        // Get title
        String title = driver.getTitle();
        Assert.assertNotNull("PageTitle is null", title);
        Assert.assertFalse("Page title is empty", title.trim().isEmpty());

        System.out.println("Page is loaded with title: " + title);
    }

    public void validateTableContainsData(List<String> reportDataList) {
        for (String data : reportDataList) {
            data = data.replace("\\", "");

            validatePartialTextInElement(
                    DriverFactory2.getDriver().findElement(
                            By.xpath("//td[.='" + data + "']")
                    ),
                    data
            );
        }
    }

    public static String getSelectedOptionText(WebElement dropDownElement) {
        Select select = new Select(dropDownElement);
        return select.getFirstSelectedOption().getText();
    }

    /**
     * Find first element with exact attribute-value match.
     * Example: findByAttr(driver, "*", "data-test", "login-btn", 10)
     */
    public static WebElement findByAttr(String tag, String attribute, String value) {
        if (tag == null || tag.isBlank()) {
            tag = "*";
        }

        String predicate = String.format("@%s='%s'", attribute, value);
        String xpath = String.format("//%s[%s]", tag, predicate);

        By dynamicLink = By.xpath(xpath);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement =
                wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));

        return webElement;
    }

    public static WebElement findByAttr(String tag,
                                        String attribute,
                                        String value,
                                        String axis) {

        if (tag == null || tag.isBlank()) {
            tag = "*";
        }
        if (axis == null || axis.isBlank()) {
            axis = "";
        }

        String xpath = String.format("//%s[@%s='%s']%s",
                tag, attribute, value, axis);

        System.out.println("Xpath was created: " + xpath);

        By dynamicLocator = By.xpath(xpath);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement =
                wait.until(ExpectedConditions.elementToBeClickable(dynamicLocator));

        return webElement;
    }

    public static boolean clickCheckboxIfRowExists(String searchText) {

        String xpath = "//tr[td[.='" + searchText + "']]/td/input";

        List<WebElement> elements = driver.findElements(By.xpath(xpath));

        if (!elements.isEmpty()) {
            WebElement checkbox = elements.get(0);

            if (!checkbox.isSelected()) {
                checkbox.click();
            }

            System.out.println("Checkbox clicked for row: " + searchText);
            return true;
        } else {
            System.out.println("Row with text '" + searchText + "' not found. Skipping...");
            return false;
        }
    }

    public static void validateThatElementIsPresent(String xpath) {
        try {
            By dynamicLink = By.xpath(xpath);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(dynamicLink));

            System.out.println("Element is available: " + xpath);
        } catch (TimeoutException e) {
            throw new RuntimeException("Link with text '" + xpath + "' was not clickable in time", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click link with text: " + xpath, e);
        }
    }

    public static void validateThatElementIsVisible(String xpath, String tagName) {
        try {
            By dynamicElement = By.xpath(xpath);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicElement));

            System.out.println("Validation Success: <" + tagName + "> is present with XPath: " + xpath);
        } catch (TimeoutException e) {
            throw new RuntimeException("Element <" + tagName + "> was not visible within 10 seconds. XPath: " + xpath, e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while validating element: " + tagName, e);
        }
    }

    public void sendKeysByXpath(String xpath, String value) {
        try {
            By dynamicInput = By.xpath(xpath);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for presence/visibility before sending keys
            WebElement inputElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(dynamicInput)
            );

            inputElement.sendKeys(value);
            System.out.println("File uploaded to locator: " + xpath);

        } catch (TimeoutException e) {
            throw new RuntimeException("Element not found/visible for upload at: " + xpath, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to: " + xpath, e);
        }
    }

    public static void validateThatElementIsNotVisible(String xpath, String tagName) {
        try {
            By dynamicElement = By.xpath(xpath);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // This returns true if the element is either NOT in the DOM or is hidden
            boolean isNotVisible = wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(dynamicElement)
            );

            if (!isNotVisible) {
                throw new RuntimeException("Element <" + tagName + "> was still visible on the page.");
            }

            System.out.println("Validation Success: <" + tagName + "> is NOT present/visible.");

        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "Element <" + tagName + "> was still visible after 10 seconds. XPath: " + xpath
            );
        }
    }
}