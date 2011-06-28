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

import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.LicenseManager;
import net.nicholaswilliams.java.licensing.LicenseProvider;
import net.nicholaswilliams.java.licensing.SignedLicense;
import net.nicholaswilliams.java.licensing.encryption.Encryptor;
import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;
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
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.*;

/**
 * Test class for .
 */
public class TestLicenseCreatorAndLicenseManagerInteraction
{
	private static final char[] keyPassword = "testLicenseCreatorPassword".toCharArray();

	private static LicenseProvider licenseProvider;

	private static KeyPasswordProvider keyPasswordProvider;

	private static PublicKeyDataProvider publicKeyDataProvider;

	private static PrivateKeyDataProvider privateKeyDataProvider;

	private static IMocksControl control;

	private static byte[] encryptedPrivateKey;

	private static byte[] encryptedPublicKey;

	private LicenseCreator creator;

	private LicenseManager manager;

	public TestLicenseCreatorAndLicenseManagerInteraction()
	{
		this.creator = LicenseCreator.getInstance();
		this.manager = LicenseManager.getInstance();
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		control = EasyMock.createStrictControl();

		keyPasswordProvider = control.createMock(KeyPasswordProvider.class);
		publicKeyDataProvider = control.createMock(PublicKeyDataProvider.class);
		privateKeyDataProvider = control.createMock(PrivateKeyDataProvider.class);
		licenseProvider = control.createMock(LicenseProvider.class);

		LicenseCreator.createInstance(keyPasswordProvider, privateKeyDataProvider);
		LicenseManager.createInstance(licenseProvider, keyPasswordProvider, publicKeyDataProvider, null, 5);

		KeyPair keyPair = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm).generateKeyPair();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
		IOUtils.write(Encryptor.encryptRaw(pkcs8EncodedKeySpec.getEncoded(), keyPassword), outputStream);
		encryptedPrivateKey = outputStream.toByteArray();

		outputStream = new ByteArrayOutputStream();
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
		IOUtils.write(Encryptor.encryptRaw(x509EncodedKeySpec.getEncoded(), keyPassword), outputStream);
		encryptedPublicKey = outputStream.toByteArray();
	}

	@Before
	public void setUp()
	{
		TestLicenseCreatorAndLicenseManagerInteraction.control.reset();
	}

	@After
	public void tearDown()
	{
		TestLicenseCreatorAndLicenseManagerInteraction.control.verify();
	}

	@Test
	public void firstTest()
	{
		EasyMock.expect(keyPasswordProvider.getKeyPassword()).andReturn(keyPassword.clone());
		EasyMock.expect(privateKeyDataProvider.getEncryptedPrivateKeyData()).andReturn(encryptedPrivateKey.clone());

		TestLicenseCreatorAndLicenseManagerInteraction.control.replay();

		License license = new License(
				new License.Builder().withSubject("myLicense").withNumberOfLicenses(22).withFeature("newFeature")
		);

		SignedLicense signedLicense = this.creator.signLicense(license);

		assertNotNull("The signed license should not be null.", signedLicense);
		assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
		assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

		TestLicenseCreatorAndLicenseManagerInteraction.control.verify();
		TestLicenseCreatorAndLicenseManagerInteraction.control.reset();

		EasyMock.expect(licenseProvider.getLicense("LICENSE-1")).andReturn(signedLicense);
		EasyMock.expect(keyPasswordProvider.getKeyPassword()).andReturn(keyPassword.clone());
		EasyMock.expect(publicKeyDataProvider.getEncryptedPublicKeyData()).andReturn(encryptedPublicKey.clone());

		TestLicenseCreatorAndLicenseManagerInteraction.control.replay();

		License returned = this.manager.getLicense("LICENSE-1");

		assertNotNull("The returned license should not be null.", returned);
		assertEquals("The returned license is not correct.", license, returned);
	}
}