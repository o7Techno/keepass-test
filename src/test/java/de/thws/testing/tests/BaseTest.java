package de.thws.testing.tests;

import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import de.thws.testing.support.SuiteDatabaseSupport;
import io.appium.java_client.windows.WindowsDriver;

public abstract class BaseTest {

	protected WindowsDriver driver;

	@BeforeAll
	public static void globalSetup() throws Exception {
		SuiteDatabaseSupport.prepareFreshSuiteDatabase();
	}

	protected void sleepMs(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@AfterEach
	public void commonTearDown() {
		if (driver != null) {

			try {
				new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE)
						.pause(200).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
			} catch (Exception ignored) {
			}

			try {
				driver.quit();
			} catch (Exception ignored) {
			}
		}

		try {
			Runtime.getRuntime().exec("taskkill /F /IM KeePassXC.exe").waitFor();
		} catch (Exception ignored) {
		}

		try {
			Files.deleteIfExists(SuiteDatabaseSupport.suiteDatabasePath().resolveSibling("suite.kdbx.lock"));
		} catch (Exception ignored) {
		}
	}
}