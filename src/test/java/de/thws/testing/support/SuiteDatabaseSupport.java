package de.thws.testing.support;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class SuiteDatabaseSupport {

	private static final String SUITE_FILENAME = "suite.kdbx";
	private static final Path RELATIVE_SUITE_ROOT = Path.of("target", ".keepass-junit-suite");
	private static final Path TEMPLATE_DB_PATH = Path.of("Passwords.kdbx");

	private static final Object LOCK = new Object();

	private SuiteDatabaseSupport() {
	}

	public static Path suiteWorkspaceDirectory() {
		return Path.of(System.getProperty("user.dir")).resolve(RELATIVE_SUITE_ROOT).toAbsolutePath().normalize();
	}

	public static Path suiteDatabasePath() {
		return suiteWorkspaceDirectory().resolve(SUITE_FILENAME).normalize();
	}

	public static String suiteMasterPassword() {
		return "softwaretesting08";
	}

	public static void prepareFreshSuiteDatabase() throws Exception {
		synchronized (LOCK) {
			Path targetDb = suiteDatabasePath();
			Path parent = targetDb.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}

			Path sourceDb = TEMPLATE_DB_PATH.toAbsolutePath().normalize();

			if (!Files.exists(sourceDb)) {
				throw new IllegalStateException("Error: template.kdbx doesnt exist!");
			}

			Files.copy(sourceDb, targetDb, StandardCopyOption.REPLACE_EXISTING);
		}
	}
}