package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
	
	WebDriver driver;

	@FindBy(id="input-firstname")
	private WebElement firstNameField;
	
	@FindBy(id="input-lastname")
	private WebElement lastNameField;
	
	@FindBy(id="input-email")
	private WebElement emailAddressField;
	
	@FindBy(id="input-telephone")
	private WebElement telephoneField;
	
	@FindBy(id="input-password")
	private WebElement passwordFeld;
	
	@FindBy(id="input-confirm")
	private WebElement passwordConfirmFeld;
	
	@FindBy(name="agree")
	private WebElement privacyPolicyField;
	
	@FindBy(xpath="//input[@value='Continue']")
	private WebElement continueButton;
	
	@FindBy(xpath="//input[@name='newsletter'][@value='1']")
	private WebElement yesNewsLetterOption;
	
	@FindBy(xpath="//div[@class='alert alert-danger alert-dismissible']")
	private WebElement duplicateEmailAddressWarning;
	
	@FindBy(xpath="//div[@class='alert alert-danger alert-dismissible'][1]")
	private WebElement privacyPolicyWarning;
	
	@FindBy(xpath="//div[contains(text(),'First Name must be between 1 and 32 characters!')]")
	private WebElement firstNameWarning;
	
	@FindBy(xpath="//div[contains(text(),'Last Name must be between 1 and 32 characters!')]")
	private WebElement lastNameWarning;
	
	@FindBy(xpath="//div[contains(text(),'E-Mail Address does not appear to be valid!')]")
	private WebElement emailWarning;
	
	@FindBy(xpath="//div[contains(text(),'Telephone must be between 3 and 32 characters!')]")
	private WebElement telephoneWarning;
	
	@FindBy(xpath="(//div[contains(text(),'Password must be between 4 and 20 characters!')])[1]")
	private WebElement passwordWarning;
	
	public RegisterPage(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	public String getPasswordWarning() {
		
		String passwordWarningText = passwordWarning.getText();
		return passwordWarningText;
		
	}
	
	public String getTelephoneWarning() {
		
		String telephoneWarningText = telephoneWarning.getText();
		return telephoneWarningText;
		
	}
	
	public String getEmailWarning() {
		
		String emailWarningText = emailWarning.getText();
		return emailWarningText;
	}
	
	public String getLastNameWarning() {
		
		String lastNameWarningText = lastNameWarning.getText();
		return lastNameWarningText;
		
	}
	
	public String getFirstNameWarning() {
		
		String firstNameWarningText = firstNameWarning.getText();
		return firstNameWarningText;
		
	}
	
	public String getPrivacyPolicyWarning() {
		
		String privacyPolicyWarningText = privacyPolicyWarning.getText();
		return privacyPolicyWarningText;
		
	}
	
	public void enterFirstName(String firstNameText) {
		
		firstNameField.sendKeys(firstNameText);
		
	}
	
	public void enterLastName(String lastNameText) {
		
		lastNameField.sendKeys(lastNameText);
		
	}
	
	public void enterEmailAddress(String emailText) {
		
		emailAddressField.sendKeys(emailText);
		
	}
	
	public void enterTelephoneNumber(String telephoneNumberText) {
		
		telephoneField.sendKeys(telephoneNumberText);
		
	}
	
	public void enterPassword(String passwordText) {
		
		passwordFeld.sendKeys(passwordText);
		
	}
	
	public void enterConfirmPassword(String confirmPasswordText) {
		
		passwordConfirmFeld.sendKeys(confirmPasswordText);
		
	}
	
	public void selectPrivacyPolicy() {
		
		privacyPolicyField.click();
		
	}
	
	public AccountSuccessPage clickOnContinueButton() {
		
		continueButton.click();
		return new AccountSuccessPage(driver);
		
	}
	
	public void selectYesNewsLetterOption() {
		
		yesNewsLetterOption.click();
		
	}
	
	public String getDuplicateEmailAddressWarning() {
		
		String duplicateEmailAddressWarningtext = duplicateEmailAddressWarning.getText();
		return duplicateEmailAddressWarningtext;
		
	}
	
	public AccountSuccessPage registerWithMandatoryFields(String firstNameText, String lastNameText, String emailText, 
									   					  String telephoneNumberText, String passwordText) {
		
		firstNameField.sendKeys(firstNameText);
		lastNameField.sendKeys(lastNameText);
		emailAddressField.sendKeys(emailText);
		telephoneField.sendKeys(telephoneNumberText);
		passwordFeld.sendKeys(passwordText);
		passwordConfirmFeld.sendKeys(passwordText);
		privacyPolicyField.click();
		continueButton.click();
		return new AccountSuccessPage(driver);
		
	}
	
	public AccountSuccessPage registerWithAllFields(String firstNameText, String lastNameText, String emailText, 
			   										String telephoneNumberText, String passwordText) {

		firstNameField.sendKeys(firstNameText);
		lastNameField.sendKeys(lastNameText);
		emailAddressField.sendKeys(emailText);
		telephoneField.sendKeys(telephoneNumberText);
		passwordFeld.sendKeys(passwordText);
		passwordConfirmFeld.sendKeys(passwordText);
		yesNewsLetterOption.click();
		privacyPolicyField.click();
		continueButton.click();
		return new AccountSuccessPage(driver);

	}
	
	public boolean displayStatusOfWarningMessages(String expectedPrivacyPolicyWarning, String expectedFirstNameWarning, 
												  String expectedLastNameWarning, String expectedEmailWarning, 
												  String expectedTelephoneWarning, String expectedPasswordWarning) {
		
		boolean privacyPolicyWarningStatus = privacyPolicyWarning.getText().contains(expectedPrivacyPolicyWarning);
		boolean firstNameWarningStatus = firstNameWarning.getText().equals(expectedFirstNameWarning);
		boolean lastNameWarningStatus = lastNameWarning.getText().equals(expectedLastNameWarning);
		boolean emailWarningStatus = emailWarning.getText().equals(expectedEmailWarning);
		boolean telephoneWarningStatus = telephoneWarning.getText().equals(expectedTelephoneWarning);
		boolean passwordWarningStatus = passwordWarning.getText().equals(expectedPasswordWarning);
		return privacyPolicyWarningStatus && firstNameWarningStatus && lastNameWarningStatus && 
			   emailWarningStatus && telephoneWarningStatus && passwordWarningStatus;
		
	}

}
