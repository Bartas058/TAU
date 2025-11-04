package pl.tau.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.tau.core.BaseTest;
import pl.tau.page.PjaSearchPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PjaSearchTest extends BaseTest {

    @Test
    @Tag("PJA")
    @Tag("Website")
    @DisplayName("PJATK Website - Search and open first result")
    void searchTestAndOpenFirstResult() {
        PjaSearchPage page = new PjaSearchPage(driver, wait);

        assertTrue(page.openHome(), "Home page is opened");
        assertTrue(page.acceptCookiesIfPresent(), "Cookies are accepted");
        assertTrue(page.goToSearchResults("test"), "Search returns results for 'test'");
        assertTrue(page.hasAnyResults(), "Search results exist for 'test'");
        assertTrue(page.openFirstResult(), "First result is opened");
    }
}
