package testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.Base;
import pages.AccountPage;
import pages.HomePage;
import pages.LoginPage;
import utils.Utilities;
import org.openqa.selenium.WebDriver;

public class LoginTest extends Base {

    public LoginTest() {
        super();
    }

    public WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setup() {

        driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
        HomePage homePage = new HomePage(driver);
        loginPage = homePage.navigateToLoginPage();

    }

    @AfterMethod
    public void tearDown() {

        driver.close();

    }

    @Test(priority = 1, dataProvider = "validCredentialsSupplier")
    public void verifyLoginWithValidCredentials(String email, String password) {

        AccountPage accountPage = loginPage.login(email, password);
        Assert.assertTrue(accountPage.getDisplayStatusOfEditYourAccountOption(),
                "Edit Your Account Information option is not displayed");

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
    public void verifyLoginWitnInvalidEmailAndValidPassword() {

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

}
