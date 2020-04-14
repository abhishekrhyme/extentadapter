package facebook;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Steps {

	private WebDriver driver;
	Scenario scenario;
	String screenshotdir = System.getProperty("user.dir") + "/test-output/Screenshots/";

	@Before
	public void beforMethodSetUp(Scenario scenario) throws Throwable {
		this.scenario = scenario;
		if ((new File(screenshotdir)).exists())
			FileUtils.cleanDirectory(new File(screenshotdir));
	}
	
	@AfterStep
	public void attach_screenshot() throws Throwable {
		//Thread.sleep(2000);
		ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(getBase64Screenshot(driver)); //for html
		//scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png"); //for spark
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Given("open facebook")
	public void open_facebook() throws Throwable {
		WebDriverManager.chromedriver().setup();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);
		options.setPageLoadStrategy(PageLoadStrategy.NONE);
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get("https://www.facebook.com");
		Thread.sleep(2000);
		scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
	}

	@When("enter creds")
	public void enter_creds() throws Throwable  {
		driver.findElement(By.id("email")).sendKeys("letstrylogin");
		driver.findElement(By.id("pass")).sendKeys("sorry");
		driver.findElement(By.id("pass")).submit();
		Thread.sleep(2000);
		scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
	}

	@Then("user login")
	public void user_login() throws Throwable {
		WebElement loginbutton = driver.findElement(By.id("loginbutton"));
		Assert.assertTrue(loginbutton.isDisplayed(), "User failed to login");
		Thread.sleep(2000);
		scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
	}
	
	public String getBase64Screenshot(WebDriver driver) throws IOException {
	    String Base64StringofScreenshot="";
	    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    // for saving screenshots in local - this is optional 
	    Date oDate = new Date();
	    SimpleDateFormat oSDF = new SimpleDateFormat("ddMMYYYY_HHmmss");
	    String sDate = oSDF.format(oDate);
	    FileUtils.copyFile(src, new File(screenshotdir + "Screenshot_" + sDate + ".png"));
	    //
	    byte[] fileContent = FileUtils.readFileToByteArray(src);
	    Base64StringofScreenshot = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
	    return Base64StringofScreenshot;
	}
}