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
package io.oddsource.java.licensing.licensor.encryption;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Locale;

import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.licensor.exception.RSA2048NotSupportedException;

/**
 * The generator one should use to create public/private key pairs for use with
 * the application.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class RSAKeyPairGenerator implements RSAKeyPairGeneratorInterface
{
    private static final String BYTE_TYPE_NAME = "byte";

    private static final String CODE_LF = "\n";

    private static final String CODE_TAB = "    ";

    private static final String CODE_STATEMENT_END = ";" + CODE_LF;

    private static final String CODE_OPENING_BRACE = "{";

    private static final String CODE_OPENING_BRACE_LF = CODE_OPENING_BRACE + CODE_LF;

    private static final String CODE_CLOSING_BRACE = "}";

    private static final String CODE_CLOSING_BRACE_LF = CODE_CLOSING_BRACE + CODE_LF;

    private static final short CODE_ARRAY_VALUES_PER_LINE = 8;

    /**
     * Constructor.
     */
    public RSAKeyPairGenerator()
    {

    }

    /**
     * Generates a key pair with RSA 2048-bit security.
     *
     * @return a public/private key pair.
     *
     * @throws RSA2048NotSupportedException if RSA or 2048-bit encryption are not supported.
     */
    @Override
    public KeyPair generateKeyPair() throws RSA2048NotSupportedException
    {
        final KeyPairGenerator keyGenerator;

        try
        {
            keyGenerator = KeyPairGenerator.getInstance(
                KeyFileUtilities.keyAlgorithm
            );
        }
        catch(final NoSuchAlgorithmException e)
        {
            throw new RSA2048NotSupportedException(
                "RSA keys are not supported on your system. Contact your system administrator for assistance.",
                e
            );
        }

        try
        {
            keyGenerator.initialize(RSAKeyPairGeneratorInterface.KEY_SIZE);
        }
        catch(final InvalidParameterException e)
        {
            throw new RSA2048NotSupportedException(
                "RSA is supported on your system, but 2048-bit keys are not. Contact your system administrator for " +
                "assistance.",
                e
            );
        }

        return keyGenerator.generateKeyPair();
    }

    /**
     * Saves the key pair specified to output files specified, encrypting both with the specified password.
     *
     * @param keyPair The key pair to save to the files specified
     * @param privateOutputFileName The name of the file to save the encrypted private key to
     * @param publicOutputFileName The name of the file to save the encrypted public key to
     * @param password The password to encrypt both keys with
     *
     * @throws IOException if an error occurs while writing to the files.
     * @throws AlgorithmNotSupportedException If the encryption algorithm is not supported
     * @throws InappropriateKeyException If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToFiles(
        final KeyPair keyPair, final String privateOutputFileName, final String publicOutputFileName,
        final char[] password
    )
        throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
               InappropriateKeySpecificationException
    {
        this.saveKeyPairToFiles(keyPair, privateOutputFileName, publicOutputFileName, password, password);
    }

    /**
     * Saves the key pair specified to output files specified, encrypting each with their specified passwords.
     *
     * @param keyPair The key pair to save to the files specified
     * @param privateOutputFileName The name of the file to save the encrypted private key to
     * @param publicOutputFileName The name of the file to save the encrypted public key to
     * @param privatePassword The password to encrypt the private key with
     * @param publicPassword The password to encrypt the public key with
     *
     * @throws IOException if an error occurs while writing to the files.
     * @throws AlgorithmNotSupportedException If the encryption algorithm is not supported
     * @throws InappropriateKeyException If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToFiles(
        final KeyPair keyPair, final String privateOutputFileName, final String publicOutputFileName,
        final char[] privatePassword, final char[] publicPassword
    )
        throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
               InappropriateKeySpecificationException
    {
        final PrivateKey privateKey = keyPair.getPrivate();
        final PublicKey publicKey = keyPair.getPublic();

        KeyFileUtilities.writeEncryptedPrivateKey(privateKey, new File(privateOutputFileName), privatePassword);
        KeyFileUtilities.writeEncryptedPublicKey(publicKey, new File(publicOutputFileName), publicPassword);
    }

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}s, encrypting both with the specified
     * password.
     *
     * @param keyPair The key pair to save
     * @param privateKeyProvider An object describing the {@link PrivateKeyDataProvider} class to generate, and into
     *     which the generated code will be saved
     * @param publicKeyProvider An object describing the {@link PublicKeyDataProvider} class to generate, and into
     *     which the generated code will be saved
     * @param password The password to encrypt the keys with
     *
     * @throws AlgorithmNotSupportedException If the encryption algorithm is not supported
     * @throws InappropriateKeyException If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToProviders(
        final KeyPair keyPair, final GeneratedClassDescriptor privateKeyProvider,
        final GeneratedClassDescriptor publicKeyProvider, final char[] password
    )
        throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException
    {
        this.saveKeyPairToProviders(keyPair, privateKeyProvider, publicKeyProvider, password, password);
    }

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}s, encrypting each with their
     * respective passwords.
     *
     * @param keyPair The key pair to save
     * @param privateKeyProvider An object describing the {@link PrivateKeyDataProvider} class to generate, and into
     *     which the generated code will be saved
     * @param publicKeyProvider An object describing the {@link PublicKeyDataProvider} class to generate, and into
     *     which the generated code will be saved
     * @param privatePassword The password to encrypt the private key with
     * @param publicPassword The password to encrypt the public key with
     *
     * @throws AlgorithmNotSupportedException If the encryption algorithm is not supported
     * @throws InappropriateKeyException If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToProviders(
        final KeyPair keyPair, final GeneratedClassDescriptor privateKeyProvider,
        final GeneratedClassDescriptor publicKeyProvider, final char[] privatePassword,
        final char[] publicPassword
    )
        throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException
    {
        if(keyPair == null)
        {
            throw new IllegalArgumentException("Parameter keyPair cannot be null.");
        }
        if(privateKeyProvider == null || publicKeyProvider == null)
        {
            throw new IllegalArgumentException("Parameter privateKeyProvider and publicKeyProvider cannot be null.");
        }
        if(privatePassword == null || privatePassword.length == 0)
        {
            throw new IllegalArgumentException("Parameter privatePassword cannot be null or zero-length.");
        }
        if(publicPassword == null || publicPassword.length == 0)
        {
            throw new IllegalArgumentException("Parameter publicPassword cannot be null or zero-length.");
        }

        final String privateKeyCode = this.arrayToCodeString(
            this.byteArrayToIntArray(KeyFileUtilities.writeEncryptedPrivateKey(keyPair.getPrivate(), privatePassword)),
            RSAKeyPairGenerator.BYTE_TYPE_NAME
        );
        final String publicKeyCode = this.arrayToCodeString(
            this.byteArrayToIntArray(KeyFileUtilities.writeEncryptedPublicKey(keyPair.getPublic(), publicPassword)),
            RSAKeyPairGenerator.BYTE_TYPE_NAME
        );

        privateKeyProvider.setJavaFileContents(this.generateJavaCode(
            privateKeyProvider.getPackageName(), privateKeyProvider.getClassName(),
            "PrivateKeyDataProvider",
            new String[] {
                PrivateKeyDataProvider.class.getCanonicalName(),
                KeyNotFoundException.class.getCanonicalName(),
            },
            "public byte[] getEncryptedPrivateKeyData() throws KeyNotFoundException",
            privateKeyCode
        ));

        publicKeyProvider.setJavaFileContents(this.generateJavaCode(
            publicKeyProvider.getPackageName(), publicKeyProvider.getClassName(),
            "PublicKeyDataProvider",
            new String[] {
                PublicKeyDataProvider.class.getCanonicalName(),
                KeyNotFoundException.class.getCanonicalName(),
            },
            "public byte[] getEncryptedPublicKeyData() throws KeyNotFoundException",
            publicKeyCode
        ));
    }

    /**
     * Saves the password specified to the
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} field in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}.
     *
     * @param password The password to save to the specified Java class
     * @param passwordProvider An object describing the {@link PasswordProvider} class to generate, and into which
     *     the generated code will be saved
     */
    @Override
    public void savePasswordToProvider(final char[] password, final GeneratedClassDescriptor passwordProvider)
    {
        if(password == null || password.length == 0)
        {
            throw new IllegalArgumentException("Parameter password cannot be null or zero-length.");
        }

        if(passwordProvider == null)
        {
            throw new IllegalArgumentException("Parameter passwordProvider cannot be null.");
        }

        final String passwordCode = this.arrayToCodeString(this.charArrayToIntArray(password), "char");

        passwordProvider.setJavaFileContents(this.generateJavaCode(
            passwordProvider.getPackageName(),
            passwordProvider.getClassName(),
            "PasswordProvider",
            new String[] {"io.oddsource.java.licensing.encryption.PasswordProvider"},
            "public char[] getPassword()",
            passwordCode
        ));
    }

    /**
     * Generates a final, compilable Java class implementing the specified interface with a single, one-statement
     * method that returns a simple value.
     *
     * @param packageName The package name the class should be contained in, or {@code null} if no package
     * @param className The name of the class to create
     * @param interfaceName The interface this class should implement, or null if no interface
     * @param imports An array of classes to import at the top of the Java code
     * @param methodSignature The signature of the sole method in the class
     * @param returnValue The statement that the method should return (will be prepended with "return ")
     *
     * @return the Java code for the specified class.
     */
    protected String generateJavaCode(
        final String packageName, final String className, final String interfaceName, final String[] imports,
        final String methodSignature, final String returnValue
    )
    {
        final StringBuilder stringBuilder = new StringBuilder();

        if(packageName != null && packageName.trim().length() > 0)
        {
            stringBuilder.append("package ").append(packageName.trim()).append(RSAKeyPairGenerator.CODE_STATEMENT_END).
                append(RSAKeyPairGenerator.CODE_LF);
        }

        if(imports != null && imports.length > 0)
        {
            for(final String importClass : imports)
            {
                stringBuilder.append("import ").append(importClass.trim()).
                    append(RSAKeyPairGenerator.CODE_STATEMENT_END);
            }
            stringBuilder.append(RSAKeyPairGenerator.CODE_LF);
        }

        final boolean hasInterface = interfaceName != null && interfaceName.trim().length() > 0;

        stringBuilder.append("public final class ").append(className.trim());
        if(hasInterface)
        {
            stringBuilder.append(" implements ").append(interfaceName.trim());
        }
        stringBuilder.append(RSAKeyPairGenerator.CODE_LF).append(RSAKeyPairGenerator.CODE_OPENING_BRACE_LF);
        if(hasInterface)
        {
            stringBuilder.append(RSAKeyPairGenerator.CODE_TAB).append("@Override" + RSAKeyPairGenerator.CODE_LF);
        }
        stringBuilder.append(RSAKeyPairGenerator.CODE_TAB).append(methodSignature.trim()).
            append(RSAKeyPairGenerator.CODE_LF);
        stringBuilder.append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_OPENING_BRACE_LF);

        stringBuilder.append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_TAB).append("return ").
            append(returnValue).append(RSAKeyPairGenerator.CODE_STATEMENT_END);

        stringBuilder.append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_CLOSING_BRACE_LF);

        stringBuilder.append(RSAKeyPairGenerator.CODE_CLOSING_BRACE_LF);

        return stringBuilder.toString();
    }

    /**
     * Takes an array of integer-representable primitives ({@code byte}, {@code char}, {@code short}, {@code int})
     * and returns a Java code array-literal representation of the array, with values in hexadecimal literal format.
     * It is the user's responsibility to ensure that the values contained in the array can fit within the smaller
     * precision of the array type, if applicable.
     *
     * @param values The array of values to include in the array code
     * @param type The data type ({@code byte}, {@code char}, {@code short}, {@code int}) of the array to return
     *
     * @return the Java code representation of this array.
     */
    protected String arrayToCodeString(final int[] values, final String type)
    {
        final StringBuilder stringBuilder = new StringBuilder("new ").append(type).append("[] ").
            append(RSAKeyPairGenerator.CODE_OPENING_BRACE_LF).append(RSAKeyPairGenerator.CODE_TAB).
            append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_TAB).
            append(RSAKeyPairGenerator.CODE_TAB);
        int i = 0;
        int j = 1;
        for(final int value : values)
        {
            if(i++ > 0)
            {
                stringBuilder.append(", ");
            }
            if(j++ > RSAKeyPairGenerator.CODE_ARRAY_VALUES_PER_LINE)
            {
                j = 2;
                stringBuilder.append(RSAKeyPairGenerator.CODE_LF).append(RSAKeyPairGenerator.CODE_TAB).
                    append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_TAB).
                    append(RSAKeyPairGenerator.CODE_TAB);
            }
            stringBuilder.append("0x");
            stringBuilder.append(String.format("%08x", value).toUpperCase(Locale.US));
        }
        stringBuilder.append(RSAKeyPairGenerator.CODE_LF).append(RSAKeyPairGenerator.CODE_TAB).
            append(RSAKeyPairGenerator.CODE_TAB).append(RSAKeyPairGenerator.CODE_CLOSING_BRACE);
        return stringBuilder.toString();
    }

    /**
     * Converts a {@code byte} array to an {@code int} array.
     *
     * @param array The {@code byte} array to convert
     *
     * @return the converted array as an {@code int} array.
     */
    protected int[] byteArrayToIntArray(final byte[] array)
    {
        final int[] a = new int[array.length];
        int i = 0;
        for(final byte b : array)
        {
            a[i++] = b;
        }
        return a;
    }

    /**
     * Converts a {@code char} array to an {@code int} array.
     *
     * @param array The {@code char} array to convert
     *
     * @return the converted array as an {@code int} array.
     */
    protected int[] charArrayToIntArray(final char[] array)
    {
        final int[] a = new int[array.length];
        int i = 0;
        for(final char c : array)
        {
            a[i++] = c;
        }
        return a;
    }
}
