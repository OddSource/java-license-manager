/*
 * TestAbstractTextInterfaceDevice.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction;

import net.nicholaswilliams.java.mock.StateFlag;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.IllegalFormatException;

import static org.junit.Assert.*;

/**
 * Test class for AbstractTextInterfaceDevice.
 */
public class TestAbstractTextInterfaceDevice
{
	private AbstractTextInterfaceDevice device;

	private InputStream inputStream;

	private PrintStream outputStream1;

	private PrintStream outputStream2;

	private final StateFlag exitCalled = new StateFlag();

	@Before
	public void setUp() throws FileNotFoundException
	{
		this.inputStream = new ByteArrayInputStream("hello".getBytes());
		this.outputStream1 = new PrintStream(new ByteArrayOutputStream());
		this.outputStream2 = new PrintStream(new ByteArrayOutputStream());

		this.device = new AbstractTextInterfaceDevice(this.inputStream, this.outputStream1, this.outputStream2)
		{
			@Override
			public void registerShutdownHook(Thread hook)
					throws IllegalArgumentException, IllegalStateException, SecurityException
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean unregisterShutdownHook(Thread hook) throws IllegalStateException, SecurityException
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void exit(int exitCode)
			{
				assertEquals("The exit code is not correct.", 0, exitCode);
				TestAbstractTextInterfaceDevice.this.exitCalled.state = true;
			}

			@Override
			public TextInterfaceDevice format(String format, Object... arguments) throws IllegalFormatException
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public TextInterfaceDevice printf(String format, Object... arguments) throws IllegalFormatException, IOError
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public String readLine() throws IOError
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public String readLine(String format, Object... arguments) throws IllegalFormatException, IOError
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public char[] readPassword() throws IOError
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public char[] readPassword(String format, Object... arguments) throws IllegalFormatException, IOError
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public Reader getReader()
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public PrintWriter getWriter()
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void flush() throws IOException
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testIn()
	{
		assertEquals("The input stream is not correct (1).", this.inputStream, this.device.in);
		assertEquals("The input stream is not correct (2).", this.inputStream, this.device.in());
	}

	@Test
	public void testOut()
	{
		assertEquals("The output stream is not correct (1).", this.outputStream1, this.device.out);
		assertEquals("The output stream is not correct (2).", this.outputStream1, this.device.out());
	}

	@Test
	public void testErr()
	{
		assertEquals("The error output stream is not correct (1).", this.outputStream2, this.device.err);
		assertEquals("The error output stream is not correct (2).", this.outputStream2, this.device.err());
	}

	@Test
	public void testExit()
	{
		assertFalse("The flag has the wrong initial value.", this.exitCalled.state);

		this.device.exit();

		assertTrue("The flag has the wrong new value.", this.exitCalled.state);
	}
}