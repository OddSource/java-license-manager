/*
 * KeyFileUtilities.java from LicenseManager modified Tuesday, February 21, 2012 10:59:34 CST (-0600).
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

package io.oddsource.java.licensing.encryption;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.io.FileUtils;

import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;

/**
 * A class of utility methods for reading and writing private and public keys
 * to files.
 *
 * @author Nicholas Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class KeyFileUtilities
{
    /**
     * The standard key algorithm used for all of our keys.
     */
    public static final String keyAlgorithm = "RSA";

    private KeyFileUtilities()
    {
        throw new AssertionError("This class cannot be instantiated.");
    }

    /**
     * Encrypts and writes the private key to this file.
     *
     * @param privateKey The private key
     * @param file The file
     * @param passphrase The passphrase with which to protect the key
     *
     * @throws IOException if writing fails.
     */
    protected static void writeEncryptedPrivateKey(
        final PrivateKey privateKey,
        final File file,
        final char[] passphrase
    )
        throws IOException
    {

        FileUtils.writeByteArrayToFile(file, KeyFileUtilities.writeEncryptedPrivateKey(privateKey, passphrase));
    }

    /**
     * Encrypts and writes the private key to a byte array and returns it.
     *
     * @param privateKey The private key
     * @param passphrase The passphrase with which to protect the key
     *
     * @return the encrypted private key bytes.
     */
    protected static byte[] writeEncryptedPrivateKey(final PrivateKey privateKey, final char[] passphrase)
    {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        return Encryptor.encryptRaw(pkcs8EncodedKeySpec.getEncoded(), passphrase);
    }

    /**
     * Encrypts and writes the public key to this file.
     *
     * @param publicKey The public key
     * @param file The file
     * @param passphrase The passphrase with which to protect the key
     *
     * @throws IOException if writing fails.
     */
    protected static void writeEncryptedPublicKey(final PublicKey publicKey, final File file, final char[] passphrase)
        throws IOException
    {
        FileUtils.writeByteArrayToFile(file, KeyFileUtilities.writeEncryptedPublicKey(publicKey, passphrase));
    }

    /**
     * Encrypts and writes the public key to a byte array and returns it.
     *
     * @param publicKey The public key
     * @param passphrase The passphrase with which to protect the key
     *
     * @return the encrypted public key bytes.
     */
    protected static byte[] writeEncryptedPublicKey(final PublicKey publicKey, final char[] passphrase)
    {
        final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        return Encryptor.encryptRaw(x509EncodedKeySpec.getEncoded(), passphrase);
    }

    /**
     * Reads and decrypts the encrypted private key from this file.
     *
     * @param file The file
     * @param passphrase The passphrase with which the key was protected
     *
     * @return the read, decrypted private key.
     * @throws IOException if reading fails.
     */
    protected static PrivateKey readEncryptedPrivateKey(final File file, final char[] passphrase) throws IOException
    {
        return KeyFileUtilities.readEncryptedPrivateKey(FileUtils.readFileToByteArray(file), passphrase);
    }

    /**
     * Reads and decrypts the encrypted private key from the provided bytes.
     *
     * @param fileContents The encrypted key bytes
     * @param passphrase The passphrase with which the key was protected
     *
     * @return the decrypted private key.
     */
    public static PrivateKey readEncryptedPrivateKey(final byte[] fileContents, final char[] passphrase)
    {
        final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
            Encryptor.decryptRaw(fileContents, passphrase)
        );

        try
        {
            return KeyFactory.getInstance(KeyFileUtilities.keyAlgorithm).generatePrivate(privateKeySpec);
        }
        catch(final NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException(KeyFileUtilities.keyAlgorithm, e);
        }
        catch(final InvalidKeySpecException e)
        {
            throw new InappropriateKeySpecificationException(e);
        }
    }

    /**
     * Reads and decrypts the encrypted public key from this file.
     *
     * @param file The file
     * @param passphrase The passphrase with which the key was protected
     *
     * @return the read, decrypted public key.
     *
     * @throws IOException if reading fails.
     */
    protected static PublicKey readEncryptedPublicKey(final File file, final char[] passphrase) throws IOException
    {
        return KeyFileUtilities.readEncryptedPublicKey(FileUtils.readFileToByteArray(file), passphrase);
    }

    /**
     * Reads and decrypts the encrypted public key from the provided bytes.
     *
     * @param fileContents The encrypted key bytes
     * @param passphrase The passphrase with which the key was protected
     *
     * @return the decrypted public key.
     */
    public static PublicKey readEncryptedPublicKey(final byte[] fileContents, final char[] passphrase)
    {
        final X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Encryptor.decryptRaw(fileContents, passphrase));

        try
        {
            return KeyFactory.getInstance(KeyFileUtilities.keyAlgorithm).generatePublic(publicKeySpec);
        }
        catch(final NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException(KeyFileUtilities.keyAlgorithm, e);
        }
        catch(final InvalidKeySpecException e)
        {
            throw new InappropriateKeySpecificationException(e);
        }
    }
}
