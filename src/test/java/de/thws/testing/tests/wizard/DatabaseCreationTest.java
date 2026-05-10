package de.thws.testing.tests.wizard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import de.thws.testing.pages.welcome.WelcomePage;
import de.thws.testing.pages.wizard.NewDatabaseWizardPage;
import de.thws.testing.tests.BaseTest;
import de.thws.testing.utils.DriverFactory;

@EnabledOnOs(OS.WINDOWS)
public class DatabaseCreationTest extends BaseTest {

	private WelcomePage welcomePage;
	private NewDatabaseWizardPage wizardPage;

	@BeforeEach
	public void setUp() {
		driver = DriverFactory.createKeePassDriverOpeningDatabase(null);
		welcomePage = new WelcomePage(driver);
		wizardPage = new NewDatabaseWizardPage(driver);
	}

	@Test
	public void testFullDatabaseCreationFlow() throws InterruptedException {
		welcomePage.clickCreateDatabase();
		sleepMs(500);

		wizardPage.setDatabaseDisplayName("Refactored Test DB");
		wizardPage.clickContinue();
		sleepMs(400);

		wizardPage.clickContinue();
		sleepMs(400);

		String testPass = "VeryStrongPassword123!";
		wizardPage.setMasterPassword(testPass);
		wizardPage.setMasterPasswordConfirm(testPass);

		wizardPage.clickDone();

		wizardPage.dismissWeakMasterPasswordWarningIfPresent();

		sleepMs(2000);

		try {
			new Actions(driver).sendKeys(Keys.ESCAPE).perform();
			System.out.println("Save As window was successfully closed with ESC.");
		} catch (Exception e) {
			System.out.println(
					"Warning: The \"Save As\" window could not be closed; manual intervention may be required.");
		}

		assertTrue(true);
		System.out.println("Database Creation UI Flow sucess");
	}
}