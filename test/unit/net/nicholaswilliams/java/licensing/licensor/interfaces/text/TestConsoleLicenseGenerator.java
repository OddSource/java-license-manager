/*
 * TestConsoleLicenseGenerator.java from LicenseManager modified Saturday, May 19, 2012 22:03:29 CDT (-0500).
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

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.exception.ObjectSerializationException;
import net.nicholaswilliams.java.licensing.licensor.FilePrivateKeyDataProvider;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreator;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreatorProperties;
import net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import net.nicholaswilliams.java.licensing.samples.SampleFilePrivateKeyDataProvider;
import net.nicholaswilliams.java.licensing.samples.SamplePasswordProvider;
import net.nicholaswilliams.java.mock.MockPermissiveSecurityManager;
import org.apache.commons.cli.CommandLine;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleLicenseGenerator.
 */
public class TestConsoleLicenseGenerator
{
	private static final String LF = System.getProperty("line.separator");

	private ConsoleLicenseGenerator console;

	private TextInterfaceDevice device;

	@Before
	public void setUp()
	{
		this.device = EasyMock.createMock(TextInterfaceDevice.class);

		this.console = new ConsoleLicenseGenerator(this.device, new GnuParser()) {
			@Override
			protected void finalize()
			{

			}
		};
	}

	@After
	public void tearDown()
	{
		EasyMock.verify(this.device);
	}

	@Test
	public void testProcessCommandLineOptions01() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(0);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-help" });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleLicenseGenerator -help" + LF +
					 "        ConsoleLicenseGenerator" + LF +
					 "        ConsoleLicenseGenerator -config <file>" + LF +
					 "        ConsoleLicenseGenerator -license <file>" + LF +
					 "        ConsoleLicenseGenerator -config <file> -license <file>" + LF +
					 "        The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of" + LF +
					 "        them, or neither. The \"config\" properties file contains information necessary to generate all" + LF +
					 "        licenses (key paths, passwords, etc.) and generally will not need to change. The \"license\"" + LF +
					 "        properties file contains all of the information you need to generate this particular license. See" + LF +
					 "        the Javadoc API documentation for information about the contents of these two files." + LF +
					 "        If you do not specify the \"config\" properties file, you will be prompted to provide the values" + LF +
					 "        that were expected in that file. Likewise, if you do not specify the \"license\" properties file," + LF +
					 "        you will be prompted to provide the values that were expected in that file." + LF +
					 " -config <file>    Specify the .properties file that configures this generator" + LF +
					 " -help             Display this help message" + LF +
					 " -license <file>   Specify the .properties file that contains the data for this license" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions02() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		this.device.printErrLn("Unrecognized option: -badOption");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-badOption" });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleLicenseGenerator -help" + LF +
					 "        ConsoleLicenseGenerator" + LF +
					 "        ConsoleLicenseGenerator -config <file>" + LF +
					 "        ConsoleLicenseGenerator -license <file>" + LF +
					 "        ConsoleLicenseGenerator -config <file> -license <file>" + LF +
					 "        The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of" + LF +
					 "        them, or neither. The \"config\" properties file contains information necessary to generate all" + LF +
					 "        licenses (key paths, passwords, etc.) and generally will not need to change. The \"license\"" + LF +
					 "        properties file contains all of the information you need to generate this particular license. See" + LF +
					 "        the Javadoc API documentation for information about the contents of these two files." + LF +
					 "        If you do not specify the \"config\" properties file, you will be prompted to provide the values" + LF +
					 "        that were expected in that file. Likewise, if you do not specify the \"license\" properties file," + LF +
					 "        you will be prompted to provide the values that were expected in that file." + LF +
					 " -config <file>    Specify the .properties file that configures this generator" + LF +
					 " -help             Display this help message" + LF +
					 " -license <file>   Specify the .properties file that contains the data for this license" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions03() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		this.device.printErrLn("Missing argument for option: config");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-config" });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleLicenseGenerator -help" + LF +
					 "        ConsoleLicenseGenerator" + LF +
					 "        ConsoleLicenseGenerator -config <file>" + LF +
					 "        ConsoleLicenseGenerator -license <file>" + LF +
					 "        ConsoleLicenseGenerator -config <file> -license <file>" + LF +
					 "        The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of" + LF +
					 "        them, or neither. The \"config\" properties file contains information necessary to generate all" + LF +
					 "        licenses (key paths, passwords, etc.) and generally will not need to change. The \"license\"" + LF +
					 "        properties file contains all of the information you need to generate this particular license. See" + LF +
					 "        the Javadoc API documentation for information about the contents of these two files." + LF +
					 "        If you do not specify the \"config\" properties file, you will be prompted to provide the values" + LF +
					 "        that were expected in that file. Likewise, if you do not specify the \"license\" properties file," + LF +
					 "        you will be prompted to provide the values that were expected in that file." + LF +
					 " -config <file>    Specify the .properties file that configures this generator" + LF +
					 " -help             Display this help message" + LF +
					 " -license <file>   Specify the .properties file that contains the data for this license" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions04() throws ParseException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream printer = new PrintStream(stream);

