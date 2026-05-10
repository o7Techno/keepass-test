package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class AdvancedPanel {

	private WindowsDriver driver;

	private By punctuationCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxPunctuation");
	private By quotesCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxQuotes");
	private By dashesCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxDashes");
	private By mathCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxMath");
	private By bracesCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.checkBoxBraces");

	private By editAdditionalChars = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.editAdditionalChars");
	private By editExcludedChars = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.editExcludedChars");
	private By hexButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.buttonAddHex");

	private By excludeLookAlikeCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.checkBoxExcludeAlike");
	private By ensureEveryCheckbox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.passwordWidget.groupBox.advancedContainer.checkBoxEnsureEvery");

	public AdvancedPanel(WindowsDriver driver) {
		this.driver = driver;
	}

	public void setExcludedCharacters(String chars) {
		WebElement excludeBox = driver.findElement(editExcludedChars);
		excludeBox.click();
		excludeBox.sendKeys(Keys.CONTROL + "a");
		excludeBox.sendKeys(Keys.BACK_SPACE);
		excludeBox.sendKeys(chars);
	}

	public void enableExcludeLookAlike() {
		WebElement cb = driver.findElement(excludeLookAlikeCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableExcludeLookAlike() {
		WebElement cb = driver.findElement(excludeLookAlikeCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableEnsureEveryGroup() {
		WebElement cb = driver.findElement(ensureEveryCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableEnsureEveryGroup() {
		WebElement cb = driver.findElement(ensureEveryCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enablePunctuation() {
		WebElement cb = driver.findElement(punctuationCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disablePunctuation() {
		WebElement cb = driver.findElement(punctuationCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableQuotes() {
		WebElement cb = driver.findElement(quotesCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableQuotes() {
		WebElement cb = driver.findElement(quotesCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableDashes() {
		WebElement cb = driver.findElement(dashesCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableDashes() {
		WebElement cb = driver.findElement(dashesCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableMath() {
		WebElement cb = driver.findElement(mathCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableMath() {
		WebElement cb = driver.findElement(mathCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void enableBraces() {
		WebElement cb = driver.findElement(bracesCheckbox);
		if (!cb.isSelected()) {
			cb.click();
		}
	}

	public void disableBraces() {
		WebElement cb = driver.findElement(bracesCheckbox);
		if (cb.isSelected()) {
			cb.click();
		}
	}

	public void disableAllSpecialCharacters() {
		disablePunctuation();
		disableQuotes();
		disableDashes();
		disableMath();
		disableBraces();
	}

	public void enableAllSpecialCharacters() {
		enablePunctuation();
		enableQuotes();
		enableDashes();
		enableMath();
		enableBraces();
	}

	public void setAdditionalCharacters(String chars) {
		WebElement addBox = driver.findElement(editAdditionalChars);
		addBox.click();
		addBox.sendKeys(Keys.CONTROL + "a");
		addBox.sendKeys(Keys.BACK_SPACE);
		addBox.sendKeys(chars);
	}

	public void clickHexButton() {
		driver.findElement(hexButton).click();
	}
}
