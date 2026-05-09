package de.thws.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.windows.WindowsDriver;

public class HomePage {

	private WindowsDriver driver;
	private By diceIcon = By.name("Password Generator");

	public HomePage(WindowsDriver driver) {
		this.driver = driver;
	}

	// Opens the password generator with Smart Wait (Explicit Wait)
	public void openPasswordGenerator() {
		// Wait up to 5 seconds for the dice icon to become clickable (solves app
		// loading/login screen delays)
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement diceButton = wait.until(ExpectedConditions.elementToBeClickable(diceIcon));
		diceButton.click();
	}
}