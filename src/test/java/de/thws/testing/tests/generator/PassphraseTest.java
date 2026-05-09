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

	// --- TEST 1: Default Passphrase Generation ---
	@Test
	public void test1_DefaultPassphrase() throws InterruptedException {
		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		int spaceCount = newPassphrase.length() - newPassphrase.replace(" ", "").length();

		assertTrue(newPassphrase.length() > 10, "ERROR: Passphrase is too short!");
		assertEquals(6, spaceCount, "ERROR: Default 7 words should have exactly 6 spaces!");
		System.out.println("Test 1 Passed: Default passphrase generation verified.");
	}

	// --- TEST 2: Custom Word Count ---
	@Test
	public void test2_CustomWordCount() throws InterruptedException {
		passphraseTab.setWordCount("4");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		int spaceCount = newPassphrase.length() - newPassphrase.replace(" ", "").length();

		assertEquals(3, spaceCount, "ERROR: 4 words should have exactly 3 spaces!");
		System.out.println("Test 2 Passed: Custom word count verified.");
	}

	// --- TEST 3: Custom Separator ---
	@Test
	public void test3_CustomSeparator() throws InterruptedException {
		passphraseTab.setWordSeparator("-");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertTrue(newPassphrase.contains("-"), "ERROR: Passphrase does not contain the custom separator!");
		assertFalse(newPassphrase.contains(" "),
				"ERROR: Passphrase still contains spaces instead of the custom separator!");
		System.out.println("Test 3 Passed: Custom separator '-' verified.");
	}

	// --- TEST 4: Word Case - UPPER CASE ---
	@Test
	public void test4_WordCaseUpper() throws InterruptedException {
		passphraseTab.selectWordCase("UPPER CASE");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertEquals(newPassphrase.toUpperCase(), newPassphrase, "ERROR: Passphrase is not in UPPER CASE!");
		System.out.println("Test 4 Passed: UPPER CASE word format verified.");
	}

	// --- TEST 5: Word Case - Title Case ---
	@Test
	public void test5_WordCaseTitle() throws InterruptedException {
		passphraseTab.selectWordCase("Title Case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		String[] words = newPassphrase.split(" ");
		for (String word : words) {
			assertTrue(Character.isUpperCase(word.charAt(0)),
					"ERROR: Word does not start with an uppercase letter! Word: " + word);
		}
		System.out.println("Test 5 Passed: Title Case word format verified.");
	}

	// --- TEST 6: Word Case - MIXED case ---
	@Test
	public void test6_WordCaseMixed() throws InterruptedException {
		passphraseTab.selectWordCase("MIXED case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		boolean isNotAllUpper = !newPassphrase.equals(newPassphrase.toUpperCase());
		boolean isNotAllLower = !newPassphrase.equals(newPassphrase.toLowerCase());

		assertTrue(isNotAllUpper && isNotAllLower, "ERROR: Passphrase is not MIXED case! Passphrase: " + newPassphrase);
		System.out.println("Test 6 Passed: MIXED case word format verified.");
	}

	// --- TEST 7: Word Case - lower case ---
	@Test
	public void test7_WordCaseLower() throws InterruptedException {
		passphraseTab.selectWordCase("lower case");
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);

		String newPassphrase = commonPage.getGeneratedPassword();

		assertEquals(newPassphrase.toLowerCase(), newPassphrase, "ERROR: Passphrase is not in lower case!");
		System.out.println("Test 4 Passed: lowercase word format verified.");
	}

	// --- TEST 8: Wordlist Management & Selection ---
	@Test
	public void test7_WordlistManagement() throws InterruptedException {
		assertTrue(passphraseTab.isAddWordListButtonDisplayed(), "ERROR: Add Custom Wordlist button is not displayed!");
		assertTrue(passphraseTab.isDeleteWordListButtonDisplayed(), "ERROR: Delete Wordlist button is not displayed!");
		System.out.println("Wordlist UI components are correctly displayed.");

		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String defaultPassphrase = commonPage.getGeneratedPassword();

		passphraseTab.selectDifferentWordList();
		Thread.sleep(500);

		commonPage.clickGenerateButton();
		Thread.sleep(500);
		String newPassphrase = commonPage.getGeneratedPassword();

		assertFalse(defaultPassphrase.equals(newPassphrase),
				"ERROR: Passphrase did not change after selecting a new Wordlist!");
		System.out.println("Test 7 Passed: Different wordlist selection verified successfully.");
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
