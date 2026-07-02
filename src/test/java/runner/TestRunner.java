package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
         tags = "@OHRM",
        // tags = "@TestCase5 or @TestCase6 or @TestCase11 or @Functional or @LoginLogout",
        // tags = "@ExternalUserOperations or @Login or @eConfigWorkflow or @ExternalUserOperations",

        features = "src/test/resources/features",

        glue = {
                "stepDefinitions",
                "Hooks"
        },

        plugin = {
                "summary",
                "rerun:target/failed.txt",
                "html:target/builtInReport",
                "json:target/Cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },

        monochrome = true,
        // dryRun = true,
        snippets = SnippetType.CAMELCASE,
        stepNotifications = true

        // plugin = {
        //     "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        // }
)
public class TestRunner {

}