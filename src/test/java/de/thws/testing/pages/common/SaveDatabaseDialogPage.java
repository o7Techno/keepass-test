package de.thws.testing.pages.common;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import de.thws.testing.utils.DriverFactory;
import de.thws.testing.utils.SaveDialogWinAppSession;
import io.appium.java_client.windows.WindowsDriver;

/**
 * В поле имени файла вставляется <strong>полный путь</strong> к {@code .kdbx}
 * (Windows explorer/comdlg это поддерживает). Затем Enter и запасные действия.
 */
public class SaveDatabaseDialogPage {

	private static final int HWND_POLL_MS = 2500;

	private final WindowsDriver driver;

	public SaveDatabaseDialogPage(WindowsDriver driver) {
		this.driver = driver;
	}

	public void saveAs(String fullPath) throws InterruptedException {
		Thread.sleep(2000);
		pasteClipboardAndRobotConfirm(fullPath);
		Thread.sleep(600);
	}

	private void pasteClipboardAndRobotConfirm(String fullPath) throws InterruptedException {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fullPath), null);
		Thread.sleep(350);

		Robot robot = createRobot();
		robot.setAutoDelay(40);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(750);

		robotPressEnter(robot, 6, 320);
		Thread.sleep(900);
		robotPressEnter(robot, 4, 280);

		if (SaveDialogWinAppSession.tryClickSave(DriverFactory.WIN_APP_DRIVER_URL, HWND_POLL_MS)) {
			return;
		}

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(250);
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
