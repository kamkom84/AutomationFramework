package testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.Base;
import pages.AccountSuccessPage;
import pages.HomePage;
import pages.RegisterPage;
import utils.Utilities;
import org.openqa.selenium.WebDriver;

public class RegisterTest extends Base{
	
	public RegisterTest() {
		super();
	}
	
	public WebDriver driver;
	RegisterPage registerPage;
	AccountSuccessPage accountSuccessPage; //
	
	@BeforeMethod
	public void setup() {
		
		driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
		HomePage homePage = new HomePage(driver);
		registerPage = homePage.navigateToRegisterPage();
		
	}
	
	@AfterMethod
	public void tearDown() {
		
		driver.close();
		
	}
	
	@Test(priority=1)
	public void verifyRegisteringAnAccountWithMandatoryFields() {
		
		accountSuccessPage = registerPage.registerWithMandatoryFields(dataProp.getProperty("firstName"), dataProp.getProperty("lastName"), 
												                      Utilities.generateEmailWithTimeStamp(), dataProp.getProperty("telephoneNumber"),
												                      prop.getProperty("validPassword"));
		Assert.assertEquals(accountSuccessPage.getAccountSuccessPageHeading(), 
				dataProp.get("accountSuccessfullyCreatedHeading"), "Account success page is not displayed");
		
	}
	
	@Test(priority=2)
	public void verifyRegisteringAnAccountByProvidingAllFields() {
		
		accountSuccessPage = registerPage.registerWithAllFields(dataProp.getProperty("firstName"), dataProp.getProperty("lastName"), 
				  					       						Utilities.generateEmailWithTimeStamp(), dataProp.getProperty("telephoneNumber"),
				  					       						prop.getProperty("validPassword"));
		Assert.assertEquals(accountSuccessPage.getAccountSuccessPageHeading(), 
				dataProp.get("accountSuccessfullyCreatedHeading"), "Account success page is not displayed");
		
	}
	
	@Test(priority=3)
	public void verifyRegisteringAnAccountWithExistingEmailAddress() {
		
		accountSuccessPage = registerPage.registerWithAllFields(dataProp.getProperty("firstName"), dataProp.getProperty("lastName"), 
																prop.getProperty("validEmail"), dataProp.getProperty("telephoneNumber"),
																prop.getProperty("validPassword"));
		Assert.assertTrue(registerPage.getDuplicateEmailAddressWarning().contains(
				dataProp.getProperty("duplicatedEmailWarning")),"Warning message regarding duplicate email address");
		
	}
	
	@Test(priority=4)
	public void verifyRegisteringAnAccountWitoutFillingAnyCredentials() {
		
		registerPage.clickOnContinueButton();
		
		Assert.assertTrue(registerPage.displayStatusOfWarningMessages(dataProp.getProperty("privacyPolicyWarning"), 
																	  dataProp.getProperty("firstNameWarning"),
																	  dataProp.getProperty("lastNameWarning"),
																	  dataProp.getProperty("emailWarning"),
																	  dataProp.getProperty("telephoneWarning"), 
																	  dataProp.getProperty("passwordWarning")), 
																	  "Warning message/s is/are not displayed");
		
	}
	
}

























