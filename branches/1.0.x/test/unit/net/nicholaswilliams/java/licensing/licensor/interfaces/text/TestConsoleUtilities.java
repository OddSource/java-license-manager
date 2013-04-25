/*
 * TestConsoleUtilities.java from LicenseManager modified Sunday, March 4, 2012 12:41:41 CST (-0600).
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

import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void testConstruction()
	{
		new ConsoleUtilities();
	}

	@Test
	public void testConfigureInterfaceDevice() throws InterruptedException
	{
		TextInterfaceDevice textInterfaceDevice = EasyMock.createStrictMock(TextInterfaceDevice.class);

		Capture<Thread> threadCapture = new Capture<Thread>();

		textInterfaceDevice.registerShutdownHook(EasyMock.capture(threadCapture));
		EasyMock.expectLastCall();
		textInterfaceDevice.printOutLn();
		EasyMock.expectLastCall();

		EasyMock.replay(textInterfaceDevice);

		ConsoleUtilities.configureInterfaceDevice(textInterfaceDevice);

		Thread captured = threadCapture.getValue();

		assertNotNull("The thread should not be null.", captured);

		captured.start();

		int i = 0;
		while(captured.getState() != Thread.State.TERMINATED && i < 10)
		{
			Thread.sleep(100);
			i++;
		}

		assertTrue("The thread took too long to complete.", i < 10);

		EasyMock.verify(textInterfaceDevice);
	}
}