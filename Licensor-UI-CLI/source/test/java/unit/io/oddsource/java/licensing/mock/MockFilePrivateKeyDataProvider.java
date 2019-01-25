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
package io.oddsource.java.licensing.mock;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.licensor.encryption.PrivateKeyDataProvider;

public class MockFilePrivateKeyDataProvider implements PrivateKeyDataProvider
{
    /**
     * This method returns the data from the file containing the encrypted
     * private key from the public/private key pair. The contract for this
     * method can be fulfilled by storing the data in a byte array literal
     * in the source code itself.
     *
     * @return the encrypted file contents from the private key file.
     *
     * @throws KeyNotFoundException if the key data could not be retrieved; an acceptable message or chained cause
     *     must be provided.
     */
    public byte[] getEncryptedPrivateKeyData() throws KeyNotFoundException
    {
        try
        {
            return IOUtils.toByteArray(
                this.getClass().getResourceAsStream("mock.private.key")
            );
        }
        catch(IOException e)
        {
            throw new KeyNotFoundException("The private key file was not found.", e);
        }
    }
}
