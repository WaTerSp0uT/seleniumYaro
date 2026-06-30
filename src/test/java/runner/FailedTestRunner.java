package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/failed.txt",

        glue = {
                "stepDefinitions",
                "Hooks"
        },

        plugin = {
                "summary",
                "rerun:target/failed.txt",
                "html:target/builtInReport_rerun",
                "json:target/Cucumber_rerun.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },

        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        stepNotifications = true
)
public class FailedTestRunner {

}