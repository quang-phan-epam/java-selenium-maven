package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FileUploadPage;
import config.ConfigReader;
import utils.DriverManager;

import java.io.File;

@Epic("DemoQA")
@Feature("File Upload")
public class FileUploadTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-07: Upload a file and verify the filename is shown on the page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "Upload a file and verify the filename appears on screen")
    @Story("Browser file upload")
    @Description("Locate the upload input, send a file path, and verify the uploaded filename is shown.")
    @Severity(SeverityLevel.CRITICAL)
    public void testFileUpload() {
        DriverManager.getDriver().get(ConfigReader.get("upload.url"));

        // Resolve the sample file from the test resources folder
        String filePath = new File(
                "src/test/resources/fileUploadSample.txt")
                .getAbsolutePath();

        FileUploadPage page = new FileUploadPage(DriverManager.getDriver());
        page.uploadFile(filePath);

        String displayedName = page.getUploadedFileName();

        Assert.assertTrue(displayedName.contains("fileUploadSample.txt"),
                "Uploaded file name should contain 'fileUploadSample.txt'. Got: " + displayedName);
    }
}
