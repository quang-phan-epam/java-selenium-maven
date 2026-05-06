package utils;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils — explicit Selenium wait strategies with Log4J2 debug logging.
 */
public class WaitUtils {

    private static final Logger log     = LogManager.getLogger(WaitUtils.class);
    private static final int    TIMEOUT = ConfigReader.getInt("explicit.wait");

    public static WebElement waitForVisible(WebDriver driver, WebElement element) {
        log.debug("Waiting up to {}s for element to be visible: {}", TIMEOUT, element);
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        log.debug("Waiting up to {}s for element to be clickable: {}", TIMEOUT, element);
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForInvisible(WebDriver driver, WebElement element) {
        log.debug("Waiting up to {}s for element to be invisible: {}", TIMEOUT, element);
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public static void waitForUrlContains(WebDriver driver, String fragment) {
        log.debug("Waiting for URL to contain: [{}]", fragment);
        new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
                .until(ExpectedConditions.urlContains(fragment));
    }
}
