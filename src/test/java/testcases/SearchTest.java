package testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.Base;
import pages.HomePage;
import pages.SearchPage;

public class SearchTest extends Base{
	
	public SearchTest() {
		super();
	}
	
	public WebDriver driver;
	SearchPage searchPage;
	HomePage homePage;
	
	@BeforeMethod
	public void setup() {
		
		driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
		homePage = new HomePage(driver);
	}
	
	@AfterMethod
	public void teardown() {
		
		driver.quit();
		
	}
	
	@Test(priority=1)
	public void verifySearchWithValidProduct() {
		
		searchPage =  homePage.searchForAProduct(dataProp.getProperty("validProduct"));
		Assert.assertTrue(searchPage.displayStatusOfHPValidProduct(), "Valid product HP is not displayed");
		
	}
	
	@Test(priority=2)
	public void verifySearchWithInvalidProduct() {
		
		searchPage =  homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
		Assert.assertEquals(searchPage.getNoProductMessageText(), dataProp.getProperty("noProductTextInSearchResults"));
		
	}
	
	@Test(priority=3)
	public void verifySearchWithoutAnyProduct() {
		
		searchPage = homePage.clickOnSearchButton();
		Assert.assertEquals(searchPage.getNoProductMessageText(), dataProp.getProperty("noProductTextInSearchResults"));
		
	}


}
