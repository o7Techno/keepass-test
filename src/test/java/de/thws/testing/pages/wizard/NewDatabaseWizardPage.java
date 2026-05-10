package de.thws.testing.pages.wizard;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import de.thws.testing.utils.SecureFieldInput;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class NewDatabaseWizardPage {

	private final WindowsDriver driver;

	public NewDatabaseWizardPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void setDatabaseDisplayName(String name) {
		WebElement field = waitClickable(KeePassAccessibility.WIZARD_EDIT_DATABASE_NAME);
		field.click();
		field.sendKeys(Keys.CONTROL + "a");
		field.sendKeys(Keys.BACK_SPACE);
		field.sendKeys(name);
	}

	public void clickContinue() {
		waitClickable(KeePassAccessibility.WIZARD_BUTTON_CONTINUE).click();
	}

	public void clickCancel() {
		waitClickable(KeePassAccessibility.WIZARD_BUTTON_CANCEL).click();
	}

	public void setMasterPassword(String password) throws InterruptedException {
		SecureFieldInput.pasteReplacing(waitClickable(KeePassAccessibility.WIZARD_EDIT_MASTER_PASSWORD), password);
	}

	public void setMasterPasswordConfirm(String password) throws InterruptedException {
		SecureFieldInput.pasteReplacing(waitClickable(KeePassAccessibility.WIZARD_EDIT_MASTER_PASSWORD_CONFIRM),
				password);
	}

	public void clickDone() {
		waitClickable(KeePassAccessibility.WIZARD_BUTTON_DONE).click();
	}

	public void dismissWeakMasterPasswordWarningIfPresent() throws InterruptedException {
		String[] buttonNames = {
				"\u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c \u0441\u043e \u0441\u043b\u0430\u0431\u044b\u043c \u043f\u0430\u0440\u043e\u043b\u0435\u043c",
				"Continue with weak password" };
		long deadline = System.currentTimeMillis() + 10_000;
		while (System.currentTimeMillis() < deadline) {
			for (String n : buttonNames) {
				try {
					WebElement el = driver.findElement(MobileBy.name(n));
					if (el.isDisplayed() && el.isEnabled()) {
						el.click();
						return;
					}
				} catch (Exception ignored) {
				}
			}
			Thread.sleep(200);
		}
	}

	private WebElement waitClickable(String automationId) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		return wait.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(automationId)));
	}
}
