package facebook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
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
	static String screenshotdir = System.getProperty("user.dir") + "/test-output/screenshots/";

	@Before
	public void clean_directory() throws Throwable {
		if ((new File(screenshotdir)).exists())
			FileUtils.cleanDirectory(new File(screenshotdir));
	}

	@AfterStep
	public void attach_screenshot(Scenario scenario) throws Throwable {
		Thread.sleep(2000);
		String imagepath = captureScreenShot(driver);
		ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(imagepath);
		scenario.embed(Files.readAllBytes(Paths.get(imagepath)), "image/png");
	}

	@After
	public void tearDown() throws Exception {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
	}

	@When("enter creds")
	public void enter_creds() {
		driver.findElement(By.id("email")).sendKeys("letstrylogin");
		driver.findElement(By.id("pass")).sendKeys("sorry");
		driver.findElement(By.id("pass")).submit();
	}

	@Then("user login")
	public void user_login() {
		WebElement loginbutton = driver.findElement(By.id("loginbutton"));
		Assert.assertTrue(loginbutton.isDisplayed(), "User failed to login");
		
	}
	public static String captureScreenShot(WebDriver driver) throws IOException {
		TakesScreenshot screen = (TakesScreenshot) driver;
		File src = screen.getScreenshotAs(OutputType.FILE);
		String dest = screenshotdir + "img" + getcurrentdateandtime() + ".png";
		File target = new File(dest);
		FileUtils.copyFile(src, target);
		return dest;
	}

	private static String getcurrentdateandtime() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
			Date date = new Date();
			str = dateFormat.format(date);
			str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");
		} catch (Exception e) {
		}
		return str;
	}

}