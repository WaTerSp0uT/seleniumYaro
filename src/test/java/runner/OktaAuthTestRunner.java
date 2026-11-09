package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/resources/features/okta_auth_capture.feature",
                "src/test/resources/features/okta_auth_reuse.feature"
        },
        tags = "@OKTA_AUTH_REUSE",
        glue = {"stepDefinitions", "hooks"},
        plugin = "summary",
        monochrome = true
)
public class OktaAuthTestRunner {
}
