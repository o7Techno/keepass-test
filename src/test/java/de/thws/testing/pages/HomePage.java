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

	public void openPasswordGenerator() {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement diceButton = wait.until(ExpectedConditions.elementToBeClickable(diceIcon));
		diceButton.click();
	}
}
