package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // ── Step 1: Email ──────────────────────────────────────────────────────────
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
    private WebElement navGreetingLine;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ────────────────────────────────────────────────────────────────

    @Step("Enter email")
    public LoginPage enterEmail(String email) {
        log.info("Entering email address into email field.");
        type(emailField, email);
        return this;
    }

    @Step("Click Continue")
    public LoginPage clickContinue() {
        log.info("Clicking Continue button — advancing to password step.");
        click(continueButton);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        log.info("Entering password (masked).");
        type(passwordField, password);
        return this;
    }

    @Step("Click Sign In")
    public HomePage clickSignIn() {
        log.info("Clicking Sign In button — submitting credentials.");
        click(signInButton);
        return new HomePage(driver);
    }

    @Step("Login with email and password")
    public HomePage loginWith(String email, String password) {
        log.info("Starting full two-step login flow.");
        return enterEmail(email)
                .clickContinue()
                .enterPassword(password)
                .clickSignIn();
    }

    // ── State Queries ──────────────────────────────────────────────────────────

    public boolean isPasswordFieldVisible() {
        boolean visible = isDisplayed(passwordField);
        log.debug("isPasswordFieldVisible() → [{}]", visible);
        return visible;
    }

    public boolean isErrorDisplayed() {
        boolean visible = isDisplayed(errorMessageBox);
        log.debug("isErrorDisplayed() → [{}]", visible);
        return visible;
    }

    public boolean isEmailAlertDisplayed() {
        boolean visible = isDisplayed(emailMissingAlert);
        log.debug("isEmailAlertDisplayed() → [{}]", visible);
        return visible;
    }

    public String getErrorText() {
        String text = getText(errorMessageBox);
        log.warn("Error message displayed on page: [{}]", text);
        return text;
    }

    public String getNavGreeting() {
        return getText(navGreetingLine);
    }
}
