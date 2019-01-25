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
package io.oddsource.java.licensing;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 * A default implementation of the {@link LicenseProvider} that assumes the binary data from the signed and serialized
 * license is stored in a file. Various properties of this provider allow configuration of file prefixes (such as
 * a directory), file suffixes (such as an extension), whether or not the file can be found on the classpath, and
 * whether or not the contents of the file are Base64 encoded.<br>
 * <br>
 * This implementation also assumes that license contexts (lookup keys) are always either strings or have a meaningful
 * {@link Object#toString()} implementation that can be used within the file name.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileLicenseProvider extends DeserializingLicenseProvider
{
    private final ClassLoader classLoader;

    private String filePrefix = "";

    private String fileSuffix = "";

    private boolean fileOnClasspath;

    private boolean base64Encoded;

    /**
     * Constructs a file-based license provider with the same class loader as the loader of this class and
     * {@link #setFileOnClasspath(boolean) fileOnClasspath} set to {@code false}. The class loader is only used if
     * {@code fileOnClasspath} is subsequently changed to {@code true}.
     */
    public FileLicenseProvider()
    {
        this.classLoader = this.getClass().getClassLoader();
    }

    /**
     * Constructs a file-based license provider with the provided class loader and
     * {@link #setFileOnClasspath(boolean) fileOnClasspath} set to {@code true}. The class loader will be used to
     * locate the file unless {@code fileOnClasspath} is subsequently changed to {@code false}.
     *
     * @param classLoader The class loader to use for finding the file
     */
    public FileLicenseProvider(final ClassLoader classLoader)
    {
        if(classLoader == null)
        {
            throw new IllegalArgumentException("Argument classLoader cannot be null.");
        }

        this.classLoader = classLoader;
        this.fileOnClasspath = true;
    }

    /**
     * Gets the stored, still-encrypted, still-serialized license content and signature from the persistence store.
     * Returns null (not an empty array) if no license is found.
     *
     * @param context The context for which to get the license
     *
     * @return the signed license data.
     */
    @Override
    protected byte[] getLicenseData(final Object context)
    {
        if(context == null)
        {
            throw new IllegalArgumentException("Argument context cannot be null.");
        }

        final File file = this.getLicenseFile(context);
        if(file == null || !file.exists() || !file.canRead())
        {
            return null;
        }

        byte[] data = null;
        try
        {
            data = FileUtils.readFileToByteArray(file);

            if(this.isBase64Encoded())
            {
                data = Base64.decodeBase64(data);
            }
        }
        catch(final IOException ignore)
        {
        }

        return data;
    }

    /**
     * Gets the license file handle. Returns null if if no license is found, but if a license is found, this may
     * return a file handle to a non-existent file. So, the file should be checked for existence and readability.
     *
     * @param context The context for which to get the license
     *
     * @return the license file handle.
     */
    protected File getLicenseFile(final Object context)
    {
        String fileName = this.getFilePrefix() + context.toString() + this.getFileSuffix();

        File file = null;

        if(this.isFileOnClasspath())
        {
            if(fileName.startsWith("/"))
            {
                fileName = fileName.substring(1);
            }

            final URL url = this.classLoader.getResource(fileName);
            if(url == null)
            {
                fileName = null;
            }
            else
            {
                try
                {
                    file = new File(url.toURI());
                }
                catch(final URISyntaxException e)
                {
                    file = new File(url.getPath());
                }
            }
        }

        if(file != null)
        {
            return file;
        }

        return fileName == null ? null : new File(fileName);
    }

    /**
     * Gets the prefix that will be prepended to the file name before looking for it. For example, if a license
     * context was "customer01" and the file name was "C:\product\licenses\file-customer01.lic", then the prefix
     * would be "C:\product\licenses\file-" and the suffix would be ".lic".
     *
     * @return the file prefix.
     */
    public String getFilePrefix()
    {
        return this.filePrefix;
    }

    /**
     * Sets the prefix that will be prepended to the file name before looking for it. For example, if a license
     * context was "customer01" and the file name was "C:\product\licenses\file-customer01.lic", then the prefix
     * would be "C:\product\licenses\file-" and the suffix would be ".lic".
     *
     * @param filePrefix The file prefix
     */
    public void setFilePrefix(final String filePrefix)
    {
        if(filePrefix == null)
        {
            throw new IllegalArgumentException("Argument filePrefix cannot be null.");
        }

        this.filePrefix = filePrefix;
    }

    /**
     * Gets the file suffix that will be appended to the file name before looking for it. For example, if a license
     * context was "customer01" and the file name was "C:\product\licenses\file-customer01.lic", then the prefix
     * would be "C:\product\licenses\file-" and the suffix would be ".lic".
     *
     * @return the file suffix.
     */
    public String getFileSuffix()
    {
        return this.fileSuffix;
    }

    /**
     * Sets the file suffix that will be appended to the file name before looking for it. For example, if a license
     * context was "customer01" and the file name was "C:\product\licenses\file-customer01.lic", then the prefix
     * would be "C:\product\licenses\file-" and the suffix would be ".lic".
     *
     * @param fileSuffix The file suffix
     */
    public void setFileSuffix(final String fileSuffix)
    {
        if(fileSuffix == null)
        {
            throw new IllegalArgumentException("Argument fileSuffix cannot be null.");
        }

        this.fileSuffix = fileSuffix;
    }

    /**
     * Indicates whether the file should be found on the file system or on the classpath via a class loader. If
     * {@code false} it will be looked for on the file system; if {@code true} it will be looked for on the classpath.
     * If {@code true}, the file prefix should be the package-path and prefix and the suffix the suffix.<br>
     * <br>
     * For example, if a license context was "customer02" and the file name was "file-customer02.lic" and was located
     * in the package io.oddsource.java.licensing.licenses, then the prefix would be
     * "io/oddsource/java/licensing/licenses/file-" and the suffix should be ".lic".
     *
     * @return whether the file is on the classpath.
     */
    public boolean isFileOnClasspath()
    {
        return this.fileOnClasspath;
    }

    /**
     * Sets whether the file should be found on the file system or on the classpath via a class loader. If
     * {@code false} it will be looked for on the file system; if {@code true} it will be looked for on the classpath.
     * If {@code true}, the file prefix should be the package-path and prefix and the suffix the suffix.<br>
     * <br>
     * For example, if a license context was "customer02" and the file name was "file-customer02.lic" and was located
     * in the package io.oddsource.java.licensing.licenses, then the prefix would be
     * "io/oddsource/java/licensing/licenses/file-" and the suffix should be ".lic".
     *
     * @param fileOnClasspath Whether the file is on the classpath
     */
    public void setFileOnClasspath(final boolean fileOnClasspath)
    {
        this.fileOnClasspath = fileOnClasspath;
    }

    /**
     * Indicates whether the file is Base64 encoded. If the file is Base64 encoded, its data will be decoded before
     * being returned by {@link #getLicenseData(Object)}.
     *
     * @return whether the file is Base64 encoded.
     */
    public boolean isBase64Encoded()
    {
        return this.base64Encoded;
    }

    /**
     * Sets whether the file is Base64 encoded. If the file is Base64 encoded, its data will be decoded before
     * being returned by {@link #getLicenseData(Object)}.
     *
     * @param base64Encoded Whether the file is Base64 encoded.
     */
    public void setBase64Encoded(final boolean base64Encoded)
    {
        this.base64Encoded = base64Encoded;
    }

    ClassLoader getClassLoader()
    {
        return this.classLoader;
    }
}
