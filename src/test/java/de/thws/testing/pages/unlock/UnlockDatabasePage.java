package de.thws.testing.pages.unlock;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import de.thws.testing.utils.SecureFieldInput;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class UnlockDatabasePage {

	private final WindowsDriver driver;

	public UnlockDatabasePage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void unlockIfNeeded(String masterPassword) throws InterruptedException {
		WebDriverWait shortWait = new WebDriverWait(driver, 12);
		WebElement pwdField;
		try {
			pwdField = shortWait.until(ExpectedConditions.presenceOfElementLocated(
					MobileBy.AccessibilityId(KeePassAccessibility.UNLOCK_DATABASE_PASSWORD_EDIT)));
		} catch (TimeoutException e) {
			return;
		}

		if (!pwdField.isDisplayed()) {
			return;
		}

		SecureFieldInput.pasteReplacing(pwdField, masterPassword);
		Thread.sleep(200);

		pwdField.sendKeys(Keys.ENTER);
		Thread.sleep(1000);

		if (isUnlockPasswordFieldStillPresent()) {
			tryClickUnlockButton();
			Thread.sleep(1000);
		}
	}

	private boolean isUnlockPasswordFieldStillPresent() {
		try {
			WebElement el = driver.findElement(
					MobileBy.AccessibilityId(KeePassAccessibility.UNLOCK_DATABASE_PASSWORD_EDIT));
			return el.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	private void tryClickUnlockButton() {
		try {
			new WebDriverWait(driver, 4).until(ExpectedConditions.elementToBeClickable(
					MobileBy.AccessibilityId(KeePassAccessibility.UNLOCK_DATABASE_BUTTON_UNLOCK))).click();
			return;
		} catch (TimeoutException ignored) {
		}
		try {
			new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.name("Unlock"))).click();
		} catch (TimeoutException ignored) {
			try {
				new WebDriverWait(driver, 3).until(ExpectedConditions
						.elementToBeClickable(By.name("\u0420\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u0442\u044c")))
						.click();
			} catch (TimeoutException ignored2) {
			}
		}
	}
}
