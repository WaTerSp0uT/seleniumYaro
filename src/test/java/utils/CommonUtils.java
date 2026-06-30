package utils;


import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import Factory.DriverFactory2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static final long IMPLICIT_WAIT_TIME = 10;
    public static final int PAGE_LOAD_TIME = 15;
    public static final int EXPLICIT_WAIT_BASIC_TIME = 50;

    public static String getEmailWithTimeStamp() {

        Date date = new Date();
        return "D St " + date.toString().replace(" ", "_").replace(":", "_");
    }

    public static String getcurrentDate() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static String getcurrentDatePlusDays(int num) {

        LocalDate today = LocalDate.now(); // Get the current date
        LocalDate tomorrow = today.plusDays(num); // Add one day
        System.out.println("Tomorrow's date: " + tomorrow);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = tomorrow.format(formatter);

        System.out.println("Next day's date: " + formattedDate);
        return formattedDate;
    }

    public static String getRandomDate() {

        //Define year range
        int startYear = 2000;
        int endYear = 2025;

        //Create LocalDate boundaries
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);

        //Generate random day between start and end
        long randomDay = ThreadLocalRandom.current().nextLong(
                startDate.toEpochDay(),
                endDate.toEpochDay() + 1);

        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        //Format to MM/dd/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return randomDate.format(formatter);
    }

    public static String getTextWithTimeStamp(String Text) {
        Date date = new Date();
        return "Text" + date.toString();
    }

    public String getReportTitle() {
        Date date = new Date();
        return "PPR" + date.toString();
    }

    public static String getDateNextYear() {

        LocalDate today = LocalDate.now();
        LocalDate nextYear = today.plusYears(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = nextYear.format(formatter);

        System.out.println("Selected date next year: " + formattedDate);
        return formattedDate;
    }

    public static String getDateInTwoYears() {

        LocalDate today = LocalDate.now();
        LocalDate nextYear = today.plusYears(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = nextYear.format(formatter);

        System.out.println("Selected date next year: " + formattedDate);
        return formattedDate;
    }

    // This method will return current time in the requested timezone
    public static String getCurrentTime(int plusTimeZone) {

        LocalTime currentTime = LocalTime.now().plusHours(plusTimeZone);

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // LocalTime timePlusFive = currentTime.plusHours(5);

        // Format the current time
        String formattedTime = currentTime.format(formatter);

        // LocalDateTime timePlusFive = currentTime.plusHours(5);

        // Print the formatted time
        System.out.println("Current Time: " + formattedTime);
        return formattedTime.toString();
    }

    /**
     * compare a List of WebElements with the List of Strings
     **/
    public static boolean compareLists(List<WebElement> actualList, List<String> expectedList) {

        List<String> textList = new ArrayList<>();

        for (WebElement element : actualList) {
            textList.add(element.getText().strip());
        }

        System.out.println("Actual List as List of Strings ---- " + textList.toString());
        System.out.println("Expected List as List of Strings ---- " + expectedList.toString());

        return textList.equals(expectedList);
    }

    public static void highlightElement(WebDriver driver, WebElement element) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String originalStyle = element.getAttribute("style");

        js.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element,
                "border: 2px solid red;border-style:dashed;"
        );

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Restore to original style
        js.executeScript(
                "arguments[0].setAttribute('style',arguments[1]);",
                element,
                originalStyle
        );
    }

    public static <T, U> List<U> convertIntListToStringList(
            List<T> listOfInteger,
            Function<T, U> function) {

        return Lists.transform(listOfInteger, function);
    }

    public static void waitForPageToLoad() {

        new WebDriverWait(DriverFactory2.getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.jsReturnsValue(
                        "return document.readyState === 'complete';"));
    }

    public static void waitForPdfLoadByTitle(String expectedTitle, Duration timeout) {

        new WebDriverWait(DriverFactory2.getDriver(), timeout)
                .until(ExpectedConditions.titleContains(expectedTitle));
    }

    public static String removeBackslashes(String inputString) {
        if (inputString == null) {
            return null;
        }
        return inputString.replace("\\", "");
    }

    public static String generateRandomAlphanumeric(int length) {

        String ALPHANUMERIC_CHARS =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom secureRandom = new SecureRandom();

        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be negative.");
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_CHARS.length());
            sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String readTextWithJSByValue(WebElement inpElement) {

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory2.getDriver();

        String text = (String) js.executeScript(
                "return arguments[0].value",
                inpElement);

        return text;
    }

    public static String readTextWithJSByTextContent(WebElement inpElement) {

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory2.getDriver();

        String text = (String) js.executeScript(
                "return arguments[0].textContent",
                inpElement);

        return text;
    }

    /**
     * Returns a string that will have 3 consecutive strings
     * that are the same
     */
    public static String findThreeConsecutiveSameStrings(List<String> stringList) {

        if (stringList == null || stringList.size() < 3) {
            return null; // Not enough elements to have three consecutive strings
        }

        for (int i = 0; i <= stringList.size() - 3; i++) {

            String s1 = stringList.get(i);
            String s2 = stringList.get(i + 1);
            String s3 = stringList.get(i + 2);

            if (s1.equals(s2) && s2.equals(s3)) {
                return s1; // Found three consecutive identical strings
            }
        }

        return null; // No three consecutive identical strings found
    }


}