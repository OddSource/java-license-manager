/*
 * LicenseManagerProperties.java from LicenseManager modified Thursday, May 17, 2012 21:31:40 CDT (-0500).
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

package io.oddsource.java.licensing;

import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;

/**
 * This class is used to set properties that will be used to instantiate the {@link LicenseManager}. Read the
 * documentation for each property below.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LicenseManagerProperties
{
    private static PublicKeyDataProvider publicKeyDataProvider;

    private static PasswordProvider publicKeyPasswordProvider;

    private static LicenseProvider licenseProvider;

    private static PasswordProvider licensePasswordProvider;

    private static LicenseValidator licenseValidator;

    private static int cacheTimeInMinutes;

    /**
     * This class cannot be instantiated.
     */
    private LicenseManagerProperties()
    {
        throw new AssertionError("This class cannot be instantiated.");
    }

    /**
     * Sets the provider of the data for the public key companion to the private key used to sign the license
     * object.<br />
     * <br />
     * This field is <b>required</b>.
     *
     * @param publicKeyDataProvider The provider of the data for the public key companion to the private key used to
     *     sign the license object
     */
    public static void setPublicKeyDataProvider(final PublicKeyDataProvider publicKeyDataProvider)
    {
        LicenseManagerProperties.publicKeyDataProvider = publicKeyDataProvider;
    }

    static PublicKeyDataProvider getPublicKeyDataProvider()
    {
        return LicenseManagerProperties.publicKeyDataProvider;
    }

    /**
     * Sets the provider of the password for decrypting the public key.<br />
     * <br />
     * This field is <b>required</b>.
     *
     * @param publicKeyPasswordProvider The provider of the password for decrypting the public key
     */
    public static void setPublicKeyPasswordProvider(final PasswordProvider publicKeyPasswordProvider)
    {
        LicenseManagerProperties.publicKeyPasswordProvider = publicKeyPasswordProvider;
    }

    static PasswordProvider getPublicKeyPasswordProvider()
    {
        return LicenseManagerProperties.publicKeyPasswordProvider;
    }

    /**
     * Sets the provider of the persisted license data.<br />
     * <br />
     * This field is <b>required</b>.
     *
     * @param licenseProvider The provider of the persisted license data
     */
    public static void setLicenseProvider(final LicenseProvider licenseProvider)
    {
        LicenseManagerProperties.licenseProvider = licenseProvider;
    }

    static LicenseProvider getLicenseProvider()
    {
        return LicenseManagerProperties.licenseProvider;
    }

    /**
     * Sets the provider of the password for the persisted license data.<br />
     * <br />
     * This field is <b>optional</b>. If not provided, the
     * {@link #setPublicKeyPasswordProvider(PasswordProvider) publicKeyPasswordProvider} will be used to decrypt
     * licenses.
     *
     * @param licensePasswordProvider The provider of the password for decrypting license data
     */
    public static void setLicensePasswordProvider(final PasswordProvider licensePasswordProvider)
    {
        LicenseManagerProperties.licensePasswordProvider = licensePasswordProvider;
    }

    static PasswordProvider getLicensePasswordProvider()
    {
        return LicenseManagerProperties.licensePasswordProvider;
    }

    /**
     * Sets the validator implementation that validates all licenses; if null, licenses are assumed to always be valid.
     * If you do not want to validate licenses automatically, you do not need to provide a validator, or you may set
     * it to null.<br />
     * <br />
     * This field is <b>optional</b> and defaults to no validation.
     *
     * @param licenseValidator The validator implementation that validates all licenses; if null, licenses are
     *     assumed to always be valid
     */
    public static void setLicenseValidator(final LicenseValidator licenseValidator)
    {
        LicenseManagerProperties.licenseValidator = licenseValidator;
    }

    static LicenseValidator getLicenseValidator()
    {
        return LicenseManagerProperties.licenseValidator;
    }

    /**
     * Sets the length of time in minutes to cache license information (for performance reasons, anything less than 1
     * minute results in a 10-second cache life; the cache cannot be disabled completely).<br />
     * <br />
     * This field is <b>optional</b> and defaults to 10 seconds.
     *
     * @param cacheTimeInMinutes The length of time in minutes to cache license information
     */
    public static void setCacheTimeInMinutes(final int cacheTimeInMinutes)
    {
        LicenseManagerProperties.cacheTimeInMinutes = cacheTimeInMinutes;
    }

    static int getCacheTimeInMinutes()
    {
        return cacheTimeInMinutes;
    }
}
