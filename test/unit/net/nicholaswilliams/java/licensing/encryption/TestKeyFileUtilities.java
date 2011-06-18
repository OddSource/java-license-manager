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

package net.nicholaswilliams.java.licensing.encryption;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * Test class for KeyFileUtilities.
 */
public class TestKeyFileUtilities
{

	public TestKeyFileUtilities()
	{

	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{

	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{

	}

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
		Constructor<KeyFileUtilities> constructor = KeyFileUtilities.class.getDeclaredConstructor();
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
	public void testPrivateKeyEncryption01() throws Throwable
	{
		File file = new File("testPrivateKeyEncryption01.key");

		if(file.exists())
			FileUtils.forceDelete(file);

		PrivateKey privateKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPrivate();

		KeyFileUtilities.writeEncryptedPrivateKey(privateKey, file, 
				"myTestPassword01".toCharArray());

		PrivateKey privateKey2 = KeyFileUtilities.readEncryptedPrivateKey(file,
				"myTestPassword01".toCharArray());

		assertNotNull("The key should not be null.", privateKey2);
		assertFalse("The objects should not be the same.", privateKey == privateKey2);
		assertEquals("The keys should be the same.", privateKey, privateKey2);

		FileUtils.forceDelete(file);
	}

	@Test
	public void testPrivateKeyEncryption02() throws Throwable
	{
		File file = new File("testPrivateKeyEncryption02.key");

		if(file.exists())
			FileUtils.forceDelete(file);

		PrivateKey privateKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPrivate();
		
		PrivateKey otherKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPrivate();

		assertFalse("The keys should not be equal ().", otherKey.equals(privateKey));

		KeyFileUtilities.writeEncryptedPrivateKey(privateKey, file,
				"yourTestPassword02".toCharArray());

		PrivateKey privateKey2 = KeyFileUtilities.readEncryptedPrivateKey(file,
				"yourTestPassword02".toCharArray());

		assertNotNull("The key should not be null.", privateKey2);
		assertFalse("The objects should not be the same.", privateKey == privateKey2);
		assertEquals("The keys should be the same.", privateKey, privateKey2);

		assertFalse("The keys should not be equal (2).", otherKey.equals(privateKey2));

		FileUtils.forceDelete(file);
	}

	@Test
	public void testPublicKeyEncryption01() throws Throwable
	{
		File file = new File("testPublicKeyEncryption01.key");

		if(file.exists())
			FileUtils.forceDelete(file);

		PublicKey publicKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPublic();

		KeyFileUtilities.writeEncryptedPublicKey(publicKey, file,
				"myTestPassword01".toCharArray());

		PublicKey publicKey2 = KeyFileUtilities.readEncryptedPublicKey(file,
				"myTestPassword01".toCharArray());

		assertNotNull("The key should not be null.", publicKey2);
		assertFalse("The objects should not be the same.", publicKey == publicKey2);
		assertEquals("The keys should be the same.", publicKey, publicKey2);

		FileUtils.forceDelete(file);
	}

	@Test
	public void testPublicKeyEncryption02() throws Throwable
	{
		File file = new File("testPublicKeyEncryption02.key");

		if(file.exists())
			FileUtils.forceDelete(file);

		PublicKey publicKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPublic();

		PublicKey otherKey = KeyPairGenerator.getInstance(
				KeyFileUtilities.keyAlgorithm
		).generateKeyPair().getPublic();

		assertFalse("The keys should not be equal (1).", otherKey.equals(publicKey));

		KeyFileUtilities.writeEncryptedPublicKey(publicKey, file,
				"yourTestPassword02".toCharArray());

		PublicKey publicKey2 = KeyFileUtilities.readEncryptedPublicKey(file,
				"yourTestPassword02".toCharArray());

		assertNotNull("The key should not be null.", publicKey2);
		assertFalse("The objects should not be the same.", publicKey == publicKey2);
		assertEquals("The keys should be the same.", publicKey, publicKey2);

		assertFalse("The keys should not be equal (2).", otherKey.equals(publicKey2));

		FileUtils.forceDelete(file);
	}
}