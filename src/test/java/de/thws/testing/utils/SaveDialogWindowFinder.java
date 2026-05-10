package de.thws.testing.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public final class SaveDialogWindowFinder {

	private SaveDialogWindowFinder() {
	}

	public static String hwndToHex(HWND hwnd) {
		long v = Pointer.nativeValue(hwnd.getPointer());
		return String.format("0x%X", v);
	}

	public static HWND waitForSaveDialogHwnd(long timeoutMs) throws InterruptedException {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			HWND fg = User32.INSTANCE.GetForegroundWindow();
			if (isSaveShellDialog(fg)) {
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

	public static HWND waitForSaveDialogHwndAndActivate(long timeoutMs) throws InterruptedException {
		HWND hwnd = waitForSaveDialogHwnd(timeoutMs);
		if (hwnd == null) {
			return null;
		}
		User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE);
		for (int i = 0; i < 8; i++) {
			User32.INSTANCE.SetForegroundWindow(hwnd);
			Thread.sleep(120);
			HWND fg = User32.INSTANCE.GetForegroundWindow();
			if (fg != null && Pointer.nativeValue(fg.getPointer()) == Pointer.nativeValue(hwnd.getPointer())) {
				break;
			}
		}
		Thread.sleep(200);
		return hwnd;
	}

	public static boolean isSaveShellDialogVisibleNow() {
		HWND fg = User32.INSTANCE.GetForegroundWindow();
		if (isSaveShellDialog(fg)) {
			return true;
		}
		return findVisibleSave32770() != null;
	}

	public static boolean isWindowStillSaveShellDialog(HWND hwnd) {
		if (hwnd == null || Pointer.nativeValue(hwnd.getPointer()) == 0) {
			return false;
		}
		if (!User32.INSTANCE.IsWindow(hwnd)) {
			return false;
		}
		return isSaveShellDialog(hwnd);
	}

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
		return title.contains("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c")
				|| title.contains("\u0431\u0430\u0437\u0443 \u0434\u0430\u043d\u043d\u044b\u0445")
				|| lower.contains("save") || lower.contains("database");
	}
}
