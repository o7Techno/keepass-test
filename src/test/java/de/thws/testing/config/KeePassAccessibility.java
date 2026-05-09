package de.thws.testing.config;

/**
 * Только элементы, у которых в Inspect есть AutomationId. Кнопка «+», вкладка
 * «Запись», OK/Отмена, кубик/глаз у пароля и меню логина здесь не перечислены —
 * для них в page objects используется поиск по {@code Name} (и Alt+↓ для
 * выпадающего списка логина).
 */
public final class KeePassAccessibility {

	private KeePassAccessibility() {
	}

	public static final String WELCOME_CREATE_DATABASE = "MainWindow.centralwidget.stackedWidget.pageWelcome.welcomeWidget.buttonNewDatabase";
	public static final String WIZARD_EDIT_DATABASE_NAME = "NewDatabaseWizardPage.pageContent.qt_scrollarea_viewport.DatabaseSettingsWidgetMetaDataSimple.databaseName";
	public static final String WIZARD_BUTTON_CONTINUE = "__qt__passive_wizardbutton1";
	public static final String WIZARD_BUTTON_CANCEL = "qt_wizard_cancel";
	public static final String WIZARD_EDIT_MASTER_PASSWORD = "KeyComponentWidget.groupBox.stackedWidget.editPage.componentWidgetContainer.PasswordEditWidget.enterPasswordEdit.passwordEdit";
	public static final String WIZARD_EDIT_MASTER_PASSWORD_CONFIRM = "KeyComponentWidget.groupBox.stackedWidget.editPage.componentWidgetContainer.PasswordEditWidget.repeatPasswordEdit.passwordEdit";
	public static final String WIZARD_BUTTON_DONE = "qt_wizard_finish";

	/**
	 * Экран «Unlock KeePassXC Database» при открытии существующей БД. Подставь свои
	 * AutomationId из Inspect.
	 */
	public static final String UNLOCK_DATABASE_PASSWORD_EDIT = "databaseOpenWidget.formContainer.centralStack.mainPage.enterPasswordComponent.editPassword.passwordEdit";
	/** Не обязательно: после пароля обычно достаточно {@link org.openqa.selenium.Keys#ENTER} в поле пароля. */
	public static final String UNLOCK_DATABASE_BUTTON_UNLOCK = "UnlockDatabaseWidget.buttonUnlock";

	/** Список записей: родитель строк (ListItem / DataItem). */
	public static final String MAIN_ENTRY_VIEW = "entryView";

	/** Главная панель инструментов; кнопка «новая запись» без AutomationId — ищется по Name внутри тулбара. */
	public static final String MAIN_TOOL_BAR = "MainWindow.toolBar";

	public static final String ENTRY_EDIT_TITLE = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.titleEdit";
	public static final String ENTRY_EDIT_USERNAME = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.usernameComboBox";

	public static final String ENTRY_EDIT_PASSWORD = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.passwordEdit.passwordEdit";

	public static final String ENTRY_EDIT_URL = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.urlEdit";
	public static final String ENTRY_BUTTON_URL_TOOLS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.fetchFaviconButton";

	public static final String ENTRY_EDIT_TAGS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.tagsList.qt_scrollarea_viewport";

	public static final String ENTRY_CHECK_EXPIRES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expireCheck";
	public static final String ENTRY_DATE_TIME_EXPIRES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expireDatePicker";
	public static final String ENTRY_BUTTON_EXPIRY_PRESETS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expirePresets";

	public static final String ENTRY_EDIT_NOTES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.notesEdit";
}
