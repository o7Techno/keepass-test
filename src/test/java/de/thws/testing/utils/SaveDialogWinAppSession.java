package de.thws.testing.utils;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.jna.platform.win32.WinDef.HWND;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

/**
 * Second WinAppDriver session with {@code appTopLevelWindow} = Save-as HWND.
 */
public final class SaveDialogWinAppSession {

	private SaveDialogWinAppSession() {
	}

	public static boolean tryClickSave(String serverUrl, long hwndWaitMs) throws InterruptedException {
		HWND hwnd = SaveDialogWindowFinder.waitForSaveDialogHwnd(hwndWaitMs);
		if (hwnd == null) {
			System.err.println(
					"[SaveDialog] HWND не найден за " + hwndWaitMs + " ms (нет видимого #32770 на переднем плане).");
			return false;
		}

		long ptr = com.sun.jna.Pointer.nativeValue(hwnd.getPointer());
		String[] hexVariants = { String.format("0x%X", ptr), String.format("%X", ptr) };

		Exception lastError = null;
		for (String hex : hexVariants) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("platformName", "Windows");
			caps.setCapability("deviceName", "WindowsPC");
			caps.setCapability("appTopLevelWindow", hex);

			try {
				@SuppressWarnings("rawtypes")
				WindowsDriver dlg = new WindowsDriver(new URL(serverUrl), caps);
				try {
					WebDriverWait w = new WebDriverWait(dlg, 10);
					if (clickIfPossible(w, MobileBy.AccessibilityId("1"))) {
						return true;
					}
					if (clickIfPossible(w, By.name("Save"))) {
						return true;
					}
					if (clickIfPossible(w, By.xpath("//Button[@Name='Save']"))) {
						return true;
					}
					System.err.println("[SaveDialog] Сессия открылась (appTopLevelWindow=" + hex
							+ "), но кнопка Save / id=1 не найдена за 15 с.");
					return false;
				} finally {
					try {
						dlg.quit();
					} catch (Exception ignored) {
						// ignore
					}
				}
			} catch (Exception e) {
				lastError = e;
				System.err.println("[SaveDialog] Не удалось создать сессию appTopLevelWindow=" + hex + ": "
						+ e.getMessage());
			}
		}
		if (lastError != null) {
			System.err.println(
					"[SaveDialog] Вторая сессия не создана на " + serverUrl + ". "
							+ "При одной сессии WinAppDriver — используй Robot Enter после ввода пути или обнови драйвер. "
							+ "Причина: " + lastError.getMessage());
			return false;
		}
		return false;
	}

	private static boolean clickIfPossible(WebDriverWait w, org.openqa.selenium.By locator) {
		try {
			w.until(ExpectedConditions.elementToBeClickable(locator)).click();
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}
}
