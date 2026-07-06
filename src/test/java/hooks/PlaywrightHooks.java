package hooks;

import com.microsoft.playwright.Page;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import factory.PlaywrightFactory;
import utils.Library;

public class PlaywrightHooks {

    private String screenshotMode;

    @Before
    public void setup() {
        screenshotMode = getScreenshotMode();
    }

    @Before
    public void setupThread() {
        long threadId = Thread.currentThread().getId();
        System.out.println("Running Playwright Scenario on Thread ID: " + threadId);
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        try {
            if ("always".equals(screenshotMode)) {
                takeScreenshot(scenario);
            } else if ("failed".equals(screenshotMode) && scenario.isFailed()) {
                takeScreenshot(scenario);
            }
        } catch (Exception e) {
            System.err.println("Playwright screenshot capture failed: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        PlaywrightFactory.closePlaywright();
    }

    private void takeScreenshot(Scenario scenario) {
        if (!PlaywrightFactory.hasPage()) {
            return;
        }

        Page page = PlaywrightFactory.getPage();
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true));

        String scenarioName = scenario.getName().replaceAll(" ", "_");
        scenario.attach(screenshot, "image/png", scenarioName);
    }

    private String getScreenshotMode() {
        String mode = Library.getLibrary().getProperty("playwrightScreenshotOnStep");

        if (mode == null || mode.isBlank()) {
            mode = "failed";
        }

        return mode.toLowerCase().trim();
    }
}
