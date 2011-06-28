/*
 * TestDataSignatureManager.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
 *
 * Copyright 2010-2011 the original author or authors.
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

import net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGenerator;
import net.nicholaswilliams.java.licensing.exception.InvalidSignatureException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * Test class for DataSignatureManager.
 */
public class TestDataSignatureManager
{
	private static final PrivateKey privateKey;

	private static final PublicKey publicKey;

	static
	{
		KeyPair keys = new RSAKeyPairGenerator().generateKeyPair();
		privateKey = keys.getPrivate();
		publicKey = keys.getPublic();
	}

	private DataSignatureManager manager;

	public TestDataSignatureManager()
	{
		this.manager = new DataSignatureManager();
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
	public void testSignature01()
	{
		byte[] data = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13, (byte)17 };

		byte[] signature = this.manager.signData(
				TestDataSignatureManager.privateKey, data
		);

		this.manager.verifySignature(
				TestDataSignatureManager.publicKey, data, signature
		);
	}
	
	@Test(expected=InvalidSignatureException.class)
	public void testSignature02()
	{
		byte[] data = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13, (byte)17 };

		byte[] signature = this.manager.signData(
				TestDataSignatureManager.privateKey, data
		);

		data[4] = 76;

		this.manager.verifySignature(
				TestDataSignatureManager.publicKey, data, signature
		);
	}
	
	@Test
	public void testSignature03()
	{
		byte[] data = Arrays.copyOf(new byte[] { (byte)19, (byte)23, (byte)29, (byte)31, (byte)37, (byte)41, (byte)43 }, 629383);

		byte[] signature = this.manager.signData(
				TestDataSignatureManager.privateKey, data
		);

		this.manager.verifySignature(
				TestDataSignatureManager.publicKey, data, signature
		);
	}

	@Test(expected=InvalidSignatureException.class)
	public void testSignature04()
	{
		byte[] data = Arrays.copyOf(new byte[] { (byte)19, (byte)23, (byte)29, (byte)31, (byte)37, (byte)41, (byte)43 }, 629383);

		byte[] signature = this.manager.signData(
				TestDataSignatureManager.privateKey, data
		);

		data[6983] = 76;

		this.manager.verifySignature(
				TestDataSignatureManager.publicKey, data, signature
		);
	}
}