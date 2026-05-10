package de.thws.testing.support;

import java.util.Locale;

public final class PlatformSupport {

	private PlatformSupport() {
	}

	public static boolean isWindows() {
		return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("windows");
	}
}
