package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FramePage;
import config.ConfigReader;
import utils.DriverManager;

@Epic("DemoQA")
@Feature("iFrame Interactions")
public class FrameTest extends BaseTest {

    private static final String EXPECTED_FRAME_TEXT = "This is a sample page";

    // ─────────────────────────────────────────────────────────────────────────
    // TC-01: Read text from Frame 1
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Read heading text from Frame 1")
    @Story("Switch into frame and read content")
    @Description("Switch into Frame 1, read the heading text, then switch back to main page.")
    @Severity(SeverityLevel.NORMAL)
    public void testReadTextFromFrame1() {
        String frameURL = ConfigReader.get("frames.url");
        FramePage framePage = new FramePage(DriverManager.getDriver());
        framePage.open();
        String text = framePage.getTextFromFrame1();

        Assert.assertEquals(text, EXPECTED_FRAME_TEXT,
                "Frame 1 heading should say '" + EXPECTED_FRAME_TEXT + "'.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-02: Read text from Frame 2
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Read heading text from Frame 2")
    @Story("Switch into frame and read content")
    @Description("Switch into Frame 2, read the heading text, then switch back to main page.")
    @Severity(SeverityLevel.NORMAL)
    public void testReadTextFromFrame2() {
        String frameURL = ConfigReader.get("frames.url");
        FramePage framePage = new FramePage(DriverManager.getDriver());
        framePage.open();
        String text = framePage.getTextFromFrame2();

        Assert.assertEquals(text, EXPECTED_FRAME_TEXT,
                "Frame 2 heading should say '" + EXPECTED_FRAME_TEXT + "'.");
    }
}
