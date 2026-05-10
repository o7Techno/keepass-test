package de.thws.testing.pages.wizard;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class NewDatabaseWizardPage extends BasePage {

	public static final String WIZARD_EDIT_DATABASE_NAME = "NewDatabaseWizardPage.pageContent.qt_scrollarea_viewport.DatabaseSettingsWidgetMetaDataSimple.databaseName";
	public static final String WIZARD_BUTTON_CONTINUE = "__qt__passive_wizardbutton1";
	public static final String WIZARD_BUTTON_CANCEL = "qt_wizard_cancel";
	public static final String WIZARD_EDIT_MASTER_PASSWORD = "KeyComponentWidget.groupBox.stackedWidget.editPage.componentWidgetContainer.PasswordEditWidget.enterPasswordEdit.passwordEdit";
	public static final String WIZARD_EDIT_MASTER_PASSWORD_CONFIRM = "KeyComponentWidget.groupBox.stackedWidget.editPage.componentWidgetContainer.PasswordEditWidget.repeatPasswordEdit.passwordEdit";
	public static final String WIZARD_BUTTON_DONE = "qt_wizard_finish";

	public NewDatabaseWizardPage(WindowsDriver driver) {
		super(driver);
	}

	private void clearAndType(String automationId, String text) {
		WebElement field = waitClickable(automationId);
		field.click();
		field.sendKeys(Keys.CONTROL + "a");
		field.sendKeys(Keys.BACK_SPACE);
		field.sendKeys(text);
	}

	public void setDatabaseDisplayName(String name) {
		clearAndType(WIZARD_EDIT_DATABASE_NAME, name);
	}

	public void clickContinue() {
		forceClick(waitClickable(WIZARD_BUTTON_CONTINUE));
	}

	public void clickCancel() {
		forceClick(waitClickable(WIZARD_BUTTON_CANCEL));
	}

	public void setMasterPassword(String password) {
		clearAndType(WIZARD_EDIT_MASTER_PASSWORD, password);
	}

	public void setMasterPasswordConfirm(String password) {
		clearAndType(WIZARD_EDIT_MASTER_PASSWORD_CONFIRM, password);
	}

	public void clickDone() {
		forceClick(waitClickable(WIZARD_BUTTON_DONE));
	}

	public void dismissWeakMasterPasswordWarningIfPresent() {
		String[] buttonNames = {
				"\u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c \u0441\u043e \u0441\u043b\u0430\u0431\u044b\u043c \u043f\u0430\u0440\u043e\u043b\u0435\u043c",
				"Continue with weak password" };

		long deadline = System.currentTimeMillis() + 8000;
		while (System.currentTimeMillis() < deadline) {
			for (String n : buttonNames) {
				try {
					WebElement el = driver.findElement(MobileBy.name(n));
					if (el.isDisplayed() && el.isEnabled()) {
						forceClick(el);
						return;
					}
				} catch (Exception ignored) {
				}
			}
			sleepMs(200);
		}
	}

	private WebElement waitClickable(String automationId) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		return wait.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(automationId)));
	}
}