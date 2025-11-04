package pl.tau.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver create() {
        String browser = System.getProperty("browser");
        String headless = System.getProperty("headless");

        if (!browser.trim().equalsIgnoreCase("chrome") && !browser.trim().equalsIgnoreCase("firefox")) {
            throw new IllegalStateException(
                    "Invalid value for 'browser' - allowed values: chrome or firefox (e.g., -Dbrowser=chrome)"
            );
        }

        if (!headless.trim().equalsIgnoreCase("true") && !headless.trim().equalsIgnoreCase("false")) {
            throw new IllegalStateException(
                    "Invalid value for 'headless' - allowed values: true or false (e.g., -Dheadless=true)"
            );
        }

        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions firefox = new FirefoxOptions();
                if (Boolean.parseBoolean(headless)) firefox.addArguments("-headless");
                yield new FirefoxDriver(firefox);
            }
            case "chrome" -> {
                ChromeOptions chrome = new ChromeOptions();
                if (Boolean.parseBoolean(headless)) chrome.addArguments("--headless=new");
                chrome.addArguments(
                        "--no-sandbox",
                        "--disable-gpu",
                        "--window-size=1400,1000",
                        "--disable-dev-shm-usage"
                );
                yield new ChromeDriver(chrome);
            }
            default -> throw new IllegalStateException("Unexpected browser: " + browser);
        };
    }
}
