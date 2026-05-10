package de.thws.testing;

import org.junit.platform.suite.api.SelectClasses;

import de.thws.testing.tests.entry.EntryTabTest;
import de.thws.testing.tests.generator.AdvancedPasswordTest;
import de.thws.testing.tests.generator.BasicPasswordTest;
import de.thws.testing.tests.generator.PassphraseTest;
import de.thws.testing.tests.unlock.UnlockTest;
import de.thws.testing.tests.wizard.DatabaseCreationTest;

@SelectClasses({ DatabaseCreationTest.class, UnlockTest.class, BasicPasswordTest.class, AdvancedPasswordTest.class,
		PassphraseTest.class, EntryTabTest.class })
public class KeePassUiTestSuite {
}