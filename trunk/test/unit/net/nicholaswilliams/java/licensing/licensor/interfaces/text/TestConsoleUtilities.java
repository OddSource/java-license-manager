/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import net.nicholaswilliams.java.mock.StateFlag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.IllegalFormatException;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleUtilities.
 */
public class TestConsoleUtilities
{
	@Before
	public void setUp()
	{

	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testConfigureInterfaceDevice()
	{
		final StateFlag registerShutdownHookCalled = new StateFlag();

		ConsoleUtilities.configureInterfaceDevice(new TextInterfaceDevice()
			{
				@Override
				public void registerShutdownHook(Thread hook)
						throws IllegalArgumentException, IllegalStateException, SecurityException
				{
					assertNotNull("The thread should not be null.", hook);
					hook.run();
					registerShutdownHookCalled.state = true;
				}

				@Override
				public boolean unregisterShutdownHook(Thread hook) throws IllegalStateException, SecurityException
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public void exit()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public void exit(int exitCode)
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public TextInterfaceDevice format(String format, Object... arguments) throws IllegalFormatException
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public TextInterfaceDevice printf(String format, Object... arguments) throws IllegalFormatException, IOError
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public String readLine() throws IOError
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public String readLine(String format, Object... arguments) throws IllegalFormatException, IOError
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public char[] readPassword() throws IOError
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public char[] readPassword(String format, Object... arguments) throws IllegalFormatException, IOError
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public Reader getReader()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public PrintWriter getWriter()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public InputStream in()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public PrintStream out()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public PrintStream err()
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}

				@Override
				public void flush() throws IOException
				{
					throw new UnsupportedOperationException("For testing purposes only.");
				}
		});

		assertTrue("registerShutdownHook should have been called.", registerShutdownHookCalled.state);
	}
}