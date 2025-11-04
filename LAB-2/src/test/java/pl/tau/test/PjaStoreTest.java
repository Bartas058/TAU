package pl.tau.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.tau.core.BaseTest;
import pl.tau.page.PjaStorePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PjaStoreTest extends BaseTest {

    @Test
    @Tag("PJA")
    @Tag("Store")
    @DisplayName("PJATK Store - Search, open first result and add to cart")
    void searchCarafeAndOpenFirstResultAndAddToCart() {
        PjaStorePage page = new PjaStorePage(driver, wait);

        assertTrue(page.openHome(), "Home page is opened");
        assertTrue(page.goToSearchResults("karafka"), "Search returns results for 'karafka'");
        assertTrue(page.hasAnyResults(), "Search results exist for 'karafka'");
        assertTrue(page.openFirstResult(), "First result is opened");
        assertTrue(page.addToCart(), "Item is added to cart");
        assertTrue(page.openCart(), "Cart page is opened");
        assertTrue(page.verifyCartItems(1), "Cart contains exactly one item");
    }
}
