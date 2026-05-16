package pages;

import config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends BasePage {

    /**@FindBy(id = "nav-link-accountList")
    private WebElement signInLink;

    @FindBy(id = "nav-logo")
    private WebElement amazonLogo;

    @FindBy(xpath = "//button[contains(text(),'Continue shopping')]")
    private WebElement continueShopping;
     */

    // ── BookStore page Locators ─────────────────────────────────────────────────────────────

    @FindBy(xpath = "//*[@id=\"item-2\"]/a/span[contains(text(),'Book')]")
    private WebElement pageHeader;

    @FindBy(id = "login")
    private WebElement loginButton;

    @FindBy(id = "searchBox")
    private WebElement searchBox;

    // Each row in the books table
    @FindBy(xpath = "//div[@class='books-wrapper']/div[2]/table/tbody/tr")
    private List<WebElement> bookRows;

    // All book title links in the table
    @FindBy(xpath = "//div[@class='books-wrapper']/div[2]/table/tbody/tr/td[2]//a")
    private List<WebElement> bookTitleLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open BookStore homepage")
    public HomePage open() {
        log.info("Opening BookStore homepage: [{}]", ConfigReader.get("bookstore.url"));
        navigateTo(ConfigReader.get("bookstore.url"));
        log.info("Homepage loaded. Title: [{}]", driver.getTitle());
        return this;
    }

    @Step("Verify Book Store page is loaded")
    public boolean isPageLoaded() {
        boolean searchVisible = isDisplayed(searchBox);
        boolean buttonVisible = isDisplayed(loginButton);
        log.info("BookStore page loaded check → searchBox={} loginBtn={}", searchVisible, buttonVisible);
        return searchVisible && buttonVisible;
    }

    /* this is obsolete
    public boolean isLoaded() {
        boolean loaded = isDisplayed(amazonLogo);
        log.debug("HomePage.isLoaded() → [{}]", loaded);
        return loaded;
    }
    */

    public String getHeaderText() {
        String headerText = getText(pageHeader);
        log.debug("Page header text: [{}]", headerText);
        return headerText;
    }

    @Step("Click Login button → navigate to Login page")
    public LoginPage clickLogin() {
        log.info("Clicking Login link → navigating to LoginPage.");
        click(loginButton);
        return new LoginPage(driver);
    }

    @Step("Search for book: {keyword}")
    public HomePage searchBook(String keyword){
        log.info("Search for book: [{}]", keyword);
        type(searchBox,keyword);
        return this;
    }

    @Step("Check if book title {title} appears in the table")
    public boolean isBookVisible(String title){
        for(WebElement element: bookTitleLinks){
            waitUtils.waitForVisible(element);
            if(element.getText().trim().equalsIgnoreCase(title)){
                log.info("Found: " +title);
                return true;
            }
        }
        log.info("Book {title} not found", title);
        return false;
    }

    @Step("Get count of visible books on table")
    public int getBookCount(){
        long count = bookRows.stream().filter(row -> !row.getText().isBlank()).count();
        log.info("Visible books: {}",count);
        return (int) count;
    }

}
