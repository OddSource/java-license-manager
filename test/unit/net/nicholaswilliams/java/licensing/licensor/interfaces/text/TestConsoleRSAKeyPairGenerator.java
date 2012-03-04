/*
 * TestConsoleRSAKeyPairGenerator.java from LicenseManager modified Sunday, March 4, 2012 13:09:09 CST (-0600).
 *
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGeneratorInterface;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import org.apache.commons.cli.CommandLineParser;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleRSAKeyPairGenerator.
 */
public class TestConsoleRSAKeyPairGenerator
{
	private ConsoleRSAKeyPairGenerator console;

	private RSAKeyPairGeneratorInterface generator;

	private TextInterfaceDevice device;

	private CommandLineParser parser;

	@Before
	public void setUp()
	{
		this.generator = EasyMock.createMock(RSAKeyPairGeneratorInterface.class);
		this.device = EasyMock.createMock(TextInterfaceDevice.class);
		this.parser = EasyMock.createMock(CommandLineParser.class);

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser);
	}

	@After
	public void tearDown()
	{
		EasyMock.verify(this.generator, this.device, this.parser);
	}

	@Test
	public void testPromptToGenerateClasses01()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Save the public and private keys to .key files?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(null);
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
	}

	@Test
	public void testPromptToGenerateClasses02()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Save the public and private keys to .key files?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
	}

	@Test
	public void testPromptToGenerateClasses03()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Save the public and private keys to .key files?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
	}

	@Test
	public void testPromptToGenerateClasses04()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Save the public and private keys to .key files?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertTrue("The return value should be true.", this.console.promptToGenerateClasses());
	}

	@Test
	public void testPromptToUseDifferentPasswords01()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Use a different password for each key?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(null);
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
	}

	@Test
	public void testPromptToUseDifferentPasswords02()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Use a different password for each key?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
	}

	@Test
	public void testPromptToUseDifferentPasswords03()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Use a different password for each key?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
	}

	@Test
	public void testPromptToUseDifferentPasswords04()
	{
		this.device.printOutLn("Would you like to...");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
		EasyMock.expectLastCall();
		this.device.printOutLn("    (2) Use a different password for each key?");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		assertTrue("The return value should be true.", this.console.promptToUseDifferentPasswords());
	}

	@Test
	public void testPromptForValidPassword01()
	{
		char[] password1 = "testPassword01".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);

		char[] password2 = "testPassword01".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt both keys: ")).andReturn(password2);

		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.device.printOutLn("Passwords match.");
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		char[] password = this.console.promptForValidPassword("both keys");

		assertArrayEquals("The password is not correct.", "testPassword01".toCharArray(), password);
		assertNotSame("The arrays should be different objects (1).", password1, password);
		assertNotSame("The arrays should be different objects (2).", password2, password);
		assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
		assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));
	}

	@Test
	public void testPromptForValidPassword02()
	{
		char[] password1 = "test2".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);
		this.device.printErrLn("The password must be at least six characters and no more than 32 characters long.");
		EasyMock.expectLastCall();
		this.device.printErrLn();
		EasyMock.expectLastCall();
		password1 = "123456789012345678901234567890123".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);
		this.device.printErrLn("The password must be at least six characters and no more than 32 characters long.");
		EasyMock.expectLastCall();
		this.device.printErrLn();
		EasyMock.expectLastCall();
		password1 = "12345678901234567890123456789012".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);

		char[] password2 = "testPassword01".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt the private key: ")).andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(false);
		this.device.printErrLn("ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.");
		EasyMock.expectLastCall();
		this.device.printErrLn();
		EasyMock.expectLastCall();

		password1 = "test02".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);

		password2 = "test02".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt the private key: ")).andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.device.printOutLn("Passwords match.");
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		char[] password = this.console.promptForValidPassword("the private key");

		assertArrayEquals("The password is not correct.", "test02".toCharArray(), password);
		assertNotSame("The arrays should be different objects (1).", password1, password);
		assertNotSame("The arrays should be different objects (2).", password2, password);
		assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
		assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));
	}

	@Test
	public void testPromptForValidPassword03()
	{
		char[] password1 = "another03".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the public key: ")).andReturn(password1);

		char[] password2 = "testPassword01".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt the public key: ")).andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(false);
		this.device.printErrLn("ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.");
		EasyMock.expectLastCall();
		this.device.printErrLn();
		EasyMock.expectLastCall();

		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt the public key: ")).andReturn(password1);

		password2 = "another03".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt the public key: ")).andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.device.printOutLn("Passwords match.");
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		char[] password = this.console.promptForValidPassword("the public key");

		assertArrayEquals("The password is not correct.", "another03".toCharArray(), password);
		assertNotSame("The arrays should be different objects (1).", password1, password);
		assertNotSame("The arrays should be different objects (2).", password2, password);
		assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
		assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));
	}

	@Test
	public void testPromptForValidPassword04()
	{
		char[] password1 = "test".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);
		this.device.printErrLn("The password must be at least six characters and no more than 32 characters long.");
		EasyMock.expectLastCall();
		this.device.printErrLn();
		EasyMock.expectLastCall();
		password1 = "finalTest04".toCharArray();
		EasyMock.expect(this.device.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);

		char[] password2 = "finalTest04".toCharArray();
		EasyMock.expect(this.device.readPassword("Verifying - Reenter pass phrase to encrypt both keys: ")).andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.device.printOutLn("Passwords match.");
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device, this.parser);

		char[] password = this.console.promptForValidPassword("both keys");

		assertArrayEquals("The password is not correct.", "finalTest04".toCharArray(), password);
		assertNotSame("The arrays should be different objects (1).", password1, password);
		assertNotSame("The arrays should be different objects (2).", password2, password);
		assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
		assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));
	}
}