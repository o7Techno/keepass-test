package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class PasswordTabPage {

	private WindowsDriver driver;

	private By uppercaseCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxUpper");
	private By lowercaseCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxLower");
	private By numbersCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxNumbers");
	private By specialCharsCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxSpecialChars");
	private By extASCIICheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxExtASCII");
	private By lengthSpinBox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.spinBoxLength");

	private By advancedModeButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.buttonAdvancedMode");

	private By editExcludedChars = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.editExcludedChars");

	public PasswordTabPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void enableUppercase() {
		WebElement cb = driver.findElement(uppercaseCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableUppercase() {
		WebElement cb = driver.findElement(uppercaseCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableLowercase() {
		WebElement cb = driver.findElement(lowercaseCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableLowercase() {
		WebElement cb = driver.findElement(lowercaseCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableNumbers() {
		WebElement cb = driver.findElement(numbersCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableNumbers() {
		WebElement cb = driver.findElement(numbersCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableSpecialChars() {
		WebElement cb = driver.findElement(specialCharsCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableSpecialChars() {
		WebElement cb = driver.findElement(specialCharsCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableExtASCII() {
		WebElement cb = driver.findElement(extASCIICheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableExtASCII() {
		WebElement cb = driver.findElement(extASCIICheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void setPasswordLength(String length) {
		WebElement lengthBox = driver.findElement(lengthSpinBox);
		lengthBox.click();
		lengthBox.sendKeys(Keys.CONTROL + "a");
		lengthBox.sendKeys(Keys.BACK_SPACE);
		lengthBox.sendKeys(length);
	}

	public void ensureAdvancedModeOpen() {
		try {
			boolean isAdvancedVisible = driver.findElement(editExcludedChars).isDisplayed();
			if (!isAdvancedVisible) {
				driver.findElement(advancedModeButton).click();
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			driver.findElement(advancedModeButton).click();
		}
	}
}
