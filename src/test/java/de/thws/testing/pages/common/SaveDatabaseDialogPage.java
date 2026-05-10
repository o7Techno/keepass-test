package de.thws.testing.pages.common;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import com.sun.jna.platform.win32.WinDef.HWND;

import de.thws.testing.utils.DriverFactory;
import de.thws.testing.utils.SaveDialogWinAppSession;
import de.thws.testing.utils.SaveDialogWindowFinder;
import io.appium.java_client.windows.WindowsDriver;

public class SaveDatabaseDialogPage {

	private static final long SAVE_DIALOG_WAIT_MS = 28_000;
	private static final long SAVE_DIALOG_STILL_OPEN_POLL_MS = 1_200;

	private final WindowsDriver driver;

	public SaveDatabaseDialogPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void saveAs(String fullPath) throws InterruptedException {
		Thread.sleep(1200);
		HWND hwnd = SaveDialogWindowFinder.waitForSaveDialogHwndAndActivate(SAVE_DIALOG_WAIT_MS);
		if (hwnd == null) {
			throw new IllegalStateException(
					"Save-as dialog (#32770) not found within " + SAVE_DIALOG_WAIT_MS + " ms. Run WinAppDriver on an interactive session.");
		}
		pastePathAndConfirmSave(hwnd, fullPath);
		Thread.sleep(400);
	}

	private void pastePathAndConfirmSave(HWND dialogHwnd, String fullPath) throws InterruptedException {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fullPath), null);
		Thread.sleep(200);

		Robot robot = createRobot();
		robot.setAutoDelay(35);

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(280);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(600);

		robotPressEnter(robot, 2, 400);

		Thread.sleep(350);
		if (!SaveDialogWindowFinder.isSaveShellDialogVisibleNow()) {
			return;
		}
		SaveDialogWinAppSession.tryClickSaveIfDialogPresent(DriverFactory.WIN_APP_DRIVER_URL, dialogHwnd,
				SAVE_DIALOG_STILL_OPEN_POLL_MS);

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(300);
		robotPressEnter(robot, 1, 200);
	}

	private static void robotPressEnter(Robot robot, int times, int pauseMs) throws InterruptedException {
		for (int i = 0; i < times; i++) {
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(pauseMs);
		}
	}

	private static Robot createRobot() {
		try {
			return new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
}
