package pl.tau.pages;

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
public class PjaSearchPage {

    private static final Logger log = LoggerFactory.getLogger(PjaSearchPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String BASE = "https://pja.edu.pl/en/";
    private static final String SEARCH_BASE = BASE + "?s=";

    private static final By COOKIES_ACCEPT_BUTTON = By.cssSelector("button.cscks-acceptAll");
    private static final By SEARCH_RESULT_LINKS = By.cssSelector(".list.list--results .list__item .list__body h2 a[href]");

    public boolean openHome() {
        log.info("Opening home pages: {}", BASE);

        try {
            driver.get(BASE);
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open home pages in time", e);
            return false;
        }
    }

    public boolean acceptCookiesIfPresent() {
        log.info("Accepting cookies");

        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(COOKIES_ACCEPT_BUTTON));
            button.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(COOKIES_ACCEPT_BUTTON));
            log.info("Cookies banner detected and accepted");
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to accept cookies in time", e);
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
            log.warn("Failed to open search results pages in time", e);
            return false;
        }
    }

    public boolean hasAnyResults() {
        log.info("Checking if any search results exist");

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(SEARCH_RESULT_LINKS, 7));
            int count = driver.findElements(SEARCH_RESULT_LINKS).size();
            log.info("Results found: {}", count);
            return count > 0;
        } catch (TimeoutException e) {
            log.warn("Failed to detect any search results in time", e);
            return false;
        }
    }

    public boolean openFirstResult() {
        log.info("Opening first result pages");

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
            wait.until(ExpectedConditions.urlContains(href));
            log.info("First result pages opened");
            return true;
        } catch (TimeoutException e) {
            log.warn("Failed to open first result pages time", e);
            return false;
        }
    }
}
