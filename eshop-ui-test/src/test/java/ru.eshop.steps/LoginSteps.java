package ru.eshop.steps;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.eshop.DriverInitializer;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {
    private static WebDriver webDriver;

    @Given("I open web browser for login test")
    public void iOpenWebBrowser() {
        webDriver = DriverInitializer.getDriver();
        webDriver.manage().window().maximize();
    }

    @When("I navigate to login.html page")
    public void iNavigateToLoginHtmlPage() {
        webDriver.get(DriverInitializer.getProperty("login.url"));
    }

    @And("I provide username as {string} and password as {string}")
    public void iProvideUsernameAsAndPasswordAs(String arg0, String arg1) {
        WebElement usernameField = webDriver.findElement(By.id("username"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        usernameField.sendKeys(arg0);
        passwordField.sendKeys(arg1);
    }

    @And("I click on login button")
    public void iClickOnLoginButton() {
        webDriver.findElement(By.id("btn-login")).click();
    }

    @Then("name should be {string}")
    public void nameShouldBe(String arg0) throws Exception {
        assertThat(webDriver.findElement(By.id("user")).getText()).isEqualTo(arg0);
    }

    @And("click logout button")
    public void clickLogoutButton() {
        webDriver.findElement(By.id("btn-logout")).click();
    }

    @Then("user logged out")
    public void userLoggedOut() {
        webDriver.findElement(By.id("username"));
        webDriver.findElement(By.id("password"));
    }

    @After
    public void quitBrowser() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
