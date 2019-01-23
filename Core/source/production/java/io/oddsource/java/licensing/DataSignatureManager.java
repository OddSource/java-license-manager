/*
 * DataSignatureManager.java from LicenseManager modified Tuesday, February 21, 2012 10:59:35 CST (-0600).
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

package io.oddsource.java.licensing;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.CorruptSignatureException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InvalidSignatureException;

/**
 * This class manages the signatures for objects.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DataSignatureManager
{
    /**
     * Constructs a data signature manager.
     */
    public DataSignatureManager()
    {

    }

    /**
     * Signs the given data using the provided private key and returns the signature bytes.
     *
     * @param key The private key with which to sign the data
     * @param data The data to sign
     *
     * @return the signature content.
     *
     * @throws AlgorithmNotSupportedException if the signature algorithm is not supported.
     * @throws InappropriateKeyException if the key is not a proper key for signing.
     */
    public final byte[] signData(final PrivateKey key, final byte[] data)
        throws AlgorithmNotSupportedException, InappropriateKeyException
    {
        final Signature signature = this.getSignature();

        try
        {
            signature.initSign(key);
        }
        catch(final InvalidKeyException e)
        {
            throw new InappropriateKeyException("Failed to initialize the signature object with the private key.", e);
        }

        try
        {
            signature.update(data);
        }
        catch(final SignatureException e)
        {
            throw new RuntimeException("Programming error on signature.update(data) (sign).", e);
        }

        try
        {
            return signature.sign();
        }
        catch(final SignatureException e)
        {
            throw new RuntimeException("Programming error on signature.sign().", e);
        }
    }

    /**
     * Verifies the given signature on the given data using the given public key.
     *
     * @param key The public key with which to verify the signature
     * @param data The data that was signed
     * @param signatureContent The signature to verify
     *
     * @throws AlgorithmNotSupportedException if the signature algorithm is not supported.
     * @throws InappropriateKeyException if the key is not a proper key for signature verification.
     * @throws CorruptSignatureException if the signature was corrupt.
     * @throws InvalidSignatureException if the signature was not valid.
     */
    public final void verifySignature(final PublicKey key, final byte[] data, final byte[] signatureContent)
        throws AlgorithmNotSupportedException, InappropriateKeyException, CorruptSignatureException,
               InvalidSignatureException
    {
        final Signature signature = this.getSignature();

        try
        {
            signature.initVerify(key);
        }
        catch(final InvalidKeyException e)
        {
            throw new InappropriateKeyException("Failed to initialize the signature object with the public key.", e);
        }

        try
        {
            signature.update(data);
        }
        catch(final SignatureException e)
        {
            throw new RuntimeException("Programming error on signature.update(data) (verify).", e);
        }

        try
        {
            if(!signature.verify(signatureContent))
            {
                throw new InvalidSignatureException("The license signature is invalid.");
            }
        }
        catch(final SignatureException e)
        {
            throw new CorruptSignatureException("The license signature is corrupt.", e);
        }
    }

    private Signature getSignature()
    {
        try
        {
            return Signature.getInstance("SHA1with" + KeyFileUtilities.keyAlgorithm);
        }
        catch(final NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException("SHA-1 with " + KeyFileUtilities.keyAlgorithm);
        }
    }
}
