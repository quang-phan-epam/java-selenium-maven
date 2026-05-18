package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;
import config.ConfigReader;

public class AlertsPage extends BasePage {
    // ── Locators ─────────────────────────────────────────────────────────────

    @FindBy(id = "alertButton")
    private WebElement alertButton;

    @FindBy(id = "confirmButton")
    private WebElement confirmButton;

    @FindBy(id = "timerAlertButton")
    private WebElement timerAlertButton;

    @FindBy(id = "confirmResult")
    private WebElement confirmResult;

    // Note: DemoQA has a typo in its HTML — "promt" not "prompt"
    @FindBy(id = "promtButton")
    private WebElement promptButton;

    @FindBy(id = "promptResult")
    private WebElement promptResult;

    private String alertPageURL = ConfigReader.get("alerts.url");

    // ── Constructor ───────────────────────────────────────────────────────────

    public AlertsPage(WebDriver driver) {
        super(driver);
    }

    // ── Alert Actions ─────────────────────────────────────────────────────────

    @Step("Open demo Alert page")
    public AlertsPage open(){
        log.info("Opening Alert page: [{}]", ConfigReader.get("alerts.url"));
        navigateTo(alertPageURL);
        log.info("Alert page loaded. Title: [{}]", driver.getTitle());
        return this;

    }

    @Step("Click Alert button and accept the alert")
    public String clickAlertAndAccept() {
        log.info("Clicking Alert button.");
        scrollAndClick(alertButton);
        waitUtils.waitForAlertPresent();

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        log.info("Alert text: [{}]", alertText);
        alert.accept();
        log.debug("Alert accepted.");
        return alertText;
    }

    @Step("Click Confirm button → accept → get result text")
    public String clickConfirmAndAccept() {
        log.info("Clicking Confirm button.");
        scrollAndClick(confirmButton);
        waitUtils.waitForAlertPresent();

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        log.info("Confirm dialog text: [{}]", alertText);
        alert.accept();                             // click OK
        log.debug("Confirm accepted.");

        String result = getText(confirmResult);
        log.info("Confirm result message: [{}]", result);
        return result;
    }

    @Step("Click Confirm button → dismiss → get result text")
    public String clickConfirmAndDismiss() {
        log.info("Clicking Confirm button (will dismiss).");
        scrollAndClick(confirmButton);
        waitUtils.waitForAlertPresent();

        Alert alert = driver.switchTo().alert();
        alert.dismiss();                            // click Cancel
        log.debug("Confirm dismissed.");

        String result = getText(confirmResult);
        log.info("Confirm result message: [{}]", result);
        return result;
    }

    @Step("Click Prompt button, type '{text}', and accept")
    public String clickPromptAndType(String text) {
        log.info("Clicking Prompt button, will type: [{}]", text);
        safeClick(promptButton);
        waitUtils.waitForAlertPresent();

        Alert alert = driver.switchTo().alert();
        log.info("Prompt text: [{}]", alert.getText());
        alert.sendKeys(text);                       // type into the prompt
        alert.accept();
        log.debug("Prompt accepted with text: [{}]", text);

        String result = getText(promptResult);
        log.info("Prompt result message: [{}]", result);
        return result;
    }
}
