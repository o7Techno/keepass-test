package de.thws.testing.pages.entry;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

/**
 * «Запись» / Entry tab. Поля — по AutomationId; кнопки и вкладка без id — по
 * Name в дереве доступности.
 * <p>
 * Кнопки OK/Cancel — {@code QPushButton} без AutomationId: ищем по {@code Name}
 * внутри окна формы (привязка к полю заголовка), иначе глобально под окном
 * KeePass; клик — как у тулбара ({@code Actions} / {@code click} / пробел).
 */
public class EntryEditDialogPage {

	private static final long IMPLICIT_RESTORE_SEC = 5;

	private final WindowsDriver driver;

	public EntryEditDialogPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void clickEntryTab() {
		clickFirstMatchingName(20, "Запись", "Entry");
	}

	public void setTitle(String title) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_TITLE, title);
	}

	public String getTitle() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_TITLE));
	}

	public void setUsername(String username) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_USERNAME, username);
	}

	public String getUsername() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_USERNAME));
	}

	public void openUsernameMenu() {
		WebElement combo = waitClickable(KeePassAccessibility.ENTRY_EDIT_USERNAME);
		combo.click();
		new Actions(driver).keyDown(Keys.ALT).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.ALT).perform();
	}

	public void setPassword(String password) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_PASSWORD, password);
	}

	public String getPassword() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_PASSWORD));
	}

	public void clickPasswordGenerator() {
		// RU strings below are written as Unicode escapes so Cyrillic is not tied to source file encoding.
		clickFirstMatchingName(15, "Generate Password", "Password Generator", "\u0421\u0433\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c",
				"\u0413\u0435\u043d\u0435\u0440\u0430\u0442\u043e\u0440 \u043f\u0430\u0440\u043e\u043b\u0435\u0439");
	}

	public void clickPasswordToggleVisibility() {
		clickFirstMatchingName(15, "Toggle password visibility", "Show password", "Hide password",
				"\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c",
				"\u0421\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u0440\u043e\u043b\u044c");
	}

	public void setUrl(String url) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_URL, url);
	}

	public String getUrl() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_URL));
	}

	public void clickUrlToolsButton() {
		waitClickable(KeePassAccessibility.ENTRY_BUTTON_URL_TOOLS).click();
	}

	public void setTags(String tags) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_TAGS, tags);
	}

	public String getTags() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_TAGS));
	}

	public void setExpires(boolean enabled) {
		WebElement cb = waitClickable(KeePassAccessibility.ENTRY_CHECK_EXPIRES);
		boolean on = cb.isSelected();
		if (enabled && !on) {
			cb.click();
		} else if (!enabled && on) {
			cb.click();
		}
	}

	public boolean isExpiresChecked() {
		return waitClickable(KeePassAccessibility.ENTRY_CHECK_EXPIRES).isSelected();
	}

	public void setExpiryDateTimeText(String dateTimeText) {
		setTextField(KeePassAccessibility.ENTRY_DATE_TIME_EXPIRES, dateTimeText);
	}

	public String getExpiryDateTimeText() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_DATE_TIME_EXPIRES));
	}

	public void clickExpiryPresets() {
		waitClickable(KeePassAccessibility.ENTRY_BUTTON_EXPIRY_PRESETS).click();
	}

	public void selectMenuItemByName(String name) {
		WebDriverWait wait = new WebDriverWait(driver, 8);
		wait.until(ExpectedConditions.elementToBeClickable(MobileBy.name(name))).click();
	}

	public void setNotes(String notes) {
		setTextField(KeePassAccessibility.ENTRY_EDIT_NOTES, notes);
	}

	public String getNotes() {
		return readTextValue(waitClickable(KeePassAccessibility.ENTRY_EDIT_NOTES));
	}

	public void clickOk() {
		clickQPushButton(findOkButton(), "OK / ОК");
	}

	public void clickCancel() {
		clickQPushButton(findCancelButton(), "Cancel / Отмена");
	}

	public void dismissOpenMenuWithEscape() {
		new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	}

	private void clickQPushButton(WebElement btn, String debugLabel) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			if (btn == null) {
				throw new TimeoutException("QPushButton «" + debugLabel + "» не найдена (область формы по "
						+ KeePassAccessibility.ENTRY_EDIT_TITLE + ").");
			}
			invokeQtPushButton(btn);
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private String[] entryEditorScopes() {
		String anchor = KeePassAccessibility.ENTRY_EDIT_TITLE;
		return new String[] { "//*[@AutomationId='" + anchor + "']/ancestor::*[contains(@Name,'KeePass')][1]",
				"//*[@AutomationId='" + anchor + "']/ancestor::Window[1]",
				"//*[@AutomationId='" + anchor + "']/ancestor::Pane[1]",
				"//Window[contains(@Name,'KeePass')]", };
	}

	/**
	 * В Insights OK стоит слева от Cancel как сосед — это самый стабильный якорь.
	 * Дальше — по имени; «последний OK в окне» часто оказывается не той кнопкой,
	 * поэтому берём первое совпадение в области формы или нижнюю по Y среди OK.
	 */
	private WebElement findOkButton() {
		String[] scopes = entryEditorScopes();
		for (String scope : scopes) {
			List<WebElement> bySibling = driver.findElements(By.xpath(scope
					+ "//*[@ClassName='QPushButton' and (@Name='Cancel' or @Name='Отмена')]/preceding-sibling::*[@ClassName='QPushButton' and (@Name='OK' or @Name='ОК')][1]"));
			WebElement hit = firstOrNull(bySibling);
			if (hit != null) {
				return hit;
			}
		}
		for (String scope : scopes) {
			for (String label : new String[] { "OK", "ОК" }) {
				WebElement found = pickFirstQPushButtonOrButton(scope, label);
				if (found != null) {
					return found;
				}
			}
		}
		for (String scope : scopes) {
			WebElement geom = findQPushButtonWithLabelLowestOnScreen(scope, "OK", "ОК");
			if (geom != null) {
				return geom;
			}
		}
		return null;
	}

	private WebElement findCancelButton() {
		String[] scopes = entryEditorScopes();
		for (String scope : scopes) {
			List<WebElement> bySibling = driver.findElements(By.xpath(scope
					+ "//*[@ClassName='QPushButton' and (@Name='OK' or @Name='ОК')]/following-sibling::*[@ClassName='QPushButton' and (@Name='Cancel' or @Name='Отмена')][1]"));
			WebElement hit = firstOrNull(bySibling);
			if (hit != null) {
				return hit;
			}
		}
		for (String scope : scopes) {
			for (String label : new String[] { "Cancel", "Отмена" }) {
				WebElement found = pickFirstQPushButtonOrButton(scope, label);
				if (found != null) {
					return found;
				}
			}
		}
		for (String scope : scopes) {
			WebElement geom = findQPushButtonWithLabelLowestOnScreen(scope, "Cancel", "Отмена");
			if (geom != null) {
				return geom;
			}
		}
		return null;
	}

	private WebElement pickFirstQPushButtonOrButton(String scopeXpath, String label) {
		List<WebElement> qp = driver.findElements(By.xpath(scopeXpath + "//*[@ClassName='QPushButton' and @Name='"
				+ label + "']"));
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

	private static WebElement firstOrNull(List<WebElement> elements) {
		return elements.isEmpty() ? null : elements.get(0);
	}

	private void invokeQtPushButton(WebElement btn) {
		RuntimeException last = null;
		try {
			btn.click();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			new Actions(driver).moveToElement(btn).pause(50).click().perform();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			new Actions(driver).moveToElement(btn).pause(40).sendKeys(Keys.SPACE).perform();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			btn.sendKeys(Keys.SPACE);
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			btn.sendKeys(Keys.ENTER);
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		throw new IllegalStateException("QPushButton found but could not be invoked (WinAppDriver/Qt).", last);
	}

	private void clickFirstMatchingName(int secondsTotal, String... names) {
		int perName = Math.max(3, secondsTotal / Math.max(1, names.length));
		for (String n : names) {
			try {
				new WebDriverWait(driver, perName).until(ExpectedConditions.elementToBeClickable(MobileBy.name(n)))
						.click();
				return;
			} catch (TimeoutException ignored) {
				// next name
			}
		}
		throw new TimeoutException("No element matched by Name: " + String.join(", ", names));
	}

	private void setTextField(String automationId, String value) {
		WebElement field = waitClickable(automationId);
		field.click();
		// chord: Win/Qt + WinAppDriver — конкатенация CONTROL+"a" даёт «битый» ввод (раскладка / лишние символы)
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		field.sendKeys(Keys.BACK_SPACE);
		field.sendKeys(value);
	}

	private String readTextValue(WebElement field) {
		String value = field.getAttribute("Value.Value");
		if (value != null && !value.isEmpty()) {
			return value;
		}
		String legacy = field.getAttribute("LegacyIAccessible.Value");
		if (legacy != null && !legacy.isEmpty()) {
			return legacy;
		}
		return field.getText();
	}

	private WebElement waitClickable(String automationId) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		return wait.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(automationId)));
	}
}
