/*
 * LicenseCreator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:10 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.licensor;

import net.nicholaswilliams.java.licensing.DataSignatureManager;
import net.nicholaswilliams.java.licensing.License;
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
 * This class manages the creation of licenses in the master application.
 *
 * @author Nick Williams
 * @version 1.0.5
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

	public static synchronized LicenseCreator createInstance(KeyPasswordProvider passwordProvider,
															 PrivateKeyDataProvider privateKeyDataProvider)
	{
		if(LicenseCreator.instance == null)
		{
			LicenseCreator.instance = new LicenseCreator(passwordProvider, privateKeyDataProvider);
		}

		return LicenseCreator.instance;
	}

	public static synchronized LicenseCreator getInstance()
	{
		if(LicenseCreator.instance == null)
			throw new RuntimeException("The LicenseCreator instance has not been created yet. Please create it with createInstance().");

		return LicenseCreator.instance;
	}

	public final SignedLicense signLicense(License license)
			throws AlgorithmNotSupportedException, KeyNotFoundException, InappropriateKeySpecificationException,
				   InappropriateKeyException, ObjectSerializationException
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
}
