package de.thws.testing.tests.generator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import de.thws.testing.pages.HomePage;
import de.thws.testing.pages.generator.AdvancedPanel;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.generator.PasswordTabPage;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

@EnabledOnOs(OS.WINDOWS)
public class AdvancedPasswordTest {

	private WindowsDriver driver;
	private HomePage homePage;
	private GeneratorCommonPage commonPage;
	private PasswordTabPage passwordTab;
	private AdvancedPanel advancedPanel;

	@BeforeEach
	public void setUp() throws InterruptedException {
		driver = DriverFactory.createKeePassDriver();
		homePage = new HomePage(driver);
		commonPage = new GeneratorCommonPage(driver);
		passwordTab = new PasswordTabPage(driver);
		advancedPanel = new AdvancedPanel(driver);

		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		resetToDefaultState();
	}

	private void resetToDefaultState() throws InterruptedException {
		passwordTab.ensureAdvancedModeOpen();
		Thread.sleep(500);

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
		Thread.sleep(500);
	}

	@Test
	public void test1_HexPasswordsOnly() throws InterruptedException {
		advancedPanel.clickHexButton();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isHex = newPassword.matches("^[a-fA-F0-9]+$");
		assertTrue(isHex, newPassword);
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

		assertFalse(containsRestricted, newPassword);
	}

	@Test
	public void test3_ExcludeLookAlikeCharacters() throws InterruptedException {
		advancedPanel.enableExcludeLookAlike();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean containsLookAlike = newPassword.matches(".*[O0lI1].*");

		assertFalse(containsLookAlike, newPassword);
	}

	@Test
	public void test4_DisableAllSpecialCharacters() throws InterruptedException {
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		boolean isAlphanumericOnly = newPassword.matches("^[A-Za-z0-9]+$");
		assertTrue(isAlphanumericOnly, newPassword);
	}

	@Test
	public void test5_IncludeAdditionalCharacters() throws InterruptedException {
		passwordTab.setPasswordLength("50");
		Thread.sleep(500);

		String customChar = "\u20ac";
		advancedPanel.setAdditionalCharacters(customChar);
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();

		assertTrue(newPassword.contains(customChar));
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

		assertTrue(containsExtendedASCII, newPassword);
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

		assertTrue(hasUpper && hasLower && hasNumber, newPassword);
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
		assertTrue(isOnlyBraces, newPassword);
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			try {
				if (commonPage.isGeneratorWindowVisible()) {
					commonPage.clickCloseButton();
					Thread.sleep(500);
				}
			} catch (Exception ignored) {
			}
			driver.quit();
		}
	}
}
