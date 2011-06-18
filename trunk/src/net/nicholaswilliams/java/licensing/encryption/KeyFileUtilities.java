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

package net.nicholaswilliams.java.licensing.encryption;

import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * A class of utility methods for reading and writing private and public keys
 * to files.
 *
 * @author Nicholas Williamo
 * @since 1.0.0
 * @version 1.0.0
 */
public class KeyFileUtilities
{
	public static final String keyAlgorithm = "RSA";

	protected static void writeEncryptedPrivateKey(PrivateKey privateKey,
												   File file, char[] passphrase)
			throws IOException
	{
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded()
		);
		FileUtils.writeByteArrayToFile(file, Encryptor.encryptRaw(
				pkcs8EncodedKeySpec.getEncoded(), passphrase
		));
	}

	protected static void writeEncryptedPublicKey(PublicKey publicKey,
												  File file, char[] passphrase)
			throws IOException
	{
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded()
		);
		FileUtils.writeByteArrayToFile(file, Encryptor.encryptRaw(
				x509EncodedKeySpec.getEncoded(), passphrase
		));
	}

	protected static PrivateKey readEncryptedPrivateKey(File file,
														char[] passphrase)
			throws IOException
	{
		return KeyFileUtilities.readEncryptedPrivateKey(
				FileUtils.readFileToByteArray(file),
				passphrase
		);
	}

	protected static PublicKey readEncryptedPublicKey(File file,
													  char[] passphrase)
			throws IOException
	{
		return KeyFileUtilities.readEncryptedPublicKey(
				FileUtils.readFileToByteArray(file),
				passphrase
		);
	}

	public static PrivateKey readEncryptedPrivateKey(byte[] fileContents,
														char[] passphrase)
	{
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				Encryptor.decryptRaw(fileContents, passphrase)
		);

		try
		{
			return KeyFactory.getInstance(KeyFileUtilities.keyAlgorithm).
					generatePrivate(privateKeySpec);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new AlgorithmNotSupportedException(KeyFileUtilities.keyAlgorithm, e);
		}
		catch(InvalidKeySpecException e)
		{
			throw new InappropriateKeySpecificationException(e);
		}
	}

	public static PublicKey readEncryptedPublicKey(byte[] fileContents,
													  char[] passphrase)
	{
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				Encryptor.decryptRaw(fileContents, passphrase)
		);

		try
		{
			return KeyFactory.getInstance(KeyFileUtilities.keyAlgorithm).
					generatePublic(publicKeySpec);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new AlgorithmNotSupportedException(KeyFileUtilities.keyAlgorithm, e);
		}
		catch(InvalidKeySpecException e)
		{
			throw new InappropriateKeySpecificationException(e);
		}
	}

	private KeyFileUtilities()
	{
		throw new RuntimeException("This class cannot be instantiated.");
	}
}