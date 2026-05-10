package de.thws.testing.pages.generator;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class PasswordTabPage extends BasePage {

	// --- LOCATORS ---
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

	private By passwordTabButton = MobileBy.xpath(
			"//*[@Name='Password' and (@LocalizedControlType='tab item' or @ControlType='ControlType.TabItem')]");

	public PasswordTabPage(WindowsDriver driver) {
		super(driver);
	}

	private void setCheckboxState(By locator, boolean targetState) {
		WebElement cb = driver.findElement(locator);
		if (cb.isSelected() != targetState) {
			forceClick(cb);
		}
	}

	public void enableUppercase() {
		setCheckboxState(uppercaseCheckbox, true);
	}

	public void disableUppercase() {
		setCheckboxState(uppercaseCheckbox, false);
	}

	public void enableLowercase() {
		setCheckboxState(lowercaseCheckbox, true);
	}

	public void disableLowercase() {
		setCheckboxState(lowercaseCheckbox, false);
	}

	public void enableNumbers() {
		setCheckboxState(numbersCheckbox, true);
	}

	public void disableNumbers() {
		setCheckboxState(numbersCheckbox, false);
	}

	public void enableSpecialChars() {
		setCheckboxState(specialCharsCheckbox, true);
	}

	public void disableSpecialChars() {
		setCheckboxState(specialCharsCheckbox, false);
	}

	public void enableExtASCII() {
		setCheckboxState(extASCIICheckbox, true);
	}

	public void disableExtASCII() {
		setCheckboxState(extASCIICheckbox, false);
	}

	public void setPasswordLength(String length) {
		WebElement lengthBox = driver.findElement(lengthSpinBox);
		lengthBox.click();
		lengthBox.sendKeys(Keys.CONTROL + "a");
		lengthBox.sendKeys(Keys.BACK_SPACE);
		lengthBox.sendKeys(length);
	}

	public void ensureAdvancedModeOpen() {
		if (driver.findElements(editExcludedChars).isEmpty()) {
			forceClick(driver.findElement(advancedModeButton));
		}
	}

	public void ensurePasswordTabOpen() {
		if (driver.findElements(lengthSpinBox).isEmpty()) {
			System.out.println("Currently on Passphrase tab. Switching to Password tab...");
			try {
				driver.findElement(passwordTabButton).click();
			} catch (Exception clickEx) {
				List<WebElement> passElements = driver.findElements(MobileBy.name("Password"));
				if (passElements.size() > 1) {
					passElements.get(1).click();
				} else if (!passElements.isEmpty()) {
					passElements.get(0).click();
				}
			}
		} else {
			System.out.println("Already on the Password tab. Skipping click.");
		}
	}
}