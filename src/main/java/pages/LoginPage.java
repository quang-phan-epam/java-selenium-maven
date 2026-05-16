package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    /* ── Step 1: Email ──────────────────────────────────────────────────────────
    @FindBy(id = "ap_email_login")
    @CacheLookup
    private WebElement emailField;

    @FindBy(className = "a-button-input")
    @CacheLookup
    private WebElement continueButton;

    // ── Step 2: Password ───────────────────────────────────────────────────────
    @FindBy(id = "ap_password")
    @CacheLookup
    private WebElement passwordField;

    @FindBy(id = "signInSubmit")
    @CacheLookup
    private WebElement signInButton;

    // ── Errors ─────────────────────────────────────────────────────────────────
    @FindBy(id = "auth-error-message-box")
    private WebElement errorMessageBox;

    @FindBy(id = "auth-email-missing-alert")
    private WebElement emailMissingAlert;

    // ── Post-login ─────────────────────────────────────────────────────────────
    @FindBy(id = "nav-link-accountList-nav-line-1")
    private WebElement navGreetingLine;*/

    @FindBy(id = "userName")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login")
    private WebElement loginButton;

    // Shown after a failed login attempt
    @FindBy(id = "name")
    private WebElement invalidMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ────────────────────────────────────────────────────────────────

    @Step("Enter username")
    public LoginPage enterUsername(String username) {
        log.info("Entering username into Username field.");
        type(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        log.info("Entering password (masked).");
        type(passwordInput, password);
        return this;
    }

    @Step("Click Login button")
    public ProfilePage Login(String username, String password) {
        log.info("Clicking Loging button — Navigate to Profile page");
        enterUsername(username);
        enterPassword(password);
        scrollAndClick(loginButton);
        return new ProfilePage(driver);
    }

    @Step("Assert that Username textfield is visible")
    public boolean isUsernameFieldVisible() {
        boolean visible = isDisplayed(usernameInput);
        log.debug("isPasswordFieldVisible() → [{}]", visible);
        return visible;
    }

    @Step("Assert that Password textfield is visible")
    public boolean isPasswordFieldVisible() {
        boolean visible = isDisplayed(passwordInput);
        log.debug("isPasswordFieldVisible() → [{}]", visible);
        return visible;
    }

    @Step("Click Login button — expecting failure")
    public LoginPage clickLoginExpectingFailure() {
        log.info("Clicking Login button — expecting failure.");
        scrollAndClick(loginButton);
        return this;
    }

    @Step("Verify still on Login page (login failed)")
    public boolean isStillOnLoginPage() {
        boolean onPage = driver.getCurrentUrl().contains("/login");
        log.info("Still on login page: {}", onPage);
        return onPage;
    }

    @Step("Get invalid credentials message")
    public String getInvalidMessage() {
        // On DemoQA, failed login shows an "Invalid username or password!" message
        if (isDisplayed(invalidMessage)) {
            String errorMessage = getText(invalidMessage);
            log.warn("Error message displayed on page: [{}]", errorMessage);
            return errorMessage;
        }
        // Fallback: confirm we're still on the login page
        return driver.getCurrentUrl().contains("/login") ? "Login failed" : "";
    }
}
