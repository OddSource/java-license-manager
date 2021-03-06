/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing.licensor.interfaces.cli;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.oddsource.java.licensing.licensor.interfaces.cli.spi.TextInterfaceDevice;

/**
 * Test class for ConsoleUtilities.
 */
@SuppressWarnings("EmptyMethod")
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
    public void testConstructionForbidden()
        throws IllegalAccessException, InstantiationException, NoSuchMethodException
    {
        Constructor<ConsoleUtilities> constructor = ConsoleUtilities.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try
        {
            constructor.newInstance();
            fail("Expected exception java.lang.reflect.InvocationTargetException, but got no exception.");
        }
        catch(InvocationTargetException e)
        {
            Throwable cause = e.getCause();
            assertNotNull("Expected cause for InvocationTargetException, but got no cause.", cause);
            assertSame(
                "Expected exception java.lang.RuntimeException, but got " + cause.getClass(),
                AssertionError.class,
                cause.getClass()
            );
            assertEquals("The message was incorrect.", "This class cannot be instantiated.", cause.getMessage());
        }
    }

    @Test
    public void testConfigureInterfaceDevice() throws InterruptedException
    {
        TextInterfaceDevice textInterfaceDevice = EasyMock.createStrictMock(TextInterfaceDevice.class);

        Capture<Thread> threadCapture = EasyMock.newCapture();

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
