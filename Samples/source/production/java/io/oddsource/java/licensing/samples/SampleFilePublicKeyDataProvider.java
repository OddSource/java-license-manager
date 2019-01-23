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
package io.oddsource.java.licensing.samples;

import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;
import io.oddsource.java.licensing.exception.KeyNotFoundException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * A sample implementation of the {@link PublicKeyDataProvider} interface.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 * @see PublicKeyDataProvider
 */
@SuppressWarnings("unused")
public class SampleFilePublicKeyDataProvider implements PublicKeyDataProvider
{
    /**
     * This method returns the data from the file containing the encrypted
     * public key from the public/private key pair. The contract for this
     * method can be fulfilled by storing the data in a byte array literal
     * in the source code itself.
     *
     * @return the encrypted file contents from the public key file.
     * @throws KeyNotFoundException if the key data could not be retrieved; an acceptable message or chained cause must be provided.
     */
    public byte[] getEncryptedPublicKeyData() throws KeyNotFoundException
    {
        try
        {
            return IOUtils.toByteArray(
                    this.getClass().getResourceAsStream("sample.public.key")
            );
        }
        catch(IOException e)
        {
            throw new KeyNotFoundException("The public key file was not found.", e);
        }
    }
}