		this.device.printErrLn("Missing argument for option: license");
		EasyMock.expectLastCall();
		EasyMock.expect(this.device.out()).andReturn(printer);
		this.device.exit(1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-license" });

		String output = stream.toString();

		assertNotNull("There should be output.", output);
		assertTrue("The output should have length.", output.length() > 0);
		assertEquals("The output is not correct.",
					 "usage:  ConsoleLicenseGenerator -help" + LF +
					 "        ConsoleLicenseGenerator" + LF +
					 "        ConsoleLicenseGenerator -config <file>" + LF +
					 "        ConsoleLicenseGenerator -license <file>" + LF +
					 "        ConsoleLicenseGenerator -config <file> -license <file>" + LF +
					 "        The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of" + LF +
					 "        them, or neither. The \"config\" properties file contains information necessary to generate all" + LF +
					 "        licenses (key paths, passwords, etc.) and generally will not need to change. The \"license\"" + LF +
					 "        properties file contains all of the information you need to generate this particular license. See" + LF +
					 "        the Javadoc API documentation for information about the contents of these two files." + LF +
					 "        If you do not specify the \"config\" properties file, you will be prompted to provide the values" + LF +
					 "        that were expected in that file. Likewise, if you do not specify the \"license\" properties file," + LF +
					 "        you will be prompted to provide the values that were expected in that file." + LF +
					 " -config <file>    Specify the .properties file that configures this generator" + LF +
					 " -help             Display this help message" + LF +
					 " -license <file>   Specify the .properties file that contains the data for this license" + LF,
					 output);
	}

	@Test
	public void testProcessCommandLineOptions05() throws ParseException
	{
		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-config", "config.properties" });

		assertNotNull("There should be a cli value.", this.console.cli);
	}

	@Test
	public void testProcessCommandLineOptions06() throws ParseException
	{
		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(new String[] { "-license", "license.properties" });

		assertNotNull("There should be a cli value.", this.console.cli);
	}

	@Test
	public void testProcessCommandLineOptions07() throws ParseException
	{
		EasyMock.replay(this.device);

		this.console.processCommandLineOptions(
				new String[] { "-config", "config.properties", "-license", "license.properties" }
		);

		assertNotNull("There should be a cli value.", this.console.cli);
	}

