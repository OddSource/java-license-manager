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

package net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleInterfaceDevice.
 */
public class TestConsoleInterfaceDevice
{
	private ConsoleInterfaceDevice device;

	@Before
	public void setUp()
	{
		this.device = new ConsoleInterfaceDevice();
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testConsoleProperty()
	{
		assertSame("The console object should be the same.", System.console(), this.device.getConsole());
	}

	@Test
	public void testRuntimeProperty()
	{
		assertSame("The runtime object should be the same.", Runtime.getRuntime(), this.device.getRuntime());
	}

	@Test
	public void testRegisterShutdownHook()
	{
		Thread thread = new Thread();

		this.device.registerShutdownHook(thread);

		assertTrue("The hook should have been unregistered.", this.device.unregisterShutdownHook(thread));
		assertFalse("The hook should not have been unregistered again.", this.device.unregisterShutdownHook(thread));
	}

	@Test(expected=NullPointerException.class)
	public void testFormat()
	{
		this.device.format("Test formatted string %d", 10923.0D);
	}

	@Test(expected=NullPointerException.class)
	public void testPrintf()
	{
		this.device.printf("Test formatted string %d", 10923.0D);
	}

	@Test(expected=NullPointerException.class)
	public void testFlush()
	{
		this.device.flush();
	}

	@Test(expected=NullPointerException.class)
	public void testReadLine01()
	{
		this.device.readLine();
	}

	@Test(expected=NullPointerException.class)
	public void testReadLine02()
	{
		this.device.readLine("Test formatted string %d", 10923.0D);
	}

	@Test(expected=NullPointerException.class)
	public void testReadPassword01()
	{
		this.device.readPassword();
	}

	@Test(expected=NullPointerException.class)
	public void testReadPassword02()
	{
		this.device.readPassword("Test formatted string %d", 10923.0D);
	}

	@Test(expected=NullPointerException.class)
	public void testGetReader()
	{
		this.device.getReader();
	}

	@Test(expected=NullPointerException.class)
	public void testGetWriter()
	{
		this.device.getWriter();
	}
}