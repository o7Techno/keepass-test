package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class AdvancedPanel extends BasePage {

	// --- LOCATORS ---
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
		super(driver);
	}

	private void setCheckboxState(By locator, boolean targetState) {
		WebElement cb = driver.findElement(locator);
		if (cb.isSelected() != targetState) {
			forceClick(cb); // BasePage'den güvenli tıklama
		}
	}

	private void clearAndType(By locator, String text) {
		WebElement box = driver.findElement(locator);
		box.click();
		box.sendKeys(Keys.CONTROL + "a");
		box.sendKeys(Keys.BACK_SPACE);
		box.sendKeys(text);
	}

	// --- ACTION METHODS ---
	public void setExcludedCharacters(String chars) {
		clearAndType(editExcludedChars, chars);
	}

	public void setAdditionalCharacters(String chars) {
		clearAndType(editAdditionalChars, chars);
	}

	public void enableExcludeLookAlike() {
		setCheckboxState(excludeLookAlikeCheckbox, true);
	}

	public void disableExcludeLookAlike() {
		setCheckboxState(excludeLookAlikeCheckbox, false);
	}

	public void enableEnsureEveryGroup() {
		setCheckboxState(ensureEveryCheckbox, true);
	}

	public void disableEnsureEveryGroup() {
		setCheckboxState(ensureEveryCheckbox, false);
	}

	public void enablePunctuation() {
		setCheckboxState(punctuationCheckbox, true);
	}

	public void disablePunctuation() {
		setCheckboxState(punctuationCheckbox, false);
	}

	public void enableQuotes() {
		setCheckboxState(quotesCheckbox, true);
	}

	public void disableQuotes() {
		setCheckboxState(quotesCheckbox, false);
	}

	public void enableDashes() {
		setCheckboxState(dashesCheckbox, true);
	}

	public void disableDashes() {
		setCheckboxState(dashesCheckbox, false);
	}

	public void enableMath() {
		setCheckboxState(mathCheckbox, true);
	}

	public void disableMath() {
		setCheckboxState(mathCheckbox, false);
	}

	public void enableBraces() {
		setCheckboxState(bracesCheckbox, true);
	}

	public void disableBraces() {
		setCheckboxState(bracesCheckbox, false);
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

	public void clickHexButton() {
		forceClick(driver.findElement(hexButton));
	}
}