/*
 * TestConsoleRSAKeyPairGenerator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:10 CDT (-0500).
 *
 * Copyright 2010-2011 the original author or authors.
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
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.CliOptionsBuilder;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import net.nicholaswilliams.java.mock.StateFlag;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;

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

	private IMocksControl control;

	private ByteArrayOutputStream out;

	@Before
	public void setUp()
	{
		this.control = EasyMock.createStrictControl();

		this.generator = this.control.createMock(RSAKeyPairGeneratorInterface.class);
		this.device = this.control.createMock(TextInterfaceDevice.class);
		this.parser = this.control.createMock(CommandLineParser.class);

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser);

		this.out = new ByteArrayOutputStream();
	}

	@After
	public void tearDown()
	{
		this.control.verify();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProcessCommandLineOptions01() throws ParseException
	{
		final String[] arguments = new String[] {};

		Capture<Options> capturedOptions = new Capture<Options>();
		Capture<String[]> capturedArguments = new Capture<String[]>();

		PrintStream outStream = new PrintStream(this.out);
		PrintStream errStream = new PrintStream(this.out);

		EasyMock.expect(this.parser.parse(EasyMock.capture(capturedOptions), EasyMock.capture(capturedArguments), EasyMock.eq(true))).
				andThrow(new ParseException("message01"));
		EasyMock.expect(this.device.err()).andReturn(errStream);
		EasyMock.expect(this.device.out()).andReturn(outStream);
		this.device.exit(1);
		EasyMock.expectLastCall();
		this.control.replay();

		CommandLine returned = this.console.processCommandLineOptions(arguments);

		assertNull("The returned value should be null.", returned);

		assertSame("The arguments arrays should be the same object.", arguments, capturedArguments.getValue());

		Options options = capturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		Collection<Option> optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 1, optionCollection.size());
		assertEquals("The only option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));

		assertEquals("The output is not correct.",
					 "message01\n" +
						 "usage: ConsoleRSAKeyPairGenerator\n" +
						 " -help                 Display this help message\n" +
						 " -private <key file>   The name of the private key file to generate\n" +
						 " -public <key file>    The name of the public key file to generate\n",
					 new String(this.out.toByteArray()));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProcessCommandLineOptions02() throws ParseException
	{
		final String[] arguments = new String[] {};

		Capture<Options> capturedOptions = new Capture<Options>();
		Capture<String[]> capturedArguments = new Capture<String[]>();

		PrintStream outStream = new PrintStream(this.out);

		final CommandLine cli = this.control.createMock(CommandLine.class);

		EasyMock.expect(this.parser.parse(EasyMock.capture(capturedOptions), EasyMock.capture(capturedArguments),
										  EasyMock.eq(true))).
				andReturn(cli);
		EasyMock.expect(cli.hasOption("help")).andReturn(true);
		EasyMock.expect(this.device.out()).andReturn(outStream);
		this.device.exit(0);
		EasyMock.expectLastCall();
		this.control.replay();

		CommandLine returned = this.console.processCommandLineOptions(arguments);

		assertNull("The returned value should be null.", returned);

		assertSame("The arguments arrays should be the same object.", arguments, capturedArguments.getValue());

		Options options = capturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		Collection<Option> optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 1, optionCollection.size());
		assertEquals("The only option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));

		assertEquals("The output is not correct.",
					 "usage: ConsoleRSAKeyPairGenerator\n" +
						 " -help                 Display this help message\n" +
						 " -private <key file>   The name of the private key file to generate\n" +
						 " -public <key file>    The name of the public key file to generate\n",
					 new String(this.out.toByteArray()));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProcessCommandLineOptions03() throws ParseException
	{
		final String[] arguments = new String[] {};

		Capture<Options> firstCapturedOptions = new Capture<Options>();
		Capture<Options> secondCapturedOptions = new Capture<Options>();
		Capture<String[]> firstCapturedArguments = new Capture<String[]>();
		Capture<String[]> secondCapturedArguments = new Capture<String[]>();

		PrintStream outStream = new PrintStream(this.out);
		PrintStream errStream = new PrintStream(this.out);

		final CommandLine cli = this.control.createMock(CommandLine.class);

		EasyMock.expect(this.parser.parse(EasyMock.capture(firstCapturedOptions), EasyMock.capture(firstCapturedArguments), EasyMock.eq(true))).
				andReturn(cli);
		EasyMock.expect(cli.hasOption("help")).andReturn(false);
		EasyMock.expect(this.parser.parse(EasyMock.capture(secondCapturedOptions), EasyMock.capture(
				secondCapturedArguments))).
				andThrow(new ParseException("message02"));
		EasyMock.expect(this.device.err()).andReturn(errStream);
		EasyMock.expect(this.device.out()).andReturn(outStream);
		this.device.exit(1);
		EasyMock.expectLastCall();
		this.control.replay();

		CommandLine returned = this.console.processCommandLineOptions(arguments);

		assertNull("The returned value should be null.", returned);

		assertSame("The arguments arrays should be the same object (1).", arguments, firstCapturedArguments.getValue());
		assertSame("The arguments arrays should be the same object (2).", arguments, secondCapturedArguments.getValue());

		Options options = firstCapturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		Collection<Option> optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 1, optionCollection.size());
		assertEquals("The only option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));

		options = secondCapturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 3, optionCollection.size());
		assertEquals("The help option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));
		assertEquals("The privateKeyFile option is not correct.",
					 CliOptionsBuilder.get().withArgName("key file").withDescription("The name of the private key file to generate").isRequired(true).hasArg(true).create("private"),
					 options.getOption("private"));
		assertEquals("The publicKeyFile option is not correct.",
					 CliOptionsBuilder.get().withArgName("key file").withDescription("The name of the public key file to generate").isRequired(true).hasArg(true).create("public"),
					 options.getOption("public"));

		assertEquals("The output is not correct.",
					 "message02\n" +
						 "usage: ConsoleRSAKeyPairGenerator\n" +
						 " -help                 Display this help message\n" +
						 " -private <key file>   The name of the private key file to generate\n" +
						 " -public <key file>    The name of the public key file to generate\n",
					 new String(this.out.toByteArray()));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProcessCommandLineOptions04() throws ParseException
	{
		final String[] arguments = new String[] {};

		Capture<Options> firstCapturedOptions = new Capture<Options>();
		Capture<Options> secondCapturedOptions = new Capture<Options>();
		Capture<String[]> firstCapturedArguments = new Capture<String[]>();
		Capture<String[]> secondCapturedArguments = new Capture<String[]>();

		final CommandLine cli = this.control.createMock(CommandLine.class);

		EasyMock.expect(this.parser.parse(EasyMock.capture(firstCapturedOptions), EasyMock.capture(
				firstCapturedArguments), EasyMock.eq(true))).
				andReturn(cli);
		EasyMock.expect(cli.hasOption("help")).andReturn(false);
		EasyMock.expect(this.parser.parse(EasyMock.capture(secondCapturedOptions), EasyMock.capture(secondCapturedArguments))).
				andReturn(cli);
		EasyMock.expectLastCall();
		this.control.replay();

		CommandLine returned = this.console.processCommandLineOptions(arguments);

		assertNotNull("The returned value should not be null.", returned);
		assertSame("The returned value is not correct.", cli, returned);

		assertSame("The arguments arrays should be the same object (1).", arguments, firstCapturedArguments.getValue());
		assertSame("The arguments arrays should be the same object (2).", arguments, secondCapturedArguments.getValue());

		Options options = firstCapturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		Collection<Option> optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 1, optionCollection.size());
		assertEquals("The only option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));

		options = secondCapturedOptions.getValue();
		assertNotNull("The captured options are not correct.", options);
		optionCollection = options.getOptions();
		assertNotNull("The list of options should not be null.", optionCollection);
		assertEquals("The list of options should have one element.", 3, optionCollection.size());
		assertEquals("The help option is not correct.",
					 CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).create("help"),
					 options.getOption("help"));
		assertEquals("The privateKeyFile option is not correct.",
					 CliOptionsBuilder.get().withArgName("key file").withDescription("The name of the private key file to generate").isRequired(true).hasArg(true).create("private"),
					 options.getOption("private"));
		assertEquals("The publicKeyFile option is not correct.",
					 CliOptionsBuilder.get().withArgName("key file").withDescription("The name of the public key file to generate").isRequired(true).hasArg(true).create("public"),
					 options.getOption("public"));

		assertEquals("The output is not correct.", "", new String(this.out.toByteArray()));
	}

	@Test
	public void testGetValidPassword01()
	{
		char[] password1 = "testPassword1".toCharArray();
		char[] password2 = "testPassword1".toCharArray();
		char[] testBadPassword = "testPassword2".toCharArray();
		char[] empty = new char[password1.length];
		Arrays.fill(empty, '\u0000');

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(this.device.readPassword("Enter pass phrase for the key encryption: ")).
				andReturn("12345".toCharArray());
		EasyMock.expect(this.device.err()).andReturn(stream).times(2);
		EasyMock.expect(this.device.readPassword("Enter pass phrase for the key encryption: ")).
				andReturn("123456789012345678901234567890123".toCharArray());
		EasyMock.expect(this.device.err()).andReturn(stream).times(2);
		EasyMock.expect(this.device.readPassword("Enter pass phrase for the key encryption: ")).
				andReturn(password1);
		EasyMock.expect(this.device.readPassword("Verifying - Enter pass phrase for the key encryption: ")).
				andReturn(testBadPassword);
		EasyMock.expect(this.generator.passwordsMatch(password1, testBadPassword)).andReturn(false);
		EasyMock.expect(this.device.err()).andReturn(stream).times(2);
		EasyMock.expect(this.device.readPassword("Enter pass phrase for the key encryption: ")).
				andReturn(password1);
		EasyMock.expect(this.device.readPassword("Verifying - Enter pass phrase for the key encryption: ")).
				andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.control.replay();

		assertArrayEquals("The array value is not correct (1).", "testPassword1".toCharArray(), password1);
		assertArrayEquals("The array value is not correct (2).", "testPassword1".toCharArray(), password2);

		char[] returned = this.console.getValidPassword();

		assertArrayEquals("The returned value is not correct.", "testPassword1".toCharArray(), returned);

		assertArrayEquals("The new array value is not correct (1).", "testPassword1".toCharArray(), password1);
		assertArrayEquals("The new array value is not correct (2).", empty, password2);

		assertEquals("The output is not correct.",
					 "The password must be at least six characters and no more than 32 characters long.\n" +
						 "\n" +
						 "The password must be at least six characters and no more than 32 characters long.\n" +
						 "\n" +
						 "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.\n" +
						 "\n",
					 new String(this.out.toByteArray()));
	}

	@Test
	public void testGetValidPassword02()
	{
		char[] password1 = "testPassword1".toCharArray();
		char[] password2 = "testPassword1".toCharArray();
		char[] empty = new char[password1.length];
		Arrays.fill(empty, '\u0000');

		EasyMock.expect(this.device.readPassword("Enter pass phrase for the key encryption: ")).
				andReturn(password1);
		EasyMock.expect(this.device.readPassword("Verifying - Enter pass phrase for the key encryption: ")).
				andReturn(password2);
		EasyMock.expect(this.generator.passwordsMatch(password1, password2)).andReturn(true);
		this.control.replay();

		assertArrayEquals("The array value is not correct (1).", "testPassword1".toCharArray(), password1);
		assertArrayEquals("The array value is not correct (2).", "testPassword1".toCharArray(), password2);

		char[] returned = this.console.getValidPassword();

		assertArrayEquals("The returned value is not correct.", "testPassword1".toCharArray(), returned);

		assertArrayEquals("The new array value is not correct (1).", "testPassword1".toCharArray(), password1);
		assertArrayEquals("The new array value is not correct (2).", empty, password2);
	}

	@Test
	public void testCheckAndPromptToOverwriteFile01() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile01.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile02() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile02.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile03() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile03.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("   ");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile04() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile04.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("y");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile05() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile05.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("Y");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile06() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile06.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("yes");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile07() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile07.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("YeS");
		this.control.replay();

		assertTrue("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile08() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile08.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("n");
		this.control.replay();

		assertFalse("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile09() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile09.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		EasyMock.expect(this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", file.getCanonicalPath())).andReturn("no");
		this.control.replay();

		assertFalse("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));
	}

	@Test
	public void testCheckAndPromptToOverwriteFile10() throws IOException
	{
		String fileName = "testCheckAndPromptToOverwriteFile10.txt";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);
		FileUtils.forceDeleteOnExit(file);

		FileUtils.writeStringToFile(file, "data");

		assertTrue("The change should have succeeded.", file.setWritable(false));
		assertFalse("The writable flag should be false.", file.canWrite());

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(this.device.err()).andReturn(stream);
		this.control.replay();

		assertFalse("The method returned the wrong value.", this.console.checkAndPromptToOverwriteFile(fileName));

		assertEquals("The output is not correct.",
					 "The file " + file.getCanonicalPath() + " already exists and cannot be overwritten.\n",
					 new String(this.out.toByteArray()));
	}

	@Test
	public void testGenerateAndSaveKeyPair01() throws InterruptedException, IOException
	{
		PrintStream stream = new PrintStream(this.out);

		char[] password = "keyPassword1".toCharArray();

		PrivateKey privateKey = this.control.createMock(PrivateKey.class);
		PublicKey publicKey = this.control.createMock(PublicKey.class);
		final KeyPair keyPair = new KeyPair(publicKey, privateKey);

		EasyMock.expect(this.device.out()).andReturn(stream).times(2);
		EasyMock.expect(this.generator.generateKeyPair()).andAnswer(new IAnswer<KeyPair>()
		{
			@Override
			public KeyPair answer() throws Throwable
			{
				Thread.sleep(200);
				return keyPair;
			}
		});
		EasyMock.expect(this.device.out()).andReturn(stream).times(3);
		this.generator.saveKeyPairToFiles(
				keyPair, "testGenerateAndSaveKeyPair01a.key",  "testGenerateAndSaveKeyPair01b.key", password
		);
		EasyMock.expectLastCall().andAnswer(new IAnswer<Void>()
		{
			@Override
			public Void answer() throws Throwable
			{
				Thread.sleep(200);
				return null;
			}
		});
		EasyMock.expect(this.device.out()).andReturn(stream).times(3);
		this.control.replay();

		this.console.generateAndSaveKeyPair(
				password, "testGenerateAndSaveKeyPair01a.key", "testGenerateAndSaveKeyPair01b.key"
		);

		String output = new String(this.out.toByteArray());
		assertTrue("The output does not start correctly.",
				   output.startsWith("Passwords match. Generating RSA key pair, 2048-bit long modulus......"));
		assertTrue("The output does not have the correct middle content.",
				   output.contains("+++\n" +
								   "Key pair generated. Encrypting keys with 256-bit AES security......"));
		assertTrue("The output does not end correctly.",
				   output.endsWith("+++\n" +
								   "Private key written to testGenerateAndSaveKeyPair01a.key\n" +
								   "Public key written to testGenerateAndSaveKeyPair01b.key\n"));
	}

	@Test
	public void testRun01()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				assertFalse("checkAndPromptToOverwriteFile should only be called once.", checkAndPromptToOverwriteFile.state);
				checkAndPromptToOverwriteFile.state = true;
				assertEquals("The public file is not correct.", "publicKeyFileName01", f);
				return false;
			}
		};

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName01");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName01");
		this.device.exit(81);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile.state);
	}

	@Test
	public void testRun02()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				boolean returnValue = !checkAndPromptToOverwriteFile1.state || checkAndPromptToOverwriteFile2.state;
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName02", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName02", f);
				}
				return returnValue;
			}
		};

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName02");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName02");
		this.device.exit(81);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
	}

	@Test
	public void testRun03()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword03".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName03", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName03", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName03", v);
				assertEquals("The public file is not correct.", "publicKeyFileName03", u);

				throw new RSA2048NotSupportedException("message03:", new NoSuchAlgorithmException());
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName03");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName03");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(51);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "message03:\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun04()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword04".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName04", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName04", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName04", v);
				assertEquals("The public file is not correct.", "publicKeyFileName04", u);

				throw new RSA2048NotSupportedException("message04:");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName04");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName04");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(52);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "message04:\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun05()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword05".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName05", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName05", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName05", v);
				assertEquals("The public file is not correct.", "publicKeyFileName05", u);

				throw new AlgorithmNotSupportedException("message05");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		this.control.reset();
		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName05");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName05");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(41);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "The algorithm \"message05\" is not supported on this system. Contact your system administrator for assistance.\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun06()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword06".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName06", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName06", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName06", v);
				assertEquals("The public file is not correct.", "publicKeyFileName06", u);

				throw new InappropriateKeyException("message06:");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName06");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName06");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(42);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "message06: Contact your system administrator for assistance.\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun07()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword07".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName07", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName07", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName07", v);
				assertEquals("The public file is not correct.", "publicKeyFileName07", u);

				throw new InappropriateKeySpecificationException("message07:");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName07");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName07");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(43);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "message07: Contact your system administrator for assistance.\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun08()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword08".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName08", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName08", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName08", v);
				assertEquals("The public file is not correct.", "publicKeyFileName08", u);

				throw new InterruptedException("message08:");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName08");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName08");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(44);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "The system was interrupted while waiting for events to complete.\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun09()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword09".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName09", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName09", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName09", v);
				assertEquals("The public file is not correct.", "publicKeyFileName09", u);

				throw new IOException("message09");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName09");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName09");
		EasyMock.expect(this.device.err()).andReturn(stream).times(2);
		this.device.exit(21);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.",
					 "An error occurred writing the key files to the file system. Analyze the error below to determine " +
					 "what went wrong and fix it!\njava.io.IOException: message09\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun10()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword10".toCharArray();

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName10", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName10", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName10", v);
				assertEquals("The public file is not correct.", "publicKeyFileName10", u);

				throw new RuntimeException("message10");
			}
		};

		PrintStream stream = new PrintStream(this.out);

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName10");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName10");
		EasyMock.expect(this.device.err()).andReturn(stream);
		this.device.exit(-1);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertEquals("The output is not correct.", "java.lang.RuntimeException: message10\n", new String(this.out.toByteArray()));
	}

	@Test
	public void testRun11()
	{
		final StateFlag processCommandLineOptions = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile1 = new StateFlag();
		final StateFlag checkAndPromptToOverwriteFile2 = new StateFlag();
		final StateFlag getValidPassword = new StateFlag();
		final StateFlag generateAndSaveKeyPair = new StateFlag();

		final CommandLine commandLine = this.control.createMock(CommandLine.class);

		final String[] arguments = new String[] {"foo", "bar"};

		final char[] password = "runPassword11".toCharArray();

		char[] empty = new char[password.length];
		Arrays.fill(empty, '\u0000');

		this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, this.parser) {
			@Override
			protected CommandLine processCommandLineOptions(String[] a)
			{
				assertFalse("processCommandLineOptions should only be called once.", processCommandLineOptions.state);
				processCommandLineOptions.state = true;
				assertSame("The arguments are not correct.", arguments, a);
				return commandLine;
			}

			@Override
			protected boolean checkAndPromptToOverwriteFile(String f)
			{
				if(checkAndPromptToOverwriteFile1.state)
				{
					checkAndPromptToOverwriteFile2.state = true;
					assertEquals("The private file is not correct.", "privateKeyFileName11", f);
				}
				else
				{
					checkAndPromptToOverwriteFile1.state = true;
					assertEquals("The public file is not correct.", "publicKeyFileName11", f);
				}
				return true;
			}

			@Override
			protected char[] getValidPassword()
			{
				assertFalse("getValidPassword should only be called once.", getValidPassword.state);
				getValidPassword.state = true;
				return password;
			}

			@Override
			protected void generateAndSaveKeyPair(char[] p, String v, String u)
					throws InterruptedException, IOException
			{
				assertFalse("generateAndSaveKeyPair should only be called once.", generateAndSaveKeyPair.state);
				generateAndSaveKeyPair.state = true;
				assertArrayEquals("The password is not correct.", password, p);
				assertEquals("The private file is not correct.", "privateKeyFileName11", v);
				assertEquals("The public file is not correct.", "publicKeyFileName11", u);
			}
		};

		EasyMock.expect(commandLine.getOptionValue("private")).andReturn("privateKeyFileName11");
		EasyMock.expect(commandLine.getOptionValue("public")).andReturn("publicKeyFileName11");
		this.device.exit(0);
		EasyMock.expectLastCall();
		this.control.replay();

		this.console.run(arguments);

		assertTrue("processCommandLineOptions should have been called.", processCommandLineOptions.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called.", checkAndPromptToOverwriteFile1.state);
		assertTrue("checkAndPromptToOverwriteFile should have been called twice.", checkAndPromptToOverwriteFile2.state);
		assertTrue("getValidPassword should have been called.", getValidPassword.state);
		assertTrue("generateAndSaveKeyPair should have been called.", generateAndSaveKeyPair.state);

		assertArrayEquals("The password should have been erased.", empty, password);
	}
}