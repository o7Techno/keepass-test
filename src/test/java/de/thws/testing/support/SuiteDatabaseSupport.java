package de.thws.testing.support;

import java.nio.file.Files;
import java.nio.file.Path;

import de.thws.testing.flow.DatabaseCreationFlow;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

public final class SuiteDatabaseSupport {

	private static final String SUITE_DIR = ".keepass-junit-suite";
	private static final String SUITE_FILENAME = "suite.kdbx";

	private static final String PROP_DB = "keepass.suite.database";
	private static final String PROP_MASTER = "keepass.suite.master";

	private static final Object LOCK = new Object();

	private SuiteDatabaseSupport() {
	}

	public static Path suiteDatabasePath() {
		String override = System.getProperty(PROP_DB);
		if (override != null && !override.isBlank()) {
			return Path.of(override).toAbsolutePath().normalize();
		}
		return Path.of(System.getProperty("user.home")).resolve(SUITE_DIR).resolve(SUITE_FILENAME).toAbsolutePath()
				.normalize();
	}

	public static String suiteDatabasePathForSaveDialog() {
		String override = System.getProperty(PROP_DB);
		if (override != null && !override.isBlank()) {
			return Path.of(override).toAbsolutePath().normalize().toString();
		}
		return "%USERPROFILE%\\" + SUITE_DIR + "\\" + SUITE_FILENAME;
	}

	public static String suiteMasterPassword() {
		String p = System.getProperty(PROP_MASTER);
		if (p != null && !p.isBlank()) {
			return p;
		}
		return "Tp9#xK2$mQw8@vLn4&Rz6_long_test_master";
	}

	public static void prepareFreshSuiteDatabase() throws Exception {
		synchronized (LOCK) {
			Path db = suiteDatabasePath();
			Path parent = db.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}

			try {
				Files.deleteIfExists(db);
			} catch (Exception e) {
				throw new IllegalStateException("Cannot delete suite database at " + db
						+ " (close KeePassXC if locked): " + e.getMessage(), e);
			}

			@SuppressWarnings("rawtypes")
			WindowsDriver driver = DriverFactory.createKeePassDriverWithoutDatabaseFile();
			try {
				DatabaseCreationFlow.createDatabaseFileOnly(driver, suiteDatabasePathForSaveDialog(),
						suiteMasterPassword(), "SuiteDb");
			} finally {
				try {
					driver.quit();
				} catch (Exception ignored) {
				}
			}

			if (!Files.exists(db)) {
				throw new IllegalStateException("Suite database missing at " + db
						+ ". Check Save-as path from suiteDatabasePathForSaveDialog() and %USERPROFILE% expansion.");
			}
		}
	}

	@Deprecated
	public static void ensureSuiteDatabaseExistsOnce() throws Exception {
		prepareFreshSuiteDatabase();
	}
}
