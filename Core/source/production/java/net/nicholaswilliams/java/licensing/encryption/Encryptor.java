/*
 * Encryptor.java from LicenseManager modified Monday, April 8, 2013 12:11:51 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.encryption;

import net.nicholaswilliams.java.licensing.LicensingCharsets;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.FailedToDecryptException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * A class for easy, strong, two-way encryption/decryption of strings. Versions prior to 0.9.1-beta used 256-bit AES
 * encryption, which is not exportable, and will only work by default on Mac OS X and Linux. The Windows JVM will throw
 * an exception without the JCE Unlimited Strength policy file. Versions 0.9.1-beta and higher use 128-bit AES
 * encryption that is both exportable and platform-independent.<br />
 * <br />
 * This encryptor still uses a combination of MD5+DES and SHA-1+AES encryption.<br />
 * <br />
 * Data encrypted with this class prior to version 0.9.1-beta cannot be decrypted anymore.
 * 
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.5.0
 */
public final class Encryptor
{
    private static final int minimumPaddedLength = 20;

    /**
     * Encrypt the plain-text string using the default passphrase.
     * For encrypting, the data will first be padded to a safe number of
     * bytes with randomized data.
     *
     * @param unencrypted The plain-text string to encrypt
     * @return the encrypted string Base64-encoded.
     * @see Encryptor#pad(byte[], int)
     */
    public static String encrypt(String unencrypted)
    {
        return Encryptor.encrypt(unencrypted.getBytes(LicensingCharsets.UTF_8));
    }

    /**
     * Encrypt the plain-text string. For encrypting, the
     * string will first be padded to a safe number of
     * characters with randomized data.
     *
     * @param unencrypted The plain-text string to encrypt
     * @param passphrase The passphrase to encrypt the data with
     * @return the encrypted string Base64-encoded.
     */
    public static String encrypt(String unencrypted, char[] passphrase)
    {
        return Encryptor.encrypt(unencrypted.getBytes(LicensingCharsets.UTF_8), passphrase);
    }

    /**
     * Encrypt the binary data using the default passphrase.
     * For encrypting, the data will first be padded to a safe number of
     * bytes with randomized data.
     *
     * @param unencrypted The binary data to encrypt
     * @return the encrypted string Base64-encoded.
     * @see Encryptor#pad(byte[], int)
     */
    public static String encrypt(byte[] unencrypted)
    {
        return new String(
                Base64.encodeBase64URLSafe(Encryptor.encryptRaw(unencrypted)), LicensingCharsets.UTF_8
        );
    }

    /**
     * Encrypt the binary data. For encrypting, the
     * data will first be padded to a safe number of
     * bytes with randomized data.
     *
     * @param unencrypted The binary data to encrypt
     * @param passphrase The passphrase to encrypt the data with
     * @return the encrypted string Base64-encoded.
     * @see Encryptor#pad(byte[], int)
     */
    public static String encrypt(byte[] unencrypted, char[] passphrase)
    {
        return new String(
                Base64.encodeBase64URLSafe(Encryptor.encryptRaw(
                        unencrypted, passphrase
                )),
                LicensingCharsets.UTF_8
        );
    }

    /**
     * Encrypt the binary data using the default passphrase.
     * For encrypting, the data will first be padded to a safe number of
     * bytes with randomized data.
     *
     * @param unencrypted The binary data to encrypt
     * @return the encrypted data.
     * @see Encryptor#pad(byte[], int)
     */
    public static byte[] encryptRaw(byte[] unencrypted)
    {
        try
        {
            return Encryptor.getDefaultEncryptionCipher().doFinal(
                    Encryptor.pad(unencrypted, Encryptor.minimumPaddedLength)
            );
        }
        catch(IllegalBlockSizeException e)
        {
            throw new RuntimeException("While encrypting the data...", e);
        }
        catch(BadPaddingException e)
        {
            throw new RuntimeException("While encrypting the data...", e);
        }
    }

    /**
     * Encrypt the binary data. For encrypting, the
     * data will first be padded to a safe number of
     * bytes with randomized data.
     *
     * @param unencrypted The binary data to encrypt
     * @param passphrase The passphrase to encrypt the data with
     * @return the encrypted data.
     * @see Encryptor#pad(byte[], int)
     */
    public static byte[] encryptRaw(byte[] unencrypted, char[] passphrase)
    {
        try
        {
            return Encryptor.getEncryptionCipher(passphrase).doFinal(
                    Encryptor.pad(unencrypted, Encryptor.minimumPaddedLength)
            );
        }
        catch(IllegalBlockSizeException e)
        {
            throw new RuntimeException("While encrypting the data...", e);
        }
        catch(BadPaddingException e)
        {
            throw new RuntimeException("While encrypting the data...", e);
        }
    }

