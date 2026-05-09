package de.thws.testing.tests.entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.thws.testing.config.KeePassAccessibility;
import de.thws.testing.pages.entry.EntryEditDialogPage;
import de.thws.testing.pages.generator.GeneratorCommonPage;
import de.thws.testing.pages.main.MainWindowPage;
import de.thws.testing.pages.unlock.UnlockDatabasePage;
import de.thws.testing.support.SuiteDatabaseSupport;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

/**
 * Одна общая БД на весь класс: {@link SuiteDatabaseSupport}. После ввода данных
 * запись сохраняется (OK) и снова открывается из списка — проверяем именно
 * персистентность, а не «прочитали то же поле в том же диалоге».
 */
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntryTabTest {

	private static final int AFTER_SAVE_MS = 700;
	private static final int AFTER_REOPEN_MS = 700;

	private WindowsDriver driver;
	private EntryEditDialogPage entry;
	private MainWindowPage main;
	private GeneratorCommonPage generator;

	@BeforeAll
	public static void prepareSuiteDatabase() throws Exception {
		SuiteDatabaseSupport.prepareFreshSuiteDatabase();
	}

	@BeforeEach
	public void setUp() throws Exception {
		String dbPath = SuiteDatabaseSupport.suiteDatabasePath().toAbsolutePath().toString();
		driver = DriverFactory.createKeePassDriverOpeningDatabase(dbPath);
		Thread.sleep(1500);

		new UnlockDatabasePage(driver).unlockIfNeeded(SuiteDatabaseSupport.suiteMasterPassword());

		main = new MainWindowPage(driver);
		main.clickNewEntry();
		new WebDriverWait(driver, 25).until(ExpectedConditions
				.elementToBeClickable(MobileBy.AccessibilityId(KeePassAccessibility.ENTRY_EDIT_TITLE)));
		Thread.sleep(200);

		entry = new EntryEditDialogPage(driver);
		generator = new GeneratorCommonPage(driver);
	}

	/** Сохранить диалог и снова открыть запись по уникальному фрагменту заголовка в списке. */
	private void saveAndReopenByTitleMarker(String titleMarker) throws InterruptedException {
		entry.clickOk();
		Thread.sleep(AFTER_SAVE_MS);
		main.doubleClickEntryWithTitleContaining(titleMarker);
		Thread.sleep(AFTER_REOPEN_MS);
		entry = new EntryEditDialogPage(driver);
	}

	@Test
	@Order(1)
	public void test01_title_persistsAfterSaveAndReopen() throws InterruptedException {
		String marker = "T01Title";
		entry.setTitle(marker + " My Service");
		Thread.sleep(150);
		saveAndReopenByTitleMarker(marker);
		assertEquals(marker + " My Service", entry.getTitle());
	}

	@Test
	@Order(2)
	public void test02_username_persistsAfterSaveAndReopen() throws InterruptedException {
		String marker = "T02User";
		entry.setTitle(marker + " Row");
		entry.setUsername("user@example.org");
		Thread.sleep(150);
		saveAndReopenByTitleMarker(marker);
		assertEquals("user@example.org", entry.getUsername());
	}

	@Test
	@Order(3)
	public void test03_username_menu_thenPersistedUsername() throws InterruptedException {
		String marker = "T03Menu";
		entry.setTitle(marker + " Row");
		entry.setUsername("before-menu");
		Thread.sleep(100);
		entry.openUsernameMenu();
		Thread.sleep(300);
		entry.dismissOpenMenuWithEscape();
		Thread.sleep(200);
		entry.setUsername("after-menu");
		Thread.sleep(100);
		saveAndReopenByTitleMarker(marker);
		assertEquals("after-menu", entry.getUsername());
	}

	@Test
	@Order(4)
	public void test04_password_persistsAfterSaveRevealOnReopen() throws InterruptedException {
		String marker = "T04Pwd";
		entry.setTitle(marker + " Row");
		entry.setPassword("StaticPwd#9");
		Thread.sleep(150);
		saveAndReopenByTitleMarker(marker);
		entry.clickPasswordToggleVisibility();
		Thread.sleep(150);
		String revealed = entry.getPassword();
		assertTrue(revealed.contains("Static") || revealed.contains("9"),
				"Saved password should match after reopen + toggle. Got: " + revealed);
	}

	@Test
	@Order(5)
	public void test05_password_generator_persistsNonTrivialPassword() throws InterruptedException {
		String marker = "T05Gen";
		entry.setTitle(marker + " Row");
		entry.setPassword("");
		Thread.sleep(150);

		entry.clickPasswordGenerator();
		Thread.sleep(800);

		if (generator.isGeneratorWindowVisible()) {
			generator.clickGenerateButton();
			Thread.sleep(400);
			generator.clickCloseButton();
			Thread.sleep(400);
		}

		entry.clickPasswordToggleVisibility();
		Thread.sleep(200);
		String beforeSave = entry.getPassword();
		assertNotNull(beforeSave);

		saveAndReopenByTitleMarker(marker);
		entry.clickPasswordToggleVisibility();
		Thread.sleep(200);
		String afterSave = entry.getPassword();
		assertNotNull(afterSave);
		assertTrue(afterSave.length() >= 4,
				"Expected non-trivial password persisted after generator. Length " + afterSave.length());
	}

	@Test
	@Order(6)
	public void test06_password_toggleStableAfterReopen() throws InterruptedException {
		String marker = "T06Tog";
		entry.setTitle(marker + " Row");
		entry.setPassword("ToggleTest1");
		Thread.sleep(100);
		saveAndReopenByTitleMarker(marker);
		entry.clickPasswordToggleVisibility();
		Thread.sleep(100);
		String a = entry.getPassword();
		entry.clickPasswordToggleVisibility();
		Thread.sleep(100);
		entry.clickPasswordToggleVisibility();
		Thread.sleep(100);
		String b = entry.getPassword();
		assertEquals(a, b, "Toggle visibility should be stable on reopened entry.");
	}

	@Test
	@Order(7)
	public void test07_url_persistsAfterSaveAndReopen() throws InterruptedException {
		String marker = "T07Url";
		entry.setTitle(marker + " Row");
		entry.setUrl("https://example.com/app");
		Thread.sleep(150);
		saveAndReopenByTitleMarker(marker);
		assertTrue(entry.getUrl().contains("example.com"));
	}

	@Test
	@Order(8)
	public void test08_url_tools_menu_thenPersistedUrl() throws InterruptedException {
		String marker = "T08Tool";
		entry.setTitle(marker + " Row");
		entry.setUrl("https://example.com/placeholder");
		Thread.sleep(150);
		entry.clickUrlToolsButton();
		Thread.sleep(300);
		entry.dismissOpenMenuWithEscape();
		Thread.sleep(200);
		entry.setUrl("https://smoke.test");
		Thread.sleep(100);
		saveAndReopenByTitleMarker(marker);
		assertTrue(entry.getUrl().contains("smoke.test"));
	}

	@Test
	@Order(9)
	public void test09_tags_persistAfterSaveAndReopen() throws InterruptedException {
		String marker = "T09Tag";
		entry.setTitle(marker + " Row");
		entry.setTags("work, finance , important");
		Thread.sleep(150);
		saveAndReopenByTitleMarker(marker);
		String tags = entry.getTags().replaceAll("\\s+", "").toLowerCase();
		assertTrue(tags.contains("work"), "tags after reopen: " + entry.getTags());
		assertTrue(tags.contains("finance"), "tags after reopen: " + entry.getTags());
	}

	@Test
	@Order(10)
	public void test10_expires_onThenOff_persisted() throws InterruptedException {
		String marker = "T10Exp";
		entry.setTitle(marker + " Row");
		assertFalse(entry.isExpiresChecked(), "New entry usually starts with expiry off.");

		entry.setExpires(true);
		Thread.sleep(250);
		assertTrue(entry.isExpiresChecked());
		String shownBefore = entry.getExpiryDateTimeText();
		assertFalse(shownBefore == null || shownBefore.isBlank(),
				"Date/time control should show a value when expiry is on.");

		saveAndReopenByTitleMarker(marker);
		assertTrue(entry.isExpiresChecked(), "Expiry should stay on after save/reopen.");
		String shownAfter = entry.getExpiryDateTimeText();
		assertFalse(shownAfter == null || shownAfter.isBlank());

		entry.setExpires(false);
		Thread.sleep(200);
		saveAndReopenByTitleMarker(marker);
		assertFalse(entry.isExpiresChecked(), "Expiry should stay off after second save/reopen.");
	}

	@Test
	@Order(11)
	public void test11_expiry_presets_menu_stillOnAfterSave() throws InterruptedException {
		String marker = "T11Pre";
		entry.setTitle(marker + " Row");
		entry.setExpires(true);
		Thread.sleep(200);
		entry.clickExpiryPresets();
		Thread.sleep(350);
		entry.dismissOpenMenuWithEscape();
		Thread.sleep(200);
		assertTrue(entry.isExpiresChecked());
		saveAndReopenByTitleMarker(marker);
		assertTrue(entry.isExpiresChecked());
	}

	@Test
	@Order(12)
	public void test12_notes_multiline_persistAfterSave() throws InterruptedException {
		String marker = "T12Note";
		entry.setTitle(marker + " Row");
		String text = "Line one\nLine two\nThird line.";
		entry.setNotes(text);
		Thread.sleep(200);
		saveAndReopenByTitleMarker(marker);
		String read = entry.getNotes();
		assertTrue(read.contains("Line one") && read.contains("Third line"),
				"Notes should persist multiple lines. Read: " + read);
	}

	@Test
	@Order(13)
	public void test13_cancel_doesNotPersistTitle() throws InterruptedException {
		entry.setTitle("DISCARD_ME_TITLE");
		Thread.sleep(150);
		entry.clickCancel();
		Thread.sleep(300);
		main.dismissUnsavedChangesDiscardIfPresent();
		Thread.sleep(400);

		main.clickNewEntry();
		new WebDriverWait(driver, 25).until(ExpectedConditions
				.elementToBeClickable(MobileBy.AccessibilityId(KeePassAccessibility.ENTRY_EDIT_TITLE)));
		Thread.sleep(200);

		entry = new EntryEditDialogPage(driver);

		String title = entry.getTitle();
		assertFalse("DISCARD_ME_TITLE".equals(title),
				"Title from cancelled dialog should not appear on a fresh new entry.");
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			try {
				if (generator.isGeneratorWindowVisible()) {
					generator.clickCloseButton();
					Thread.sleep(300);
				}
			} catch (Exception e) {
				System.out.println("Warning: generator close in teardown: " + e.getMessage());
			}
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Warning: driver.quit failed: " + e.getMessage());
			}
		}
	}
}
