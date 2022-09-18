package driver.project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testUnauthorizedVisit() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testAccess() {
		// Create a test account
		signUp("URL","Test","123","123");
		logIn("123", "123");

		// Check for homepage
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		// Click log out
		Homepage homepage = new Homepage(driver);
		homepage.logout();

		// Check for login page
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void createNote() {
		// Create a test account
		signUp("URL","Test","1","1");
		logIn("1", "1");

		// Create a note
		Homepage homepage = new Homepage(driver);
		homepage.createNote("TestTitle","TestDescription");

		// Check that the note was created
		WebDriverWait wait = new WebDriverWait (driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-success")));
		Assertions.assertTrue(driver.findElement(By.id("message-success")).getText().contains("Note added successfully"));
	}

	@Test
	public void editNote(){
		// Create a test account
		signUp("URL","Test","2","2");
		logIn("2", "2");

		// Create a note
		Homepage homepage = new Homepage(driver);
		homepage.createNote("TestTitle","TestDescription");

		// Edit the note
		homepage.editNote("newTestTitle", "newTestDescription");

		// Check the note is updated
		WebDriverWait wait = new WebDriverWait (driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-success")));
		Assertions.assertTrue(driver.findElement(By.id("message-success")).getText().contains("Note edited successfully"));
	}

	@Test
	public void deleteNote(){
		// Create a test account
		signUp("URL","Test","3","3");
		logIn("3", "3");

		// Create a note
		Homepage homepage = new Homepage(driver);
		homepage.createNote("TestTitle","TestDescription");

		// Delete the note
		homepage.deleteNote();

		// Check the note is updated
		WebDriverWait wait = new WebDriverWait (driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-success")));
		Assertions.assertTrue(driver.findElement(By.id("message-success")).getText().contains("Note deleted successfully"));
	}

	@Test
	public void createEditCred() {
		WebDriverWait wait = new WebDriverWait (driver, 1);
		// Create a test account
		signUp("URL","Test","3","3");
		logIn("3", "3");

		// Create a credential
		Homepage homepage = new Homepage(driver);
		homepage.createCred("TestUrl","TestUsername", "TestPassword");
		Assertions.assertNotEquals("TestPassword",driver.findElement(By.id("cred-encryptedPassword")).getAttribute("value"));

		// Edit a credential
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cred-edit")));
		driver.findElement(By.id("cred-edit")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		Assertions.assertEquals(driver.findElement(By.id("credential-password")).getAttribute("value"),"TestPassword");
		driver.findElement(By.id("credential-url")).clear();
		driver.findElement(By.id("credential-url")).sendKeys("editedUrl");
		driver.findElement(By.id("save-cred")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cred-url")));
		Assertions.assertEquals(driver.findElement(By.id("cred-url")).getText(),"editedUrl");

	}

	@Test
	public void deleteCred(){
		// Create a test account
		signUp("URL","Test","3","3");
		logIn("3", "3");

		// Create a note
		Homepage homepage = new Homepage(driver);
		homepage.createCred("TestUrl","TestUsername", "TestPassword");

		// Delete the note
		homepage.deleteCred();

		// Check the note is updated
		WebDriverWait wait = new WebDriverWait (driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message-success")));
		Assertions.assertTrue(driver.findElement(By.id("message-success")).getText().contains("Credential deleted successfully"));
	}

	private void signUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials and sign up.
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, userName, password);

		// Check that the sign up was successful.
		// Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	private void logIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	@Test
	public void testRedirection() {
		// Create a test account
		signUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void testBadUrl() {
		// Create a test account
		signUp("URL","Test","4","4");
		logIn("4", "4");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	@Test
	public void testLargeUpload() {
		// Create a test account
		signUp("Large File","Test","LFT","123");
		logIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

}
