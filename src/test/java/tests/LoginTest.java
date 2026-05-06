package tests;

import base.BaseTest;
import config.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.AllureAttachmentUtils;

@Epic("Amazon Web Automation")
@Feature("Authentication")
public class LoginTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC01 — Successful Login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC01 — Valid credentials should log in and show user greeting")
    @Story("User logs in with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies that a registered user can complete the two-step login flow " +
                 "and the nav bar displays a personalised greeting after sign-in.")
    @TmsLink("TC-001")
    public void testSuccessfulLogin() {
        log.info("TC01 — Starting successful login test.");

        String email    = ConfigReader.get("user.email");
        String password = ConfigReader.get("user.password");

        AllureAttachmentUtils.attachText("Test Email Used", email);

        HomePage homePage = new HomePage(driver)
                .open()
                .clickSignIn()
                .loginWith(email, password);

        AllureAttachmentUtils.attachScreenshotWithLabel(driver, "After Login Attempt");

        String greeting = homePage.getAccountGreeting();
        log.info("TC01 — Nav greeting after login: [{}]", greeting);

        Assert.assertTrue(
                greeting.toLowerCase().startsWith("hello"),
                "Expected personalized greeting. Got: " + greeting
        );
        Assert.assertFalse(
                greeting.toLowerCase().contains("sign in"),
                "Nav should not say 'sign in' after login. Got: " + greeting
        );
        log.info("TC01 — PASSED. Login successful.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC02 — Unregistered Email
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC02 — Unregistered email should show account-not-found error")
    @Story("User attempts login with an unregistered email")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that submitting an email not linked to any Amazon account " +
                 "shows the 'We cannot find an account' error message.")
    @TmsLink("TC-002")
    public void testUnregisteredEmail() {
        log.info("TC02 — Starting unregistered email test.");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .clickSignIn();

        loginPage.enterEmail("ghost_user_xyz_404@nowhere.com")
                 .clickContinue();

        log.debug("TC02 — Checking for error message.");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Expected error for unregistered email.");
        Assert.assertTrue(loginPage.getErrorText().toLowerCase().contains("cannot find"),
                "Unexpected error text: " + loginPage.getErrorText());

        log.info("TC02 — PASSED. Error message displayed correctly.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC03 — Wrong Password
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC03 — Valid email + wrong password should show password error")
    @Story("User enters an incorrect password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the correct email but an incorrect password keeps " +
                 "the user on the password screen with an appropriate error.")
    @TmsLink("TC-003")
    public void testWrongPassword() {
        log.info("TC03 — Starting wrong password test.");

        String email = ConfigReader.get("user.email");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .clickSignIn();

        loginPage.enterEmail(email)
                 .clickContinue()
                 .enterPassword("WrongPassword_000!")
                 .clickSignIn();

        log.debug("TC03 — Verifying password error is shown.");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Expected a password error message.");
        Assert.assertTrue(loginPage.getErrorText().toLowerCase().contains("password"),
                "Error did not mention password. Got: " + loginPage.getErrorText());

        log.info("TC03 — PASSED. Password error displayed correctly.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC04 — Empty Email
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC04 — Submitting blank email should trigger a validation error")
    @Story("User submits login form with empty email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the email field shows an inline validation error " +
                 "when Continue is clicked without entering an email.")
    @TmsLink("TC-004")
    public void testEmptyEmail() {
        log.info("TC04 — Starting empty email validation test.");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .clickSignIn();

        loginPage.enterEmail("")
                 .clickContinue();

        Assert.assertTrue(
                loginPage.isErrorDisplayed() || loginPage.isEmailAlertDisplayed(),
                "Expected a validation error for empty email."
        );
        log.info("TC04 — PASSED. Validation error shown for empty email.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC05 — Valid Email Advances to Password Step
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC05 — Registered email should advance to the password screen")
    @Story("User successfully completes the email step")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that entering a registered email and clicking Continue " +
                 "navigates to the password entry step.")
    @TmsLink("TC-005")
    public void testValidEmailAdvancesToPasswordStep() {
        log.info("TC05 — Starting email-to-password step transition test.");

        String email = ConfigReader.get("user.email");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .clickSignIn();

        loginPage.enterEmail(email)
                 .clickContinue();

        Assert.assertTrue(loginPage.isPasswordFieldVisible(),
                "Password field should appear after a valid email is accepted.");

        log.info("TC05 — PASSED. Password step reached successfully.");
    }
}
