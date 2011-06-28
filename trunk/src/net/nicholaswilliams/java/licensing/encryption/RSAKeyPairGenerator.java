/*
 * RSAKeyPairGenerator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:10 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.encryption;

import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.RSA2048NotSupportedException;

import java.io.File;
import java.io.IOException;
import java.security.*;

/**
 * The generator one should use to create public/private key pairs for use with
 * the application.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
public final class RSAKeyPairGenerator implements RSAKeyPairGeneratorInterface
{
	public RSAKeyPairGenerator()
	{
		
	}

	/**
	 * Checks whether the two passwords match.
	 *
	 * @param password1 The entered password
	 * @param password2 The confirmed password
	 * @return {@code true} if the passwords match, {@code false} otherwise.
	 */
	@Override
	public boolean passwordsMatch(char[] password1, char[] password2)
	{
		if(password1.length != password2.length)
			return false;

		for(int i = 0; i < password1.length; i++)
		{
			if(password1[i] != password2[i])
				return false;
		}

		return true;
	}

	/**
	 * Generates a key pair with RSA 2048-bit security.
	 * @return a public/private key pair.
	 * @throws RSA2048NotSupportedException if RSA or 2048-bit encryption are not supported.
	 */
	@Override
	public KeyPair generateKeyPair() throws RSA2048NotSupportedException
	{
		KeyPairGenerator keyGenerator;
		
		try
		{
			keyGenerator = KeyPairGenerator.getInstance(
					KeyFileUtilities.keyAlgorithm
			);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new RSA2048NotSupportedException("RSA keys are not supported on your system. Contact your system administrator for assistance.", e);
		}

		try
		{
			keyGenerator.initialize(2048);
		}
		catch(InvalidParameterException e)
		{
			throw new RSA2048NotSupportedException("RSA is supported on your system, but 2048-bit keys are not. Contact your system administrator for assistance.", e);
		}

		return keyGenerator.generateKeyPair();
	}

	/**
	 * Saves the key pair specified to output files specified.
	 *
	 * @param keyPair The key pair to save to the files specified
	 * @param privateOutputFileName The name of the file to save the encrypted private key to
	 * @param publicOutputFileName The name of the file to save the encrypted public key to
	 * @param password The password to encrypt the keys with
	 * @throws IOException if an error occurs while writing to the files.
	 * @throws AlgorithmNotSupportedException If the encryption algorithm is not supported
	 * @throws InappropriateKeyException If the public or private keys are invalid
	 * @throws InappropriateKeySpecificationException If the public or private keys are invalid
	 */
	@Override
	public void saveKeyPairToFiles(KeyPair keyPair,
								   String privateOutputFileName,
								   String publicOutputFileName,
								   char[] password)
			throws IOException, AlgorithmNotSupportedException,
				   InappropriateKeyException,
				   InappropriateKeySpecificationException
	{
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		KeyFileUtilities.writeEncryptedPrivateKey(
				privateKey,
				new File(privateOutputFileName),
				password
		);
		KeyFileUtilities.writeEncryptedPublicKey(
				publicKey,
				new File(publicOutputFileName),
				password
		);
	}
}
