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

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySteps {
    private static WebDriver webDriver;

    @Given("I open web browser for category test")
    public void iOpenWebBrowser() {
        webDriver = DriverInitializer.getDriver();
        webDriver.manage().window().maximize();
    }

    @When("I navigate to categories.html page")
    public void iNavigateToCategoriesHtmlPage() {
        webDriver.get(DriverInitializer.getProperty("category.url"));
    }

    @And("I click on Add category button")
    public void iClickOnButton() {
        webDriver.findElement(By.id("add_category")).click();
    }

    @And("I provide category name as {string}")
    public void iProvideCategoryNameAs(String arg0) {
        webDriver.findElement(By.id("name")).sendKeys(arg0);
    }

    @And("I click on submit button")
    public void iClickOnSubmitButton() {
        webDriver.findElement(By.id("submit")).click();
    }

    @Then("I find {string}")
    public void iFind(String arg0) {
        List<WebElement> categories = webDriver.findElements(By.id("category"));
        assertThat(categories).extracting(WebElement::getText).anyMatch(text -> text.contains(arg0));
    }

    @And("I delete {string}")
    public void iDelete(String arg0) {
        List<WebElement> categories = webDriver.findElements(By.id("category"));
        WebElement element = categories.stream().filter(category -> category.findElement(By.id("category_name")).getText()
                .equals(arg0)).collect(Collectors.toList()).get(0);
        element.findElement(By.id("delete")).click();
    }

    @After
    public void quitBrowser() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
