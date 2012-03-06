/*
 * TestConsoleRSAKeyPairGenerator.java from LicenseManager modified Monday, March 5, 2012 18:50:38 CST (-0600).
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
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.RSA2048NotSupportedException;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleRSAKeyPairGenerator.
 */
public class TestConsoleRSAKeyPairGenerator
{
	private static final String LF = System.getProperty("line.separator");

	private ConsoleRSAKeyPairGenerator console;

	private RSAKeyPairGeneratorInterface generator;

	private TextInterfaceDevice device;

	@Before
	public void setUp()
	{
		this.generator = EasyMock.createMock(RSAKeyPairGeneratorInterface.class);
		this.device = EasyMock.createMock(TextInterfaceDevice.class);

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, new GnuParser()) {
			@Override
			protected void finalize()
			{
				
			}
		};
	}

	@After
	public void tearDown()
	{
		EasyMock.verify(this.generator, this.device);
	}

	@Test
	public void testProcessCommandLineOptions01() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(0);
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		this.console.processCommandLineOptions(new String[] { "-help" });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleRSAKeyPairGenerator -help" + LF +
					 "        ConsoleRSAKeyPairGenerator -interactive" + LF +
					 "        ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> -public <file|class" + LF +
					 "        name> [-privatePassword <password>] [-classes -passwordClass <class name> -privatePasswordClass" + LF +
					 "        <class name> [-privatePackage <package>] [-publicPackage <package>] [-passwordPackage <package>]" + LF +
					 "        [-privatePasswordPackage <package>]]" + LF +
					 " -classes                             Specify to generate compilable Java classes instead of key files" + LF +
					 " -help                                Display this help message" + LF +
					 " -interactive                         Specify to use interactive mode and ignore command-line options" + LF +
					 " -password <password>                 The password to use to encrypt the public and private keys" + LF +
					 "                                      (required unless in interactive mode)" + LF +
					 " -passwordClass <class name>          The name of the password storage class to generate (optional," + LF +
					 "                                      ignored unless generating classes)" + LF +
					 " -passwordPackage <package>           The name of the package to use for the password storage class" + LF +
					 "                                      (optional, ignored unless generating classes)" + LF +
					 " -private <file|class name>           The name of the private key file or class to generate (required" + LF +
					 "                                      unless in interactive mode)" + LF +
					 " -privatePackage <package>            The name of the package to use for the private key class (optional," + LF +
					 "                                      ignored unless generating classes)" + LF +
					 " -privatePassword <password>          A different password to use to encrypt the private key (optional)" + LF +
					 " -privatePasswordClass <class name>   The name of the private key password storage class to generate" + LF +
					 "                                      (optional, ignored unless generating classes)" + LF +
					 " -privatePasswordPackage <package>    The name of the package to use for the private key password storage" + LF +
					 "                                      class (optional, ignored unless generating classes)" + LF +
					 " -public <file|class name>            The name of the public key file or class to generate (required" + LF +
					 "                                      unless in interactive mode)" + LF +
					 " -publicPackage <package>             The name of the package to use for the public key class (optional," + LF +
					 "                                      ignored unless generating classes)" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions02() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		this.device.printErrLn("Missing required options: private, public, password");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		this.console.processCommandLineOptions(new String[] { });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleRSAKeyPairGenerator -help" + LF +
					 "        ConsoleRSAKeyPairGenerator -interactive" + LF +
					 "        ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> -public <file|class" +
					 LF +
					 "        name> [-privatePassword <password>] [-classes -passwordClass <class name> -privatePasswordClass" +
					 LF +
					 "        <class name> [-privatePackage <package>] [-publicPackage <package>] [-passwordPackage <package>]" +
					 LF +
					 "        [-privatePasswordPackage <package>]]" + LF +
					 " -classes                             Specify to generate compilable Java classes instead of key files" +
					 LF +
					 " -help                                Display this help message" + LF +
					 " -interactive                         Specify to use interactive mode and ignore command-line options" +
					 LF +
					 " -password <password>                 The password to use to encrypt the public and private keys" +
					 LF +
					 "                                      (required unless in interactive mode)" + LF +
					 " -passwordClass <class name>          The name of the password storage class to generate (optional," +
					 LF +
					 "                                      ignored unless generating classes)" + LF +
					 " -passwordPackage <package>           The name of the package to use for the password storage class" +
					 LF +
					 "                                      (optional, ignored unless generating classes)" + LF +
					 " -private <file|class name>           The name of the private key file or class to generate (required" +
					 LF +
					 "                                      unless in interactive mode)" + LF +
					 " -privatePackage <package>            The name of the package to use for the private key class (optional," +
					 LF +
					 "                                      ignored unless generating classes)" + LF +
					 " -privatePassword <password>          A different password to use to encrypt the private key (optional)" +
					 LF +
					 " -privatePasswordClass <class name>   The name of the private key password storage class to generate" +
					 LF +
					 "                                      (optional, ignored unless generating classes)" + LF +
					 " -privatePasswordPackage <package>    The name of the package to use for the private key password storage" +
					 LF +
					 "                                      class (optional, ignored unless generating classes)" + LF +
					 " -public <file|class name>            The name of the public key file or class to generate (required" +
					 LF +
					 "                                      unless in interactive mode)" + LF +
					 " -publicPackage <package>             The name of the package to use for the public key class (optional," +
					 LF +
					 "                                      ignored unless generating classes)" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions03() throws ParseException
	{
		EasyMock.replay(this.generator, this.device);

		this.console.processCommandLineOptions(new String[] { "-interactive" });

		assertNull("There should be no cli value.", this.console.cli);
		assertTrue("The interactive flag should be true.", this.console.interactive);
	}

	@Test
	public void testProcessCommandLineOptions04() throws ParseException
	{
		EasyMock.replay(this.generator, this.device);

		this.console.processCommandLineOptions(new String[] { "-private", "private.key", "-public", "public.key", "-password", "myPassword01" });

		assertNotNull("There should be a cli value.", this.console.cli);
		assertFalse("The interactive flag should be false.", this.console.interactive);
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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

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

		EasyMock.replay(this.generator, this.device);

		char[] password = this.console.promptForValidPassword("both keys");

		assertArrayEquals("The password is not correct.", "finalTest04".toCharArray(), password);
		assertNotSame("The arrays should be different objects (1).", password1, password);
		assertNotSame("The arrays should be different objects (2).", password2, password);
		assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
		assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));
	}

	@Test
	public void testPromptForString01()
	{
		EasyMock.expect(this.device.readLine("Test message 01")).andReturn(null);
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		assertNull("The returned value should be null.", this.console.promptForString("Test message 01"));
	}

	@Test
	public void testPromptForString02()
	{
		EasyMock.expect(this.device.readLine("Another message 02")).andReturn("");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		assertNull("The returned value should be null.", this.console.promptForString("Another message 02"));
	}

	@Test
	public void testPromptForString03()
	{
		EasyMock.expect(this.device.readLine("Another message 03")).andReturn("     ");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		assertNull("The returned value should be null.", this.console.promptForString("Another message 03"));
	}

	@Test
	public void testPromptForString04()
	{
		EasyMock.expect(this.device.readLine("Final message 04")).andReturn("Returned console value");
		this.device.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(this.generator, this.device);

		assertEquals("The returned value should be null.", "Returned console value",
					 this.console.promptForString("Final message 04"));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile01()
	{
		EasyMock.replay(this.generator, this.device);

		assertTrue("The value returned should be true.",
				   this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile01"));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile02() throws IOException
	{
		File file = new File("testCheckAndPromptToOverwriteFile02");
		FileUtils.writeStringToFile(file, "test string");

		try
		{
			EasyMock.expect(this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ",
												 file.getCanonicalPath())).andReturn("y");

			EasyMock.replay(this.generator, this.device);

			assertTrue("The value returned should be true.",
					   this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile02"));
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	@Test
	public void testCheckAndPromptToOverwriteFile03() throws IOException
	{
		File file = new File("testCheckAndPromptToOverwriteFile03");
		FileUtils.writeStringToFile(file, "test string");

		try
		{
			EasyMock.expect(this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ",
												 file.getCanonicalPath())).andReturn("Y");

			EasyMock.replay(this.generator, this.device);

			assertTrue("The value returned should be true.",
					   this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile03"));
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	@Test
	public void testCheckAndPromptToOverwriteFile04() throws IOException
	{
		File file = new File("testCheckAndPromptToOverwriteFile04");
		FileUtils.writeStringToFile(file, "test string");

		try
		{
			EasyMock.expect(this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ",
												 file.getCanonicalPath())).andReturn("yes");

			EasyMock.replay(this.generator, this.device);

			assertTrue("The value returned should be true.",
					   this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile04"));
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	@Test
	public void testCheckAndPromptToOverwriteFile05() throws IOException
	{
		File file = new File("testCheckAndPromptToOverwriteFile05");
		FileUtils.writeStringToFile(file, "test string");

		try
		{
			EasyMock.expect(this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ",
												 file.getCanonicalPath())).andReturn("");

			EasyMock.replay(this.generator, this.device);

			assertTrue("The value returned should be true.",
					   this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile05"));
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	@Test
	public void testCheckAndPromptToOverwriteFile06() throws IOException
	{
		File file = new File("testCheckAndPromptToOverwriteFile06");
		FileUtils.writeStringToFile(file, "test string");

		try
		{
			EasyMock.expect(this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ",
												 file.getCanonicalPath())).andReturn("no");

			EasyMock.replay(this.generator, this.device);

			assertFalse("The value returned should be true.",
						this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile06"));
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	// TODO: Test do interactive

	// TODO: Test do command line

	@Test
	public void testRun01() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-interactive" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doInteractive();
		EasyMock.expectLastCall();
		this.device.exit(0);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.interactive = true;
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun02() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall();
		this.device.exit(0);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun03() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new RSA2048NotSupportedException("Message 03.",
																			new NoSuchAlgorithmException()));
		this.device.printErrLn("Message 03.");
		EasyMock.expectLastCall();
		this.device.exit(51);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun04() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new AlgorithmNotSupportedException("Message 04."));
		this.device.printErrLn("The algorithm \"Message 04.\" is not supported on this system. Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(41);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun05() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new InappropriateKeyException("Message 05."));
		this.device.printErrLn("Message 05. Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(42);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun06() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new InappropriateKeySpecificationException("Message 06."));
		this.device.printErrLn("Message 06. Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(43);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun07() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new InterruptedException("Message 07."));
		this.device.printErrLn("The system was interrupted while waiting for events to complete.");
		EasyMock.expectLastCall();
		this.device.exit(44);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun08() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new IOException("Message 08."));
		this.device.printErrLn(
				"An error occurred writing the key files to the file system. Analyze the error below to determine what went wrong and fix it!");
		EasyMock.expectLastCall();
		this.device.printErrLn("java.io.IOException: Message 08.");
		EasyMock.expectLastCall();
		this.device.exit(21);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	@Test
	public void testRun09() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
				withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.generator, this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions", String[].class).
				addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

		String[] arguments = new String[] { "-private" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.doCommandLine();
		EasyMock.expectLastCall().andThrow(new Exception("Message 09."));
		this.device.printErrLn("java.lang.Exception: Message 09.");
		EasyMock.expectLastCall();
		this.device.exit(-1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.console, this.generator, this.device);

		try
		{
			this.console.run(arguments);
		}
		finally
		{
			EasyMock.verify(this.console);
		}
	}

	private static class ThisExceptionMeansTestSucceededException extends SecurityException
	{

	}

	@Test(expected=ThisExceptionMeansTestSucceededException.class)
	public void testMain01()
	{
		SecurityManager securityManager = new MockPermissiveSecurityManager() {
			private boolean active = true;

			@Override
			public void checkExit(int status)
			{
				if(this.active)
				{
					this.active = false;
					assertEquals("The exit status is not correct.", 0, status);
					throw new ThisExceptionMeansTestSucceededException();
				}
			}
		};

		EasyMock.replay(this.generator, this.device);

		System.setSecurityManager(securityManager);

		ConsoleRSAKeyPairGenerator.main(new String[] { "-help" });

		System.setSecurityManager(null);
	}

	@Test(expected=ThisExceptionMeansTestSucceededException.class)
	public void testMain02()
	{
		SecurityManager securityManager = new MockPermissiveSecurityManager() {
			private boolean active = true;

			@Override
			public void checkExit(int status)
			{
				if(this.active)
				{
					this.active = false;
					assertEquals("The exit status is not correct.", 1, status);
					throw new ThisExceptionMeansTestSucceededException();
				}
			}
		};

		EasyMock.replay(this.generator, this.device);

		System.setSecurityManager(securityManager);

		ConsoleRSAKeyPairGenerator.main(new String[] { "-private" });

		System.setSecurityManager(null);
	}
}

class MockPermissiveSecurityManager extends SecurityManager
{
	@Override
	public void checkPermission(Permission permission)
	{
	}

	@Override
	public void checkPermission(Permission permission, Object o)
	{
	}

	@Override
	public void checkCreateClassLoader()
	{
	}

	@Override
	public void checkAccess(Thread thread)
	{
	}

	@Override
	public void checkAccess(ThreadGroup threadGroup)
	{
	}

	@Override
	public void checkExit(int i)
	{
	}

	@Override
	public void checkExec(String s)
	{
	}

	@Override
	public void checkLink(String s)
	{
	}

	@Override
	public void checkRead(FileDescriptor fileDescriptor)
	{
	}

	@Override
	public void checkRead(String s)
	{
	}

	@Override
	public void checkRead(String s, Object o)
	{
	}

	@Override
	public void checkWrite(FileDescriptor fileDescriptor)
	{
	}

	@Override
	public void checkWrite(String s)
	{
	}

	@Override
	public void checkDelete(String s)
	{
	}

	@Override
	public void checkConnect(String s, int i)
	{
	}

	@Override
	public void checkConnect(String s, int i, Object o)
	{
	}

	@Override
	public void checkListen(int i)
	{
	}

	@Override
	public void checkAccept(String s, int i)
	{
	}

	@Override
	public void checkMulticast(InetAddress inetAddress)
	{
	}

	@Override
	public void checkPropertiesAccess()
	{
	}

	@Override
	public void checkPropertyAccess(String s)
	{
	}

	@Override
	public boolean checkTopLevelWindow(Object o)
	{
		return true;
	}

	@Override
	public void checkPrintJobAccess()
	{
	}

	@Override
	public void checkSystemClipboardAccess()
	{
	}

	@Override
	public void checkAwtEventQueueAccess()
	{
	}

	@Override
	public void checkPackageAccess(String s)
	{
	}

	@Override
	public void checkPackageDefinition(String s)
	{
	}

	@Override
	public void checkSetFactory()
	{
	}

	@Override
	public void checkMemberAccess(Class<?> aClass, int i)
	{
	}

	@Override
	public void checkSecurityAccess(String s)
	{
	}
}