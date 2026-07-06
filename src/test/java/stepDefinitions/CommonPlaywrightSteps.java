package stepDefinitions;

import io.cucumber.java.en.Given;
import factory.PlaywrightFactory;

public class CommonPlaywrightSteps {

    @Given("I navigate to {string}")
    public void iNavigateTo(String url) {
        PlaywrightFactory.getPage().navigate(url);
    }
}
