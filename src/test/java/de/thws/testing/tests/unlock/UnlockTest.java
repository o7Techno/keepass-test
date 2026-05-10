package de.thws.testing.tests.unlock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.thws.testing.pages.unlock.UnlockPage;
import de.thws.testing.support.SuiteDatabaseSupport;
import de.thws.testing.tests.BaseTest;
import de.thws.testing.utils.DriverFactory;

public class UnlockTest extends BaseTest {

	private UnlockPage unlockPage;

	@BeforeEach
	public void setUp() throws Exception {

		String dbPath = SuiteDatabaseSupport.suiteDatabasePath().toAbsolutePath().toString();
		driver = DriverFactory.createKeePassDriverOpeningDatabase(dbPath);
		unlockPage = new UnlockPage(driver);
		Thread.sleep(1500);
	}

	@Test
	public void test1_InvalidMasterPassword() throws InterruptedException {
		unlockPage.enterMasterPassword("WrongPassword123!");
		Thread.sleep(500);

		unlockPage.clickUnlockButton();
		Thread.sleep(1000);

		assertTrue(unlockPage.isErrorMessageDisplayed(),
				"ERROR: The invalid credentials error message was NOT displayed!");
		System.out.println("Test 1 Passed: Invalid credentials correctly blocked.");
	}

	@Test
	public void test2_TogglePasswordVisibilityShortcut() throws InterruptedException {
		unlockPage.enterMasterPassword("SecretPass");
		Thread.sleep(500);

		unlockPage.togglePasswordVisibility();
		Thread.sleep(500);

		System.out.println("Test 2 Passed: Ctrl+H shortcut executed successfully without errors.");
	}

	@Test
	public void test3_ValidMasterPassword() throws InterruptedException {
		String validPassword = SuiteDatabaseSupport.suiteMasterPassword();
		unlockPage.enterMasterPassword(validPassword);
		Thread.sleep(500);

		unlockPage.clickUnlockButton();
		Thread.sleep(2000);

		assertFalse(unlockPage.isErrorMessageDisplayed(), "ERROR: A valid password triggered an error message!");
		System.out.println("Test 3 Passed: Database unlocked successfully with valid credentials.");
	}

	@Test
	public void test4_EmptyPasswordSubmission() throws InterruptedException {
		unlockPage.enterMasterPassword("");
		Thread.sleep(500);

		unlockPage.clickUnlockButton();
		Thread.sleep(1000);

		assertTrue(unlockPage.isEmptyPasswordPopupDisplayed(), "ERROR: Empty password pop-up was NOT displayed!");

		unlockPage.clickCancelOnPopup();
		Thread.sleep(500);

		System.out.println("Test 4 Passed: Empty password pop-up correctly triggered and safely handled.");
	}

	@Test
	public void test5_CloseButtonFunctionality() throws InterruptedException {
		unlockPage.clickCloseButton();
		Thread.sleep(1500);

		assertTrue(unlockPage.isWelcomeScreenDisplayed(),
				"ERROR: Application did not return to the Welcome screen after clicking Close!");
		System.out.println("Test 5 Passed: Close button successfully navigated to the Welcome screen.");
	}
}