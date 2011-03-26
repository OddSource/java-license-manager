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
}