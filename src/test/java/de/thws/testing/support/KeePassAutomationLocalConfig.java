package de.thws.testing.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class KeePassAutomationLocalConfig {

	private static final String SUITE_DIR = ".keepass-junit-suite";
	private static final String LOCAL_INI_NAME = "keepassxc-local.ini";

	private KeePassAutomationLocalConfig() {
	}

	public static Path localConfigFile() {
		return Path.of(System.getProperty("user.home")).resolve(SUITE_DIR).resolve(LOCAL_INI_NAME).toAbsolutePath()
				.normalize();
	}

	public static void ensureQuickUnlockDisabled() throws IOException {
		Path file = localConfigFile();
		Path parent = file.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		String ini = "[Security]\nQuickUnlock=false\n";
		Files.writeString(file, ini, StandardCharsets.UTF_8);
	}
}
