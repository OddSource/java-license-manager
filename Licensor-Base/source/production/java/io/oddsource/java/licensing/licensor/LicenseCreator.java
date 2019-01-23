/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing.licensor;

import java.security.PrivateKey;
import java.util.Arrays;

import io.oddsource.java.licensing.DataSignatureManager;
import io.oddsource.java.licensing.License;
import io.oddsource.java.licensing.ObjectSerializer;
import io.oddsource.java.licensing.SignedLicense;
import io.oddsource.java.licensing.encryption.Encryptor;
import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PrivateKeyDataProvider;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.exception.ObjectSerializationException;

/**
 * This class manages the creation of licenses in the master application. Use this class within your license generation
 * software to sign and serialize license for distribution to the client. This class is not needed for the client
 * application, and in fact you should <b>not</b> use this class in your client application. For this reason, it is
 * in the package of classes (io.oddsource.java.licensing.licensor) that is packaged separately from the
 * distributable client binary.<br />
 * <br />
 * Before getting the creator instance for the first time, relevant properties should be set in
 * {@link LicenseCreatorProperties}. The values in this class will be used to instantiate the license creator.
 * After setting all the necessary properties there, one can retrieve an instance using {@link #getInstance()}. Be
 * sure to set all the properties first; once {@link #getInstance()} is called for the first time, any changes to
 * {@link LicenseCreatorProperties} will be ignored.<br />
 *
 * @author Nick Williams
 * @version 1.0.6
 * @since 1.0.0
 */
public final class LicenseCreator
{
    private static LicenseCreator instance;

    private final PrivateKeyDataProvider privateKeyDataProvider;

    private final PasswordProvider privateKeyPasswordProvider;

    private LicenseCreator()
    {
        if(LicenseCreatorProperties.getPrivateKeyDataProvider() == null)
        {
            throw new IllegalArgumentException("Parameter privateKeyDataProvider must not be null.");
        }

        if(LicenseCreatorProperties.getPrivateKeyPasswordProvider() == null)
        {
            throw new IllegalArgumentException("Parameter privateKeyPasswordProvider must not be null.");
        }

        this.privateKeyPasswordProvider = LicenseCreatorProperties.getPrivateKeyPasswordProvider();
        this.privateKeyDataProvider = LicenseCreatorProperties.getPrivateKeyDataProvider();
    }

    /**
     * Returns the license creator instance. Before this method can be called the first time, all of the parameters must
     * bet set in {@link LicenseCreatorProperties}. See the documentation for that class for more details.
     *
     * @return the license creator instance.
     *
     * @throws IllegalArgumentException if
     * {@link LicenseCreatorProperties#setPrivateKeyDataProvider(PrivateKeyDataProvider)
     *     privateKeyDataProvider} or {@link LicenseCreatorProperties#setPrivateKeyPasswordProvider(PasswordProvider)
     *     privateKeyPasswordProvider} are null
     */
    public static synchronized LicenseCreator getInstance()
    {
        if(LicenseCreator.instance == null)
        {
            LicenseCreator.instance = new LicenseCreator();
        }

        return LicenseCreator.instance;
    }

    /**
     * Takes a license object and creates a secure version of it for serialization and delivery to the customer.
     *
     * @param license The license object to be signed
     * @param licensePassword The password to encrypt the license with
     *
     * @return the signed license object.
     *
     * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
     * @throws KeyNotFoundException if the public key data could not be found.
     * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
     * @throws InappropriateKeyException if the key type and cipher type do not match.
     */
    public final SignedLicense signLicense(final License license, final char[] licensePassword)
        throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeySpecificationException,
               InappropriateKeyException
    {
        final char[] password = this.privateKeyPasswordProvider.getPassword();
        final byte[] keyData = this.privateKeyDataProvider.getEncryptedPrivateKeyData();

        final PrivateKey key = KeyFileUtilities.readEncryptedPrivateKey(keyData, password);

        Arrays.fill(password, '\u0000');
        Arrays.fill(keyData, (byte) 0);

        final byte[] encrypted = Encryptor.encryptRaw(license.serialize(), licensePassword);

        final byte[] signature = new DataSignatureManager().signData(key, encrypted);

        final SignedLicense signed = new SignedLicense(encrypted, signature);

        Arrays.fill(encrypted, (byte) 0);
        Arrays.fill(signature, (byte) 0);

        return signed;
    }

    /**
     * Takes a license object and creates a secure version of it for serialization and delivery to the customer.
     *
     * @param license The license object to be signed
     *
     * @return the signed license object.
     *
     * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
     * @throws KeyNotFoundException if the public key data could not be found.
     * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
     * @throws InappropriateKeyException if the key type and cipher type do not match.
     */
    public final SignedLicense signLicense(final License license)
        throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeySpecificationException,
               InappropriateKeyException
    {
        return this.signLicense(license, this.privateKeyPasswordProvider.getPassword());
    }

    /**
     * Takes a license object and creates a secure and serialized version of it for delivery to the customer.
     *
     * @param license The license object to be signed and serialized
     * @param licensePassword The password to encrypt the license with
     *
     * @return the signed and serialized license object.
     *
     * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
     * @throws KeyNotFoundException if the public key data could not be found.
     * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
     * @throws InappropriateKeyException if the key type and cipher type do not match.
     * @throws ObjectSerializationException if an error is encountered while serializing the key.
     */
    public final byte[] signAndSerializeLicense(final License license, final char[] licensePassword)
        throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeyException,
               ObjectSerializationException
    {
        return new ObjectSerializer().writeObject(this.signLicense(license, licensePassword));
    }

    /**
     * Takes a license object and creates a secure and serialized version of it for delivery to the customer.
     *
     * @param license The license object to be signed and serialized
     *
     * @return the signed and serialized license object.
     *
     * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
     * @throws KeyNotFoundException if the public key data could not be found.
     * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
     * @throws InappropriateKeyException if the key type and cipher type do not match.
     * @throws ObjectSerializationException if an error is encountered while serializing the key.
     */
    public final byte[] signAndSerializeLicense(final License license)
        throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeyException,
               ObjectSerializationException
    {
        return new ObjectSerializer().writeObject(this.signLicense(license));
    }
}
