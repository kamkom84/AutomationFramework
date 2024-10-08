package testcases;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
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
import java.time.LocalTime;
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

	//@AfterMethod
	public void captureTestResults(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			testResults.add(new TestResult(result.getName(), "Failed"));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			testResults.add(new TestResult(result.getName(), "Passed"));
		} else if (result.getStatus() == ITestResult.SKIP) {
			testResults.add(new TestResult(result.getName(), "Skipped"));
		}
	}

	//@AfterClass
	public void generateReport() throws IOException, InterruptedException {
		int reportIndex = testResults.size();
		String credentialsUser = "kamen.dimitrov@ctgaming.com";

		// K's private List:
		String listId1 = "901506232548";  // Fake private ClickUp list

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
			Assert.assertTrue(isDisplayed, "The valid product (HP) is not displayed as expected.");
		} catch (NoSuchElementException e) {
			throw new AssertionError("Failed to locate the element that represents the valid product on the search results page.", e);
		} catch (TimeoutException e) {
			throw new AssertionError("The search operation took too long to complete.", e);
		} catch (Exception e) {
			throw new AssertionError("An unexpected error occurred while verifying search with a valid product.", e);
		}
	}

	@Test(priority = 2)
	public void verifySearchWithInvalidProduct() {
		try {
			searchPage = homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"),
					"The message displayed for no products found does not match the expected result.");
		} catch (NoSuchElementException e) {
			throw new AssertionError("Failed to find the element that displays the no-product message on the search results page.", e);
		} catch (TimeoutException e) {
			throw new AssertionError("The search took too long to complete.", e);
		} catch (Exception e) {
			throw new AssertionError("An unexpected error occurred while verifying search with an invalid product.", e);
		}
	}

	@Test(priority = 3)
	public void verifySearchWithoutAnyProduct() {
		try {
			searchPage = homePage.clickOnSearchButton();
			String noProductMessage = searchPage.getNoProductMessageText();
			Assert.assertEquals(noProductMessage, dataProp.getProperty("noProductTextInSearchResults"),
					"The no-product message does not match the expected result.");
		} catch (NoSuchElementException e) {
			throw new AssertionError("Failed to locate the element for no product message on the search page.", e);
		} catch (TimeoutException e) {
			throw new AssertionError("The search page took too long to load.", e);
		} catch (Exception e) {
			throw new AssertionError("An unexpected error occurred during the search verification.", e);
		}
	}
}
