package de.thws.testing.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public final class SecureFieldInput {

	private SecureFieldInput() {
	}

	public static void pasteReplacing(WebElement field, String text) throws InterruptedException {
		field.click();
		Thread.sleep(180);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
		Thread.sleep(80);
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		Thread.sleep(40);
		field.sendKeys(Keys.chord(Keys.CONTROL, "v"));
		Thread.sleep(120);
	}
}
