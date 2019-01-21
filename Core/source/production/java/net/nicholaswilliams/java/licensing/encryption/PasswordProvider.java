/*
 * PasswordProvider.java from LicenseManager modified Tuesday, April 9, 2013 10:55:17 CDT (-0500).
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

/**
 * This specifies an interface for providing a password for decrypting a key or license. Every user of this library
 * must implement this (one to three times). See the documentation for
 * {@code net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGenerator},
 * {@code net.nicholaswilliams.java.licensing.licensor.LicenseCreator} and
 * {@link net.nicholaswilliams.java.licensing.LicenseManager} for more information.
 *
 * @author Nick Williams
 * @version 1.5.0
 * @since 1.0.0
 */
public interface PasswordProvider
{
    /**
     * When integrating the license manager in your application, you must
     * implement this interface. It should return the password that you used
     * when encrypting the key or license.<br/>
     * <br/>
     * Also, do not implement this using any strings, i.e:<br/>
     * <br/>
     * <code>
     * return "myInsecurePassword".toCharArray();
     * </code><br/>
     * <br/>
     * Strings persist in memory for a long time, and the string's contents
     * cannot be overwritten. Using only a character array allows the license
     * manager to overwrite the password when it is finished with it, thus
     * keeping it in the memory pool for as short a time as possible.<br/>
     * <br/>
     * Finally, the password must be between six and 32 characters
     * (inclusively). We recommend it be at least 10 characters.<br/>
     * <br/>
     * It is <em>imperative</em> that you obfuscate the bytecode for the
     * implementation of this class. It is also imperative that the character
     * array exist only for the life of this method (i.e., DO NOT store it as
     * an instance or class field).
     *
     * @return the password in character array form.
     */
    public char[] getPassword();
}
