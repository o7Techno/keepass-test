package de.thws.testing.tests.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.thws.testing.pages.HomePage;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.generator.PasswordTabPage;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

public class BasicPasswordTest {

	private WindowsDriver driver;
	private HomePage homePage;
	private GeneratorCommonPage commonPage;
	private PasswordTabPage passwordTab;

	@BeforeEach
	public void setUp() {
		driver = DriverFactory.createKeePassDriver();
		homePage = new HomePage(driver);
		commonPage = new GeneratorCommonPage(driver);
		passwordTab = new PasswordTabPage(driver);
	}

	// --- TEST 1: Basic Password Generation ---
	@Test
	public void test1_BasicPasswordGeneration() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		String initialPassword = commonPage.getGeneratedPassword();
		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String newPassword = commonPage.getGeneratedPassword();

		assertNotNull(newPassword, "ERROR: Password field is empty!");
		assertNotEquals(initialPassword, newPassword, "ERROR: Password did not change!");
		System.out.println("Test 01 Passed: Password generated successfully.");
	}

	// --- TEST 2: POSITIVE - Include Uppercase Letters ---
	@Test
	public void test2_IncludeUppercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.enableUppercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertTrue(newPassword.matches(".*[A-Z].*"),
				"ERROR: Password does NOT contain uppercase letters despite the setting being enabled! Password: "
						+ newPassword);
		System.out.println("Test 2 Passed: Uppercase inclusion verified.");
	}

	// --- TEST 3: NEGATIVE - Exclude Uppercase Letters ---
	@Test
	public void test3_ExcludeUppercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableUppercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[A-Z].*"),
				"ERROR: Password contains uppercase letters! Password: " + newPassword);
		System.out.println("Test 3 Passed: Uppercase exclusion verified.");
	}

	// --- TEST 4: POSITIVE - Include Lowercase Letters ---
	@Test
	public void test4_IncludeLowercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.enableLowercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertTrue(newPassword.matches(".*[a-z].*"),
				"ERROR: Password does NOT contain lowercase letters! Password: " + newPassword);
		System.out.println("Test 4 Passed: Lowercase inclusion verified.");
	}

	// --- TEST 5: NEGATIVE - Exclude Lowercase Letters ---
	@Test
	public void test5_ExcludeLowercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableLowercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[a-z].*"),
				"ERROR: Password contains lowercase letters! Password: " + newPassword);
		System.out.println("Test 5 Passed: Lowercase exclusion verified.");
	}

	@Test
	public void test6_IncludeNumbers() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.enableNumbers();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertTrue(newPassword.matches(".*[0-9].*"),
				"ERROR: Password does NOT contains numbers! Password: " + newPassword);
		System.out.println("Test 6 Passed: Number inclusion verified.");
	}

	// --- TEST 7: NEGATIVE - Exclude Numbers ---
	@Test
	public void test7_ExcludeNumbers() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableNumbers();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[0-9].*"), "ERROR: Password contains numbers! Password: " + newPassword);
		System.out.println("Test 7 Passed: Number exclusion verified.");
	}

	// --- TEST 8: Boundary Value - Custom Password Length ---
	@Test
	public void test8_CustomPasswordLength() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.setPasswordLength("25");
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertEquals(25, newPassword.length(), "ERROR: Length does not match requested value!");
		System.out.println("Test 8 Passed: Custom password length verified.");
	}

	// --- TEST 9: Copy to Clipboard ---
	@Test
	public void test9_CopyToClipboard() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		String generatedPassword = commonPage.getGeneratedPassword();
		commonPage.clickCopyButton();
		Thread.sleep(500);

		String clipboardText = commonPage.getClipboardText();
		assertEquals(generatedPassword, clipboardText, "ERROR: Clipboard content does not match!");
		System.out.println("Test 9 Passed: Password copied to clipboard successfully.");
	}

	// --- TEST 10: Close Window Action ---
	@Test
	public void test10_CloseButton() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		// Assert window is open
		assertTrue(commonPage.isGeneratorWindowVisible(), "ERROR: Generator window did not open!");

		commonPage.clickCloseButton();
		Thread.sleep(1000);

		// Assert window is closed
		assertFalse(commonPage.isGeneratorWindowVisible(),
				"ERROR: Generator window is still visible after clicking Close!");
		System.out.println("Test 10 Passed: Close button closed the dialog successfully.");
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			try {
				// Only click close if the test itself hasn't already closed it (like Test 09)
				if (commonPage.isGeneratorWindowVisible()) {
					commonPage.clickCloseButton();
					Thread.sleep(500);
				}
			} catch (Exception e) {
				System.out.println("Warning: Teardown close check failed.");
			}
			driver.quit();
		}
	}
}