	private void resetLicenseCreator()
	{
		try
		{
			Field field = LicenseCreator.class.getDeclaredField("instance");
			field.setAccessible(true);
			field.set(null, null);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private PrivateKeyDataProvider getPrivateKeyDataProvider()
	{
		try
		{
			Field field = LicenseCreatorProperties.class.getDeclaredField("privateKeyDataProvider");
			field.setAccessible(true);
			return (PrivateKeyDataProvider)field.get(null);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private PasswordProvider getPasswordProvider()
	{
		try
		{
			Field field = LicenseCreatorProperties.class.getDeclaredField("privateKeyPasswordProvider");
			field.setAccessible(true);
			return (PasswordProvider)field.get(null);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testInitializeLicenseCreator01() throws Exception
	{
		this.resetLicenseCreator();

		String fileName = "testInitializeLicenseCreator01.properties";
		File file = new File(fileName);
		if(file.exists())
			FileUtils.forceDelete(file);

		this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
				addMockedMethod("hasOption", String.class).
				addMockedMethod("getOptionValue", String.class).
				createStrictMock();

		EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
		EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

		EasyMock.replay(this.console.cli, this.device);

		try
		{
			this.console.initializeLicenseCreator();
			fail("Expected exception FileNotFoundException.");
		}
		catch(FileNotFoundException ignore) { }
		finally
		{
			this.resetLicenseCreator();

			EasyMock.verify(this.console.cli);
		}
	}

	@Test
	public void testInitializeLicenseCreator02() throws Exception
	{
		this.resetLicenseCreator();

		String fileName = "testInitializeLicenseCreator02.properties";
		File file = new File(fileName);
		if(file.exists())
			FileUtils.forceDelete(file);

		FileUtils.writeStringToFile(file, "test");

		assertTrue("Setting the file to not readable should have returned true.", file.setReadable(false));
		assertTrue("The file should be writable.", file.canWrite());
		assertFalse("The file should not be readable.", file.canRead());

		this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
				addMockedMethod("hasOption", String.class).
				addMockedMethod("getOptionValue", String.class).
				createStrictMock();

		EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
		EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

		EasyMock.replay(this.console.cli, this.device);

		try
		{
			this.console.initializeLicenseCreator();
			fail("Expected exception IOException.");
		}
		catch(IOException ignore) { }
		finally
		{
			this.resetLicenseCreator();

			FileUtils.forceDelete(file);

			EasyMock.verify(this.console.cli);
		}
	}

	@Test
	public void testInitializeLicenseCreator03() throws Exception
	{
		this.resetLicenseCreator();

		String fileName = "testInitializeLicenseCreator03.properties";
		File file = new File(fileName);
		if(file.exists())
			FileUtils.forceDelete(file);

		FileUtils.writeStringToFile(
				file,
				"net.nicholaswilliams.java.licensing.privateKeyFile=testInitializeLicenseCreator03.key\r\n" +
				"net.nicholaswilliams.java.licensing.privateKeyPassword=testPassword03"
								   );

		this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
				addMockedMethod("hasOption", String.class).
				addMockedMethod("getOptionValue", String.class).
				createStrictMock();

		EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
		EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

		EasyMock.replay(this.console.cli, this.device);

		try
		{
			this.console.initializeLicenseCreator();
			fail("Expected exception FileNotFoundException.");
		}
		catch(FileNotFoundException e) { }
		finally
		{
			this.resetLicenseCreator();

			FileUtils.forceDelete(file);

			EasyMock.verify(this.console.cli);
		}
	}

	@Test
	public void testInitializeLicenseCreator04() throws Exception
	{
		this.resetLicenseCreator();

		String fileName = "testInitializeLicenseCreator04.properties";
		File file = new File(fileName);
		if(file.exists())
			FileUtils.forceDelete(file);

		File keyFile = new File("testInitializeLicenseCreator04.key");
		FileUtils.writeStringToFile(keyFile, "aKey");

		FileUtils.writeStringToFile(
				file,
				"net.nicholaswilliams.java.licensing.privateKeyFile=testInitializeLicenseCreator04.key\r\n" +
				"net.nicholaswilliams.java.licensing.privateKeyPassword=testPassword04"
		);

		this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
				addMockedMethod("hasOption", String.class).
				addMockedMethod("getOptionValue", String.class).
				createStrictMock();

		EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
		EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

		EasyMock.replay(this.console.cli, this.device);

		try
		{
			this.console.initializeLicenseCreator();

			PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
			assertNotNull("The key provider should not be null.", key);
			assertSame("The key provider is not correct.", FilePrivateKeyDataProvider.class, key.getClass());
			assertEquals("The file is not correct.", keyFile.getAbsolutePath(),
						 ((FilePrivateKeyDataProvider)key).getPrivateKeyFile().getAbsolutePath());

			PasswordProvider password = this.getPasswordProvider();
			assertNotNull("The password provider should not be null.", password);
			assertArrayEquals("The password is not correct.", "testPassword04".toCharArray(), password.getPassword());
		}
		finally
		{
			LicenseCreatorProperties.setPrivateKeyDataProvider(null);
			LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

			this.resetLicenseCreator();

			FileUtils.forceDelete(file);
			FileUtils.forceDelete(keyFile);

			EasyMock.verify(this.console.cli);
		}
	}

	@Test
	public void testInitializeLicenseCreator05() throws Exception
	{
		this.resetLicenseCreator();

		String fileName = "testInitializeLicenseCreator04.properties";
		File file = new File(fileName);
		if(file.exists())
			FileUtils.forceDelete(file);

		FileUtils.writeStringToFile(
				file,
				"net.nicholaswilliams.java.licensing.privateKeyProvider=net.nicholaswilliams.java.licensing.samples.SampleFilePrivateKeyDataProvider\r\n" +
				"net.nicholaswilliams.java.licensing.privateKeyPasswordProvider=net.nicholaswilliams.java.licensing.samples.SamplePasswordProvider"
		);

		this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
				addMockedMethod("hasOption", String.class).
				addMockedMethod("getOptionValue", String.class).
				createStrictMock();

		EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
		EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

		EasyMock.replay(this.console.cli, this.device);

		try
		{
			this.console.initializeLicenseCreator();

			PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
			assertNotNull("The key provider should not be null.", key);
			assertSame("The key provider is not correct.", SampleFilePrivateKeyDataProvider.class, key.getClass());

			PasswordProvider password = this.getPasswordProvider();
			assertNotNull("The password provider should not be null.", password);
			assertSame("The password provider is not correct.", SamplePasswordProvider.class, password.getClass());
		}
		finally
		{
			LicenseCreatorProperties.setPrivateKeyDataProvider(null);
			LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

			this.resetLicenseCreator();

			FileUtils.forceDelete(file);

			EasyMock.verify(this.console.cli);
		}
	}



	@Test
	public void testRun01() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall().andThrow(new KeyNotFoundException("message01."));

		this.device.printErrLn("message01. Correct the error and try again.");
		EasyMock.expectLastCall();
		this.device.exit(51);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
	public void testRun02() throws Exception
	{
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall().andThrow(new ObjectSerializationException("message02."));

		this.device.printErrLn("message02. Correct the error and try again.");
		EasyMock.expectLastCall();
		this.device.exit(52);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall().andThrow(new AlgorithmNotSupportedException("message03."));

		this.device.printErrLn("The algorithm \"message03.\" is not supported on this system. " +
							   "Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(41);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall().andThrow(new InappropriateKeyException("message04."));

		this.device.printErrLn("message04. Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(42);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall().andThrow(new InappropriateKeySpecificationException("message05."));

		this.device.printErrLn("message05. Contact your system administrator for assistance.");
		EasyMock.expectLastCall();
		this.device.exit(43);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall();
		this.console.generateLicense();
		EasyMock.expectLastCall().andThrow(new InterruptedException("message06."));

		this.device.printErrLn("The system was interrupted while waiting for events to complete.");
		EasyMock.expectLastCall();
		this.device.exit(44);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "help" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall();
		this.console.generateLicense();
		EasyMock.expectLastCall().andThrow(new IOException("message07."));

		this.device.printErrLn("An error occurred writing or reading files from the system. Analyze the error " +
					"below to determine what went wrong and fix it!");
		EasyMock.expectLastCall();
		this.device.printErrLn("java.io.IOException: message07.");
		EasyMock.expectLastCall();
		this.device.exit(21);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "testOption08" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall();
		this.console.generateLicense();
		EasyMock.expectLastCall().andThrow(new RuntimeException("message08."));

		this.device.printErrLn("java.lang.RuntimeException: message08.");
		EasyMock.expectLastCall();
		this.device.exit(-1);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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
		this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
				withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
				withArgs(this.device, new GnuParser()).
				addMockedMethod("processCommandLineOptions").
				addMockedMethod("initializeLicenseCreator").
				addMockedMethod("generateLicense").
				createStrictMock();

		String[] arguments = new String[] { "lastOption09" };

		this.console.processCommandLineOptions(arguments);
		EasyMock.expectLastCall();
		this.console.initializeLicenseCreator();
		EasyMock.expectLastCall();
		this.console.generateLicense();
		EasyMock.expectLastCall();

		this.device.exit(0);
		EasyMock.expectLastCall();

		EasyMock.replay(this.device, this.console);

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

		EasyMock.replay(this.device);

		System.setSecurityManager(securityManager);

		ConsoleLicenseGenerator.main(new String[] { "-help" });

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

		EasyMock.replay(this.device);

		System.setSecurityManager(securityManager);

		ConsoleLicenseGenerator.main(new String[] { "-badOption" });

		System.setSecurityManager(null);
	}
}