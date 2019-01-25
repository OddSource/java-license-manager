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
package io.oddsource.java.licensing.encryption;

import io.oddsource.java.licensing.exception.KeyNotFoundException;

/**
 * Specifies an interface for retrieving the public key file data. This
 * interface only needs to be implemented in the application using the
 * licenses. It need not be implemented in the application generating the
 * licenses.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface PublicKeyDataProvider
{
    /**
     * This method returns the data from the file containing the encrypted
     * public key from the public/private key pair. The contract for this
     * method can be fulfilled by storing the data in a byte array literal
     * in the source code itself.<br>
     * <br>
     * It is <em>imperative</em> that you obfuscate the bytecode for the
     * implementation of this class. It is also imperative that the byte
     * array exist only for the life of this method (i.e., DO NOT store it as
     * an instance or class field).
     *
     * @return the encrypted file contents from the public key file.
     *
     * @throws KeyNotFoundException if the key data could not be retrieved; an acceptable message or chained cause
     *     must be provided.
     */
    public abstract byte[] getEncryptedPublicKeyData() throws KeyNotFoundException;
}
