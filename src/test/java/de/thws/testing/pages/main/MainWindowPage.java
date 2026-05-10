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

	private static final String[] NEW_ENTRY_EXACT_NAMES = { "New Entry...", "New Entry\u2026",
			"\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c...",
			"\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c\u2026", };

	private static final String[] NEW_ENTRY_TOOLBAR_NAMES = { "New Entry...", "New Entry\u2026",
			"\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c\u2026",
			"\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c...", "Add new entry",
			"New Entry", "\u041d\u043e\u0432\u0430\u044f \u0437\u0430\u043f\u0438\u0441\u044c",
			"\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c" };

	private static final long IMPLICIT_RESTORE_SEC = 5;

	private final WindowsDriver driver;

	public MainWindowPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void clickNewEntry() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			WebElement btn = findNewEntryToolbarButtonOnce(driver);
			if (btn == null) {
				throw new NoSuchElementException(
						"New Entry toolbar button not found under AutomationId=" + KeePassAccessibility.MAIN_TOOL_BAR);
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
		if (lower.contains("\u0441\u043e\u0437\u0434\u0430\u0442\u044c")
				&& lower.contains("\u0437\u0430\u043f\u0438\u0441\u044c")) {
			return true;
		}
		if (lower.contains("add new") && lower.contains("entry")) {
			return true;
		}
		if ("new entry".equals(lower)) {
			return true;
		}
		return lower.contains("\u043d\u043e\u0432\u0430\u044f \u0437\u0430\u043f\u0438\u0441\u044c")
				|| lower.contains("\u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0437\u0430\u043f\u0438\u0441\u044c");
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

	/**
	 * Best-effort: exit quick-search / clear entry list filter. Must not throw —
	 * WinAppDriver often returns "unknown error" on broad XPath or fragile edits.
	 */
	public void clearEntrySearchFilter() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			try {
				String xp = "//*[@ClassName='QLineEdit' and "
						+ "(contains(@AutomationId,'search') or contains(@AutomationId,'Search'))]";
				int n = 0;
				for (Object o : driver.findElements(By.xpath(xp))) {
					if (!(o instanceof WebElement)) {
						continue;
					}
					if (++n > 3) {
						break;
					}
					WebElement el = (WebElement) o;
					try {
						if (el.isDisplayed()) {
							el.click();
							el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
							el.sendKeys(Keys.BACK_SPACE);
							sleepMs(50);
						}
					} catch (RuntimeException ignored) {
					}
				}
			} catch (RuntimeException ignored) {
			}
			try {
				driver.findElement(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW)).click();
				sleepMs(80);
			} catch (RuntimeException ignored) {
			}
			try {
				new Actions(driver).sendKeys(Keys.ESCAPE).perform();
				sleepMs(60);
				new Actions(driver).sendKeys(Keys.ESCAPE).perform();
				sleepMs(60);
			} catch (RuntimeException ignored) {
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

	private static void sleepMs(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void doubleClickFirstEntry() {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement list = wait.until(ExpectedConditions
				.visibilityOfElementLocated(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW)));
		List<WebElement> rows = list
				.findElements(By.xpath(".//ListItem | .//DataItem | .//TreeItem | .//ListBoxItem"));
		WebElement target = rows.isEmpty() ? list : rows.get(0);
		new Actions(driver).doubleClick(target).perform();
	}

	public void doubleClickEntryWithTitleContaining(String titleSubstring) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			clearEntrySearchFilter();
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 25, 200);
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW)));
			WebElement row = wait.until(d -> {
				WebElement list = d.findElement(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW));
				return findEntryRowContaining(list, titleSubstring);
			});
			new Actions(driver).moveToElement(row).pause(80).doubleClick().perform();
		} catch (TimeoutException e) {
			try {
				WebElement list = driver.findElement(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW));
				List<WebElement> rows = list.findElements(
						By.xpath(".//ListItem | .//DataItem | .//TreeItem | .//ListBoxItem"));
				if (rows.size() == 1) {
					new Actions(driver).moveToElement(rows.get(0)).pause(80).doubleClick().perform();
					return;
				}
			} catch (RuntimeException ignored) {
			}
			throw new NoSuchElementException(
					"entryView: no row with title containing \"" + titleSubstring + "\"", e);
		} finally {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_RESTORE_SEC, TimeUnit.SECONDS);
		}
	}

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

	public boolean entryListContainsTitleSubstring(String titleSubstring) {
		if (titleSubstring == null || titleSubstring.isEmpty()) {
			return false;
		}
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		try {
			List<WebElement> lists = driver.findElements(MobileBy.AccessibilityId(KeePassAccessibility.MAIN_ENTRY_VIEW));
			if (lists.isEmpty()) {
				return false;
			}
			return findEntryRowContaining(lists.get(0), titleSubstring) != null;
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
			List<WebElement> rows = list
					.findElements(By.xpath(".//ListItem | .//DataItem | .//TreeItem | .//ListBoxItem"));
			for (int i = 0; i < rows.size(); i++) {
				if (rowSubtreeContainsNeedle(rows.get(i), needle)) {
					return rows.get(i);
				}
			}
		} catch (StaleElementReferenceException e) {
			return null;
		}
		return null;
	}

	private static boolean rowSubtreeContainsNeedle(WebElement row, String needle) {
		if (elementTextBundle(row).contains(needle)) {
			return true;
		}
		int seen = 0;
		for (WebElement el : row.findElements(By.xpath(".//*"))) {
			if (elementTextBundle(el).contains(needle)) {
				return true;
			}
			if (++seen > 200) {
				break;
			}
		}
		return false;
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
		}
		return sb.toString();
	}
}
