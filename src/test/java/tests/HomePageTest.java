package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import config.ConfigReader;
import pages.HomePage;
import utils.DriverManager;

@Epic("DemoQA BookStore")
@Feature("BookStore Homepage")
public class HomePageTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-01: Verify homepage loads correctly
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Verify user is on the BookStore homepage")
    @Story("Homepage verification")
    @Description("Navigate to the BookStore URL and verify the page header and Login button are visible.")
    @Severity(SeverityLevel.BLOCKER)
    public void testHomepageLoads() {
        HomePage homePage = new HomePage(driver).open();

        Assert.assertTrue(homePage.isPageLoaded(),
                "BookStore page should display header and Login button.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-02: Search for a book and verify it appears in the table
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Search for a book and verify it appears in the results table")
    @Story("Book search")
    @Description("Type 'Git' in the search box and verify 'Git Pocket Guide' appears in the table.")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchBook() throws InterruptedException {
        HomePage homePage = new HomePage(driver).open();

        homePage.searchBook("Pr");

        Assert.assertTrue(homePage.isBookVisible("Programming JavaScript Applications"),
                "Book 'Git Pocket Guide' should appear in the search results.");

        Assert.assertTrue(homePage.getBookCount() > 0,
                "At least one book row should be visible after searching.");
    }
}
