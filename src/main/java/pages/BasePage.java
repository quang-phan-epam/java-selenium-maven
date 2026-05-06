package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * BasePage
 *
 * Parent of all Page Object classes.
 *
 * Log4J2 + @Step together give two views of the same event:
 *   → Log4J2  writes to console + file  (persists across sessions, great for CI)
 *   → @Step   writes to Allure report   (visual timeline, great for sharing)
 */
public abstract class BasePage {

    // Each subclass gets its own logger via getClass() — log lines show
    // "LoginPage" or "HomePage" instead of the generic "BasePage".
    protected final Logger     log;
    protected final WebDriver  driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.log    = LogManager.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
        log.debug("PageFactory.initElements() completed for [{}]", this.getClass().getSimpleName());
    }

    // ── Navigation ─────────────────────────────────────────────────────────────

    @Step("Navigate to: {url}")
    protected void navigateTo(String url) {
        log.info("Navigating to URL: [{}]", url);
        driver.get(url);
        log.debug("Navigation complete. Current URL: [{}]", driver.getCurrentUrl());
    }

    public String getCurrentUrl() { return driver.getCurrentUrl(); }
    public String getTitle()      { return driver.getTitle(); }

    // ── Interactions ───────────────────────────────────────────────────────────

    @Step("Click: {element}")
    protected void click(WebElement element) {
        log.debug("Clicking element: [{}]", element);
        WaitUtils.waitForClickable(driver, element).click();
        log.debug("Click successful.");
    }

    @Step("Type '{text}' into: {element}")
    protected void type(WebElement element, String text) {
        // Mask passwords so they never appear in plain text logs
        String displayText = text.length() > 3 ? text.substring(0, 2) + "***" : "***";
        log.debug("Typing [{}] into element: [{}]", displayText, element);
        WaitUtils.waitForVisible(driver, element).clear();
        element.sendKeys(text);
        log.debug("Type successful.");
    }

    @Step("Get text from: {element}")
    protected String getText(WebElement element) {
        log.debug("Getting text from element: [{}]", element);
        String text = WaitUtils.waitForVisible(driver, element).getText().trim();
        log.debug("Text retrieved: [{}]", text);
        return text;
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            boolean visible = element.isDisplayed();
            log.debug("isDisplayed check → [{}] for element: [{}]", visible, element);
            return visible;
        } catch (Exception e) {
            log.debug("Element not found in DOM — isDisplayed returns false. [{}]", e.getMessage());
            return false;
        }
    }

    // ── JS Helpers ─────────────────────────────────────────────────────────────

    @Step("JS click: {element}")
    protected void jsClick(WebElement element) {
        log.debug("Performing JS click on element: [{}]", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    @Step("Scroll to: {element}")
    protected void scrollTo(WebElement element) {
        log.debug("Scrolling to element: [{}]", element);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", element);
    }
}
