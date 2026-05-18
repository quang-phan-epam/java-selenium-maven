package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfilePage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────

    // Shows the logged-in username
    @FindBy(id = "userName-value")
    private WebElement usernameLabel;

    @FindBy(xpath = "//button[normalize-space()='Log out']")
    private WebElement logoutButton;

    // ── Constructor ───────────────────────────────────────────────────────────

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    // ── Page Actions ──────────────────────────────────────────────────────────

    @Step("Verify user is logged in")
    public boolean isLoggedIn() {
        waitUtils.waitForUrlContains(driver,"/profile");
        boolean onProfile = getCurrentUrl().contains("/profile");
        boolean userShown = isDisplayed(usernameLabel);
        log.info("Login verified → url contains /profile: {}, username label visible: {}",
                onProfile, userShown);
        return onProfile && userShown;
    }

    @Step("Get logged-in username")
    public String getLoggedInUsername() {
        String name = getText(usernameLabel);
        log.info("Logged-in username: [{}]", name);
        return name;
    }

    @Step("Click Logout")
    public void logout() {
        log.info("Logging out.");
        click(logoutButton);
    }
}
