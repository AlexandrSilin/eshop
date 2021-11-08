package ru.eshop.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import ru.eshop.DriverInitializer;

public class LoginSteps {
    private static WebDriver webDriver;

    @Given("I open web browser")
    public void iOpenWebBrowser() {
        webDriver = DriverInitializer.getDriver();
    }

    @When("I navigate to login.html page")
    public void iNavigateToLoginHtmlPage() {
        webDriver.get(DriverInitializer.getProperty("login.url"));
    }

    @And("I provide username as {string} and password as {string}")
    public void iProvideUsernameAsAndPasswordAs(String arg0, String arg1) {

    }

    @And("I click on login button")
    public void iClickOnLoginButton() {
    }

    @Then("name should be {string}")
    public void nameShouldBe(String arg0) {
    }

    @And("click logout button")
    public void clickLogoutButton() {
    }

    @Then("user logged out")
    public void userLoggedOut() {
    }
}
