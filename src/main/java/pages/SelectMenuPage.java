package pages;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;


public class SelectMenuPage extends BasePage {
    // ── Locators ─────────────────────────────────────────────────────────────

    // Standard <select> element — easiest to automate with Selenium's Select class
    @FindBy(id = "oldSelectMenu")
    private WebElement oldStyleSelect;

    // Standard <select multiple> element
    @FindBy(id = "cars")
    private WebElement multiSelect;

    // ── Constructor ───────────────────────────────────────────────────────────

    public SelectMenuPage(WebDriver driver) {
        super(driver);
    }

    // ── Dropdown Actions ──────────────────────────────────────────────────────

    @Step("Select option by visible text: '{text}' from old-style dropdown")
    public SelectMenuPage selectByText(String text) {
        log.info("Selecting option by text: [{}]", text);
        Select select = new Select(oldStyleSelect);
        select.selectByVisibleText(text);
        log.debug("Option selected {}",text);
        return this;
    }

    @Step("Select option by value: '{value}' from old-style dropdown")
    public SelectMenuPage selectByValue(String value) {
        log.info("Selecting option by value: [{}]", value);
        new Select(oldStyleSelect).selectByValue(value);
        return this;
    }

    @Step("Get currently selected option text from old-style dropdown")
    public String getSelectedOption() {
        String text = new Select(oldStyleSelect)
                .getFirstSelectedOption()
                .getText().trim();
        log.info("Currently selected option: [{}]", text);
        return text;
    }

    @Step("Select multiple options in multi-select: {options}")
    public SelectMenuPage selectMultiple(List<String> options) {
        Select select = new Select(multiSelect);
        select.deselectAll();
        for (String option : options) {
            log.info("Selecting multi-select option: [{}]", option);
            select.selectByVisibleText(option);
        }
        return this;
    }

    @Step("Get all selected options from multi-select")
    public List<String> getSelectedOptions() {
        List<String> selected = new Select(multiSelect)
                .getAllSelectedOptions()
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
        log.info("Selected options: {}", selected);
        return selected;
    }
}
