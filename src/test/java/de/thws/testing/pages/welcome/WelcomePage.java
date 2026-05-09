package de.thws.testing.pages.welcome;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class WelcomePage {

	private final WindowsDriver driver;

	public WelcomePage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void clickCreateDatabase() {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions
				.elementToBeClickable(MobileBy.AccessibilityId(KeePassAccessibility.WELCOME_CREATE_DATABASE)))
				.click();
	}
}
