package de.thws.testing.pages.unlock;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class UnlockPage extends BasePage {

	// --- LOCATORS ---
	private By passwordInput = MobileBy.AccessibilityId(
			"databaseOpenWidget.formContainer.centralStack.mainPage.enterPasswordComponent.editPassword.passwordEdit");
	private By unlockButton = MobileBy.name("Unlock");
	private By closeButton = MobileBy.name("Close");

	private String exactErrorMessage = "Error while reading the database: Invalid credentials were provided, please try again.\nIf this reoccurs, then your database file may be corrupt. (HMAC mismatch)";
	private By errorMessageLabel = MobileBy.name(exactErrorMessage);

	private By retryEmptyPasswordButton = MobileBy.name("Retry with empty password");
	private By cancelPopupButton = MobileBy.name("Cancel");

	private By createDatabaseButton = MobileBy
			.AccessibilityId("MainWindow.centralwidget.stackedWidget.pageWelcome.welcomeWidget.buttonNewDatabase");

	public UnlockPage(WindowsDriver driver) {
		super(driver);
	}
	// --- ACTION METHODS ---

	public void enterMasterPassword(String password) {
		WebElement inputField = driver.findElement(passwordInput);
		inputField.click();
		inputField.sendKeys(Keys.CONTROL + "a");
		inputField.sendKeys(Keys.BACK_SPACE);
		inputField.sendKeys(password);
	}

	public void clickUnlockButton() {
		forceClick(driver.findElement(unlockButton));
	}

	public void clickCloseButton() {
		forceClick(driver.findElement(closeButton));
	}

	public void togglePasswordVisibility() {
		WebElement inputField = driver.findElement(passwordInput);
		inputField.sendKeys(Keys.CONTROL + "h");
	}

	public boolean isErrorMessageDisplayed() {
		return isElementDisplayedSafely(errorMessageLabel);
	}

	public boolean isEmptyPasswordPopupDisplayed() {
		return isElementDisplayedSafely(retryEmptyPasswordButton);
	}

	public void clickCancelOnPopup() {
		forceClick(driver.findElement(cancelPopupButton));
	}

	public boolean isWelcomeScreenDisplayed() {
		return isElementDisplayedSafely(createDatabaseButton);
	}

	private boolean isElementDisplayedSafely(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
			return false;
		}
	}
}