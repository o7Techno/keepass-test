package de.thws.testing.pages.generator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import org.openqa.selenium.By;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class GeneratorCommonPage {

	private WindowsDriver driver;

	// Locators for common elements across all generator tabs
	private By generateButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.buttonGenerate");
	private By passwordField = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.editNewPassword.passwordEdit");
	private By copyButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.buttonCopy");
	private By closeButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.buttonClose");

	public GeneratorCommonPage(WindowsDriver driver) {
		this.driver = driver;
	}

	// Clicks the refresh/generate button
	public void clickGenerateButton() {
		driver.findElement(generateButton).click();
	}

	public void clickCopyButton() {
		driver.findElement(copyButton).click();
	}

	public void clickCloseButton() {
		driver.findElement(closeButton).click();
	}

	// Reads and returns the currently generated password
	public String getGeneratedPassword() {
		return driver.findElement(passwordField).getText();
	}

	public String getClipboardText() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			throw new RuntimeException("ERROR: Could not read from system clipboard! " + e.getMessage());
		}
	}

	public boolean isGeneratorWindowVisible() {
		try {
			return driver.findElement(generateButton).isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
			return false;
		}
	}
}