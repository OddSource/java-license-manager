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

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.CorruptSignatureException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InvalidSignatureException;

import java.security.*;

/**
 * This class manages the signatures for objects.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DataSignatureManager
{
	public final byte[] signData(PrivateKey key, byte[] data)
			throws AlgorithmNotSupportedException, InappropriateKeyException
	{
		Signature signature = this.getSignature();

		try
		{
			signature.initSign(key);
		}
		catch(InvalidKeyException e)
		{
			throw new InappropriateKeyException("While initializing the signature object with the public key.", e);
		}

		try
		{
			signature.update(data);
		}
		catch(SignatureException e)
		{
			throw new RuntimeException("This should never happen.", e);
		}

		try
		{
			return signature.sign();
		}
		catch(SignatureException e)
		{
			throw new RuntimeException("This should never happen.", e);
		}
	}

	final void verifySignature(PublicKey key, byte[] data, byte[] signatureContent)
			throws AlgorithmNotSupportedException, InappropriateKeyException,
				   CorruptSignatureException, InvalidSignatureException
	{
		Signature signature = this.getSignature();

		try
		{
			signature.initVerify(key);
		}
		catch(InvalidKeyException e)
		{
			throw new InappropriateKeyException("While initializing the signature object with the public key.", e);
		}

		try
		{
			signature.update(data);
		}
		catch(SignatureException e)
		{
			throw new RuntimeException("This should never happen.", e);
		}

		try
		{
			if(!signature.verify(signatureContent))
				throw new InvalidSignatureException("The license signature is invalid.");
		}
		catch(SignatureException e)
		{
			throw new CorruptSignatureException("While verifying the signature.", e);
		}
	}

	@SuppressWarnings("FinalPrivateMethod")
	private final Signature getSignature()
	{
		try
		{
			return Signature.getInstance("SHA1with" + KeyFileUtilities.keyAlgorithm);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new AlgorithmNotSupportedException("SHA-1 with " + KeyFileUtilities.keyAlgorithm);
		}
	}
}
