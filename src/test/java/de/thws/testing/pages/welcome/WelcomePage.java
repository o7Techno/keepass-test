package de.thws.testing.pages.welcome;

import org.openqa.selenium.By;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class WelcomePage extends BasePage {

	private By createDatabaseButton = MobileBy
			.AccessibilityId("MainWindow.centralwidget.stackedWidget.pageWelcome.welcomeWidget.buttonNewDatabase");

	public WelcomePage(WindowsDriver driver) {
		super(driver);
	}

	public void clickCreateDatabase() {

		forceClick(driver.findElement(createDatabaseButton));
	}
}