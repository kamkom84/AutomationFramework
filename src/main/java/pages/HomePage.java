package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	WebDriver driver;

	@FindBy(xpath="//span[normalize-space()='My Account']")
	private WebElement myAccountDropMenu;

	@FindBy(linkText="Login")
	private WebElement loginOption;

	@FindBy(linkText="Register")
	private WebElement registerOption;

	@FindBy(name="search")
	private WebElement searchBoxField;

	@FindBy(xpath="//div[@id='search']/descendant::button")
	private WebElement searchButton;



	public HomePage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	//Methods:

	public SearchPage clickOnSearchButton() {

		searchButton.click();
		return new SearchPage(driver);

	}

	public SearchPage searchForAProduct(String productText) {

		searchBoxField.sendKeys(productText);
		searchButton.click();
		return new SearchPage(driver);

	}

	public void enterProductOntoSearchBoxField(String productText) {

		searchBoxField.sendKeys(productText);

	}

	public void clickOnMyAccount() {

		myAccountDropMenu.click();

	}

	public LoginPage selectLoginOption() {

		loginOption.click();
		return new LoginPage(driver);

	}

	public RegisterPage navigateToRegisterPage() {

		myAccountDropMenu.click();
		registerOption.click();
		return new RegisterPage(driver);

	}

	public LoginPage navigateToLoginPage() {

		myAccountDropMenu.click();
		loginOption.click();
		return new LoginPage(driver);

	}

	public RegisterPage selectRegisterOption() {

		registerOption.click();
		return new RegisterPage(driver);

	}

}