    /**
     * Decrypt an encrypted string using the default passphrase.
     * Any padded data will be removed from the string prior to its return.
     *
     * @param encrypted The encrypted string to decrypt
     * @return the decrypted string.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static String decrypt(String encrypted)
    {
        return Encryptor.decrypt(Base64.decodeBase64(encrypted));
    }

    /**
     * Decrypt an encrypted string. Any padded data will
     * be removed from the string prior to its return.
     *
     * @param encrypted The encrypted string to decrypt
     * @param passphrase The passphrase to decrypt the string with
     * @return the decrypted string.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static String decrypt(String encrypted, char[] passphrase)
    {
        return Encryptor.decrypt(Base64.decodeBase64(encrypted), passphrase);
    }

    /**
     * Decrypt encrypted data using the default passphrase.
     * Any padded data will be removed from the string prior to its return.
     *
     * @param encrypted The encrypted data to decrypt
     * @return the decrypted string.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static String decrypt(byte[] encrypted)
    {
        return new String(Encryptor.decryptRaw(encrypted), LicensingCharsets.UTF_8);
    }

    /**
     * Decrypt an encrypted data. Any padded data will
     * be removed from the string prior to its return.
     *
     * @param encrypted The encrypted data to decrypt
     * @param passphrase The passphrase to decrypt the data with
     * @return the decrypted string.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static String decrypt(byte[] encrypted, char[] passphrase)
    {
        return new String(
                Encryptor.decryptRaw(encrypted, passphrase), LicensingCharsets.UTF_8
        );
    }

    /**
     * Decrypt encrypted data using the default passphrase.
     * Any padded data will be removed from the string prior to its return.
     *
     * @param encrypted The encrypted data to decrypt
     * @return the decrypted binary data.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static byte[] decryptRaw(byte[] encrypted)
    {
        try
        {
            return Encryptor.unPad(
                    Encryptor.getDefaultDecryptionCipher().doFinal(encrypted)
            );
        }
        catch(IllegalBlockSizeException e)
        {
            throw new FailedToDecryptException(e);
        }
        catch(BadPaddingException e)
        {
            throw new FailedToDecryptException(e);
        }
    }

    /**
     * Decrypt encrypted data. Any padded data will
     * be removed from the string prior to its return.
     *
     * @param encrypted The encrypted data to decrypt
     * @param passphrase The passphrase to decrypt the data with
     * @return the decrypted binary data.
     * @throws FailedToDecryptException when the data was corrupt and undecryptable or when the provided decryption password was incorrect. It is impossible to know which is the actual cause.
     * @see Encryptor#unPad(byte[])
     */
    public static byte[] decryptRaw(byte[] encrypted, char[] passphrase)
    {
        try
        {
            return Encryptor.unPad(
                    Encryptor.getDecryptionCipher(passphrase).doFinal(encrypted)
            );
        }
        catch(IllegalBlockSizeException e)
        {
            throw new FailedToDecryptException(e);
        }
        catch(BadPaddingException e)
        {
            throw new FailedToDecryptException(e);
        }
    }

    private static final SecureRandom random = new SecureRandom();

    /**
     * Pads a {@code byte} array to the specified length.
     * The output is pretty simple. The begin {@code byte}s
     * are the values from {@code bytes}. The last
     * {@code byte}, when cast to an integer, indicates the
     * number of end {@code byte}s (including itself) that
     * make up the padding. The returned array will always
     * be at least one element longer than the input.<br/>
     * <br/>
     * For example, if passed an array of 5 {@code byte}s and
     * the length 10, the first five {@code byte}s will be the
     * values from {@code bytes}. {@code byte}s 6-10 (indexes
     * 5-9) will be randomized data and {@code byte} 11
     * (index 10) will be the integer 6 cast as a byte. The
     * actual returned array will be 11 {@code byte}s long.<br/>
     * <br/>
     * If passed an array of 10 {@code byte}s and the length
     * of 10, the first 10 {@code byte}s will be the input
     * and {@code byte} 11 will be 1.
     *
     * @param bytes The array of {@code byte}s to pad
     * @param length The length to pad the array of {@code byte}s to
     * @return the padded {@code byte} array.
     * @see Encryptor#unPad(byte[])
     */
    private static byte[] pad(byte[] bytes, int length)
    {
        if(bytes.length >= length)
        {
            byte[] out = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, out, 0, bytes.length);
            out[bytes.length] = (byte)1;
            return out;
        }

        byte[] out = new byte[length + 1];

        int i = 0;
        for(; i < bytes.length; i++)
            out[i] = bytes[i];

        int padded = length - i;

        // fill the rest with random bytes
        byte[] fill = new byte[padded - 1];
        Encryptor.random.nextBytes(fill);
        System.arraycopy(fill, 0, out, i, padded - 1);

