package pl.tau.page;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class PjaStorePage {

    private static final Logger log = LoggerFactory.getLogger(PjaStorePage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String BASE = "https://sklep.pja.edu.pl/";
    private static final String SEARCH_BASE = BASE + "?s=";
    private static final String CART_BASE = BASE + "koszyk/";

    private static final By SEARCH_RESULT_LINKS = By.cssSelector("li.has-thumbnail .post-thumbnail a[href]");
    private static final By ADD_TO_CART_BTN = By.cssSelector("button.single_add_to_cart_button.alt");
    private static final By CART_NOTICE = By.cssSelector(".woocommerce-message");
    private static final By CART_ROWS = By.cssSelector("tr.woocommerce-cart-form__cart-item.cart_item");

    public boolean openHome() {
        log.info("Opening home page: {}", BASE);
        try {
            driver.get(BASE);
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open home page in time", e);
            return false;
        }
    }

    public boolean goToSearchResults(String query) {
        String url = SEARCH_BASE + URLEncoder.encode(query, StandardCharsets.UTF_8);
        log.info("Opening search results: {}", url);

        try {
            driver.get(url);
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open search result page in time", e);
            return false;
        }
    }

    public boolean hasAnyResults() {
        log.info("Checking if any search results exist");

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(SEARCH_RESULT_LINKS, 1));
            int count = driver.findElements(SEARCH_RESULT_LINKS).size();
            log.info("Results found: {}", count);
            return count > 0;
        } catch (TimeoutException e) {
            log.warn("Failed to detect any search results in time", e);
            return false;
        }
    }

    public boolean openFirstResult() {
        log.info("Opening first result page");

        try {
            List<WebElement> links = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(SEARCH_RESULT_LINKS)
            );

            if (links.isEmpty()) {
                log.warn("No result links visible");
                return false;
            }

            WebElement first = links.getFirst();
            String href = first.getAttribute("href");

            wait.until(ExpectedConditions.elementToBeClickable(first)).click();
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains(href),
                    ExpectedConditions.visibilityOfElementLocated(ADD_TO_CART_BTN)
            ));
            log.info("First result page opened");
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open first result page time", e);
            return false;
        }
    }

    public boolean addToCart() {
        log.info("Adding item to cart");

        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART_BTN));
            button.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(CART_NOTICE));
            log.info("Item added to cart (notice visible)");
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to add to cart in time", e);
            return false;
        }
    }

    public boolean openCart() {
        log.info("Opening cart page: {}", CART_BASE);

        try {
            driver.get(CART_BASE);
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open cart page in time", e);
            return false;
        }
    }

    public boolean verifyCartItems(int expectedCount) {
        log.info("Verifying cart items == {}", expectedCount);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBe(CART_ROWS, expectedCount));
            log.info("Cart verified");
            return true;
        } catch (TimeoutException e) {
            int actual = driver.findElements(CART_ROWS).size();
            log.warn("Cart verification failed. Expected {}, actual {}", expectedCount, actual);
            return false;
        }
    }
}
