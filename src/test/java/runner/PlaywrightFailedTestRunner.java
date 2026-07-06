package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/playwright-failed.txt",

        glue = {
                "stepDefinitions",
                "hooks"
        },

        plugin = {
                "summary",
                "rerun:target/playwright-failed.txt",
                "html:target/playwright-builtInReport_rerun",
                "json:target/PlaywrightCucumber_rerun.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },

        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        stepNotifications = true
)
public class PlaywrightFailedTestRunner {

}
