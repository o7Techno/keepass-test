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

	@Test
	public void test1_BasicPasswordGeneration() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		String initialPassword = commonPage.getGeneratedPassword();
		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String newPassword = commonPage.getGeneratedPassword();

		assertNotNull(newPassword);
		assertNotEquals(initialPassword, newPassword);
	}

	@Test
	public void test2_IncludeUppercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.enableUppercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertTrue(newPassword.matches(".*[A-Z].*"), newPassword);
	}

	@Test
	public void test3_ExcludeUppercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableUppercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[A-Z].*"), newPassword);
	}

	@Test
	public void test4_IncludeLowercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.enableLowercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertTrue(newPassword.matches(".*[a-z].*"), newPassword);
	}

	@Test
	public void test5_ExcludeLowercaseLetters() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableLowercase();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[a-z].*"), newPassword);
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
		assertTrue(newPassword.matches(".*[0-9].*"), newPassword);
	}

	@Test
	public void test7_ExcludeNumbers() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.disableNumbers();
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertFalse(newPassword.matches(".*[0-9].*"), newPassword);
	}

	@Test
	public void test8_CustomPasswordLength() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passwordTab.setPasswordLength("25");
		Thread.sleep(500);
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassword = commonPage.getGeneratedPassword();
		assertEquals(25, newPassword.length());
	}

	@Test
	public void test9_CopyToClipboard() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		String generatedPassword = commonPage.getGeneratedPassword();
		commonPage.clickCopyButton();
		Thread.sleep(500);

		String clipboardText = commonPage.getClipboardText();
		assertEquals(generatedPassword, clipboardText);
	}

	@Test
	public void test10_CloseButton() throws InterruptedException {
		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		assertTrue(commonPage.isGeneratorWindowVisible());

		commonPage.clickCloseButton();
		Thread.sleep(1000);

		assertFalse(commonPage.isGeneratorWindowVisible());
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
