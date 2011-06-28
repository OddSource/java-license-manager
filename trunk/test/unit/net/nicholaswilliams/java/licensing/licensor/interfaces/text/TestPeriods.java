/*
 * TestPeriods.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for Periods.
 */
public class TestPeriods
{
	private Periods periods;

	private ByteArrayOutputStream outputStream;

	@Before
	public void setUp()
	{
		this.outputStream = new ByteArrayOutputStream();

		PrintStream printStream = new PrintStream(outputStream);

		this.periods = new Periods(25, printStream);
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testRun01() throws InterruptedException
	{
		new Thread(this.periods).start();

		Thread.sleep(100);

		byte[] bytes = this.outputStream.toByteArray();
		assertTrue("The length is not correct.", bytes.length >= 3);
		assertArrayEquals("The arrays are not correct.", Arrays.copyOf(bytes, 3), new byte[] {'.', '.', '.'});
	}

	@Test
	public void testRun02() throws InterruptedException
	{
		new Thread(this.periods).start();

		Thread.sleep(200);

		byte[] bytes = this.outputStream.toByteArray();
		assertTrue("The length is not correct.", bytes.length >= 6);
		assertArrayEquals("The arrays are not correct.", Arrays.copyOf(bytes, 6), new byte[] {'.', '.', '.', '.', '.', '.'});
	}
}