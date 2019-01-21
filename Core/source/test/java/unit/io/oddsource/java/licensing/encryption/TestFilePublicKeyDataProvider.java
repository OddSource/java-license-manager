/*
 * TestFilePublicKeyDataProvider.java from LicenseManager modified Sunday, September 2, 2012 14:09:10 CDT (-0500).
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

package io.oddsource.java.licensing.encryption;

import io.oddsource.java.licensing.exception.KeyNotFoundException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for FilePublicKeyDataProvider.
 */
public class TestFilePublicKeyDataProvider
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
    public void testKeyFile01() throws IOException
    {
        final String fileName = "testKeyFile01.key";
        File file = new File(fileName);

        if(file.exists())
            FileUtils.forceDelete(file);

        FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(fileName);

        assertNotNull("The key file should not be null.", provider.getPublicKeyFile());
        assertEquals("The key file is not correct.", file.getAbsoluteFile(), provider.getPublicKeyFile());
        assertFalse("The paths should not be the same.", fileName.equals(provider.getPublicKeyFile().getPath()));
        assertTrue("The paths should end the same.", provider.getPublicKeyFile().getPath().endsWith(fileName));
    }

    @Test
    public void testKeyFile02() throws IOException
    {
        final String fileName = "testKeyFile02.key";
        File file = new File(fileName);

        if(file.exists())
            FileUtils.forceDelete(file);

        FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(file);

        assertNotNull("The key file should not be null.", provider.getPublicKeyFile());
        assertNotSame("The objects should not be the same.", file, provider.getPublicKeyFile());
        assertEquals("The key file is not correct.", file.getAbsoluteFile(), provider.getPublicKeyFile());
        assertFalse("The paths should not be the same.", fileName.equals(provider.getPublicKeyFile().getPath()));
        assertTrue("The paths should end the same.", provider.getPublicKeyFile().getPath().endsWith(fileName));
    }

    @Test
    public void testGetEncryptedPublicKeyData01() throws IOException
    {
        final String fileName = "testGetEncryptedPublicKeyData01.key";
        File file = new File(fileName);

        if(file.exists())
            FileUtils.forceDelete(file);

        FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(file);

        try
        {
            provider.getEncryptedPublicKeyData();
            fail("Expected exception KeyNotFoundException.");
        }
        catch(KeyNotFoundException e)
        {
            assertNull("The cause should be null.", e.getCause());
        }
    }

    @Test
    @Ignore("canRead()/canWrite() do not work on Win; setReadable()/setWritable() do not work on some Macs.")
    public void testGetEncryptedPublicKeyData02() throws IOException
    {
        final String fileName = "testGetEncryptedPublicKeyData02.key";
        File file = new File(fileName);
        file = file.getCanonicalFile();

        if(file.exists())
            FileUtils.forceDelete(file);

        byte[] data = new byte[] { 0x01, 0x71, 0x33 };

        FileUtils.writeByteArrayToFile(file, data);

        try
        {
            assertTrue("Setting the file to not-readable should have succeeded.", file.setReadable(false, false));
            assertFalse("The file should not be readable.", file.canRead());
            assertTrue("The file should still be writable.", file.canWrite());

            FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(file);

            try
            {
                provider.getEncryptedPublicKeyData();
                fail("Expected exception KeyNotFoundException.");
            }
            catch(KeyNotFoundException e)
            {
                assertNotNull("The cause should not be null.", e.getCause());
            }
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testGetEncryptedPublicKeyData03() throws IOException
    {
        final String fileName = "testGetEncryptedPublicKeyData03.key";
        File file = new File(fileName);

        if(file.exists())
            FileUtils.forceDelete(file);

        byte[] data = new byte[] { 0x01, 0x71, 0x33 };

        FileUtils.writeByteArrayToFile(file, data);

        try
        {
            FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(file);

            byte[] returnedData = provider.getEncryptedPublicKeyData();

            assertNotNull("The data should not be null.", returnedData);
            assertArrayEquals("The data is not correct.", data, returnedData);
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testGetEncryptedPublicKeyData04() throws IOException
    {
        final String fileName = "testGetEncryptedPublicKeyData04.key";
        File file = new File(fileName);

        if(file.exists())
            FileUtils.forceDelete(file);

        byte[] data = new byte[] { 0x51, 0x12, 0x23 };

        FileUtils.writeByteArrayToFile(file, data);

        try
        {
            FilePublicKeyDataProvider provider = new FilePublicKeyDataProvider(file);

            byte[] returnedData = provider.getEncryptedPublicKeyData();

            assertNotNull("The data should not be null.", returnedData);
            assertArrayEquals("The data is not correct.", data, returnedData);
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }
}