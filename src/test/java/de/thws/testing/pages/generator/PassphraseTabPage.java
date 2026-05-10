package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class PassphraseTabPage extends BasePage {

	private By passphraseTabButton = MobileBy.name("Passphrase");
	private By wordCountSpinBox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.spinBoxWordCount");
	private By wordSeparatorBox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.editWordSeparator");
	private By wordCaseComboBox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.wordCaseComboBox");
	private By wordListComboBox = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.comboBoxWordList");
	private By addWordListButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.buttonAddWordList");
	private By deleteWordListButton = MobileBy.AccessibilityId(
			"MainWindow.centralwidget.stackedWidget.pagePasswordGenerator.passwordGeneratorWidget.tabWidget.qt_tabwidget_stackedwidget.dicewareWidget.buttonDeleteWordList");

	public PassphraseTabPage(WindowsDriver driver) {
		super(driver);
	}

	private void clearAndType(By locator, String text) {
		WebElement box = driver.findElement(locator);
		box.click();
		box.sendKeys(Keys.CONTROL + "a");
		box.sendKeys(Keys.BACK_SPACE);
		box.sendKeys(text);
	}

	public void clickPassphraseTab() {
		forceClick(driver.findElement(passphraseTabButton));
	}

	public void setWordCount(String count) {
		clearAndType(wordCountSpinBox, count);
	}

	public void setWordSeparator(String separator) {
		clearAndType(wordSeparatorBox, separator);
	}

	public void selectWordCase(String caseName) {
		driver.findElement(wordCaseComboBox).click();
		sleepMs(300);
		driver.findElement(MobileBy.name(caseName)).click();
	}

	public boolean isAddWordListButtonDisplayed() {
		return !driver.findElements(addWordListButton).isEmpty();
	}

	public boolean isDeleteWordListButtonDisplayed() {
		return !driver.findElements(deleteWordListButton).isEmpty();
	}

	public void selectDifferentWordList() {
		WebElement comboBox = driver.findElement(wordListComboBox);
		comboBox.click();
		sleepMs(300);
		comboBox.sendKeys(Keys.UP);
		comboBox.sendKeys(Keys.ENTER);
	}
}