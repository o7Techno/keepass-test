package de.thws.testing.utils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import de.thws.testing.support.KeePassAutomationLocalConfig;
import io.appium.java_client.windows.WindowsDriver;

public class DriverFactory {

	public static final String WIN_APP_DRIVER_URL = "http://127.0.0.1:4723";

	private static final String KEEPASSXC_EXE = "C:\\Program Files\\KeePassXC\\KeePassXC.exe";

	@SuppressWarnings("rawtypes")
	public static WindowsDriver createKeePassDriver() {
		return createKeePassDriverOpeningDatabase(null);
	}

	/** Запуск без файла — экран приветствия / последнее состояние приложения. */
	@SuppressWarnings("rawtypes")
	public static WindowsDriver createKeePassDriverWithoutDatabaseFile() {
		return createKeePassDriverOpeningDatabase(null);
	}

	/**
	 * При старте сразу открывает указанный {@code .kdbx} — появляется экран Unlock
	 * (или главное окно). Аргумент передаётся в WinAppDriver как {@code appArguments}.
	 *
	 * @param absoluteDatabasePath полный путь к файлу или {@code null}
	 */
	@SuppressWarnings("rawtypes")
	public static WindowsDriver createKeePassDriverOpeningDatabase(String absoluteDatabasePath) {
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.setCapability("app", KEEPASSXC_EXE);
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");
			capabilities.setCapability("appArguments", buildAppArguments(absoluteDatabasePath));

			WindowsDriver driver = new WindowsDriver(new URL(WIN_APP_DRIVER_URL), capabilities);

			// Selenium 3 tarzı bekleme kodu
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			return driver;
		} catch (Exception e) {
			throw new RuntimeException("Driver başlatılamadı: " + e.getMessage());
		}
	}

	private static String buildAppArguments(String absoluteDatabasePath) {
		try {
			KeePassAutomationLocalConfig.ensureQuickUnlockDisabled();
		} catch (IOException e) {
			throw new RuntimeException("KeePassXC local config (QuickUnlock off): " + e.getMessage(), e);
		}
		StringBuilder args = new StringBuilder();
		args.append("--localconfig ").append(quoteIfNeeded(KeePassAutomationLocalConfig.localConfigFile().toString()));
		if (absoluteDatabasePath != null && !absoluteDatabasePath.isBlank()) {
			args.append(' ').append(quoteIfNeeded(absoluteDatabasePath.trim()));
		}
		return args.toString();
	}

	private static String quoteIfNeeded(String path) {
		String p = path.trim();
		if (p.isEmpty()) {
			return "\"\"";
		}
		if (p.contains(" ")) {
			return "\"" + p + "\"";
		}
		return p;
	}
}