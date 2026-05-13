package pages;

import config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(id = "nav-link-accountList")
    private WebElement signInLink;

    @FindBy(id = "nav-logo")
    private WebElement amazonLogo;

    @FindBy(xpath = "//button[contains(text(),'Continue shopping')]")
    private WebElement continueShopping;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Amazon homepage")
    public HomePage open() {
        log.info("Opening Amazon homepage: [{}]", ConfigReader.get("base.url"));
        navigateTo(ConfigReader.get("base.url"));
        log.info("Homepage loaded. Title: [{}]", driver.getTitle());
        return this;
    }

    @Step("Click Sign In link")
    public LoginPage clickSignIn() {
        log.info("Check if the Continue shopping page displays");
        if (isDisplayed(continueShopping)){
            log.info("'Continue Shopping' page detected — clicking to dismiss.");
            click(continueShopping);
            log.info("Interstitial dismissed, proceeding to Sign In.");
        }else {
            log.info("'Continue Shopping' page not detected — proceeding directly.");
        }
        log.info("Clicking Sign In link → navigating to LoginPage.");
        click(signInLink);
        return new LoginPage(driver);
    }

    public boolean isLoaded() {
        boolean loaded = isDisplayed(amazonLogo);
        log.debug("HomePage.isLoaded() → [{}]", loaded);
        return loaded;
    }

    public String getAccountGreeting() {
        String greeting = getText(signInLink);
        log.debug("Account greeting text: [{}]", greeting);
        return greeting;
    }
}
