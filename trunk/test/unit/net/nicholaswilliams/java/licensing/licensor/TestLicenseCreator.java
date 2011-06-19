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

package net.nicholaswilliams.java.licensing.licensor;

import net.nicholaswilliams.java.licensing.encryption.Encryptor;
import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.junit.Assert.*;

/**
 * Test class for LicenseCreator.
 */
public class TestLicenseCreator
{
	private static final char[] keyPassword = "testLicenseManagerPassword".toCharArray();

	private static KeyPasswordProvider keyPasswordProvider;

	private static PrivateKeyDataProvider keyDataProvider;

	private static IMocksControl control;

	private static byte[] encryptedPrivateKey;

	private LicenseCreator creator;

	public TestLicenseCreator()
	{
		this.creator = LicenseCreator.getInstance();
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		TestLicenseCreator.control = EasyMock.createStrictControl();

		TestLicenseCreator.keyPasswordProvider = TestLicenseCreator.control.createMock(KeyPasswordProvider.class);
		TestLicenseCreator.keyDataProvider = TestLicenseCreator.control.createMock(PrivateKeyDataProvider.class);

		LicenseCreator.createInstance(
				TestLicenseCreator.keyPasswordProvider,
				TestLicenseCreator.keyDataProvider
		);

		KeyPair keyPair = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm).generateKeyPair();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
		IOUtils.write(Encryptor.encryptRaw(pkcs8EncodedKeySpec.getEncoded(), keyPassword), outputStream);
		TestLicenseCreator.encryptedPrivateKey = outputStream.toByteArray();
	}

	@Before
	public void setUp()
	{
		TestLicenseCreator.control.reset();
	}

	@After
	public void tearDown()
	{
		TestLicenseCreator.control.verify();
	}

	@Test
	public void firstTest()
	{
		TestLicenseCreator.control.replay();

		assertNull(null);
		assertTrue(true);
	}
}