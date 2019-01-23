/*
 * FilePublicKeyDataProvider.java from LicenseManager modified Thursday, May 17, 2012 21:31:40 CDT (-0500).
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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.oddsource.java.licensing.exception.KeyNotFoundException;

/**
 * A default implementation of {@link PublicKeyDataProvider} that reads the public key from a file.<br />
 * <br />
 * This provider is immutable. Once created, the file that the public key is located at cannot be changed.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class FilePublicKeyDataProvider implements PublicKeyDataProvider
{
    private final File publicKeyFile;

    /**
     * Create a new provider, specifying the file from which the public key can be read.
     *
     * @param publicKeyFile the public key file
     */
    public FilePublicKeyDataProvider(final File publicKeyFile)
    {
        this.publicKeyFile = publicKeyFile.getAbsoluteFile();
    }

    /**
     * Create a new provider, specifying the name of the file from which the public key can be read.
     *
     * @param publicKeyFileName The public key file name
     */
    public FilePublicKeyDataProvider(final String publicKeyFileName)
    {
        this.publicKeyFile = new File(publicKeyFileName).getAbsoluteFile();
    }

    /**
     * This method returns the data from the file containing the encrypted
     * public key from the public/private key pair. The contract for this
     * method can be fulfilled by storing the data in a byte array literal
     * in the source code itself.<br/>
     * <br/>
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
    @Override
    public byte[] getEncryptedPublicKeyData() throws KeyNotFoundException
    {
        try
        {
            return FileUtils.readFileToByteArray(this.publicKeyFile);
        }
        catch(final FileNotFoundException e)
        {
            throw new KeyNotFoundException(
                "The public key file [" + this.publicKeyFile.getPath() + "] does not exist."
            );
        }
        catch(final IOException e)
        {
            throw new KeyNotFoundException(
                "Could not read from the public key file [" + this.publicKeyFile.getPath() + "].",
                e
            );
        }
    }

    /**
     * Gets the file that the public key is located at.
     *
     * @return the file.
     */
    public File getPublicKeyFile()
    {
        return this.publicKeyFile;
    }
}
