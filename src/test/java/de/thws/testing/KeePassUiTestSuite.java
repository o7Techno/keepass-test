package de.thws.testing;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import de.thws.testing.tests.entry.EntryTabTest;
import de.thws.testing.tests.generator.AdvancedPasswordTest;
import de.thws.testing.tests.generator.BasicPasswordTest;
import de.thws.testing.tests.generator.PassphraseTest;

@Suite
@SelectClasses({ BasicPasswordTest.class, AdvancedPasswordTest.class, PassphraseTest.class, EntryTabTest.class,
		UnlockTest.class, DatabaseCreationTest.class })
public class KeePassUiTestSuite {
}
