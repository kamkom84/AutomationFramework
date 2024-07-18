package base;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.Utilities;

public class Base {
	
	WebDriver driver;
	public Properties prop;
	public Properties dataProp;
	
	public Base() {
		
		prop = new Properties();
		File propFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\config\\config.properties");

		dataProp = new Properties();
		File dataPropFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\testdata\\testdata.properties");
		
		try {
			FileInputStream dataFis = new FileInputStream(dataPropFile);
			dataProp.load(dataFis);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		try {
			FileInputStream fis = new FileInputStream(propFile);
			prop.load(fis);
		} catch (Throwable e) {
			
			e.printStackTrace();
		}
		
	}
	
	public WebDriver initializeBrowserAndOpenApplicationURL(String browser) {
		
		if(browser.equalsIgnoreCase("chrome")) {
			
			driver = new ChromeDriver();
			
		}else if(browser.equalsIgnoreCase("firefoxxx")) {
			
			driver = new FirefoxDriver();
			
		}else if(browser.equalsIgnoreCase("edge")) {
			
			driver = new EdgeDriver();
			
		}else {
			throw new IllegalArgumentException("Browser type not supported");
		}

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");

		//driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Utilities.IMPLICIT_WAIT_TIME));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Utilities.PAGE_LOAD_TIME));
		driver.get(prop.getProperty("url"));
		
		return driver;
		
	}

}
