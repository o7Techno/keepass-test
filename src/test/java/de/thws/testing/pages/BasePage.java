package de.thws.testing.pages;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import io.appium.java_client.windows.WindowsDriver;

public abstract class BasePage {

	protected final WindowsDriver driver;

	public BasePage(WindowsDriver driver) {
		this.driver = driver;
	}

	protected void sleepMs(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected WebElement firstOrNull(List<WebElement> elements) {
		return elements.isEmpty() ? null : elements.get(0);
	}

	protected void forceClick(WebElement btn) {
		RuntimeException last = null;
		try {
			btn.click();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			new Actions(driver).moveToElement(btn).pause(50).click().perform();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			new Actions(driver).moveToElement(btn).pause(40).sendKeys(Keys.SPACE).perform();
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			btn.sendKeys(Keys.SPACE);
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		try {
			btn.sendKeys(Keys.ENTER);
			return;
		} catch (RuntimeException e) {
			last = e;
		}
		throw new IllegalStateException("Element found but could not be invoked (WinAppDriver/Qt).", last);
	}
}