        out[length] = (byte)(padded + 1);

        return out;
    }

    /**
     * Un-pads the specified array of {@code byte}s. Expects
     * an input that was padded with
     * {@link Encryptor#pad(byte[], int)}. Its behavior is
     * unspecified if passed an input that was not the
     * result of {@link Encryptor#pad(byte[], int)}.<br/>
     * <br/>
     * The returned array will be the {@code byte}s with all
     * the padding removed and the original {@code byte}s
     * left intact.
     *
     * @param bytes The array of {@code byte}s to un-pad
     * @return the un-padded {@code byte} array.
     * @see Encryptor#pad(byte[], int)
     */
    private static byte[] unPad(byte[] bytes)
    {
        int padded = (int)bytes[bytes.length - 1];
        int targetLength = bytes.length - padded;

        byte[] out = new byte[targetLength];

        System.arraycopy(bytes, 0, out, 0, targetLength);

        return out;
    }

    private static Cipher defaultEncryptionCipher;

    private static Cipher defaultDecryptionCipher;

    private static final char[] defaultPassphrase = {
            'j', '4', 'K', 'g', 'U', '3', '0', '5', 'P', 'Z', 'p', '\'', 't',
            '.', '"', '%', 'o', 'r', 'd', 'A', 'Y', '7', 'q', '*', '?', 'z',
            '9', '%', '8', ']', 'a', 'm', 'N', 'L', '(', '0', 'W', 'x', '5',
            'e', 'G', '4', '9', 'b', '1', 's', 'R', 'j', '(', '^', ';', '8',
            'K', 'g', '2', 'w', '0', 'E', 'o', 'M'
    };

    private static final byte[] salt = {
            (byte)0xA9, (byte)0xA2, (byte)0xB5, (byte)0xDE,
            (byte)0x2A, (byte)0x8A, (byte)0x9A, (byte)0xE6
    };

    private static final int iterationCount = 1024;

    // must be 128, 192, 256; 128 is maximum without "unlimited strength" JCE policy files
    private static final int aesKeyLength = 128;

    private static SecretKey getSecretKey(char[] passphrase)
    {
        try
        {
            PBEKeySpec keySpec = new PBEKeySpec(
                    passphrase,
                    Encryptor.salt,
                    Encryptor.iterationCount,
                    Encryptor.aesKeyLength
            );

            byte[] shortKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").
                    generateSecret(keySpec).getEncoded();

            byte[] intermediaryKey = new byte[Encryptor.aesKeyLength / 8];
            for(int i = 0, j = 0; i < Encryptor.aesKeyLength / 8; i++)
            {
                intermediaryKey[i] = shortKey[j];
                if(++j == shortKey.length)
                    j = 0;
            }

            return new SecretKeySpec(intermediaryKey, "AES");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException("DES with an MD5 Digest", e);
        }
        catch(InvalidKeySpecException e)
        {
            throw new InappropriateKeySpecificationException(e);
        }
    }

    private static Cipher getDefaultEncryptionCipher()
    {
        if(Encryptor.defaultEncryptionCipher == null)
            Encryptor.defaultEncryptionCipher = Encryptor.getEncryptionCipher(Encryptor.defaultPassphrase);

        return Encryptor.defaultEncryptionCipher;
    }

    private static Cipher getEncryptionCipher(char[] passphrase)
    {
        return Encryptor.getEncryptionCipher(Encryptor.getSecretKey(passphrase));
    }

    private static Cipher getEncryptionCipher(SecretKey secretKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, Encryptor.random);
            return cipher;
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException("AES With SHA-1 digest", e);
        }
        catch(NoSuchPaddingException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(InvalidKeyException e)
        {
            throw new InappropriateKeyException(e.getMessage(), e);
        }
    }

    private static Cipher getDefaultDecryptionCipher()
    {
        if(Encryptor.defaultDecryptionCipher == null)
            Encryptor.defaultDecryptionCipher = Encryptor.getDecryptionCipher(Encryptor.defaultPassphrase);

        return Encryptor.defaultDecryptionCipher;
    }

    private static Cipher getDecryptionCipher(char[] passphrase)
    {
        return Encryptor.getDecryptionCipher(Encryptor.getSecretKey(passphrase));
    }

    private static Cipher getDecryptionCipher(SecretKey secretKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, Encryptor.random);
            return cipher;
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException("AES With SHA-1 digest", e);
        }
        catch(NoSuchPaddingException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(InvalidKeyException e)
        {
            throw new InappropriateKeyException(e.getMessage(), e);
        }
    }

    /**
     * This class cannot be instantiated.
     */
    private Encryptor()
    {
        throw new RuntimeException("This class cannot be instantiated.");
    }
}
