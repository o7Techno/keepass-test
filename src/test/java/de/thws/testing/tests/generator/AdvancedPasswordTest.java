package de.thws.testing.tests.generator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.thws.testing.pages.generator.AdvancedPanel;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.generator.PasswordTabPage;
import de.thws.testing.pages.main.MainWindowPage;
import de.thws.testing.pages.unlock.UnlockPage;
import de.thws.testing.support.SuiteDatabaseSupport;
import de.thws.testing.tests.BaseTest;
import de.thws.testing.utils.DriverFactory;

public class AdvancedPasswordTest extends BaseTest {

	private MainWindowPage homePage;
	private GeneratorCommonPage commonPage;
	private PasswordTabPage passwordTab;
	private AdvancedPanel advancedPanel;

	@BeforeEach
	public void setUp() throws Exception {
		String dbPath = SuiteDatabaseSupport.suiteDatabasePath().toAbsolutePath().toString();
		driver = DriverFactory.createKeePassDriverOpeningDatabase(dbPath);
		Thread.sleep(1500);

		UnlockPage unlockPage = new UnlockPage(driver);
		unlockPage.enterMasterPassword(SuiteDatabaseSupport.suiteMasterPassword());
		unlockPage.clickUnlockButton();
		Thread.sleep(1500);

		homePage = new MainWindowPage(driver);
		commonPage = new GeneratorCommonPage(driver);
		passwordTab = new PasswordTabPage(driver);
		advancedPanel = new AdvancedPanel(driver);

		homePage.openPasswordGenerator();
		Thread.sleep(800);

		passwordTab.ensurePasswordTabOpen();
		Thread.sleep(400);

		resetToDefaultState();
	}

	private void resetToDefaultState() throws InterruptedException {
		passwordTab.ensureAdvancedModeOpen();
		Thread.sleep(400);

		passwordTab.enableUppercase();
		passwordTab.enableLowercase();
		passwordTab.enableNumbers();
		passwordTab.disableSpecialChars();
		passwordTab.disableExtASCII();

		passwordTab.setPasswordLength("16");

		advancedPanel.setExcludedCharacters("");
		advancedPanel.setAdditionalCharacters("");
		advancedPanel.disableExcludeLookAlike();
		advancedPanel.disableEnsureEveryGroup();
		advancedPanel.disableAllSpecialCharacters();
		Thread.sleep(400);
	}

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

	@Test
	public void test3_ExcludeLookAlikeCharacters() throws InterruptedException {
		advancedPanel.enableExcludeLookAlike();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		boolean containsLookAlike = newPassword.matches(".*[O0lI1].*");

		assertFalse(containsLookAlike, "ERROR: Password contains look-alike characters! Password: " + newPassword);
		System.out.println("Test 3 Passed: Look-alike characters were successfully excluded.");
	}

	@Test
	public void test4_DisableAllSpecialCharacters() throws InterruptedException {
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isAlphanumericOnly = newPassword.matches("^[A-Za-z0-9]+$");
		assertTrue(isAlphanumericOnly, "ERROR: Password still contains special characters! Password: " + newPassword);
		System.out.println("Test 4 Passed: All special character groups were disabled.");
	}

	@Test
	public void test5_IncludeAdditionalCharacters() throws InterruptedException {
		passwordTab.disableUppercase();
		passwordTab.disableLowercase();
		passwordTab.disableNumbers();
		Thread.sleep(500);

		String customChar = "€";
		advancedPanel.setAdditionalCharacters(customChar);
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		assertTrue(newPassword.contains(customChar), "ERROR: The additional character was not included!");

		boolean isOnlyCustomChar = newPassword.matches("^[" + customChar + "]+$");
		assertTrue(isOnlyCustomChar, "ERROR: Password contains unexpected characters! Password: " + newPassword);

		System.out.println("Test 5 Passed: Additional custom character inclusion verified deterministically.");
	}

	@Test
	public void test6_ExtendedASCII() throws InterruptedException {
		passwordTab.enableExtASCII();
		passwordTab.setPasswordLength("64");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean containsExtendedASCII = !newPassword.matches("^[\\x00-\\x7F]+$");

		assertTrue(containsExtendedASCII,
				"ERROR: Password does not seem to contain Extended ASCII characters! Password: " + newPassword);
		System.out.println("Test 6 Passed: Extended ASCII characters are present.");
	}

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

	@Test
	public void test8_BracesOnly() throws InterruptedException {
		passwordTab.disableUppercase();
		passwordTab.disableLowercase();
		passwordTab.disableNumbers();

		advancedPanel.disableAllSpecialCharacters();
		advancedPanel.enableBraces();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isOnlyBraces = newPassword.matches("^[\\{\\}\\[\\]\\(\\)<>]+$");
		assertTrue(isOnlyBraces, "ERROR: Password contains characters other than Braces! Password: " + newPassword);
		System.out.println("Test 8 Passed: Password successfully generated using ONLY Braces.");
	}

	@AfterEach
	public void closeGeneratorWindow() {
		try {
			if (commonPage != null && commonPage.isGeneratorWindowVisible()) {
				commonPage.clickCloseButton();
				Thread.sleep(500);
			}
		} catch (Exception ignored) {
		}
	}
}