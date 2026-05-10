package de.thws.testing.flow;

import de.thws.testing.pages.common.SaveDatabaseDialogPage;
import de.thws.testing.pages.main.MainWindowPage;
import de.thws.testing.pages.welcome.WelcomePage;
import de.thws.testing.pages.wizard.NewDatabaseWizardPage;
import io.appium.java_client.windows.WindowsDriver;

public final class DatabaseCreationFlow {

	private DatabaseCreationFlow() {
	}

	public static void createDatabaseFileOnly(WindowsDriver driver, String databasePath, String masterPassword,
			String databaseDisplayName) throws InterruptedException {
		WelcomePage welcome = new WelcomePage(driver);
		NewDatabaseWizardPage wizard = new NewDatabaseWizardPage(driver);
		SaveDatabaseDialogPage saveAs = new SaveDatabaseDialogPage(driver);

		welcome.clickCreateDatabase();
		Thread.sleep(400);

		wizard.setDatabaseDisplayName(databaseDisplayName);
		wizard.clickContinue();
		Thread.sleep(400);

		wizard.clickContinue();
		Thread.sleep(400);

		wizard.setMasterPassword(masterPassword);
		wizard.setMasterPasswordConfirm(masterPassword);
		wizard.clickDone();
		Thread.sleep(400);
		wizard.dismissWeakMasterPasswordWarningIfPresent();
		Thread.sleep(400);

		Thread.sleep(900);
		saveAs.saveAs(databasePath);
		Thread.sleep(800);
	}

	public static void createDatabaseAndOpenNewEntry(WindowsDriver driver, String databasePath, String masterPassword,
			String databaseDisplayName) throws InterruptedException {
		WelcomePage welcome = new WelcomePage(driver);
		NewDatabaseWizardPage wizard = new NewDatabaseWizardPage(driver);
		SaveDatabaseDialogPage saveAs = new SaveDatabaseDialogPage(driver);
		MainWindowPage main = new MainWindowPage(driver);

		welcome.clickCreateDatabase();
		Thread.sleep(400);

		wizard.setDatabaseDisplayName(databaseDisplayName);
		wizard.clickContinue();
		Thread.sleep(400);

		wizard.clickContinue();
		Thread.sleep(400);

		wizard.setMasterPassword(masterPassword);
		wizard.setMasterPasswordConfirm(masterPassword);
		wizard.clickDone();
		Thread.sleep(400);
		wizard.dismissWeakMasterPasswordWarningIfPresent();
		Thread.sleep(400);

		Thread.sleep(900);
		saveAs.saveAs(databasePath);
		Thread.sleep(800);

		main.clickNewEntry();
		Thread.sleep(500);
	}

}
