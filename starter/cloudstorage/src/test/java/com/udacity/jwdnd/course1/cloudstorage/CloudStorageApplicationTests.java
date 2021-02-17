package com.udacity.jwdnd.course1.cloudstorage;
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
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.Priority;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
	@LocalServerPort
	public int port;
	public static WebDriver driver;
	public String baseURL;

	public String username = "aaa";
	public String password = "a123";

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}
	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}
	@BeforeEach
	public void beforeEach() {
		baseURL = baseURL = "http://localhost:" + port;
	}

	/**
	 * Perform signup process
	 */
	public void signup(){
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Arpit", "Trivedi", username, password);
	}

	/**
	 * Perform login process
	 */
	public void login(){
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	/**
	 * Perform unauthorized check
	 */
	public void unauthorizedCheck(){
		// Without signing up, user will be redirected to login
		driver.get(baseURL + "/home");
		String url = driver.getCurrentUrl();
		assertEquals(true, url.equals(baseURL + "/login"));
		assertEquals(false, url.equals(baseURL + "/home"));
	}

	@Test
	@Order(1)
	public void testUnauthorizedAccess(){
		unauthorizedCheck();
	}

	@Test
	@Order(2)
	public void testUserSignupLoginAndHomepage() {
		signup();
		login();

		WebElement fileElement = driver.findElement(By.id("nav-files"));
		assertEquals(true, fileElement.isDisplayed());
	}

	@Test
	@Order(3)
	public void testAccessAfterLogout(){

		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-title")));
		String url = driver.getCurrentUrl();
		assertEquals(true, url.equals(baseURL + "/login"));
		unauthorizedCheck();
 }

	@Test
	@Order(4)
	public void testNoteCreationAndDisplay(){
		login();

		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		List<WebElement> tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(0, tableRows.size());
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
		WebElement addNewNoteButton = driver.findElement(By.id("add-new-note"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", addNewNoteButton);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		String title = "title1";
		String description = "description1";
		NotePage notePage = new NotePage(driver);
		notePage.addNote(title, description);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(1, tableRows.size());
	}

	@Test
	@Order(5)
	public void testNoteEditAndDisplay(){

		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		//Edit click
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		//Save edited note
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		String title = "title-new";
		String description = "description-new";
		NotePage notePage = new NotePage(driver);
		notePage.addNote(title, description);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		// Check new table data
		List<WebElement> tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("th"));
		assertEquals(true, tableRows.get(0).getText().contains(title));
		tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("td"));
		assertEquals(true, tableRows.get(1).getText().contains(description));
	}

	@Test
	@Order(6)
	public void testNoteDeletionAndDisplay(){

		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.btn-danger")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		List<WebElement> tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(0, tableRows.size());
	}

	@Test
	@Order(7)
	public void testCredentialCreationWithEncryptPassword(){

		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		List<WebElement> tableRows = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(0, tableRows.size());
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-credential")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		String url = "google.com";
		String username = "arpit";
		String password = "arpit123";
		CredentialPage credentialPage = new CredentialPage(driver);
		credentialPage.addCredential(url, username, password);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		tableRows = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(1, tableRows.size());
		List<WebElement> tabledata = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("td"));
		assertEquals(false, tabledata.get(2).getText().equals(password));
	}

	@Test
	@Order(8)
	public void testCredentialEditWithDecryptPassword(){

		// click on edit
		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		// checks that the password on edit is decrypted back
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		assertEquals("arpit123", driver.findElement(By.id("credential-password")).getAttribute("value"));
		// edit new values
		String url = "yahoo.com";
		String username = "new-user";
		String password = "newpassword";
		CredentialPage credentialPage = new CredentialPage(driver);
		credentialPage.addCredential(url, username, password);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-success")));
		List<WebElement> tableRows = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(1, tableRows.size());
		List<WebElement> tabledata = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("td"));
		assertEquals(true, tabledata.get(1).getText().contains(username));
	}

	@Test
	@Order(9)
	public void testCredentialDeletionAndDisplay(){

		// click on delete
		WebDriverWait wait = new WebDriverWait(driver, 50);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.btn-danger")));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		List<WebElement> tableRows = driver.findElement(By.id("credentialTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(0, tableRows.size());
	}
}