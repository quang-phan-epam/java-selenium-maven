package tests;

import base.BaseTest;
import config.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;
import utils.AllureAttachmentUtils;

@Epic("Amazon Web Automation")
@Feature("Authentication")
public class LoginTest extends BaseTest {
 /**   //TC-001 - will simulate this case as FAILED to demo error log and capture screenshot

    @Test(groups = {"smoke"},description = "TC01 — Valid credentials should log in and show user greeting")
    @Story("User logs in with valid credentials")
    @Description("Verifies that a registered user can login to BookStore site ")
    @TmsLink("TC-001")
    public void testSuccessfulLogin() {
        log.info("TC01 — Starting successful login test.");
        String userName    = ConfigReader.get("user.username");
        String password = ConfigReader.get("user.password");

        AllureAttachmentUtils.attachText("Test Email Used", userName);
        HomePage homePage = new HomePage(driver)
                .open()
                .clickSignIn()
                .loginWith(userName, password);

        AllureAttachmentUtils.attachScreenshotWithLabel(driver, "After Login Attempt");

        String greeting = homePage.getAccountGreeting();
        log.info("TC01 — Nav greeting after login: [{}]", greeting);

        Assert.assertTrue(greeting.toLowerCase().startsWith("hello"),
                "Expected personalized greeting. Got: " + greeting);
        Assert.assertFalse(greeting.toLowerCase().contains("sign in"),
                "Nav should not say 'sign in' after login. Got: " + greeting);
        log.info("TC01 — PASSED. Login successful.");
    }

    // TC02 — Wrong Password

    @Test(groups = {"smoke"}, description = "TC03 — Valid email + wrong password should show password error")
    @Story("User enters an incorrect password")
    @Description("Verifies that incorrect password returns error message")
    @TmsLink("TC-002")
    public void testWrongPassword() {
        log.info("TC02 — Starting wrong password test.");

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

        log.info("TC02 — PASSED. Password error displayed correctly.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC03 — Valid Email Advances to Password Step
    // ─────────────────────────────────────────────────────────────────────────
    @Parameters("env")
    @Test(description = "TC05 — Registered email should advance to the password screen")
    @Story("User successfully completes the email step")
    @Description("Verifies that entering a registered email and clicking Continue " +
                 "navigates to the password entry step.")
    @TmsLink("TC-003")
    public void testValidEmailAdvancesToPasswordStep() {
        log.info("TC03 — Starting email-to-password step transition test.");

        String email = ConfigReader.get("user.email");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .clickSignIn();

        loginPage.enterEmail(email)
                 .clickContinue();

        Assert.assertTrue(loginPage.isPasswordFieldVisible(),
                "Password field should appear after a valid email is accepted.");

        log.info("TC03 — PASSED. Password step reached successfully.");
    }*/

 // ─────────────────────────────────────────────────────────────────────────
 // TC-03: Successful login with valid credentials
 // ─────────────────────────────────────────────────────────────────────────
 @Test(priority = 1,description = "Login successfully with valid credentials")
 @Story("Valid login")
 @Description("Navigate to the BookStore, click Login, enter valid credentials, and verify the Profile page loads.")
 @Severity(SeverityLevel.BLOCKER)
 public void testValidLogin() {
     String username = ConfigReader.get("user.username");
     String password = ConfigReader.get("user.password");

     HomePage homePage = new HomePage(driver).open();

     LoginPage loginPage = new HomePage(driver)
             .clickLogin();

     ProfilePage profilePage = loginPage
             .Login(username,password);

     Assert.assertTrue(profilePage.isLoggedIn(),
             "User should be redirected to Profile page after successful login.");

     Assert.assertEquals(
             profilePage.getLoggedInUsername().toLowerCase(),
             username.toLowerCase(),
             "Logged-in username should match the one used to log in.");
 }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-04: Failed login with invalid credentials
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 2, description = "Login fails with invalid credentials")
    @Story("Invalid login")
    @Description("Attempt login with wrong credentials and verify the user stays on the Login page.")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLogin() {
        HomePage homePage = new HomePage(driver).open();

        LoginPage loginPage = new HomePage(driver)
                .clickLogin();

        loginPage.enterUsername("invalidUser_xyz");
        loginPage.enterPassword("WrongPass999!");
        loginPage.clickLoginExpectingFailure();

        Assert.assertEquals(loginPage.getInvalidMessage(),"Invalid username or password!");

        Assert.assertTrue(loginPage.isStillOnLoginPage(),
                "User should remain on the Login page after invalid credentials.");
    }
}
