package stepDefinitions;



import Factory.DriverFactory2;
import Pages.cis.CISCommonPage;

import io.cucumber.java.en.Then;

import utils.ElementUtils;

public class CISSteps {


    private CISCommonPage cisCommonPage;

    private ElementUtils elementUtils;

    public CISSteps() {

        cisCommonPage = new CISCommonPage();
        elementUtils = new ElementUtils(DriverFactory2.getDriver());
    }

    @Then("Community Information System application is displayed")
    public void communityInformationSystemApplicationIsDisplayed() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! This is inside the step with examples");
    }


}