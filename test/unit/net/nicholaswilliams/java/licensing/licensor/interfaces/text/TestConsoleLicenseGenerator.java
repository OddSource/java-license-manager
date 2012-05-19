/*
 * TestConsoleLicenseGenerator.java from LicenseManager modified Saturday, May 19, 2012 10:16:11 CDT (-0500).
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

import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import net.nicholaswilliams.java.mock.MockPermissiveSecurityManager;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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