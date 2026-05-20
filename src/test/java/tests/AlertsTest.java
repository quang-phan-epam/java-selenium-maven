package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AlertsPage;
import config.ConfigReader;
import utils.DriverManager;

@Epic("DemoQA")
@Feature("Alerts / Popups")
public class AlertsTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-01: Simple Alert — accept
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 0, description = "Handle a simple alert popup")
    @Story("Simple alert")
    @Description("Click the Alert button, read the alert text, and accept it.")
    @Severity(SeverityLevel.NORMAL)
    public void testSimpleAlert() {
        AlertsPage page = new AlertsPage(driver)
                .open();

        String alertText = page.clickAlertAndAccept();

        Assert.assertFalse(alertText.isBlank(),
                "Alert text should not be empty.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-02: Confirm popup — accept (OK)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 1, description = "Handle confirm popup — click OK")
    @Story("Confirm popup — accept")
    @Description("Click the Confirm button, accept the dialog, and verify the result message.")
    @Severity(SeverityLevel.NORMAL)
    public void testConfirmPopupAccept() {
        AlertsPage page = new AlertsPage(driver)
                .open();

        String result = page.clickConfirmAndAccept();

        Assert.assertTrue(result.contains("Ok"),
                "Result message should confirm OK was clicked. Got: " + result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-03: Confirm popup — dismiss (Cancel)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 2, description = "Handle confirm popup — click Cancel")
    @Story("Confirm popup — dismiss")
    @Description("Click the Confirm button, dismiss the dialog, and verify the result message.")
    @Severity(SeverityLevel.NORMAL)
    public void testConfirmPopupDismiss() {
        AlertsPage page = new AlertsPage(driver)
                .open();

        String result = page.clickConfirmAndDismiss();

        Assert.assertTrue(result.contains("Cancel"),
                "Result message should confirm Cancel was clicked. Got: " + result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-04: Prompt popup — type text and accept
    // ─────────────────────────────────────────────────────────────────────────
    @Test(priority = 3, description = "Handle prompt popup — type text and confirm")
    @Story("Prompt popup")
    @Description("Click the Prompt button, type a name, accept, and verify the result contains that name.")
    @Severity(SeverityLevel.NORMAL)
    public void testPromptPopup() {
        AlertsPage page = new AlertsPage(driver)
                .open();

        String typedText = "DemoQA Tester";
        String result = page.clickPromptAndType(typedText);

        Assert.assertTrue(result.contains(typedText),
                "Prompt result should contain the typed text. Got: " + result);
    }
}