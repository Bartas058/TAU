package pl.tau.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pl.tau.hooks.WebDriverHooks;
import pl.tau.pages.PjaSearchPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class PjaSearchSteps {

    private final WebDriverHooks webDriverHooks;
    private PjaSearchPage page;

    @Given("I open the PJATK homepage")
    public void openHome() {
        page = new PjaSearchPage(webDriverHooks.getDriver(), webDriverHooks.getWait());
        assertTrue(page.openHome(), "Home pages is opened");
    }

    @And("I accept cookies - if visible")
    public void acceptCookiesIfPresent() {
        assertTrue(page.acceptCookiesIfPresent(), "Cookies are accepted");
    }

    @When("I search for the phrase {string} on the PJATK website")
    public void goToSearchResults(String query) {
        assertTrue(page.goToSearchResults(query),
                "Search returns results for '" + query + "'");
    }

    @Then("I should see at least one search result")
    public void hasAnyResults() {
        assertTrue(page.hasAnyResults(), "Search results exist");
    }

    @And("I open the first search result")
    public void openFirstResult() {
        assertTrue(page.openFirstResult(), "First result is opened");
    }
}
