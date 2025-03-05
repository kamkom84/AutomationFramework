package testcases;

import org.testng.Assert;
import org.testng.annotations.*;
import base.Base;
import pages.AccountPage;
import pages.HomePage;
import pages.LoginPage;
import utils.Utilities;
import org.openqa.selenium.WebDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginTest extends Base {

    public LoginTest() {
        super();
    }

    public WebDriver driver;
    private Map<String, String> testResults = new LinkedHashMap<>();
    private static final String FILE_PATH = Paths.get("test-output", "test-results.csv").toString();

    LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
        HomePage homePage = new HomePage(driver);
        loginPage = homePage.navigateToLoginPage();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        saveResultsToFile();
    }

    @Test(priority = 1, dataProvider = "validCredentialsSupplier")
    public void verifyLoginWithValidCredentials(String email, String password) {
        try {
            AccountPage accountPage = loginPage.login(email, password);
            Assert.assertTrue(accountPage.getDisplayStatusOfEditYourAccountOption(),
                "Edit Your Account Information option is not displayed");

        } catch (AssertionError | Exception e) {
            testResults.put("verifyLoginWithValidCredentials", "❌ Failed: " + e.getMessage());
            Assert.fail("❌ Test failed: " + e.getMessage());
        }

    }

    @DataProvider(name = "validCredentialsSupplier")
    public Object[][] supplyTestData() {

        Object[][] data = Utilities.getTestDataFromExcel("Login");
        return data;

    }

    @Test(priority = 2)
    public void verifyLoginWithInvalidCredentials() {

        loginPage.login(Utilities.generateEmailWithTimeStamp(), dataProp.getProperty("invalidPassword"));
        Assert.assertTrue(loginPage.getEmailPasswordNotMatchingWarningMessage().contains
                (dataProp.getProperty("emailPasswordNoMatchWarning")));

    }

    @Test(priority = 3)
    public void verifyLoginWithInvalidEmailAndValidPassword() {

        loginPage.login(Utilities.generateEmailWithTimeStamp(), prop.getProperty("validPassword"));
        Assert.assertTrue(loginPage.getEmailPasswordNotMatchingWarningMessage().contains
                (dataProp.getProperty("emailPasswordNoMatchWarning")));

    }

    @Test(priority = 4)
    public void verifyLoginWithValidEmailAddressAndInvalidPassword() {

        loginPage.login(prop.getProperty("validEmail"), dataProp.getProperty("invalidPassword"));
        Assert.assertTrue(loginPage.getEmailPasswordNotMatchingWarningMessage().contains
                (dataProp.getProperty("emailPasswordNoMatchWarning")));

    }

    @Test(priority = 5)
    public void verifyLoginWithoutProvidingAnyCredentials() {

        loginPage.clickOnLoginButton();
        Assert.assertTrue(loginPage.getEmailPasswordNotMatchingWarningMessage().contains
                (dataProp.getProperty("emailPasswordNoMatchWarning")));

    }

    private void saveResultsToFile() {
        File file = new File(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Test Name,Result\n");
            testResults.forEach((testName, result) -> {
                try {
                    writer.write(testName + "," + result + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("\n✅: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌" + e.getMessage());
        }
    }

}
