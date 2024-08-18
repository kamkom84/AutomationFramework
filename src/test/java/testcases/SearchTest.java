package testcases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.Base;
import pages.HomePage;
import pages.SearchPage;
import utilities.HttpClientUtil;
import utils.TestResult;

import java.io.IOException;
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

	@AfterMethod
	public void captureTestResults(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			testResults.add(new TestResult(result.getName(), "Failed"));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			testResults.add(new TestResult(result.getName(), "Passed"));
		} else if (result.getStatus() == ITestResult.SKIP) {
			testResults.add(new TestResult(result.getName(), "Skipped"));
		}
	}

	@AfterClass
	public void generateReport() throws IOException, InterruptedException {
		int reportIndex = testResults.size();
		String credentialsUser = "kamen.dimitrov@ctgaming.com";

		//K List:
		String listId1 = "901506232548";  //fake list> "901506232548";
		HttpClientUtil.createTask(
				testResults.stream().map(TestResult::toString).toArray(String[]::new),
				credentialsUser,
				listId1
		);
	}

	@BeforeClass
	public void setup() {
		driver = initializeBrowserAndOpenApplicationURL(prop.getProperty("browser"));
		homePage = new HomePage(driver);
		testResults = new ArrayList<>();
	}

	@AfterClass
	public void tearDown() {
		printTestResults();
		if (driver != null) {
			driver.quit();
		}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 2)
	public void verifySearchWithInvalidProduct() {
		try {
			searchPage = homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 3)
	public void verifySearchWithoutAnyProduct() {
		try {
			searchPage = homePage.clickOnSearchButton();
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"));;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
