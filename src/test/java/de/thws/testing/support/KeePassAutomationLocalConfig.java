package de.thws.testing.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Локальный конфиг KeePassXC для автотестов: отключает Quick Unlock (Windows Hello /
 * PIN после мастер-пароля). Совпадает с пунктом «Инструменты → Настройки → Безопасность
 * → Удобство».
 *
 * <p>
 * Файл: {@code %USERPROFILE%\.keepass-junit-suite\keepassxc-local.ini}; передаётся в
 * процесс как {@code --localconfig} (см. {@link de.thws.testing.utils.DriverFactory}).
 * </p>
 */
public final class KeePassAutomationLocalConfig {

	/** Тот же каталог, что и suite {@code .kdbx} по умолчанию. */
	private static final String SUITE_DIR = ".keepass-junit-suite";
	private static final String LOCAL_INI_NAME = "keepassxc-local.ini";

	private KeePassAutomationLocalConfig() {
	}

	public static Path localConfigFile() {
		return Path.of(System.getProperty("user.home")).resolve(SUITE_DIR).resolve(LOCAL_INI_NAME).toAbsolutePath()
				.normalize();
	}

	/**
	 * Qt/QSettings: ключ {@code Security/QuickUnlock} хранится как секция {@code [Security]},
	 * параметр {@code QuickUnlock}.
	 */
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
