package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class PassphraseTabPage {

	private WindowsDriver driver;

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
		this.driver = driver;
	}

	public void clickPassphraseTab() {
		driver.findElement(passphraseTabButton).click();
	}

	public void setWordCount(String count) {
		WebElement countBox = driver.findElement(wordCountSpinBox);
		countBox.click();
		countBox.sendKeys(Keys.CONTROL + "a");
		countBox.sendKeys(Keys.BACK_SPACE);
		countBox.sendKeys(count);
	}

	public void setWordSeparator(String separator) {
		WebElement sepBox = driver.findElement(wordSeparatorBox);
		sepBox.click();
		sepBox.sendKeys(Keys.CONTROL + "a");
		sepBox.sendKeys(Keys.BACK_SPACE);
		sepBox.sendKeys(separator);
	}

	public void selectWordCase(String caseName) {
		WebElement comboBox = driver.findElement(wordCaseComboBox);
		comboBox.click();

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		driver.findElement(MobileBy.name(caseName)).click();
	}

	public boolean isAddWordListButtonDisplayed() {
		return driver.findElement(addWordListButton).isDisplayed();
	}

	public boolean isDeleteWordListButtonDisplayed() {
		return driver.findElement(deleteWordListButton).isDisplayed();
	}

	public void selectDifferentWordList() {
		WebElement comboBox = driver.findElement(wordListComboBox);
		comboBox.click();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		comboBox.sendKeys(Keys.UP);
		comboBox.sendKeys(Keys.ENTER);
	}

}
