package de.thws.testing.utils;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;

public final class SaveDialogWinAppSession {

	private SaveDialogWinAppSession() {
	}

	public static boolean tryClickSaveIfDialogPresent(String serverUrl, HWND preferredHwnd, long shortWaitMs)
			throws InterruptedException {
		HWND hwnd = preferredHwnd;
		if (hwnd == null || Pointer.nativeValue(hwnd.getPointer()) == 0
				|| !SaveDialogWindowFinder.isWindowStillSaveShellDialog(hwnd)) {
			hwnd = SaveDialogWindowFinder.waitForSaveDialogHwnd(shortWaitMs);
		}
		if (hwnd == null) {
			return false;
		}

		long ptr = Pointer.nativeValue(hwnd.getPointer());
		String[] hexVariants = { String.format("0x%X", ptr), String.format("%X", ptr) };

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
					if (clickIfPossible(w, By.name("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c"))) {
						return true;
					}
					if (clickIfPossible(w, By.xpath("//Button[@Name='Save']"))) {
						return true;
					}
					return false;
				} finally {
					try {
						dlg.quit();
					} catch (Exception ignored) {
					}
				}
			} catch (Exception ignored) {
			}
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
