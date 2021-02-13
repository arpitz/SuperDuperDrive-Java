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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	public int port;

	public static WebDriver driver;

	public String baseURL;

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

	@Test
	public void testUserSignupLoginAndHomepage() {
		String username = "aaa";
		String password = "a123";

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Arpit", "Trivedi", username, password);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebElement fileElement = driver.findElement(By.id("nav-files"));
		assertEquals(true, fileElement.isDisplayed());
	}

	public void testUnauthorizedAccess(){
		// any user without signingup can see only login and signup pages nothing else
	}

	public void testAccessAfterLogout(){
		// the home should not be accessible after logout
	}

	@Test
	public void testNoteCreationAndDisplay(){
		testUserSignupLoginAndHomepage();
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		List<WebElement> tableRows = driver.findElement(By.id("userTable")).
				findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		assertEquals(0, tableRows.size());

		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));

		WebElement addNewNoteButton = driver.findElement(By.id("add-new-note"));
		addNewNoteButton.click();

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

	public void testNoteDeletionAndDisplay(){

	}

	public void testNoteEditAndDisplay(){

	}

	public void testCredentialCreationAndDisplay(){

	}

	public void testCredCreateWithEncryptedPassword(){

	}

	public void testCredentialDeletionAndDisplay(){

	}

	public void testCredentialEditAndDisplayDecryptPassword(){

	}

}
