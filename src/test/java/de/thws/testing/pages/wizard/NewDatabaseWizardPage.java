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

	/**
	 * После «Готово» KeePassXC может показать предупреждение «Слабый пароль». Если
	 * окно есть — нажимаем продолжить со слабым паролём (RU/EN).
	 */
	public void dismissWeakMasterPasswordWarningIfPresent() throws InterruptedException {
		String[] buttonNames = { "Продолжить со слабым паролем", "Continue with weak password" };
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
					// next label
				}
			}
			Thread.sleep(200);
		}
	}

	private void setTextField(String automationId, String value) {
		WebElement field = waitClickable(automationId);
		field.click();
		field.sendKeys(Keys.CONTROL + "a");
		field.sendKeys(Keys.BACK_SPACE);
		field.sendKeys(value);
	}

	private WebElement waitClickable(String automationId) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		return wait.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(automationId)));
	}
}
