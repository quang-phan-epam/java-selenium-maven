package base;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.AllureAttachmentUtils;
import utils.DriverManager;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

/**
 * BaseTest
 *
 * TestNG lifecycle hooks with Log4J2 + Allure integration.
 *
 * Log4J2 logs appear in:  logs/automation.log  and  console
 * Allure attachments appear in: Allure HTML report on failure
 */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        // Log the test name so it's clearly visible in the log file
        log.info("══════════════════════════════════════════════════════");
        log.info("TEST START: [{}]", method.getName());
        log.info("══════════════════════════════════════════════════════");

        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        log.debug("WebDriver assigned to test thread [{}]", Thread.currentThread().getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName   = result.getName();
        String statusText = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP    -> "SKIPPED";
            default                  -> "NA";
        };

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: [{}]", testName);

            Throwable cause = result.getThrowable();
            if (cause != null) {
                log.error("Failure reason: {}", cause.getMessage());
                log.debug("Stack trace:", cause);

                // Attach failure reason to Allure report
                Allure.addAttachment(
                        "Failure Reason",
                        "text/plain",
                        new ByteArrayInputStream(cause.getMessage().getBytes()),
                        "txt"
                );
            }

            // Attach screenshot to Allure — visible in the test detail page
            AllureAttachmentUtils.attachScreenshot(driver);

            // Attach page source for deep DOM debugging
            AllureAttachmentUtils.attachPageSource(driver);

            log.info("Failure artifacts attached to Allure report.");

        } else if (result.getStatus() == ITestResult.SKIP) {
            log.warn("TEST SKIPPED: [{}]", testName);
        }

        log.info("══════════════════════════════════════════════════════");
        log.info("TEST END: [{}] → {}", testName, statusText);
        log.info("══════════════════════════════════════════════════════");

        DriverManager.quitDriver();
    }
}
