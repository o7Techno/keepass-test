package de.thws.testing.pages.entry;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.pages.BasePage;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class EntryEditDialogPage extends BasePage {

	// --- LOCATORS ---
	private static final String ENTRY_EDIT_TITLE = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.titleEdit";
	private static final String ENTRY_EDIT_USERNAME = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.usernameComboBox";
	private static final String ENTRY_EDIT_PASSWORD = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.passwordEdit.passwordEdit";
	private static final String ENTRY_BUTTON_PASSWORD_GENERATOR_APPLY = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.passwordEdit.PasswordGeneratorWidget.buttonApply";
	private static final String ENTRY_EDIT_URL = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.urlEdit";
	private static final String ENTRY_BUTTON_URL_TOOLS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.fetchFaviconButton";
	private static final String ENTRY_EDIT_TAGS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.tagsList.qt_scrollarea_viewport";
	private static final String ENTRY_CHECK_EXPIRES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expireCheck";
	private static final String ENTRY_DATE_TIME_EXPIRES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expireDatePicker";
	private static final String ENTRY_BUTTON_EXPIRY_PRESETS = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.expirePresets";
	private static final String ENTRY_EDIT_NOTES = "editEntryWidget.stackedWidget.EditEntryWidgetMain.qt_scrollarea_viewport.container.notesEdit";

	private static final long IMPLICIT_RESTORE_SEC = 5;

	public EntryEditDialogPage(WindowsDriver driver) {
		super(driver);
	}

	public void clickEntryTab() {
		clickFirstMatchingName(20, "\u0417\u0430\u043f\u0438\u0441\u044c", "Entry");
	}

	public void setTitle(String title) {
		setTextField(ENTRY_EDIT_TITLE, title);
	}

	public String getTitle() {
		return readTextValue(waitClickable(ENTRY_EDIT_TITLE));
	}

	public void setUsername(String username) {
		setTextField(ENTRY_EDIT_USERNAME, username);
	}

	public String getUsername() {
		return readTextValue(waitClickable(ENTRY_EDIT_USERNAME));
	}

	public void openUsernameMenu() {
		WebElement combo = waitClickable(ENTRY_EDIT_USERNAME);
		combo.click();
		new Actions(driver).keyDown(Keys.ALT).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.ALT).perform();
	}

	public void setPassword(String password) {
		setTextField(ENTRY_EDIT_PASSWORD, password);
	}

	public String getPassword() {
		return readTextValue(waitClickable(ENTRY_EDIT_PASSWORD));
	}

	public void clickPasswordGenerator() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			String pwdId = ENTRY_EDIT_PASSWORD;
			String gen = "[@ClassName='QLineEditIconButton' and (contains(@Name,'Generate Password') or contains(@Name,'Ctrl+G'))]";
			String[] scopedXpaths = { "//*[@AutomationId='" + pwdId + "']/ancestor::Window[1]//*" + gen,
					"//*[@AutomationId='" + pwdId + "']/ancestor::Pane[1]//*" + gen,
					"//*[@AutomationId='" + pwdId + "']/ancestor::*[contains(@Name,'KeePass')][1]//*" + gen,
					"//*[@AutomationId='" + pwdId + "']/ancestor::Group[@Name='Password field'][1]//*" + gen, };

			for (String xp : scopedXpaths) {
				List<WebElement> hits = driver.findElements(By.xpath(xp));
				if (!hits.isEmpty()) {
					forceClick(hits.get(0));
					return;
				}
			}

			List<WebElement> global = driver.findElements(By.xpath(
					"//*[@ClassName='QLineEditIconButton' and (contains(@Name,'Generate Password') or contains(@Name,'Ctrl+G'))]"));
			if (!global.isEmpty()) {
				forceClick(global.get(0));
				return;
			}

			clickFirstMatchingName(20, "Generate Password (Ctrl+G)", "Generate Password", "Password Generator",
					"\u0421\u0433\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c",
					"\u0413\u0435\u043d\u0435\u0440\u0430\u0442\u043e\u0440 \u043f\u0430\u0440\u043e\u043b\u0435\u0439");
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	public void clickPasswordToggleVisibility() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			String pwdId = ENTRY_EDIT_PASSWORD;
			String toggle = "[( @ClassName='QLineEditIconButton' or local-name()='CheckBox' ) and contains(@Name,'Toggle')]";
			String[] scopedXpaths = { "//*[@AutomationId='" + pwdId + "']/ancestor::Window[1]//*" + toggle,
					"//*[@AutomationId='" + pwdId + "']/ancestor::Pane[1]//*" + toggle,
					"//*[@AutomationId='" + pwdId + "']/ancestor::*[contains(@Name,'KeePass')][1]//*" + toggle, };

			for (String xp : scopedXpaths) {
				List<WebElement> hits = driver.findElements(By.xpath(xp));
				if (!hits.isEmpty()) {
					forceClick(hits.get(0));
					return;
				}
			}

			List<WebElement> global = driver.findElements(By.xpath(
					"//*[( @ClassName='QLineEditIconButton' or local-name()='CheckBox' ) and (contains(@Name,'Toggle Password') or contains(@Name,'Toggle password') or contains(@Name,'password visibility'))]"));
			if (!global.isEmpty()) {
				forceClick(global.get(0));
				return;
			}

			clickFirstMatchingName(20, "Toggle Password (Ctrl+H)", "Toggle password visibility", "Show password",
					"Hide password",
					"\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c",
					"\u0421\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c");
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	public void clickPasswordGeneratorApply() {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions
				.elementToBeClickable(MobileBy.AccessibilityId(ENTRY_BUTTON_PASSWORD_GENERATOR_APPLY))).click();
	}

	public boolean tryClickPasswordGeneratorApply(int waitSeconds) {
		try {
			new WebDriverWait(driver, waitSeconds)
					.until(ExpectedConditions
							.elementToBeClickable(MobileBy.AccessibilityId(ENTRY_BUTTON_PASSWORD_GENERATOR_APPLY)))
					.click();
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void setUrl(String url) {
		setTextField(ENTRY_EDIT_URL, url);
	}

	public String getUrl() {
		return readTextValue(waitClickable(ENTRY_EDIT_URL));
	}

	public void clickUrlToolsButton() {
		waitClickable(ENTRY_BUTTON_URL_TOOLS).click();
	}

	public void setTags(String tags) {
		setTextField(ENTRY_EDIT_TAGS, tags);
	}

	public String getTags() {
		WebElement root = waitClickable(ENTRY_EDIT_TAGS);
		String plain = collectTagsText(root);
		if (!plain.isBlank()) {
			return plain.trim();
		}

		String afterClick = readTagsAfterSingleChipClick(root);
		return afterClick.isBlank() ? "" : afterClick.trim();
	}

	public void clickTitleField() {
		waitClickable(ENTRY_EDIT_TITLE).click();
	}

	private String collectTagsText(WebElement root) {
		String direct = readTextValue(root);
		if (direct != null && !direct.isBlank()) {
			return direct.trim();
		}

		try {
			String whole = root.getText();
			if (whole != null && !whole.isBlank()) {
				return whole.trim();
			}
		} catch (RuntimeException ignored) {
		}

		String fromList = aggregateTagListItemNames(root);
		return !fromList.isBlank() ? fromList : "";
	}

	private String readTagsAfterSingleChipClick(WebElement root) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			clickLeftChipArea(root, false);
			sleepMs(280); // DRY: BasePage'den geldi
			String s = tryReadTagsAfterChipActivation(root);
			if (!s.isBlank()) {
				return s;
			}

			clickLeftChipArea(refreshTagsRootOr(root), true);
			sleepMs(220); // DRY: BasePage'den geldi
			return tryReadTagsAfterChipActivation(refreshTagsRootOr(root));
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private WebElement refreshTagsRootOr(WebElement fallback) {
		try {
			List<WebElement> found = driver.findElements(MobileBy.AccessibilityId(ENTRY_EDIT_TAGS));
			if (!found.isEmpty()) {
				return found.get(0);
			}
		} catch (RuntimeException ignored) {
		}
		return fallback;
	}

	private String tryReadTagsAfterChipActivation(WebElement root) {
		String s = collectTagsText(root);
		if (!s.isBlank()) {
			return s;
		}
		s = readTagsFromDescendantEdits(root);
		if (!s.isBlank()) {
			return s;
		}
		return readTagsLineEditsGlobal();
	}

	private void clickLeftChipArea(WebElement root, boolean doubleClick) {
		Dimension d = root.getSize();
		int x = Math.min(40, Math.max(8, d.getWidth() / 5));
		int y = Math.max(2, d.getHeight() / 2);
		Actions a = new Actions(driver).moveToElement(root, x, y);
		if (doubleClick) {
			a.doubleClick().perform();
		} else {
			a.click().perform();
		}
	}

	private String readTagsFromDescendantEdits(WebElement root) {
		String xpath = ".//*[@ClassName='QLineEdit' or @ClassName='QPlainTextEdit' or local-name()='Edit']";
		for (WebElement ed : root.findElements(By.xpath(xpath))) {
			String v = readTextValue(ed);
			if (v != null && !v.isBlank()) {
				return v.trim();
			}
			try {
				String t = ed.getText();
				if (t != null && !t.isBlank()) {
					return t.trim();
				}
			} catch (RuntimeException ignored) {
			}
		}
		return "";
	}

	private String readTagsLineEditsGlobal() {
		String[] xpaths = {
				"//*[contains(@AutomationId,'tagsList')]//*[@ClassName='QLineEdit' or @ClassName='QPlainTextEdit']",
				"//*[contains(@AutomationId,'tagsList')]//*[local-name()='Edit']",
				"//*[contains(@AutomationId,'tag') and contains(@AutomationId,'Edit')]",
				"//*[contains(@AutomationId,'editEntryWidget')]//*[contains(@AutomationId,'tag') or contains(@AutomationId,'Tag')]//*[@ClassName='QLineEdit' or @ClassName='QPlainTextEdit']",
				"//*[@ClassName='Group' and (contains(@Name,'Tag') or contains(@Name,'tag'))]//*[@ClassName='QLineEdit' or @ClassName='QPlainTextEdit']", };
		for (String xp : xpaths) {
			List<WebElement> elements = driver.findElements(By.xpath(xp));
			for (WebElement ed : elements) {
				String v = readTextValue(ed);
				if (v != null && !v.isBlank()) {
					return v.trim();
				}
				try {
					String t = ed.getText();
					if (t != null && !t.isBlank()) {
						return t.trim();
					}
				} catch (RuntimeException ignored) {
				}
			}
		}
		return "";
	}

	private static String aggregateTagListItemNames(WebElement root) {
		Set<String> parts = new LinkedHashSet<>();
		for (WebElement el : root.findElements(By.xpath(".//ListItem | .//ListBoxItem | .//DataItem"))) {
			String n = el.getAttribute("Name");
			if (n == null || n.isBlank()) {
				n = el.getAttribute("LegacyIAccessible.Name");
			}
			if (n != null && !n.isBlank()) {
				parts.add(n.trim());
			}
		}
		return String.join(", ", parts);
	}

	public void setExpires(boolean enabled) {
		WebElement cb = waitClickable(ENTRY_CHECK_EXPIRES);
		boolean on = cb.isSelected();
		if (enabled && !on) {
			cb.click();
		} else if (!enabled && on) {
			cb.click();
		}
	}

	public boolean isExpiresChecked() {
		return waitClickable(ENTRY_CHECK_EXPIRES).isSelected();
	}

	public void setExpiryDateTimeText(String dateTimeText) {
		setTextField(ENTRY_DATE_TIME_EXPIRES, dateTimeText);
	}

	public String getExpiryDateTimeText() {
		return readTextValue(waitClickable(ENTRY_DATE_TIME_EXPIRES));
	}

	public void clickExpiryPresets() {
		waitClickable(ENTRY_BUTTON_EXPIRY_PRESETS).click();
	}

	public void selectMenuItemByName(String name) {
		new WebDriverWait(driver, 8).until(ExpectedConditions.elementToBeClickable(MobileBy.name(name))).click();
	}

	public void setNotes(String notes) {
		setTextField(ENTRY_EDIT_NOTES, notes);
	}

	public String getNotes() {
		return readTextValue(waitClickable(ENTRY_EDIT_NOTES));
	}

	public void clickOk() {
		clickQPushButton(findOkButton(), "OK");
	}

	public void clickCancel() {
		clickQPushButton(findCancelButton(), "Cancel");
	}

	public void dismissOpenMenuWithEscape() {
		new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	}

	private void clickQPushButton(WebElement btn, String debugLabel) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			if (btn == null) {
				throw new TimeoutException(
						"QPushButton not found for " + debugLabel + " (scope " + ENTRY_EDIT_TITLE + ").");
			}
			forceClick(btn);
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private String[] entryEditorScopes() {
		String anchor = ENTRY_EDIT_TITLE;
		return new String[] { "//*[@AutomationId='" + anchor + "']/ancestor::*[contains(@Name,'KeePass')][1]",
				"//*[@AutomationId='" + anchor + "']/ancestor::Window[1]",
				"//*[@AutomationId='" + anchor + "']/ancestor::Pane[1]", "//Window[contains(@Name,'KeePass')]", };
	}

	private WebElement findOkButton() {
		String ruCancel = "\u041e\u0442\u043c\u0435\u043d\u0430";
		String ruOk = "\u041e\u041a";
		for (String scope : entryEditorScopes()) {
			List<WebElement> bySibling = driver
					.findElements(By.xpath(scope + "//*[@ClassName='QPushButton' and (@Name='Cancel' or @Name='"
							+ ruCancel + "')]/preceding-sibling::*[@ClassName='QPushButton' and (@Name='OK' or @Name='"
							+ ruOk + "')][1]"));
			WebElement hit = firstOrNull(bySibling);
			if (hit != null) {
				return hit;
			}
		}
		for (String scope : entryEditorScopes()) {
			for (String label : new String[] { "OK", ruOk }) {
				WebElement found = pickFirstQPushButtonOrButton(scope, label);
				if (found != null) {
					return found;
				}
			}
		}
		for (String scope : entryEditorScopes()) {
			WebElement geom = findQPushButtonWithLabelLowestOnScreen(scope, "OK", ruOk);
			if (geom != null) {
				return geom;
			}
		}
		return null;
	}

	private WebElement findCancelButton() {
		String ruCancel = "\u041e\u0442\u043c\u0435\u043d\u0430";
		String ruOk = "\u041e\u041a";
		for (String scope : entryEditorScopes()) {
			List<WebElement> bySibling = driver
					.findElements(By.xpath(scope + "//*[@ClassName='QPushButton' and (@Name='OK' or @Name='" + ruOk
							+ "')]/following-sibling::*[@ClassName='QPushButton' and (@Name='Cancel' or @Name='"
							+ ruCancel + "')][1]"));
			WebElement hit = firstOrNull(bySibling);
			if (hit != null) {
				return hit;
			}
		}
		for (String scope : entryEditorScopes()) {
			for (String label : new String[] { "Cancel", ruCancel }) {
				WebElement found = pickFirstQPushButtonOrButton(scope, label);
				if (found != null) {
					return found;
				}
			}
		}
		for (String scope : entryEditorScopes()) {
			WebElement geom = findQPushButtonWithLabelLowestOnScreen(scope, "Cancel", ruCancel);
			if (geom != null) {
				return geom;
			}
		}
		return null;
	}

	private WebElement pickFirstQPushButtonOrButton(String scopeXpath, String label) {
		List<WebElement> qp = driver
				.findElements(By.xpath(scopeXpath + "//*[@ClassName='QPushButton' and @Name='" + label + "']"));
		WebElement first = firstOrNull(qp);
		if (first != null) {
			return first;
		}

		List<WebElement> bt = driver.findElements(By.xpath(scopeXpath + "//Button[@Name='" + label + "']"));
		return firstOrNull(bt);
	}

	private WebElement findQPushButtonWithLabelLowestOnScreen(String scopeXpath, String... labels) {
		List<WebElement> qp = driver.findElements(By.xpath(scopeXpath + "//*[@ClassName='QPushButton']"));
		WebElement best = null;
		int bestBottom = Integer.MIN_VALUE;

		for (WebElement el : qp) {
			String n = buttonLabel(el);
			if (n == null) {
				continue;
			}

			boolean match = false;
			for (String l : labels) {
				if (l != null && l.equals(n)) {
					match = true;
					break;
				}
			}
			if (!match) {
				continue;
			}

			int bottom = el.getLocation().getY() + el.getSize().getHeight();
			if (bottom >= bestBottom) {
				bestBottom = bottom;
				best = el;
			}
		}
		return best;
	}

	private String buttonLabel(WebElement el) {
		String n = el.getAttribute("Name");
		if (n != null && !n.isEmpty()) {
			return n.trim();
		}
		n = el.getAttribute("LegacyIAccessible.Name");
		return n == null ? null : n.trim();
	}

	private void clickFirstMatchingName(int secondsTotal, String... names) {
		int perName = Math.max(3, secondsTotal / Math.max(1, names.length));
		for (String n : names) {
			try {
				new WebDriverWait(driver, perName).until(ExpectedConditions.elementToBeClickable(MobileBy.name(n)))
						.click();
				return;
			} catch (TimeoutException ignored) {
			}
		}
		throw new TimeoutException("No element matched by Name: " + String.join(", ", names));
	}

	private void setTextField(String automationId, String value) {
		WebElement field = waitClickable(automationId);
		field.click();
		sleepMs(40);
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		field.sendKeys(Keys.BACK_SPACE);
		pasteViaClipboard(field, value);
	}

	private void pasteViaClipboard(WebElement field, String value) {
		try {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
			sleepMs(60);
			field.sendKeys(Keys.chord(Keys.CONTROL, "v"));
		} catch (HeadlessException | IllegalStateException e) {
			field.sendKeys(value);
		}
	}

	private String readTextValue(WebElement field) {
		String[] valueAttrs = { "Value.Value", "Value", "LegacyIAccessible.Value", "RangeValue.Value" };
		for (String attr : valueAttrs) {
			try {
				String value = field.getAttribute(attr);
				if (value != null && !value.isEmpty()) {
					return value;
				}
			} catch (RuntimeException ignored) {
			}
		}
		try {
			String text = field.getText();
			if (text != null && !text.isBlank()) {
				return text;
			}
		} catch (RuntimeException ignored) {
		}
		return "";
	}

	private WebElement waitClickable(String automationId) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		return wait.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(automationId)));
	}
}