package de.thws.testing.tests.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.thws.testing.pages.HomePage;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.generator.PassphraseTabPage;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

public class PassphraseTest {

	private WindowsDriver driver;
	private HomePage homePage;
	private GeneratorCommonPage commonPage;
	private PassphraseTabPage passphraseTab;

	@BeforeEach
	public void setUp() throws InterruptedException {
		driver = DriverFactory.createKeePassDriver();
		homePage = new HomePage(driver);
		commonPage = new GeneratorCommonPage(driver);
		passphraseTab = new PassphraseTabPage(driver);

		homePage.openPasswordGenerator();
		Thread.sleep(1000);

		passphraseTab.clickPassphraseTab();
		Thread.sleep(500);

		resetToDefaultState();
	}

	private void resetToDefaultState() throws InterruptedException {
		passphraseTab.setWordCount("7");
		passphraseTab.setWordSeparator(" ");
		Thread.sleep(500);
	}

	@Test
	public void test1_DefaultPassphrase() throws InterruptedException {
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		int spaceCount = newPassphrase.length() - newPassphrase.replace(" ", "").length();

		assertTrue(newPassphrase.length() > 10);
		assertEquals(6, spaceCount);
	}

	@Test
	public void test2_CustomWordCount() throws InterruptedException {
		passphraseTab.setWordCount("4");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		int spaceCount = newPassphrase.length() - newPassphrase.replace(" ", "").length();

		assertEquals(3, spaceCount);
	}

	@Test
	public void test3_CustomSeparator() throws InterruptedException {
		passphraseTab.setWordSeparator("-");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertTrue(newPassphrase.contains("-"));
		assertFalse(newPassphrase.contains(" "));
	}

	@Test
	public void test4_WordCaseUpper() throws InterruptedException {
		passphraseTab.selectWordCase("UPPER CASE");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertEquals(newPassphrase.toUpperCase(), newPassphrase);
	}

	@Test
	public void test5_WordCaseTitle() throws InterruptedException {
		passphraseTab.selectWordCase("Title Case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		String[] words = newPassphrase.split(" ");
		for (String word : words) {
			assertTrue(Character.isUpperCase(word.charAt(0)), word);
		}
	}

	@Test
	public void test6_WordCaseMixed() throws InterruptedException {
		passphraseTab.selectWordCase("MIXED case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		boolean isNotAllUpper = !newPassphrase.equals(newPassphrase.toUpperCase());
		boolean isNotAllLower = !newPassphrase.equals(newPassphrase.toLowerCase());

		assertTrue(isNotAllUpper && isNotAllLower, newPassphrase);
	}

	@Test
	public void test7_WordCaseLower() throws InterruptedException {
		passphraseTab.selectWordCase("lower case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertEquals(newPassphrase.toLowerCase(), newPassphrase);
	}

	@Test
	public void test8_WordlistManagement() throws InterruptedException {
		assertTrue(passphraseTab.isAddWordListButtonDisplayed());
		assertTrue(passphraseTab.isDeleteWordListButtonDisplayed());

		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String defaultPassphrase = commonPage.getGeneratedPassword();

		passphraseTab.selectDifferentWordList();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String newPassphrase = commonPage.getGeneratedPassword();

		assertFalse(defaultPassphrase.equals(newPassphrase));
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
