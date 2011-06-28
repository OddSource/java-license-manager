/*
 * TestByteArrayInterfaceDevice.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Test class for ByteArrayInterfaceDevice.
 */
public class TestByteArrayInterfaceDevice
{
	private ByteArrayInterfaceDevice device;

	@Before
	public void setUp()
	{
		this.device = new ByteArrayInterfaceDevice(new byte[255]);
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testGetInputStream()
	{
		ByteArrayInputStream stream = this.device.getInputStream();

		assertNotNull("The input stream should not be null.", stream);
		assertSame("The objects should be the same.", this.device.in, stream);
	}

	@Test
	public void testGetOutputStream() throws IllegalAccessException, NoSuchFieldException
	{
		ByteArrayOutputStream stream = this.device.getOutputStream();

		Field field = java.io.FilterOutputStream.class.getDeclaredField("out");
		field.setAccessible(true);
		Object out = field.get(this.device.out);

		assertNotNull("The output stream should not be null.", stream);
		assertSame("The objects should be the same.", out, stream);
	}

	@Test
	public void testGetErrorOutputStream() throws IllegalAccessException, NoSuchFieldException
	{
		ByteArrayOutputStream stream = this.device.getErrorOutputStream();

		Field field = java.io.FilterOutputStream.class.getDeclaredField("out");
		field.setAccessible(true);
		Object err = field.get(this.device.err);

		assertNotNull("The output stream should not be null.", stream);
		assertSame("The objects should be the same.", err, stream);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRegisterShutdownHook()
	{
		this.device.registerShutdownHook(new Thread());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testUnregisterShutdownHook()
	{
		this.device.unregisterShutdownHook(new Thread());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testExit()
	{
		this.device.exit(0);
	}

	@Test
	public void testFormat()
	{
		assertNull("The value should have been null.", this.device.format("Test string."));
	}

	@Test
	public void testPrintf()
	{
		assertNull("The value should have been null.", this.device.printf("Test string."));
	}

	@Test
	public void testFlush()
	{
		this.device.flush();
	}

	@Test
	public void testReadLine01()
	{
		assertNull("The value should have been null.", this.device.readLine());
	}

	@Test
	public void testReadLine02()
	{
		assertNull("The value should have been null.", this.device.readLine("Test string."));
	}

	@Test
	public void testReadPassword01()
	{
		assertArrayEquals("The value should have been null.", new char[0], this.device.readPassword());
	}

	@Test
	public void testReadPassword02()
	{
		assertArrayEquals("The value should have been null.", new char[0], this.device.readPassword("Test string."));
	}

	@Test
	public void testGetReader()
	{
		assertNull("The value should have been null.", this.device.getReader());
	}

	@Test
	public void testGetWriter()
	{
		assertNull("The value should have been null.", this.device.getWriter());
	}
}