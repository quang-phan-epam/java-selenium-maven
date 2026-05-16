package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * BasePage - the Parent of all Page Object classes.
 */
public abstract class BasePage {

    // Each subclass gets its own logger via getClass() — log lines show
    // "LoginPage" or "HomePage" instead of the generic "BasePage".
    protected final Logger     log;
    protected final WebDriver  driver;
    protected final WaitUtils waitUtils;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.log    = LogManager.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
        this.waitUtils = new WaitUtils(driver);
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

    @Step("Type '{text}' into: {element}")
    protected void type(WebElement element, String text) {
        // Mask passwords so they never appear in plain text logs
        String displayText = text.length() > 3 ? text.substring(0, 2) + "***" : "***";
        log.debug("Typing [{}] into element: [{}]", displayText, element);
        waitUtils.waitForVisible(element).clear();
        element.sendKeys(text);
        log.debug("Type successful.");
    }

    @Step("Get text from: {element}")
    protected String getText(WebElement element) {
        log.debug("Getting text from element: [{}]", element);
        String text = waitUtils.waitForVisible(element).getText().trim();
        log.debug("Text retrieved: [{}]", text);
        return text;
    }

    @Step("Check if element is displayed: {element}")
    protected boolean isDisplayed(WebElement element) {
        try {
            waitUtils.waitForVisible(element);
            boolean visible = element.isDisplayed();
            log.debug("isDisplayed check → [{}] for element: [{}]", visible, element);
            return visible;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            log.debug("Element not found in DOM — isDisplayed returns false. [{}]", e.getMessage());
            return false;
        }
    }

    @Step("Click: {element}")
    protected void click(WebElement element) {
        log.debug("Clicking element: [{}]", element);
        waitUtils.waitForClickable(driver, element).click();
        log.debug("Click successful.");
    }

    /**
     * Click via JavaScript — bypasses overlapping elements and ad overlays.
     * Use when standard click() throws ElementClickInterceptedException.
     */
    protected void jsClick(WebElement element) {
        log.debug("JS clicking element: {}", element);
        waitUtils.waitForVisible(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.debug("JS click executed.");
    }

    /**
     * Smart click — tries standard click first, falls back to JS click
     * if an overlay intercepts it.
     */
    protected void safeClick(WebElement element) {
        log.debug("Safe clicking element: {}", element);
        try {
            waitUtils.waitForClickable(driver,element).click();
            log.debug("Standard click succeeded.");
        } catch (ElementClickInterceptedException e) {
            log.warn("Standard click intercepted — falling back to JS click. Reason: {}",
                    e.getMessage());
            jsClick(element);
        }
    }

    @Step("Scrolling element into the visible area to remove the overlap")
    protected void scrollIntoView(WebElement element) {
        log.debug("Scrolling element into view: {}", element);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        // Small pause to let the page settle after scroll
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    @Step ("Combined: scroll then click — use this for DemoQA buttons")
    protected void scrollAndClick(WebElement element) {
        scrollIntoView(element);
        safeClick(element);
    }
}
