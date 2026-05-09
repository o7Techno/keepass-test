package de.thws.testing.tests.generator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.thws.testing.pages.HomePage;
import de.thws.testing.pages.generator.AdvancedPanel;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.generator.PasswordTabPage;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

public class AdvancedPasswordTest {

	private WindowsDriver driver;
	private HomePage homePage;
	private GeneratorCommonPage commonPage;
	private PasswordTabPage passwordTab;
	private AdvancedPanel advancedPanel;

	@BeforeEach
	public void setUp() throws InterruptedException {
		// 1. Initialize Driver and Page Objects
		driver = DriverFactory.createKeePassDriver();
		homePage = new HomePage(driver);
		commonPage = new GeneratorCommonPage(driver);
		passwordTab = new PasswordTabPage(driver);
		advancedPanel = new AdvancedPanel(driver);

		// 2. Open Generator explicitly for every test
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		// 3. Apply Clean State (DRY Principle: Prevents State Leakage globally)
		resetToDefaultState();
	}

	/**
	 * Helper method to ensure a clean, standard baseline before every test. This
	 * eliminates cross-test contamination (State Leakage).
	 */
	private void resetToDefaultState() throws InterruptedException {
		passwordTab.ensureAdvancedModeOpen();
		Thread.sleep(500);

		// Reset Basic Checkboxes to Standard KeePassXC Default
		passwordTab.enableUppercase();
		passwordTab.enableLowercase();
		passwordTab.enableNumbers();
		passwordTab.disableSpecialChars();
		passwordTab.disableExtASCII();

		// Reset Length
		passwordTab.setPasswordLength("16");

		// Reset Advanced Text Fields and Checkboxes
		advancedPanel.setExcludedCharacters("");
		advancedPanel.setAdditionalCharacters("");
		advancedPanel.disableExcludeLookAlike();
		advancedPanel.disableEnsureEveryGroup();
		advancedPanel.disableAllSpecialCharacters();
		Thread.sleep(500);
	}

	// --- TEST 1: Hex Passwords Generation ---
	@Test
	public void test1_HexPasswordsOnly() throws InterruptedException {
		advancedPanel.clickHexButton();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isHex = newPassword.matches("^[a-fA-F0-9]+$");
		assertTrue(isHex, "ERROR: Password is not a valid Hex string! Password: " + newPassword);
		System.out.println("Test 1 Passed: Hex password format verified.");
	}

	// --- TEST 2: Do Not Include Specific Characters ---
	@Test
	public void test2_DoNotIncludeCharacters() throws InterruptedException {
		String restrictedChars = "AAA123";
		advancedPanel.setExcludedCharacters(restrictedChars);
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		boolean containsRestricted = newPassword.matches(".*[AAA123].*");

		assertFalse(containsRestricted, "ERROR: Password contains restricted characters! Password: " + newPassword);
		System.out.println("Test 2 Passed: Restricted characters were successfully excluded.");
	}

	// --- TEST 3: Exclude Look-alike Characters ---
	@Test
	public void test3_ExcludeLookAlikeCharacters() throws InterruptedException {
		advancedPanel.enableExcludeLookAlike();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		// Look-alike characters usually include: O, 0, l, 1, I
		boolean containsLookAlike = newPassword.matches(".*[O0lI1].*");

		assertFalse(containsLookAlike, "ERROR: Password contains look-alike characters! Password: " + newPassword);
		System.out.println("Test 3 Passed: Look-alike characters were successfully excluded.");
	}

	// --- TEST 4: Disable All Special Characters ---
	@Test
	public void test4_DisableAllSpecialCharacters() throws InterruptedException {
		// Note: Special chars are already disabled by resetToDefaultState(),
		// so we just generate and verify the baseline behavior.
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isAlphanumericOnly = newPassword.matches("^[A-Za-z0-9]+$");
		assertTrue(isAlphanumericOnly, "ERROR: Password still contains special characters! Password: " + newPassword);
		System.out.println("Test 4 Passed: All special character groups were disabled.");
	}

	// --- TEST 5: Include Additional Custom Characters ---
	@Test
	public void test5_IncludeAdditionalCharacters() throws InterruptedException {
		passwordTab.setPasswordLength("50");
		Thread.sleep(500);

		String customChar = "€";
		advancedPanel.setAdditionalCharacters(customChar);
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		assertTrue(newPassword.contains(customChar), "ERROR: The additional character was not included!");
		System.out.println("Test 5 Passed: Additional custom character inclusion verified.");
	}

	// --- TEST 6: Extended ASCII Characters ---
	@Test
	public void test6_ExtendedASCII() throws InterruptedException {
		passwordTab.enableExtASCII();
		passwordTab.setPasswordLength("64");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		// We verify it's NOT just standard ASCII. If it contains non-standard ASCII,
		// test passes.
		boolean containsExtendedASCII = !newPassword.matches("^[\\x00-\\x7F]+$");

		assertTrue(containsExtendedASCII,
				"ERROR: Password does not seem to contain Extended ASCII characters! Password: " + newPassword);
		System.out.println("Test 6 Passed: Extended ASCII characters are present.");
	}

	// --- TEST 7: Ensure Characters from Every Group ---
	@Test
	public void test7_EnsureEveryGroup() throws InterruptedException {
		advancedPanel.enableEnsureEveryGroup();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean hasUpper = newPassword.matches(".*[A-Z].*");
		boolean hasLower = newPassword.matches(".*[a-z].*");
		boolean hasNumber = newPassword.matches(".*[0-9].*");

		assertTrue(hasUpper && hasLower && hasNumber,
				"ERROR: Password did not pick from every selected group! Password: " + newPassword);
		System.out.println("Test 7 Passed: 'Ensure every group' functionality verified.");
	}

	// --- TEST 8: Individual Special Group - Braces Only (The Ultimate Edge Case)
	// ---
	@Test
	public void test8_BracesOnly() throws InterruptedException {
		// Override the baseline defaults for this specific edge case
		passwordTab.disableUppercase();
		passwordTab.disableLowercase();
		passwordTab.disableNumbers();

		advancedPanel.disableAllSpecialCharacters();
		advancedPanel.enableBraces(); // Sub switch ON
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isOnlyBraces = newPassword.matches("^[\\{\\}\\[\\]\\(\\)<>]+$");
		assertTrue(isOnlyBraces, "ERROR: Password contains characters other than Braces! Password: " + newPassword);
		System.out.println("Test 8 Passed: Password successfully generated using ONLY Braces.");
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			try {
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