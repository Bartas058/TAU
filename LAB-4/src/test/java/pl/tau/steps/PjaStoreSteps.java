package pl.tau.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pl.tau.hooks.WebDriverHooks;
import pl.tau.pages.PjaStorePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class PjaStoreSteps {

    private final WebDriverHooks webDriverHooks;
    private PjaStorePage page;

    @Given("I open the PJATK store homepage")
    public void openHome() {
        page = new PjaStorePage(webDriverHooks.getDriver(), webDriverHooks.getWait());
        assertTrue(page.openHome(), "Home pages is opened");
    }

    @When("I search for the product {string} in the PJATK store")
    public void goToSearchResults(String query) {
        assertTrue(page.goToSearchResults(query),
                "Search returns results for '" + query + "'");
    }

    @Then("I should see at least one search result in the store")
    public void hasAnyResults() {
        assertTrue(page.hasAnyResults(), "Search results exist");
    }

    @When("I open the first product from the search results in the store")
    public void openFirstResult() {
        assertTrue(page.openFirstResult(), "First result is opened");
    }

    @And("I add the product to the cart")
    public void addToCart() {
        assertTrue(page.addToCart(), "Item is added to cart");
    }

    @And("I open the cart page")
    public void openCart() {
        assertTrue(page.openCart(), "Cart pages is opened");
    }

    @Then("the cart should contain exactly 1 product")
    public void verifyCartItems() {
        int expected = 1;
        assertTrue(page.verifyCartItems(expected),
                "Cart contains exactly " + expected + " item(s)");
    }
}
