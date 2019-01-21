/*
 * TestLicenseCreator.java from LicenseManager modified Thursday, January 24, 2013 16:37:10 CST (-0600).
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

package io.oddsource.java.licensing.licensor;

import io.oddsource.java.licensing.encryption.PrivateKeyDataProvider;
import io.oddsource.java.licensing.DataSignatureManager;
import io.oddsource.java.licensing.License;
import io.oddsource.java.licensing.MockLicenseHelper;
import io.oddsource.java.licensing.ObjectSerializer;
import io.oddsource.java.licensing.SignedLicense;
import io.oddsource.java.licensing.encryption.Encryptor;
import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.encryption.PasswordProvider;
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

    private static final char[] licensePassword = "testLicensePassword".toCharArray();

    private static PasswordProvider passwordProvider;

    private static PrivateKeyDataProvider keyDataProvider;

    private static IMocksControl control;

    private static byte[] encryptedPrivateKey;

    private static PublicKey publicKey;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestLicenseCreator.control = EasyMock.createStrictControl();

        TestLicenseCreator.passwordProvider = TestLicenseCreator.control.createMock(PasswordProvider.class);
        TestLicenseCreator.keyDataProvider = TestLicenseCreator.control.createMock(PrivateKeyDataProvider.class);

        try
        {
            LicenseCreator.getInstance();
            fail("Expected java.lang.IllegalArgumentException, got no exception.");
        }
        catch(IllegalArgumentException ignore) { }

        LicenseCreatorProperties.setPrivateKeyDataProvider(TestLicenseCreator.keyDataProvider);

        try
        {
            LicenseCreator.getInstance();
            fail("Expected java.lang.IllegalArgumentException, got no exception.");
        }
        catch(IllegalArgumentException ignore) { }

        LicenseCreatorProperties.setPrivateKeyPasswordProvider(TestLicenseCreator.passwordProvider);

        LicenseCreator.getInstance();

        KeyPair keyPair = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm).generateKeyPair();

        TestLicenseCreator.publicKey = keyPair.getPublic();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
        IOUtils.write(Encryptor.encryptRaw(pkcs8EncodedKeySpec.getEncoded(), keyPassword), outputStream);
        TestLicenseCreator.encryptedPrivateKey = outputStream.toByteArray();
    }

    private LicenseCreator creator;

    public TestLicenseCreator()
    {
        this.creator = LicenseCreator.getInstance();
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
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
                andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

        TestLicenseCreator.control.replay();

        License license = new License.Builder().
                                    withSubject("myLicense").
                                    withNumberOfLicenses(22).
                                    addFeature("newFeature").
                                    build();

        SignedLicense signedLicense = this.creator.signLicense(license);

        assertNotNull("The signed license should not be null.", signedLicense);
        assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
        assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

        new DataSignatureManager().verifySignature(
                TestLicenseCreator.publicKey, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
        );

        byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent(), TestLicenseCreator.keyPassword);

        assertNotNull("The unencrypted license data should not be null.", unencrypted);

        License returned = MockLicenseHelper.deserialize(unencrypted);

        assertNotNull("The returned license should not be null.", returned);

        assertEquals("The license should be equal.", license, returned);
    }

    @Test
    public void testLicenseSigning02()
    {
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
                andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

        TestLicenseCreator.control.replay();

        License license = new License.Builder().
                                    withSubject("myLicense").
                                    withNumberOfLicenses(22).
                                    addFeature("newFeature").
                                    build();

        SignedLicense signedLicense = this.creator.signLicense(license, TestLicenseCreator.licensePassword);

        assertNotNull("The signed license should not be null.", signedLicense);
        assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
        assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

        new DataSignatureManager().verifySignature(
                TestLicenseCreator.publicKey, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
        );

        byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent(), TestLicenseCreator.licensePassword);

        assertNotNull("The unencrypted license data should not be null.", unencrypted);

        License returned = MockLicenseHelper.deserialize(unencrypted);

        assertNotNull("The returned license should not be null.", returned);

        assertEquals("The license should be equal.", license, returned);
    }

    @Test
    public void testLicenseSigningAndSerializing01()
    {
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
                andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

        TestLicenseCreator.control.replay();

        License license = new License.Builder().
                                    withSubject("myLicense").
                                    withNumberOfLicenses(22).
                                    addFeature("newFeature").
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

        byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent(), TestLicenseCreator.keyPassword);

        assertNotNull("The unencrypted license data should not be null.", unencrypted);

        License returned = MockLicenseHelper.deserialize(unencrypted);

        assertNotNull("The returned license should not be null.", returned);

        assertEquals("The license should be equal.", license, returned);
    }

    @Test
    public void testLicenseSigningAndSerializing02()
    {
        EasyMock.expect(TestLicenseCreator.passwordProvider.getPassword()).
                andReturn(TestLicenseCreator.keyPassword.clone());
        EasyMock.expect(TestLicenseCreator.keyDataProvider.getEncryptedPrivateKeyData()).
                andReturn(TestLicenseCreator.encryptedPrivateKey.clone());

        TestLicenseCreator.control.replay();

        License license = new License.Builder().
                                    withSubject("myLicense").
                                    withNumberOfLicenses(22).
                                    addFeature("newFeature").
                                    build();

        byte[] signedLicenseData = this.creator.signAndSerializeLicense(license, TestLicenseCreator.licensePassword);

        assertNotNull("The signed license data should not be null.", signedLicenseData);
        assertTrue("The signed license data should not be blank.", signedLicenseData.length > 0);

        SignedLicense signedLicense = new ObjectSerializer().readObject(SignedLicense.class, signedLicenseData);

        assertNotNull("The signed license should not be null.", signedLicense);
        assertNotNull("The license signature should not be null.", signedLicense.getSignatureContent());
        assertNotNull("The license content should not be null.", signedLicense.getLicenseContent());

        new DataSignatureManager().verifySignature(
                TestLicenseCreator.publicKey, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
        );

        byte[] unencrypted = Encryptor.decryptRaw(signedLicense.getLicenseContent(), TestLicenseCreator.licensePassword);

        assertNotNull("The unencrypted license data should not be null.", unencrypted);

        License returned = MockLicenseHelper.deserialize(unencrypted);

        assertNotNull("The returned license should not be null.", returned);

        assertEquals("The license should be equal.", license, returned);
    }
}