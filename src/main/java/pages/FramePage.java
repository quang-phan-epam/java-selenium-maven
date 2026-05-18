package pages;

import config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FramePage extends BasePage {
    // ── Locators ─────────────────────────────────────────────────────────────
    @FindBy(xpath = "//div[@id=\"framesWrapper\"]/div[@id=\"frame1Wrapper\"]/iframe[@id=\"frame1\"]")
    private WebElement frame1; //*[@id="sampleHeading"]

    @FindBy(xpath = "//div[@id=\"framesWrapper\"]/div[@id=\"frame2Wrapper\"]/iframe[@id=\"frame2\"]")
    private WebElement frame2;

    // This element is INSIDE the frame — accessed after switching into it
    @FindBy(id = "sampleHeading")
    private WebElement sampleHeading;

    // ── Constructor ───────────────────────────────────────────────────────────

    public FramePage(WebDriver driver) {
        super(driver);
    }

    // ── Frame Actions ─────────────────────────────────────────────────────────
    @Step("Open Frame page")
    public FramePage open() {
        log.info("Opening Frame page: [{}]", ConfigReader.get("frames.url"));
        navigateTo(ConfigReader.get("frames.url"));
        log.info("Frame page loaded. Title: [{}]", driver.getTitle());
        return this;
    }

    @Step("Switch into Frame 1")
    public String getTextFromFrame1() {
        waitUtils.waitForVisible(frame1);
        log.debug("frame 2 is showing: "+frame1);
        return getFrameText(frame1, "Frame 1");
    }

    @Step("Switch into Frame 2")
    public String getTextFromFrame2() {
        waitUtils.waitForVisible(frame2);
        log.debug("frame 2 is showing: "+frame2);
        return getFrameText(frame2, "Frame 2");
    }

    private String getFrameText(WebElement frameElement, String frameName) {
        log.info("Switching into {}", frameName);

        // Step 1: switch into the frame
        driver.switchTo().frame(frameElement);
        log.debug("Switched into frame: {}", frameName);

        // Step 2: re-initialise PageFactory INSIDE the frame context
        //         so @FindBy fields are located within the frame DOM
        PageFactory.initElements(driver, this);

        // Step 3: read the text
        String text = getText(sampleHeading);
        log.info("{} heading text: [{}]", frameName, text);

        // Step 4: always switch back to the main page when done
        driver.switchTo().defaultContent();
        log.debug("Switched back to default content.");

        // Step 5: re-initialise PageFactory for the main page context again
        PageFactory.initElements(driver, this);

        return text;
    }
}
