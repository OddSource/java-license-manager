/*
 * TestLicenseManager.java from LicenseManager modified Tuesday, February 21, 2012 10:56:33 CST (-0600).
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

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.encryption.Encryptor;
import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;
import net.nicholaswilliams.java.licensing.exception.ExpiredLicenseException;
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
import java.security.PrivateKey;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.*;

/**
 * Test class for LicenseManager.
 */
public class TestLicenseManager
{
	private static final char[] keyPassword = "testLicenseManagerPassword".toCharArray();

	private static LicenseProvider licenseProvider;

	private static KeyPasswordProvider keyPasswordProvider;

	private static PublicKeyDataProvider keyDataProvider;

	private static LicenseValidator licenseValidator;

	private static IMocksControl control;

	private static PrivateKey privateKey;

	private static byte[] encryptedPublicKey;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		TestLicenseManager.control = EasyMock.createStrictControl();

		TestLicenseManager.licenseProvider = TestLicenseManager.control.createMock(LicenseProvider.class);
		TestLicenseManager.keyPasswordProvider = TestLicenseManager.control.createMock(KeyPasswordProvider.class);
		TestLicenseManager.keyDataProvider = TestLicenseManager.control.createMock(PublicKeyDataProvider.class);
		TestLicenseManager.licenseValidator = TestLicenseManager.control.createMock(LicenseValidator.class);

		Properties.setLicenseProvider(TestLicenseManager.licenseProvider);
		Properties.setPasswordProvider(TestLicenseManager.keyPasswordProvider);
		Properties.setPublicKeyDataProvider(TestLicenseManager.keyDataProvider);
		Properties.setLicenseValidator(TestLicenseManager.licenseValidator);
		Properties.setCacheTimeInMinutes(0);

		LicenseManager.getInstance();

