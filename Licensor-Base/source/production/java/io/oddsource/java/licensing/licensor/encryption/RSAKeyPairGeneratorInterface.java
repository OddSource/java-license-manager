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

import java.io.IOException;
import java.security.KeyPair;

import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.licensor.exception.RSA2048NotSupportedException;

/**
 * An interface for the key pair generator to make unit testing possible. This interface is only implemented by
 * {@link RSAKeyPairGenerator}.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface RSAKeyPairGeneratorInterface
{
    /**
     * The key size we use for RSA key pairs. Anything larger is not portable in Java at this time.
     */
    public static final short KEY_SIZE = 2048;

    /**
     * Generates a key pair with RSA 2048-bit security.
     *
     * @return a public/private key pair.
     *
     * @throws RSA2048NotSupportedException if RSA or 2048-bit encryption are not supported.
     */
    public abstract KeyPair generateKeyPair() throws RSA2048NotSupportedException;

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
    public abstract void saveKeyPairToFiles(
        KeyPair keyPair,
        String privateOutputFileName,
        String publicOutputFileName,
        char[] password
    )
        throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
               InappropriateKeySpecificationException;

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
    public abstract void saveKeyPairToFiles(
        KeyPair keyPair,
        String privateOutputFileName,
        String publicOutputFileName,
        char[] privatePassword,
        char[] publicPassword
    )
        throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
               InappropriateKeySpecificationException;

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link GeneratedClassDescriptor}s, encrypting both with the specified password.
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
    public abstract void saveKeyPairToProviders(
        KeyPair keyPair,
        GeneratedClassDescriptor privateKeyProvider,
        GeneratedClassDescriptor publicKeyProvider,
        char[] password
    )
        throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException;

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link GeneratedClassDescriptor}s, encrypting each with their respective passwords.
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
    public abstract void saveKeyPairToProviders(
        KeyPair keyPair,
        GeneratedClassDescriptor privateKeyProvider,
        GeneratedClassDescriptor publicKeyProvider,
        char[] privatePassword,
        char[] publicPassword
    )
        throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException;

    /**
     * Saves the password specified to the
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} field in
     * the provided {@link GeneratedClassDescriptor}.
     *
     * @param password The password to save to the specified Java class
     * @param passwordProvider An object describing the {@link PasswordProvider} class to generate, and into which
     *     the generated code will be saved
     */
    public abstract void savePasswordToProvider(char[] password, GeneratedClassDescriptor passwordProvider);

    /**
     * A descriptor for classes to be generated by implementations of this interface.
     */
    public static class GeneratedClassDescriptor
    {
        private String packageName;

        private String className;

        private String javaFileContents;

        /**
         * Constructor.
         */
        public GeneratedClassDescriptor()
        {

        }

        /**
         * Get the package name to put at the top of the generated Java file.
         *
         * @return the package name.
         */
        public String getPackageName()
        {
            return this.packageName;
        }

        /**
         * Set the package name to put at the top of the generated Java file.
         *
         * @param packageName The package name
         *
         * @return {@code this}.
         */
        public GeneratedClassDescriptor setPackageName(final String packageName)
        {
            this.packageName = packageName;
            return this;
        }

        /**
         * Get the name of the class to generate.
         *
         * @return the name of the class.
         */
        public String getClassName()
        {
            return this.className;
        }

        /**
         * Set the name of the class to generate.
         *
         * @param className The name of the class
         *
         * @return {@code this}.
         */
        public GeneratedClassDescriptor setClassName(final String className)
        {
            this.className = className;
            return this;
        }

        /**
         * Get the contents of the the Java class to generate.
         *
         * @return the contents.
         */
        public String getJavaFileContents()
        {
            return javaFileContents;
        }

        /**
         * Set the contents of the Java class to generate.
         *
         * @param javaFileContents The contents
         *
         * @return {@code this}.
         */
        @SuppressWarnings("UnusedReturnValue")
        public GeneratedClassDescriptor setJavaFileContents(final String javaFileContents)
        {
            this.javaFileContents = javaFileContents;
            return this;
        }
    }
}
