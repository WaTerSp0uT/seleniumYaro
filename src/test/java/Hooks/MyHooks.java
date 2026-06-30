package Hooks;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import Factory.DriverFactory2;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import utils.Library;
import utils.SoftAssert;

public class MyHooks {

    WebDriver driver;
    private String screenshotMode;

    @Before
    public void setup() {

        // Ensures driver instance is created for this thread
        // DriverFactory2.getDriver();

        // Read screenshot mode setting from properties
        screenshotMode = Library.getLibrary()
                .getProperty("screenshotOnStep")
                .toLowerCase()
                .trim();
    }

    @Before
    public void setup_thread() {
        long threadId = Thread.currentThread().getId();
        System.out.println("Running Scenario on Thread ID: " + threadId);
    }

    @AfterStep
    public void tearDown(Scenario scenario) {

        try {
            if (screenshotMode.equals("always")) {
                takeScreenshot(scenario);
            } else if (screenshotMode.equals("failed") && scenario.isFailed()) {
                takeScreenshot(scenario);
            }
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        }
    }

    @After
    public void endAssertions(Scenario scenario) {
        try {

            if (scenario.isFailed()) {
                System.out.println("The scenario has failed, asserting all soft assertions.");
                SoftAssert.assertAll();
            } else {
                System.out.println("The scenario has passed.");
            }

        } catch (Exception e) {
            System.err.println("Error during assertions: " + e.getMessage());
        } finally {
            // Ensures driver is closed even if assertion fails
            DriverFactory2.closeDriver();
        }
    }

    private void takeScreenshot(Scenario scenario) {
        try {
            String scenarioName = scenario.getName().replaceAll(" ", "_");
            byte[] srcScreenshot =
                    ((TakesScreenshot) DriverFactory2.getDriver())
                            .getScreenshotAs(OutputType.BYTES);

            scenario.attach(srcScreenshot, "image/png", scenarioName);

        } catch (Exception e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }
    }
}