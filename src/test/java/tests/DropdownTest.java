package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SelectMenuPage;
import config.ConfigReader;
import utils.DriverManager;

import java.util.Arrays;
import java.util.List;

@Epic("DemoQA")
@Feature("Dropdown / Select")
public class DropdownTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-08a: Select a single option from the old-style dropdown
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Select an option from the old-style select dropdown")
    @Story("Single-select dropdown")
    @Description("Use Selenium's Select class to pick an option by visible text and verify the selection.")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectByText() {
        DriverManager.getDriver().get(ConfigReader.get("select.url"));
        SelectMenuPage page = new SelectMenuPage(DriverManager.getDriver());

        page.selectByText("Blue");

        Assert.assertEquals(page.getSelectedOption(), "Blue",
                "Selected option should be 'Blue'.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-08b: Select an option by value attribute
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Select an option from the dropdown by its value attribute")
    @Story("Single-select dropdown by value")
    @Description("Use selectByValue() and verify the displayed text reflects the correct option.")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectByValue() {
        DriverManager.getDriver().get(ConfigReader.get("select.url"));
        SelectMenuPage page = new SelectMenuPage(DriverManager.getDriver());

        // The <option value="3"> corresponds to "Green" in DemoQA's oldSelectMenu
        page.selectByValue("3");

        Assert.assertEquals(page.getSelectedOption(), "Yellow",
                "Selected option should be 'Yellow'.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-08c: Select multiple options from the multi-select list
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Select multiple options from the multi-select list")
    @Story("Multi-select dropdown")
    @Description("Select 'Volvo' and 'Audi' simultaneously and verify both are selected.")
    @Severity(SeverityLevel.NORMAL)
    public void testMultiSelect() {
        DriverManager.getDriver().get(ConfigReader.get("select.url"));
        SelectMenuPage page = new SelectMenuPage(DriverManager.getDriver());

        List<String> toSelect = Arrays.asList("Volvo", "Audi");
        page.selectMultiple(toSelect);

        List<String> selected = page.getSelectedOptions();

        Assert.assertTrue(selected.containsAll(toSelect),
                "Multi-select should have 'Volvo' and 'Audi' selected. Got: " + selected);
    }
}
