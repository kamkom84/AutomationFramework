package testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.Base;
import pages.HomePage;
import pages.SearchPage;
import utils.TestResult;

import java.util.ArrayList;
import java.util.List;

public class SearchTest extends Base {

	public SearchTest() {
		super();
	}

	public WebDriver driver;
	SearchPage searchPage;
	HomePage homePage;
	List<TestResult> testResults;

	@BeforeClass
	public void setup() {
		driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
		homePage = new HomePage(driver);
		testResults = new ArrayList<>();
	}

	@AfterClass
	public void tearDown() {
//		printTestResults();
//		if (driver != null) {
//			driver.quit();
//		}
	}

	private void printTestResults() {
		System.out.println("Test Results:");
		for (TestResult result : testResults) {
			System.out.println(result);
		}
	}

	@Test(priority = 1)
	public void verifySearchWithValidProduct() {
		try {
			searchPage = homePage.searchForAProduct(dataProp.getProperty("validProduct"));
			boolean isDisplayed = searchPage.displayStatusOfHPValidProduct();
			Assert.assertTrue(isDisplayed, "Valid product HP is not displayed");
			testResults.add(new TestResult("Verify Search With Valid Product", "Passed"));
		} catch (AssertionError | Exception e) {
			testResults.add(new TestResult("Verify Search With Valid Product", "Failed: " + e.getMessage()));
			throw e;
		}
	}

	@Test(priority = 2)
	public void verifySearchWithInvalidProduct() {
		try {
			searchPage = homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"));
			testResults.add(new TestResult("Verify Search With Invalid Product", "Passed"));
		} catch (AssertionError | Exception e) {
			testResults.add(new TestResult("Verify Search With Invalid Product", "Failed: " + e.getMessage()));
			throw e;
		}
	}

	@Test(priority = 3)
	public void verifySearchWithoutAnyProduct() {
		try {
			searchPage = homePage.clickOnSearchButton();
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"));
			testResults.add(new TestResult("Verify Search Without Any Product", "Passed"));
		} catch (AssertionError | Exception e) {
			testResults.add(new TestResult("Verify Search Without Any Product", "Failed: " + e.getMessage()));
			throw e;
		}
	}

}

