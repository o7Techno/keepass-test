package de.thws.testing.support;

import java.nio.file.Files;
import java.nio.file.Path;

import de.thws.testing.flow.DatabaseCreationFlow;
import de.thws.testing.utils.DriverFactory;
import io.appium.java_client.windows.WindowsDriver;

public final class SuiteDatabaseSupport {

	private static final String SUITE_FILENAME = "suite.kdbx";

	private static final Path RELATIVE_SUITE_ROOT = Path.of("target", ".keepass-junit-suite");

	private static final String PROP_DB = "keepass.suite.database";
	private static final String PROP_MASTER = "keepass.suite.master";

	private static final Object LOCK = new Object();

	private SuiteDatabaseSupport() {
	}

	public static Path suiteWorkspaceDirectory() {
		return Path.of(System.getProperty("user.dir")).resolve(RELATIVE_SUITE_ROOT).toAbsolutePath().normalize();
	}

	public static Path suiteDatabasePath() {
		String override = System.getProperty(PROP_DB);
		if (override != null && !override.isBlank()) {
			Path p = Path.of(override.trim());
			if (!p.isAbsolute()) {
				p = Path.of(System.getProperty("user.dir")).resolve(p).normalize();
			}
			return p.toAbsolutePath().normalize();
		}
		return suiteWorkspaceDirectory().resolve(SUITE_FILENAME).normalize();
	}

	public static String suiteDatabasePathForSaveDialog() {
		return suiteDatabasePath().toString();
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
				throw new IllegalStateException(
						"Suite database missing at " + db + ". Check Save-as path matches suiteDatabasePath().");
			}
		}
	}

	@Deprecated
	public static void ensureSuiteDatabaseExistsOnce() throws Exception {
		prepareFreshSuiteDatabase();
	}
}
