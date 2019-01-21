/*
 * TestLicensingCharsets.java from LicenseManager modified Wednesday, April 24, 2013 12:08:07 CDT (-0500).
 *
 * Copyright 2010-2013 the original author or authors.
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

package net.nicholaswilliams.java.licensing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class TestLicensingCharsets
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
        Constructor<LicensingCharsets> constructor = LicensingCharsets.class.getDeclaredConstructor();
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
            assertSame("Expected exception java.lang.RuntimeException, but got " + cause.getClass(), RuntimeException.class, cause.getClass());
            assertEquals("The message was incorrect.", "This class cannot be instantiated.", cause.getMessage());
        }
    }

    @Test
    public void testUtf8()
    {
        assertEquals("The UTF-8 charset is not correct.", "UTF-8", LicensingCharsets.UTF_8.name());
    }
}
