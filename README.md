# KeePassXC UI Test Showcase

Automated UI tests for KeePassXC on Windows using Java, JUnit 5, WinAppDriver, and Appium.

## Important Scenarios Covered

- Password generator tabs (basic, advanced, passphrase): generation behavior and option handling.
- Entry form fields (title, username, password, URL, notes, expiry): persistence after save and reopen.
- Database unlock window (password, close): access with the correct password.
- Cancel/unsaved behavior and reopen stability checks.

## Requirements

- Windows 10/11
- Java 17+
- Maven 3.9+
- WinAppDriver running on `http://127.0.0.1:4723`
- KeePassXC installed (default path used by the tests)

## Run

Primary run command (works out of the box in this repository):

```powershell
mvn test
```

Run selected methods in sequence:

```powershell
mvn test "-Dtest=de.thws.testing.tests.entry.EntryTabTest#test11_expiry_presets_menu_stillOnAfterSave+test12_notes_multiline_persistAfterSave"
```

Run via helper scripts in `.\scripts`:

```powershell
.\scripts\run-winappdriver
.\scripts\run-tests
```

Or run everything in one step:

```powershell
.\scripts\run-all
```