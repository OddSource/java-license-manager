/*
 * TestFileLicenseProvider.java from LicenseManager modified Wednesday, January 23, 2013 21:22:32 CST (-0600).
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

package io.oddsource.java.licensing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FileLicenseProvider.
 */
public class TestFileLicenseProvider
{
    private FileLicenseProvider provider;

    @Before
    public void setUp()
    {
        this.provider = new FileLicenseProvider();
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void testConstructor01()
    {
        assertSame("The class loader is not correct.",
                   FileLicenseProvider.class.getClassLoader(), this.provider.getClassLoader()
        );
        assertFalse("The classpath should be false.", this.provider.isFileOnClasspath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor02()
    {
        this.provider = new FileLicenseProvider(null);
    }

    @Test
    public void testConstructor03()
    {
        ClassLoader classLoader = EasyMock.createMockBuilder(ClassLoader.class).createStrictMock();
        EasyMock.replay(classLoader);

        this.provider = new FileLicenseProvider(classLoader);

        assertSame("The class loader is not correct.", classLoader, this.provider.getClassLoader());
        assertTrue("The classpath flag should be true.", this.provider.isFileOnClasspath());
    }

    @Test
    public void testSetBase64Encoded()
    {
        assertFalse("base64Encoded should be false.", this.provider.isBase64Encoded());
        this.provider.setBase64Encoded(true);
        assertTrue("base64Encoded should be true now.", this.provider.isBase64Encoded());
        this.provider.setBase64Encoded(false);
        assertFalse("base64Encoded should be false again.", this.provider.isBase64Encoded());
    }

    @Test
    public void testSetFileOnClasspath()
    {
        assertFalse("fileOnClasspath should be false.", this.provider.isFileOnClasspath());
        this.provider.setFileOnClasspath(true);
        assertTrue("fileOnClasspath should be true now.", this.provider.isFileOnClasspath());
        this.provider.setFileOnClasspath(false);
        assertFalse("fileOnClasspath should be false again.", this.provider.isFileOnClasspath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFilePrefix01()
    {
        assertEquals("The prefix should be blank.", "", this.provider.getFilePrefix());
        this.provider.setFilePrefix(null);
    }

    @Test
    public void testSetFilePrefix02()
    {
        assertEquals("The prefix should be blank.", "", this.provider.getFilePrefix());
        this.provider.setFilePrefix("test01");
        assertEquals("The prefix is not correct.", "test01", this.provider.getFilePrefix());
    }

    @Test
    public void testSetFilePrefix03()
    {
        assertEquals("The prefix should be blank.", "", this.provider.getFilePrefix());
        this.provider.setFilePrefix("");
        assertEquals("The prefix is not correct.", "", this.provider.getFilePrefix());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileSuffix01()
    {
        assertEquals("The suffix should be blank.", "", this.provider.getFileSuffix());
        this.provider.setFileSuffix(null);
    }

    @Test
    public void testSetFileSuffix02()
    {
        assertEquals("The suffix should be blank.", "", this.provider.getFileSuffix());
        this.provider.setFileSuffix("test01");
        assertEquals("The suffix is not correct.", "test01", this.provider.getFileSuffix());
    }

    @Test
    public void testSetFileSuffix03()
    {
        assertEquals("The suffix should be blank.", "", this.provider.getFileSuffix());
        this.provider.setFileSuffix("");
        assertEquals("The suffix is not correct.", "", this.provider.getFileSuffix());
    }

    @Test
    public void testGetLicenseFile01()
    {
        File file = this.provider.getLicenseFile("file01");

        assertNotNull("The file should not null.", file);
        assertEquals("The file name is not correct.", "file01", file.getPath());
    }

    @Test
    public void testGetLicenseFile02()
    {
        this.provider.setFilePrefix("prefix02/");
        this.provider.setFileSuffix(".suffix");
        File file = this.provider.getLicenseFile("file03");

        assertNotNull("The file should not null.", file);
        assertEquals(
            "The file name is not correct.",
            "prefix02" + System.getProperty("file.separator") + "file03.suffix",
            file.getPath()
        );
    }

    @Test
    public void testGetLicenseFile03() throws IOException
    {
        URL url = this.getClass().getClassLoader().getResource("");
        assertNotNull("The URL should not be null.", url);

        File temp;
        try
        {
            temp = new File(url.toURI());
        }
        catch(URISyntaxException e)
        {
            temp = new File(url.getPath());
        }

        temp = new File(temp, "io/oddsource/java/licensing/testGetLicenseFile03.lic");

        FileUtils.writeStringToFile(temp, "temp", "UTF-8");

        this.provider.setFilePrefix("io/oddsource/java/licensing/");
        this.provider.setFileSuffix(".lic");
        this.provider.setFileOnClasspath(true);
        File file = this.provider.getLicenseFile("testGetLicenseFile03");

        assertNotNull("The file should not null.", file);
        assertEquals("The file name is not correct.", temp.getPath(), file.getPath());

        FileUtils.forceDelete(temp);
    }

    @Test
    public void testGetLicenseFile04() throws IOException
    {
        URL url = this.getClass().getClassLoader().getResource("");
        assertNotNull("The URL should not be null.", url);

        File temp;
        try
        {
            temp = new File(url.toURI());
        }
        catch(URISyntaxException e)
        {
            temp = new File(url.getPath());
        }

        temp = new File(temp, "io/oddsource/java/licensing/licensor/testGetLicenseFile04.prop");

        FileUtils.writeStringToFile(temp, "temp", "UTF-8");

        this.provider.setFilePrefix("/io/oddsource/java/licensing/licensor/");
        this.provider.setFileSuffix(".prop");
        this.provider.setFileOnClasspath(true);
        File file = this.provider.getLicenseFile("testGetLicenseFile04");

        assertNotNull("The file should not null.", file);
        assertEquals("The file name is not correct.", temp.getPath(), file.getPath());

        FileUtils.forceDelete(temp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLicenseData01()
    {
        this.provider.getLicenseData(null);
    }

    @Test
    public void testGetLicenseData02()
    {
        this.provider = EasyMock.createMockBuilder(FileLicenseProvider.class).
            addMockedMethod("getLicenseFile").createStrictMock();

        EasyMock.expect(this.provider.getLicenseFile("test02")).andReturn(null);
        EasyMock.replay(this.provider);

        byte[] data = this.provider.getLicenseData("test02");

        assertNull("The data should be null.", data);
    }

    @Test
    public void testGetLicenseData03()
    {
        this.provider = EasyMock.createMockBuilder(FileLicenseProvider.class).
            addMockedMethod("getLicenseFile").createStrictMock();

        EasyMock.expect(this.provider.getLicenseFile("another03")).andReturn(new File("file03"));
        EasyMock.replay(this.provider);

        byte[] data = this.provider.getLicenseData("another03");

        assertNull("The data should be null.", data);
    }

    @Test
    public void testGetLicenseData04() throws IOException
    {
        File temp = new File("testGetLicenseData04.lic");
        FileUtils.writeStringToFile(temp, "test get 04", "UTF-8");

        this.provider = EasyMock.createMockBuilder(FileLicenseProvider.class).
            addMockedMethod("getLicenseFile").createStrictMock();

        EasyMock.expect(this.provider.getLicenseFile("test04")).andReturn(new File("testGetLicenseData04.lic"));
        EasyMock.replay(this.provider);

        byte[] data = this.provider.getLicenseData("test04");

        assertNotNull("The data should not be null.", data);
        assertEquals("The data is not correct.", "test get 04", new String(data));

        FileUtils.forceDelete(temp);
    }

    @Test
    public void testGetLicenseData05() throws IOException
    {
        File temp = new File("testGetLicenseData05.lic");
        FileUtils.writeByteArrayToFile(temp, Base64.encodeBase64("another get 05".getBytes()));

        this.provider = EasyMock.createMockBuilder(FileLicenseProvider.class).
            addMockedMethod("getLicenseFile").createStrictMock();

        EasyMock.expect(this.provider.getLicenseFile("test05")).andReturn(new File("testGetLicenseData05.lic"));
        EasyMock.replay(this.provider);

        this.provider.setBase64Encoded(true);
        byte[] data = this.provider.getLicenseData("test05");

        assertNotNull("The data should not be null.", data);
        assertEquals("The data is not correct.", "another get 05", new String(data));

        FileUtils.forceDelete(temp);
    }
}
