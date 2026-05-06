package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * AllureAttachmentUtils
 * Attaches screenshots, text, and page source to the Allure report.
 */
public class AllureAttachmentUtils {

    private static final Logger log = LogManager.getLogger(AllureAttachmentUtils.class);

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver) {
        log.debug("Attaching screenshot to Allure report.");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void attachScreenshotWithLabel(WebDriver driver, String label) {
        log.debug("Attaching screenshot with label: [{}]", label);
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(label, "image/png", new ByteArrayInputStream(screenshot), "png");
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(WebDriver driver) {
        log.debug("Attaching page source to Allure report.");
        return driver.getPageSource();
    }

    @Attachment(value = "{label}", type = "text/plain")
    public static String attachText(String label, String content) {
        return content;
    }
}
