package facebook;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Steps {

	private WebDriver driver;
	Scenario scenario;

	@Before
	public void beforMethodSetUp(Scenario scenario) throws Throwable {
		this.scenario = scenario;
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
		byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
	}

	@When("enter creds")
	public void enter_creds() {
		driver.findElement(By.id("email")).sendKeys("letstrylogin");
		driver.findElement(By.id("pass")).sendKeys("sorry");
		driver.findElement(By.id("pass")).submit();
		byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
	}

	@Then("user login")
	public void user_login() {
		WebElement loginbutton = driver.findElement(By.id("loginbutton"));
		Assert.assertTrue(loginbutton.isDisplayed(), "User failed to login");
		byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
	}
}