/*
 * TestLicenseCreator.java from LicenseManager modified Thursday, February 16, 2012 21:07:44 CST (-0600).
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

import net.nicholaswilliams.java.licensing.DataSignatureManager;
import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.LicenseHelper;
import net.nicholaswilliams.java.licensing.ObjectSerializer;
import net.nicholaswilliams.java.licensing.SignedLicense;
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
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.junit.Assert.*;

/**
 * Test class for LicenseCreator.
 */
public class TestLicenseCreator
{
	private static final char[] keyPassword = "testLicenseCreatorPassword".toCharArray();

	private static KeyPasswordProvider keyPasswordProvider;

	private static PrivateKeyDataProvider keyDataProvider;

	private static IMocksControl control;

	private static byte[] encryptedPrivateKey;

	private static PublicKey publicKey;

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

		LicenseCreator.createInstance(TestLicenseCreator.keyPasswordProvider, TestLicenseCreator.keyDataProvider);

		KeyPair keyPair = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm).generateKeyPair();

		TestLicenseCreator.publicKey = keyPair.getPublic();

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
	public void testLicenseSigning01()
	{
		EasyMock.expect(TestLicenseCreator.keyPasswordProvider.getKeyPassword()).
				andReturn(TestLicenseCreator.keyPassword.clone());
		EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
				andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

		TestLicenseCreator.control.replay();

		License license = new License.Builder().
									withSubject("myLicense").
									withNumberOfLicenses(22).
									withFeature("newFeature").
									build();

		SignedLicense signedLicense = this.creator.signLicense(license);

		assertNotNull("The signed license should not be null.", signedLicense);
		assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
		assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

		new DataSignatureManager().verifySignature(
				TestLicenseCreator.publicKey, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
		);

		byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent());

		assertNotNull("The unencrypted license data should not be null.", unencrypted);

		License returned = LicenseHelper.deserialize(unencrypted);

		assertNotNull("The returned license should not be null.", returned);

		assertEquals("The license should be equal.", license, returned);
	}

	@Test
	public void testLicenseSigningAndSerializing01()
	{
		EasyMock.expect(TestLicenseCreator.keyPasswordProvider.getKeyPassword()).
				andReturn(TestLicenseCreator.keyPassword.clone());
		EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
				andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

		TestLicenseCreator.control.replay();

		License license = new License.Builder().
									withSubject("myLicense").
									withNumberOfLicenses(22).
									withFeature("newFeature").
									build();

		byte[] signedLicenseData = this.creator.signAndSerializeLicense(license);

		assertNotNull("The signed license data should not be null.", signedLicenseData);
		assertTrue("The signed license data should not be blank.", signedLicenseData.length > 0);

		SignedLicense signedLicense = new ObjectSerializer().readObject(SignedLicense.class, signedLicenseData);

		assertNotNull("The signed license should not be null.", signedLicense);
		assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
		assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

		new DataSignatureManager().verifySignature(
				TestLicenseCreator.publicKey, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
		);

		byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent());

		assertNotNull("The unencrypted license data should not be null.", unencrypted);

		License returned = LicenseHelper.deserialize(unencrypted);

		assertNotNull("The returned license should not be null.", returned);

		assertEquals("The license should be equal.", license, returned);
	}
}