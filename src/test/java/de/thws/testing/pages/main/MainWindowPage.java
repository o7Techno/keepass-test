package de.thws.testing.pages.main;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public class MainWindowPage {

	/**
	 * Точные подписи из Accessibility Insights (EN/RU). У Qt-кнопки часто нет
	 * AutomationId, зато есть Name и ClassName {@code QToolButton}.
	 */
	private static final String[] NEW_ENTRY_EXACT_NAMES = { "New Entry...", "New Entry…", "Создать запись...",
			"Создать запись…", };

	private static final String[] NEW_ENTRY_TOOLBAR_NAMES = { "New Entry...", "New Entry…", "Создать запись…",
			"Создать запись...", "Add new entry", "New Entry", "Новая запись", "Добавить запись" };

	private static final long IMPLICIT_RESTORE_SEC = 5;

	private final WindowsDriver driver;

	public MainWindowPage(WindowsDriver driver) {
		this.driver = driver;
	}

	/**
	 * Одна попытка поиска (несколько локаторов подряд — это не «ретраи ожидания»:
	 * дерево уже есть, ищем тем способом, которым WinAppDriver реально видит Qt).
	 * <p>
	 * Важно: в WinAppDriver узлы {@code QToolButton} нередко не матчятся на
	 * {@code //Button}, поэтому есть XPath по {@code @ClassName='QToolButton'}.
	 */
	public void clickNewEntry() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			WebElement btn = findNewEntryToolbarButtonOnce(driver);
			if (btn == null) {
				throw new NoSuchElementException(
						"Кнопка «New Entry» не найдена под AutomationId=" + KeePassAccessibility.MAIN_TOOL_BAR
								+ ". Ожидаются Name вроде «New Entry...» и ClassName=QToolButton (см. Accessibility Insights).");
			}
			invokeQtToolBarButton(btn);
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private WebElement findNewEntryToolbarButtonOnce(WebDriver d) {
		String barId = KeePassAccessibility.MAIN_TOOL_BAR;

		for (String exact : NEW_ENTRY_EXACT_NAMES) {
			WebElement hit = firstOrNull(
					d.findElements(By.xpath("//*[@AutomationId='" + barId + "']//*[@Name='" + exact + "']")));
			if (hit != null) {
				return hit;
			}
			hit = firstOrNull(d.findElements(
					By.xpath("//ToolBar[@AutomationId='" + barId + "']//*[@Name='" + exact + "']")));
			if (hit != null) {
				return hit;
			}
		}

		for (String xp : xpathsNewEntryQtToolButton(barId)) {
			WebElement hit = firstOrNull(d.findElements(By.xpath(xp)));
			if (hit != null) {
				return hit;
			}
		}

		WebElement hit = scanToolbarSubtreeForQtNewEntry(d, "//*[@AutomationId='" + barId + "']");
		if (hit != null) {
			return hit;
		}
		hit = scanToolbarSubtreeForQtNewEntry(d, "//ToolBar[@AutomationId='" + barId + "']");
		if (hit != null) {
			return hit;
		}

		try {
			WebElement bar = d.findElement(MobileBy.AccessibilityId(barId));
			hit = firstMatchingNewEntry(bar.findElements(By.xpath(".//*[@ClassName='QToolButton']")));
			if (hit != null) {
				return hit;
			}
			return firstMatchingNewEntry(bar.findElements(By.xpath(".//*")));
		} catch (NoSuchElementException ignored) {
			return null;
		}
	}

	private static String[] xpathsNewEntryQtToolButton(String barId) {
		String t = "//*[@AutomationId='" + barId + "']";
		String cond = "[@ClassName='QToolButton' and contains(@Name,'New Entry') and not(contains(@Name,'Edit'))]";
		return new String[] { t + "//*" + cond, "//ToolBar[@AutomationId='" + barId + "']//*" + cond,
				"//Pane[@AutomationId='" + barId + "']//*" + cond, };
	}

	private WebElement scanToolbarSubtreeForQtNewEntry(WebDriver d, String toolbarRootXpath) {
		List<WebElement> nodes = d.findElements(By.xpath(toolbarRootXpath + "//*"));
		for (WebElement el : nodes) {
			if (!"QToolButton".equals(el.getAttribute("ClassName"))) {
				continue;
			}
			if (matchesNewEntryControl(el)) {
				return el;
			}
		}
		return null;
	}

	private static WebElement firstOrNull(List<WebElement> elements) {
		return elements.isEmpty() ? null : elements.get(0);
	}

	private WebElement firstMatchingNewEntry(List<WebElement> elements) {
		for (WebElement el : elements) {
			if (matchesNewEntryControl(el)) {
				return el;
			}
		}
		return null;
	}

	private boolean matchesNewEntryControl(WebElement el) {
		String n = readAccessibleName(el);
		String fd = readFullDescription(el);
		if (!fd.isEmpty() && fd.equalsIgnoreCase("New Entry")) {
			return true;
		}
		if (n.isEmpty()) {
			return false;
		}
		for (String exact : NEW_ENTRY_TOOLBAR_NAMES) {
			if (n.equals(exact) || n.contains(exact)) {
				return true;
			}
		}
		String lower = n.toLowerCase();
		if (lower.contains("создать") && lower.contains("запись")) {
			return true;
		}
		if (lower.contains("add new") && lower.contains("entry")) {
			return true;
		}
		if ("new entry".equals(lower)) {
			return true;
		}
		return lower.contains("новая запись") || lower.contains("добавить запись");
	}

	private String readAccessibleName(WebElement el) {
		String n = el.getAttribute("Name");
		if (n == null || n.isBlank()) {
			n = el.getAttribute("AutomationName");
		}
		if (n == null || n.isBlank()) {
			n = el.getAttribute("LegacyIAccessible.Name");
		}
		if (n == null || n.isBlank()) {
			n = el.getText();
		}
		if (n == null) {
			return "";
		}
		return n.trim();
	}

	private String readFullDescription(WebElement el) {
		String fd = el.getAttribute("FullDescription");
		return fd == null ? "" : fd.trim();
	}

	private void invokeQtToolBarButton(WebElement btn) {
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
		throw new IllegalStateException("Toolbar button found but could not be invoked (WinAppDriver/Qt).", last);
	}

	public void doubleClickFirstEntry() {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement list = wait.until(ExpectedConditions
				.visibilityOfElementLocated(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW)));
		List<WebElement> rows = list.findElements(By.xpath(".//ListItem | .//DataItem | .//TreeItem"));
		WebElement target = rows.isEmpty() ? list : rows.get(0);
		new Actions(driver).doubleClick(target).perform();
	}

	/**
	 * Двойной клик по строке записи в списке, у которой в {@code Name} (или legacy
	 * имени) есть подстрока — надёжнее {@link #doubleClickFirstEntry()}, когда в БД
	 * уже несколько записей.
	 * <p>
	 * В KeePassXC заголовок часто висит на дочернем узле, а не на {@code ListItem} —
	 * ищем любой потомок {@code entryView} с текстом и поднимаемся к строке.
	 */
	public void doubleClickEntryWithTitleContaining(String titleSubstring) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 25, 200);
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW)));
			WebElement row = wait.until(d -> {
				WebElement list = d.findElement(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW));
				return findEntryRowContaining(list, titleSubstring);
			});
			new Actions(driver).doubleClick(row).perform();
		} catch (TimeoutException e) {
			throw new NoSuchElementException(
					"entryView: не найдена строка с заголовком, содержащим «" + titleSubstring + "»", e);
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	/**
	 * После «Отмена» на новой записи KeePassXC спрашивает «Unsaved Changes» —
	 * нужно нажать {@code Discard} (QPushButton без AutomationId), иначе список
	 * не обновится и следующий New Entry ведёт себя непредсказуемо.
	 */
	public void dismissUnsavedChangesDiscardIfPresent() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			WebElement discard;
			try {
				discard = new WebDriverWait(driver, 8, 200).until(d -> {
					for (WebElement dialog : d.findElements(By.xpath("//Window[contains(@Name,'Unsaved')]"))) {
						WebElement b = findQPushButtonByName(dialog, "Discard");
						if (b != null) {
							return b;
						}
						b = findQPushButtonByName(dialog, "\u0421\u0431\u0440\u043e\u0441\u0438\u0442\u044c");
						if (b != null) {
							return b;
						}
					}
					return null;
				});
			} catch (TimeoutException e) {
				return;
			}
			try {
				discard.click();
			} catch (RuntimeException e) {
				new Actions(driver).moveToElement(discard).pause(30).click().perform();
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private static WebElement findQPushButtonByName(WebElement root, String name) {
		List<WebElement> found = root.findElements(By.xpath(".//*[@ClassName='QPushButton' and @Name='" + name + "']"));
		return found.isEmpty() ? null : found.get(0);
	}

	private static WebElement findEntryRowContaining(WebElement list, String needle) {
		if (needle == null || needle.isEmpty()) {
			return null;
		}
		try {
			List<WebElement> byName = list.findElements(By.xpath(".//*[contains(@Name,'" + needle + "')]"));
			for (WebElement inner : byName) {
				WebElement row = ascendToEntryRow(inner);
				if (row != null) {
					return row;
				}
			}
			for (WebElement inner : list.findElements(By.xpath(".//*"))) {
				if (!elementTextBundle(inner).contains(needle)) {
					continue;
				}
				WebElement row = ascendToEntryRow(inner);
				if (row != null) {
					return row;
				}
			}
		} catch (StaleElementReferenceException e) {
			return null;
		}
		return null;
	}

	private static WebElement ascendToEntryRow(WebElement inner) {
		WebElement cur = inner;
		for (int depth = 0; depth < 40 && cur != null; depth++) {
			String tag = cur.getTagName();
			if ("ListItem".equalsIgnoreCase(tag) || "DataItem".equalsIgnoreCase(tag) || "TreeItem".equalsIgnoreCase(tag)
					|| "Custom".equalsIgnoreCase(tag)) {
				return cur;
			}
			try {
				cur = cur.findElement(By.xpath(".."));
			} catch (NoSuchElementException e) {
				break;
			}
		}
		return null;
	}

	private static String elementTextBundle(WebElement el) {
		StringBuilder sb = new StringBuilder();
		for (String attr : new String[] { "Name", "LegacyIAccessible.Name", "Value.Value", "FullDescription" }) {
			String v = el.getAttribute(attr);
			if (v != null && !v.isEmpty()) {
				sb.append(v);
			}
		}
		try {
			String t = el.getText();
			if (t != null && !t.isEmpty()) {
				sb.append(t);
			}
		} catch (RuntimeException ignored) {
			// ignore
		}
		return sb.toString();
	}
}
