package de.thws.testing.utils;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.windows.WindowsDriver;

public class DriverFactory {

	@SuppressWarnings("rawtypes")
	public static WindowsDriver createKeePassDriver() {
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();

			// Gizli eklemeler yok, saf ve temiz ayarlar
			capabilities.setCapability("app", "C:\\Program Files\\KeePassXC\\KeePassXC.exe");
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("deviceName", "WindowsPC");

			WindowsDriver driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);

			// Selenium 3 tarzı bekleme kodu
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			return driver;
		} catch (Exception e) {
			throw new RuntimeException("Driver başlatılamadı: " + e.getMessage());
		}
	}
}