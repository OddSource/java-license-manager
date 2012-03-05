/*
 * LicenseManagerProperties.java from LicenseManager modified Monday, March 5, 2012 13:04:18 CST (-0600).
 *
 * Copyright 2010-2012 the original author or authors.
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

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;

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
	private static KeyPasswordProvider passwordProvider;

	private static PublicKeyDataProvider publicKeyDataProvider;

	private static LicenseProvider licenseProvider;

	private static LicenseValidator licenseValidator;

	private static int cacheTimeInMinutes;

	/**
	 * Sets the provider of the persisted license data.<br />
	 * <br />
	 * This field is <b>required</b>.
	 *
	 * @param licenseProvider The provider of the persisted license data
	 */
	public static void setLicenseProvider(LicenseProvider licenseProvider)
	{
		LicenseManagerProperties.licenseProvider = licenseProvider;
	}

	static LicenseProvider getLicenseProvider()
	{
		return LicenseManagerProperties.licenseProvider;
	}

	/**
	 * Sets the provider of the password for decrypting the license key.<br />
	 * <br />
	 * This field is <b>required</b>.
	 *
	 * @param passwordProvider The provider of the password for decrypting the license key
	 */
	public static void setPasswordProvider(KeyPasswordProvider passwordProvider)
	{
		LicenseManagerProperties.passwordProvider = passwordProvider;
	}

	static KeyPasswordProvider getPasswordProvider()
	{
		return LicenseManagerProperties.passwordProvider;
	}

	/**
	 * Sets the provider of the data for the public key companion to the private key used to sign the license
	 * object.<br />
	 * <br />
	 * This field is <b>required</b>.
	 *
	 * @param publicKeyDataProvider The provider of the data for the public key companion to the private key used to sign the license object
	 */
	public static void setPublicKeyDataProvider(PublicKeyDataProvider publicKeyDataProvider)
	{
		LicenseManagerProperties.publicKeyDataProvider = publicKeyDataProvider;
	}

	static PublicKeyDataProvider getPublicKeyDataProvider()
	{
		return LicenseManagerProperties.publicKeyDataProvider;
	}

	/**
	 * Sets the validator implementation that validates all licenses; if null, licenses are assumed to always be valid.
	 * If you do not want to validate licenses automatically, you do not need to provide a validator, or you may set
	 * it to null.<br />
	 * <br />
	 * This field is <b>optional</b> and defaults to no validation.
	 * 
	 * @param licenseValidator The validator implementation that validates all licenses; if null, licenses are assumed to always be valid
	 */
	public static void setLicenseValidator(LicenseValidator licenseValidator)
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
	public static void setCacheTimeInMinutes(int cacheTimeInMinutes)
	{
		LicenseManagerProperties.cacheTimeInMinutes = cacheTimeInMinutes;
	}

	static int getCacheTimeInMinutes()
	{
		return cacheTimeInMinutes;
	}

	/**
	 * This class cannot be instantiated.
	 */
	private LicenseManagerProperties()
	{
		throw new RuntimeException("This class cannot be instantiated.");
	}
}
