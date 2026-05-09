package de.thws.testing.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

/**
 * Finds the native Win32 “Save as” shell dialog ({@code #32770}) so WinAppDriver
 * can attach via {@code appTopLevelWindow} — invisible from the KeePassXC app
 * session when the host runs comdlg in another process.
 */
public final class SaveDialogWindowFinder {

	private SaveDialogWindowFinder() {
	}

	/** Format expected by WinAppDriver {@code appTopLevelWindow}. */
	public static String hwndToHex(HWND hwnd) {
		long v = Pointer.nativeValue(hwnd.getPointer());
		return String.format("0x%X", v);
	}

	public static HWND waitForSaveDialogHwnd(long timeoutMs) throws InterruptedException {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			HWND fg = User32.INSTANCE.GetForegroundWindow();
			// После появления «Сохранить как» активное верхнее окно часто #32770 (comdlg).
			if (isVisibleLegacyDialog(fg)) {
				return fg;
			}
			HWND found = findVisibleSave32770();
			if (found != null) {
				return found;
			}
			Thread.sleep(150);
		}
		return null;
	}

	/** Видимое окно класса {@code #32770} (без проверки заголовка). */
	private static boolean isVisibleLegacyDialog(HWND hwnd) {
		if (hwnd == null || Pointer.nativeValue(hwnd.getPointer()) == 0) {
			return false;
		}
		if (!User32.INSTANCE.IsWindowVisible(hwnd)) {
			return false;
		}
		char[] clsBuf = new char[256];
		User32.INSTANCE.GetClassName(hwnd, clsBuf, 256);
		return "#32770".equals(Native.toString(clsBuf));
	}

	private static boolean isSaveShellDialog(HWND hwnd) {
		if (!isVisibleLegacyDialog(hwnd)) {
			return false;
		}
		return titleMatchesSave(hwnd);
	}

	private static boolean titleMatchesSave(HWND hwnd) {
		char[] titleBuf = new char[512];
		User32.INSTANCE.GetWindowText(hwnd, titleBuf, 512);
		String title = Native.toString(titleBuf);
		return matchesSaveTitle(title);
	}

	private static HWND findVisibleSave32770() {
		final HWND[] match = { null };
		User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
			@Override
			public boolean callback(HWND hwnd, Pointer data) {
				if (!User32.INSTANCE.IsWindowVisible(hwnd)) {
					return true;
				}
				char[] clsBuf = new char[256];
				User32.INSTANCE.GetClassName(hwnd, clsBuf, 256);
				if (!"#32770".equals(Native.toString(clsBuf))) {
					return true;
				}
				if (!titleMatchesSave(hwnd)) {
					return true;
				}
				match[0] = hwnd;
				return false;
			}
		}, Pointer.NULL);
		return match[0];
	}

	private static boolean matchesSaveTitle(String title) {
		if (title == null || title.isBlank()) {
			return false;
		}
		String lower = title.toLowerCase();
		return title.contains("Сохранить") || title.contains("базу данных") || lower.contains("save")
				|| lower.contains("database");
	}
}
