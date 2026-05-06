package utils;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverManager
 *
 * Creates, stores, and destroys WebDriver per thread.
 * ThreadLocal keeps parallel tests isolated.
 * Log4J2 logs every lifecycle event for CI/CD debugging.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverTL = new ThreadLocal<>();

    // ── Init ───────────────────────────────────────────────────────────────────

    public static void initDriver() {
        String  browser  = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        int     pageLoad = ConfigReader.getInt("page.load.timeout");

        log.info("Initializing WebDriver — browser=[{}] headless=[{}]", browser, headless);

        WebDriver driver;
        try {
            driver = switch (browser) {
                case "firefox" -> createFirefoxDriver(headless);
                case "edge"    -> createEdgeDriver(headless);
                default        -> createChromeDriver(headless);
            };
        } catch (Exception e) {
            log.fatal("Failed to initialize WebDriver for browser [{}]: {}", browser, e.getMessage(), e);
            throw new RuntimeException("WebDriver initialization failed.", e);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        driverTL.set(driver);
        log.info("WebDriver initialized successfully — session ready.");
    }

    // ── Accessor ───────────────────────────────────────────────────────────────

    public static WebDriver getDriver() {
        WebDriver driver = driverTL.get();
        if (driver == null) {
            log.error("getDriver() called before initDriver() — driver is null.");
            throw new IllegalStateException("WebDriver not initialized. Call initDriver() first.");
        }
        return driver;
    }

    // ── Teardown ───────────────────────────────────────────────────────────────

    public static void quitDriver() {
        WebDriver driver = driverTL.get();
        if (driver != null) {
            log.info("Quitting WebDriver session.");
            try {
                driver.quit();
                log.debug("WebDriver quit successfully.");
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverTL.remove();
            }
        } else {
            log.warn("quitDriver() called but no driver found in ThreadLocal.");
        }
    }

    // ── Browser Factories ──────────────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        log.debug("Setting up ChromeDriver via WebDriverManager.");
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        if (headless) {
            opts.addArguments("--headless=new");
            log.debug("Chrome launched in headless mode.");
        }
        opts.addArguments(
            "--start-maximized",
            "--disable-notifications",
            "--disable-extensions",
            "--no-sandbox",             // required for GitHub Actions (Linux)
            "--disable-dev-shm-usage"   // prevents crashes in Docker/CI containers
        );
        return new ChromeDriver(opts);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        log.debug("Setting up FirefoxDriver via WebDriverManager.");
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opts = new FirefoxOptions();
        if (headless) opts.addArguments("--headless");
        return new FirefoxDriver(opts);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        log.debug("Setting up EdgeDriver via WebDriverManager.");
        WebDriverManager.edgedriver().setup();
        EdgeOptions opts = new EdgeOptions();
        if (headless) opts.addArguments("--headless=new");
        opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return new EdgeDriver(opts);
    }
}
