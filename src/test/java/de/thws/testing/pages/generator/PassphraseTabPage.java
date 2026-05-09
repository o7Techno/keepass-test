package de.thws.testing.pages.generator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class PassphraseTabPage {

	private WindowsDriver driver;

	// --- Senin Bulduğun Jilet Gibi Locator'lar ---
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

	// --- Action Methods ---

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

	// SMART SELECTION: ComboBox'ı açar ve senin gönderdiğin Name değeriyle seçer
	public void selectWordCase(String caseName) {
		WebElement comboBox = driver.findElement(wordCaseComboBox);
		comboBox.click(); // Menüyü aç

		try {
			Thread.sleep(300); // Menünün animasyonla açılması için kısa bir bekleme
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// "UPPER CASE", "Title Case" vb. seçenekleri isminden bulup tıkla
		driver.findElement(MobileBy.name(caseName)).click();
	}

	public boolean isAddWordListButtonDisplayed() {
		return driver.findElement(addWordListButton).isDisplayed();
	}

	public boolean isDeleteWordListButtonDisplayed() {
		return driver.findElement(deleteWordListButton).isDisplayed();
	}

	// ComboBox'a tıklar ve klavyenin YUKARI ok tuşunu kullanarak listedeki başka
	// bir wordlist'e geçer
	public void selectDifferentWordList() {
		WebElement comboBox = driver.findElement(wordListComboBox);
		comboBox.click();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Listede yukarı çıkıp Enter'a basarak farklı bir liste seçiyoruz
		comboBox.sendKeys(Keys.UP);
		comboBox.sendKeys(Keys.ENTER);
	}

}