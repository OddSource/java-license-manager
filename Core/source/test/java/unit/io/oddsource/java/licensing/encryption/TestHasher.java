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
package io.oddsource.java.licensing.encryption;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Hasher.
 */
@SuppressWarnings("EmptyMethod")
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
            assertSame(
                "Expected exception java.lang.RuntimeException, but got " + cause.getClass(),
                RuntimeException.class,
                cause.getClass()
            );
            assertEquals("The message was incorrect.", "This class cannot be instantiated.", cause.getMessage());
        }
    }

    @Test
    public void testHashSameStrings()
    {
        String unhashed = "myTestString";

        String enc1 = Hasher.hash(unhashed);
        String enc2 = Hasher.hash(unhashed);
        String enc3 = Hasher.hash(unhashed);

        assertNotNull("The first encrypted string was null.", enc1);
        assertNotNull("The second encrypted string was null.", enc2);
        assertNotNull("The third encrypted string was null.", enc3);

        assertNotEquals("The first encrypted string was not encrypted properly.", enc1, unhashed);
        assertNotEquals("The second encrypted string was not encrypted properly.", enc2, unhashed);
        assertNotEquals("The third encrypted string was not encrypted properly.", enc3, unhashed);

        assertEquals("The first and second encrypted strings do not match.", enc1, enc2);
        assertEquals("The first and third encrypted strings do not match.", enc1, enc3);
        assertEquals("The second and third encrypted strings do not match.", enc2, enc3);
    }

    @Test
    public void testHashDifferentString()
    {
        String unhashed1 = "myTestString1";
        String unhashed2 = "myTestString2";

        String encrypted1 = Hasher.hash(unhashed1);
        String encrypted2 = Hasher.hash(unhashed2);

        assertNotNull("The first encrypted string was null.", encrypted1);
        assertNotNull("The second encrypted string was null.", encrypted2);

        assertNotEquals(
            "The first encrypted string matches the second encrypted string, and it shouldn't.",
            encrypted1,
            encrypted2
        );
    }
}
