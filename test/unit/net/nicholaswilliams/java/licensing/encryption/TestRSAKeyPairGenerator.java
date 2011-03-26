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
import java.io.IOException;
import java.security.KeyPair;

import static org.junit.Assert.*;

/**
 * Test class for RSAKeyPairGenerator.
 */
public class TestRSAKeyPairGenerator
{
	private RSAKeyPairGenerator generator;

	public TestRSAKeyPairGenerator()
	{
		this.generator = new RSAKeyPairGenerator();
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
	public void testPasswordsMatch01()
	{
		assertTrue("The passwords should match.", this.generator.passwordsMatch(
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
		));
	}

	@Test
	public void testPasswordsMatch02()
	{
		assertTrue("The passwords should match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'}
		));
	}

	@Test
	public void testPasswordsMatch03()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
				new char[] {'S', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
		));
	}

	@Test
	public void testPasswordsMatch04()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '9'}
		));
	}

	@Test
	public void testPasswordsMatch05()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y'}
		));
	}

	@Test
	public void testGetKeyPair01()
	{
		KeyPair keyPair = this.generator.generateKeyPair();

		assertNotNull("The key pair should not be null.", keyPair);

		assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
				keyPair.getPrivate().getAlgorithm());
		assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
				keyPair.getPublic().getAlgorithm());
	}

	@Test
	public void testSaveKeyPairToFiles01() throws IOException
	{
		KeyPair keyPair = this.generator.generateKeyPair();

		File file1 = new File("testSaveKeyPairToFiles01.private");
		File file2 = new File("testSaveKeyPairToFiles01.public");

		if(file1.exists())
			FileUtils.forceDelete(file1);

		if(file2.exists())
			FileUtils.forceDelete(file2);

		assertFalse("File 1 should not exist.", file1.exists());
		assertFalse("File 2 should not exist.", file2.exists());

		this.generator.saveKeyPairToFiles(
				keyPair,
				"testSaveKeyPairToFiles01.private",
				"testSaveKeyPairToFiles01.public",
				"testMyPassword01".toCharArray()
		);

		assertTrue("File 1 should exist.", file1.exists());
		assertTrue("File 2 should exist.", file2.exists());

		FileUtils.forceDelete(file1);
		FileUtils.forceDelete(file2);
	}
}
