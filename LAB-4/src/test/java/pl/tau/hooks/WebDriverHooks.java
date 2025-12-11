package pl.tau.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.tau.config.DriverFactory;

import java.time.Duration;

@Getter
public class WebDriverHooks {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = DriverFactory.create();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
