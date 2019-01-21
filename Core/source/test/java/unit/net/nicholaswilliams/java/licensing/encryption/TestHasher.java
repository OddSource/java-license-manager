/*
 * TestHasher.java from LicenseManager modified Tuesday, February 21, 2012 10:59:35 CST (-0600).
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

package net.nicholaswilliams.java.licensing.encryption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Test class for Hasher.
 */
public class TestHasher
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
        Constructor<Hasher> constructor = Hasher.class.getDeclaredConstructor();
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
    public void testHashSameStrings() throws Exception
    {
        String unhashed = "myteststring";

        String enc1 = Hasher.hash(unhashed);
        String enc2 = Hasher.hash(unhashed);
        String enc3 = Hasher.hash(unhashed);

        assertNotNull("The first encrypted string was null.", enc1);
        assertNotNull("The second encrypted string was null.", enc2);
        assertNotNull("The third encrypted string was null.", enc3);

        assertFalse("The first encrypted string was not encrypted properly.", enc1.equals(unhashed));
        assertFalse("The second encrypted string was not encrypted properly.", enc2.equals(unhashed));
        assertFalse("The third encrypted string was not encrypted properly.", enc3.equals(unhashed));

        assertEquals("The first and second encrypted strings do not match.", enc1, enc2);
        assertEquals("The first and third encrypted strings do not match.", enc1, enc3);
        assertEquals("The second and third encrypted strings do not match.", enc2, enc3);
    }

    @Test
    public void testHashDifferentString() throws Exception
    {
        String unhashed1 = "myteststring1";
        String unhashed2 = "myteststring2";

        String encrypted1 = Hasher.hash(unhashed1);
        String encrypted2 = Hasher.hash(unhashed2);

        assertNotNull("The first encrypted string was null.", encrypted1);
        assertNotNull("The second encrypted string was null.", encrypted2);

        assertFalse("The first encrypted string matches the second encrypted string, and it shouldn't.", encrypted1.equals(encrypted2));
    }
}
