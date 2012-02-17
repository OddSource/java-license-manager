/*
 * LicenseCreator.java from LicenseManager modified Thursday, February 16, 2012 21:02:44 CST (-0600).
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
import net.nicholaswilliams.java.licensing.ObjectSerializer;
import net.nicholaswilliams.java.licensing.SignedLicense;
import net.nicholaswilliams.java.licensing.encryption.Encryptor;
import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.exception.ObjectSerializationException;

import java.security.PrivateKey;
import java.util.Arrays;

/**
 * This class manages the creation of licenses in the master application. Use this class within your license generation
 * software to sign and serialize license for distribution to the client. This class is not needed for the client
 * application, and in fact you should <b>not</b> use this class in your client application. For this reason, it is
 * in the package of classes (net.nicholaswilliams.java.licensing.licensor) that is packaged separately from the
 * distributable client binary.<br />
 * <br />
 * The license creator is first initialized by calling
 * {@link #createInstance(KeyPasswordProvider, PrivateKeyDataProvider)}, which also returns the singleton instance
 * created. Then all future references to the license manager are obtained by calling {@link #getInstance()}.
 *
 * @author Nick Williams
 * @version 1.0.6
 * @since 1.0.0
 */
public final class LicenseCreator
{
	private static LicenseCreator instance = null;

	private final KeyPasswordProvider passwordProvider;

	private final PrivateKeyDataProvider privateKeyDataProvider;

	private LicenseCreator(KeyPasswordProvider passwordProvider,
						   PrivateKeyDataProvider privateKeyLocationProvider)
	{
		this.passwordProvider = passwordProvider;
		this.privateKeyDataProvider = privateKeyLocationProvider;
	}

	/**
	 * The first time this is called, it creates and returns a license creator with the given providers. All subsequent
	 * calls are equivalent to calling {@link #getInstance()} (i.e., the parameters are ignored
	 * and the previously-created instance is returned).
	 *
	 * @param passwordProvider The provider of the password for decrypting the license key
	 * @param privateKeyDataProvider The provider of the data for the private key used to sign the license object.
	 * @return the created instance of the license creator.
	 * @throws IllegalArgumentException if {@code passwordProvider} or {@code privateKeyDataProvider} are null
	 */
	public static synchronized LicenseCreator createInstance(KeyPasswordProvider passwordProvider,
															 PrivateKeyDataProvider privateKeyDataProvider)
	{
		if(LicenseCreator.instance == null)
		{
			LicenseCreator.instance = new LicenseCreator(passwordProvider, privateKeyDataProvider);
		}

		return LicenseCreator.instance;
	}

	/**
	 * Returns the license creator instance previously created by
	 * {@link #createInstance(KeyPasswordProvider, PrivateKeyDataProvider)}. If this method is
	 * called before {@code createInstance()}, a {@link RuntimeException} is thrown.
	 *
	 * @return the license creator instance.
	 * @throws RuntimeException if no instance has yet be created by {@code createInstance()}.
	 */
	public static synchronized LicenseCreator getInstance()
	{
		if(LicenseCreator.instance == null)
			throw new RuntimeException("The LicenseCreator instance has not been created yet. Please create it with createInstance().");

		return LicenseCreator.instance;
	}

	/**
	 * Takes a license object and creates a secure version of it for serialization and delivery to the customer.
	 *
	 * @param license The license object to be signed
	 * @return the signed license object.
	 * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
	 * @throws KeyNotFoundException if the public key data could not be found.
	 * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
	 * @throws InappropriateKeyException if the key type and cipher type do not match.
	 */
	public final SignedLicense signLicense(License license)
			throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeySpecificationException,
				   InappropriateKeyException
	{
		PrivateKey key;
		{
			char[] password = this.passwordProvider.getKeyPassword();
			byte[] keyData = this.privateKeyDataProvider.getEncryptedPrivateKeyData();

			key = KeyFileUtilities.readEncryptedPrivateKey(keyData, password);

			Arrays.fill(password, '\u0000');
			Arrays.fill(keyData, (byte)0);
		}

		byte[] encrypted = Encryptor.encryptRaw(license.serialize());

		byte[] signature = new DataSignatureManager().signData(key, encrypted);

		SignedLicense signed = new SignedLicense(encrypted, signature);

		Arrays.fill(encrypted, (byte)0);
		Arrays.fill(signature, (byte)0);

		return signed;
	}

	/**
	 * Takes a license object and creates a secure and serialized version of it for delivery to the customer.
	 *
	 * @param license The license object to be signed and serialized
	 * @return the signed and serialized license object.
	 * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
	 * @throws KeyNotFoundException if the public key data could not be found.
	 * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
	 * @throws InappropriateKeyException if the key type and cipher type do not match.
	 * @throws ObjectSerializationException if an error is encountered while serializing the key.
	 */
	public final byte[] signAndSerializeLicense(License license)
			throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeySpecificationException,
				   InappropriateKeyException, ObjectSerializationException
	{
		return new ObjectSerializer().writeObject(this.signLicense(license));
	}
}
