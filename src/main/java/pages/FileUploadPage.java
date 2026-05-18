package pages;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.io.File;

public class FileUploadPage extends BasePage {
    // ── Locators ─────────────────────────────────────────────────────────────

    // Hidden <input type="file"> — we send the file path directly (no clicking)
    @FindBy(id = "uploadFile")
    private WebElement uploadInput;

    // Shows the uploaded file name after upload succeeds
    @FindBy(id = "uploadedFilePath")
    private WebElement uploadedFilePath;

    // ── Constructor ───────────────────────────────────────────────────────────

    public FileUploadPage(WebDriver driver) {
        super(driver);
    }

    // ── Upload Actions ────────────────────────────────────────────────────────
    /**
     * Upload a file by sending its absolute path directly to the hidden file input.
     * This works in both headed and headless mode — no OS file dialog needed.
     * @param filePath absolute path to the file, e.g. "/home/runner/.../sample.txt"
     */
    @Step("Upload file: {filePath}")
    public FileUploadPage uploadFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Upload file not found: " + filePath);
        }
        log.info("Uploading file: {}", file.getAbsolutePath());

        // sendKeys on the hidden input — bypasses the OS dialog entirely
        uploadInput.sendKeys(file.getAbsolutePath());
        log.debug("File path sent to upload input.");
        return this;
    }

    @Step("Get uploaded file name text")
    public String getUploadedFileName() {
        String text = getText(uploadedFilePath);
        log.info("Uploaded file name shown: [{}]", text);
        return text;
    }
}