		KeyPair keyPair = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm).generateKeyPair();

		TestLicenseManager.privateKey = keyPair.getPrivate();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
		IOUtils.write(Encryptor.encryptRaw(x509EncodedKeySpec.getEncoded(), keyPassword), outputStream);
		TestLicenseManager.encryptedPublicKey = outputStream.toByteArray();
	}

	private LicenseManager manager;

	public TestLicenseManager()
	{
		this.manager = LicenseManager.getInstance();
	}

	@Before
	public void setUp()
	{
		TestLicenseManager.control.reset();
	}

	@After
	public void tearDown()
	{
		TestLicenseManager.control.verify();
	}

	@Test
	public void testGetLicense01() throws Exception
	{
		EasyMock.expect(TestLicenseManager.licenseProvider.getLicense("ACCOUNT-1")).andReturn(null);
		TestLicenseManager.control.replay();

		assertNull("The returned license should be null.", this.manager.getLicense("ACCOUNT-1"));
	}

	@Test
	public void testGetLicense02() throws Exception
	{
		License license = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("nickFeature1").
									withFeature("allisonFeature2").
									build();

		byte[] data = Encryptor.encryptRaw(license.serialize());
		byte[] signature = new DataSignatureManager().signData(TestLicenseManager.privateKey, data);

		EasyMock.expect(TestLicenseManager.licenseProvider.getLicense("LICENSE-2")).andReturn(new SignedLicense(data, signature));
		EasyMock.expect(TestLicenseManager.keyPasswordProvider.getKeyPassword()).andReturn(keyPassword.clone());
		EasyMock.expect(TestLicenseManager.keyDataProvider.getEncryptedPublicKeyData()).andReturn(encryptedPublicKey.clone());
		TestLicenseManager.control.replay();

		License returned = this.manager.getLicense("LICENSE-2");

		assertNotNull("The returned license should not be null.", returned);
		assertEquals("The returned license is not correct.", license, returned);
	}

	public License setupLicenseMocking(String context)
	{
		License license = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=NWTS, C=US, ST=TN").
									withHolder("CN=Joe Customer, C=CA, ST=QE").
									withSubject("NWTS Database Browser/v.9.5").
									withIssueDate(23481149385711L).
									withGoodAfterDate(2348114987000L).
									withGoodBeforeDate(2348914987000L).
									withNumberOfLicenses(5).
									withFeature("feature#1").
									withFeature("feature#2").
									withFeature("feature#5").
									build();

		byte[] data = Encryptor.encryptRaw(license.serialize());
		byte[] signature = new DataSignatureManager().signData(TestLicenseManager.privateKey, data);

		EasyMock.expect(TestLicenseManager.licenseProvider.getLicense(context)).andReturn(new SignedLicense(data, signature));
		EasyMock.expect(TestLicenseManager.keyPasswordProvider.getKeyPassword()).andReturn(keyPassword.clone());
		EasyMock.expect(TestLicenseManager.keyDataProvider.getEncryptedPublicKeyData()).andReturn(encryptedPublicKey.clone());

		return license;
	}

	@Test
	public void testGetLicense03() throws Exception
	{
		License license = this.setupLicenseMocking("CUSTOMER-3");
		TestLicenseManager.control.replay();

		License returned = this.manager.getLicense("CUSTOMER-3");

		assertNotNull("The returned license should not be null.", returned);
		assertEquals("The returned license is not correct.", license, returned);

		Thread.sleep(5000);

		License returnedAgain = this.manager.getLicense("CUSTOMER-3");

		assertSame("The returned objects should be the same.", returned, returnedAgain);

		Thread.sleep(7000);

		TestLicenseManager.control.verify();
		TestLicenseManager.control.reset();
		EasyMock.expect(TestLicenseManager.licenseProvider.getLicense("CUSTOMER-3")).andReturn(null);
		TestLicenseManager.control.replay();

		assertNull("The license should be null now.", this.manager.getLicense("CUSTOMER-3"));
	}

	@Test
	public void testGetLicense04() throws Exception
	{
		License license = this.setupLicenseMocking("CUSTOMER-4");
		TestLicenseManager.control.replay();

		License returned = this.manager.getLicense("CUSTOMER-4");

		assertNotNull("The returned license should not be null.", returned);
		assertEquals("The returned license is not correct.", license, returned);

		Thread.sleep(5000);

		License returnedAgain = this.manager.getLicense("CUSTOMER-4");

		assertSame("The returned objects should be the same.", returned, returnedAgain);

		Thread.sleep(7000);

		TestLicenseManager.control.verify();
		TestLicenseManager.control.reset();
		license = this.setupLicenseMocking("CUSTOMER-4");
		TestLicenseManager.control.replay();

		returnedAgain = this.manager.getLicense("CUSTOMER-4");

		assertNotNull("The returned license should not be null.", returnedAgain);
		assertEquals("The returned license is not correct.", license, returnedAgain);
		assertNotSame("The returned objects should not be the same anymore.", returned, returnedAgain);
	}

	@Test
	public void testValidateLicense01()
	{
		License license = new License.Builder().build();

		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		this.manager.validateLicense(license);
	}

	@Test(expected=ExpiredLicenseException.class)
	public void testValidateLicense02()
	{
		License license = new License.Builder().build();

		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall().andThrow(new ExpiredLicenseException());
		TestLicenseManager.control.replay();

		this.manager.validateLicense(license);
	}

	@Test
	public void testHasLicenseForFeature01()
	{
		License license = this.setupLicenseMocking("LICENSE-ONE-1");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeature("LICENSE-ONE-1", "feature#1"));
	}

	@Test
	public void testHasLicenseForFeature02()
	{
		License license = this.setupLicenseMocking("LICENSE-ONE-2");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeature("LICENSE-ONE-2", "feature#2"));
	}

	@Test
	public void testHasLicenseForFeature03()
	{
		License license = this.setupLicenseMocking("LICENSE-ONE-3");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeature("LICENSE-ONE-3", "feature#3"));
	}

	@Test
	public void testHasLicenseForAllFeatures01()
	{
		License license = this.setupLicenseMocking("LICENSE-ALL-1");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAllFeatures("LICENSE-ALL-1", "feature#1"));
	}

	@Test
	public void testHasLicenseForAllFeatures02()
	{
		License license = this.setupLicenseMocking("LICENSE-ALL-2");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAllFeatures("LICENSE-ALL-2", "feature#1", "feature#2"));
	}

	@Test
	public void testHasLicenseForAllFeatures03()
	{
		License license = this.setupLicenseMocking("LICENSE-ALL-3");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAllFeatures("LICENSE-ALL-3", "feature#2", "feature#5", "feature#1"));
	}

	@Test
	public void testHasLicenseForAllFeatures04()
	{
		License license = this.setupLicenseMocking("LICENSE-ALL-4");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForAllFeatures("LICENSE-ALL-4", "feature#6"));
	}

	@Test
	public void testHasLicenseForAllFeatures05()
	{
		License license = this.setupLicenseMocking("LICENSE-ALL-5");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForAllFeatures("LICENSE-ALL-5", "feature#2",
																								"feature#5",
																								"feature#1",
																								"feature#3"));
	}

	@Test
	public void testHasLicenseForAnyFeatures01()
	{
		License license = this.setupLicenseMocking("LICENSE-ANY--1");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAnyFeature("LICENSE-ANY--1", "feature#1"));
	}

	@Test
	public void testHasLicenseForAnyFeatures02()
	{
		License license = this.setupLicenseMocking("LICENSE-ANY--2");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAnyFeature("LICENSE-ANY--2", "feature#1",
																							  "feature#2"));
	}

	@Test
	public void testHasLicenseForAnyFeatures03()
	{
		License license = this.setupLicenseMocking("LICENSE-ANY--3");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAnyFeature("LICENSE-ANY--3", "feature#2",
																							  "feature#5", "feature#1"));
	}

	@Test
	public void testHasLicenseForAnyFeatures04()
	{
		License license = this.setupLicenseMocking("LICENSE-ANY--4");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForAnyFeature("LICENSE-ANY--4", "feature#6"));
	}

	@Test
	public void testHasLicenseForAnyFeatures05()
	{
		License license = this.setupLicenseMocking("LICENSE-ANY--5");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForAnyFeature("LICENSE-ANY--5", "feature#5",
																							  "feature#3"));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation01() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-1");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#5"})
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-1", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation02() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-2");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#3"})
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-2", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation03() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-3");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#6", "feature#3"})
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-3", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation04() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-4");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#5"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-4", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation05() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-5");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#3"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-5", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByAnnotation06() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-ANNOTATION-6");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#6", "feature#3"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		FeatureRestriction annotation = object.getClass().getMethod("method").getAnnotation(FeatureRestriction.class);

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-ANNOTATION-6", annotation));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget01() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-1");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#5"})
			public void method()
			{

			}
		};

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-1", object.getClass().getMethod("method")));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget02() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-2");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#3"})
			public void method()
			{

			}
		};

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-2", object.getClass().getMethod("method")));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget03() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-3");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#6", "feature#3"})
			public void method()
			{

			}
		};

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-3", object.getClass().getMethod("method")));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget04() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-4");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#5"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-4", object.getClass().getMethod("method")));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget05() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-5");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#1", "feature#3"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		assertTrue("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-5", object.getClass().getMethod("method")));
	}

	@Test
	public void testHasLicenseForFeaturesByTarget07() throws NoSuchMethodException
	{
		License license = this.setupLicenseMocking("LICENSE-TARGET-6");
		TestLicenseManager.licenseValidator.validateLicense(license);
		EasyMock.expectLastCall();
		TestLicenseManager.control.replay();

		Object object = new Object() {
			@FeatureRestriction(value={"feature#6", "feature#3"}, operand=FeatureRestrictionOperand.OR)
			public void method()
			{

			}
		};

		assertFalse("The returned value is not correct.", this.manager.hasLicenseForFeatures("LICENSE-TARGET-6", object.getClass().getMethod("method")));
	}
}