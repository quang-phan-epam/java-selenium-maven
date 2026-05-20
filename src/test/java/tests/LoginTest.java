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

 // ─────────────────────────────────────────────────────────────────────────
 // TC-01: Successful login with valid credentials
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
    // TC-02: Failed login with invalid credentials
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
