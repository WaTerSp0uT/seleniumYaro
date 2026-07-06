package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@OHRM_PLAYWRIGHT",

        features = "src/test/resources/features",

        glue = {
                "stepDefinitions",
                "hooks"
        },

        plugin = {
                "summary",
                "rerun:target/playwright-failed.txt",
                "html:target/playwright-builtInReport",
                "json:target/PlaywrightCucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },

        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        stepNotifications = true
)
public class PlaywrightTestRunner {

}
