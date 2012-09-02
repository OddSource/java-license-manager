/*
 * TestFilePrivateKeyDataProvider.java from LicenseManager modified Sunday, September 2, 2012 13:01:57 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.licensor;

import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for FilePrivateKeyDataProvider.
 */
public class TestFilePrivateKeyDataProvider
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

		FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(fileName);

		assertNotNull("The key file should not be null.", provider.getPrivateKeyFile());
		assertEquals("The key file is not correct.", file.getAbsoluteFile(), provider.getPrivateKeyFile());
		assertFalse("The paths should not be the same.", fileName.equals(provider.getPrivateKeyFile().getPath()));
		assertTrue("The paths should end the same.", provider.getPrivateKeyFile().getPath().endsWith(fileName));
	}

	@Test
	public void testKeyFile02() throws IOException
	{
		final String fileName = "testKeyFile02.key";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);

		FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(file);

		assertNotNull("The key file should not be null.", provider.getPrivateKeyFile());
		assertNotSame("The objects should not be the same.", file, provider.getPrivateKeyFile());
		assertEquals("The key file is not correct.", file.getAbsoluteFile(), provider.getPrivateKeyFile());
		assertFalse("The paths should not be the same.", fileName.equals(provider.getPrivateKeyFile().getPath()));
		assertTrue("The paths should end the same.", provider.getPrivateKeyFile().getPath().endsWith(fileName));
	}

	@Test
	public void testGetEncryptedPrivateKeyData01() throws IOException
	{
		final String fileName = "testGetEncryptedPrivateKeyData01.key";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);

		FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(file);

		try
		{
			provider.getEncryptedPrivateKeyData();
			fail("Expected exception KeyNotFoundException.");
		}
		catch(KeyNotFoundException e)
		{
			assertNull("The cause should be null.", e.getCause());
		}
	}

	@Test
	public void testGetEncryptedPrivateKeyData02() throws IOException
	{
		final String fileName = "testGetEncryptedPrivateKeyData02.key";
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

			FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(file);

			try
			{
				provider.getEncryptedPrivateKeyData();
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
	public void testGetEncryptedPrivateKeyData03() throws IOException
	{
		final String fileName = "testGetEncryptedPrivateKeyData03.key";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);

		byte[] data = new byte[] { 0x01, 0x71, 0x33 };

		FileUtils.writeByteArrayToFile(file, data);

		try
		{
			FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(file);

			byte[] returnedData = provider.getEncryptedPrivateKeyData();

			assertNotNull("The data should not be null.", returnedData);
			assertArrayEquals("The data is not correct.", data, returnedData);
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}

	@Test
	public void testGetEncryptedPrivateKeyData04() throws IOException
	{
		final String fileName = "testGetEncryptedPrivateKeyData04.key";
		File file = new File(fileName);

		if(file.exists())
			FileUtils.forceDelete(file);

		byte[] data = new byte[] { 0x51, 0x12, 0x23 };

		FileUtils.writeByteArrayToFile(file, data);

		try
		{
			FilePrivateKeyDataProvider provider = new FilePrivateKeyDataProvider(file);

			byte[] returnedData = provider.getEncryptedPrivateKeyData();

			assertNotNull("The data should not be null.", returnedData);
			assertArrayEquals("The data is not correct.", data, returnedData);
		}
		finally
		{
			FileUtils.forceDelete(file);
		}
	}
}