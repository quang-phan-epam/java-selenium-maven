package utils;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final Logger log     = LogManager.getLogger(WaitUtils.class);
    private static final int    TIMEOUT = ConfigReader.getInt("explicit.wait");
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver){
        // Use a longer timeout on CI to account for slower runners
        boolean isCI   = Boolean.parseBoolean(
                System.getProperty("ci.environment", "false"));
        int     timeout = isCI
                ? ConfigReader.getInt("ci.timeout.seconds")
                : ConfigReader.getInt("page.load.timeout");

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        log.debug("WaitUtils initialised with {}s timeout (ci={})", timeout, isCI);
    }
    public WebElement waitForVisible(WebElement element) {
        log.debug("Waiting up to {}s for element to be visible: {}", TIMEOUT, element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForClickable(WebDriver driver, WebElement element) {
        log.debug("Waiting up to {}s for element to be clickable: {}", TIMEOUT, element);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForInvisible(WebDriver driver, WebElement element) {
        log.debug("Waiting up to {}s for element to be invisible: {}", TIMEOUT, element);
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForUrlContains(WebDriver driver, String fragment) {
        log.debug("Waiting for URL to contain: [{}]", fragment);
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.urlContains(fragment));
    }
    public void waitForAlertPresent() {
        log.debug("Waiting for alert to be present.");
        wait.until(ExpectedConditions.alertIsPresent());
    }

}
