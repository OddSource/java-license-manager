/*
 * LicenseCreatorProperties.java from LicenseManager modified Monday, March 5, 2012 13:10:33 CST (-0600).
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

package net.nicholaswilliams.java.licensing.licensor;

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;

/**
 * This class is used to set properties that will be used to instantiate the {@link LicenseCreator}. Read the
 * documentation for each property below.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LicenseCreatorProperties
{
	private static PasswordProvider passwordProvider;

	private static PrivateKeyDataProvider privateKeyDataProvider;

	/**
	 * Sets the provider of the data for the private key used to sign the license object.<br />
	 * <br />
	 * This field is <b>required</b>.
	 *
	 * @param privateKeyDataProvider The provider of the data for the private key used to sign the license object
	 */
	public static void setPrivateKeyDataProvider(PrivateKeyDataProvider privateKeyDataProvider)
	{
		LicenseCreatorProperties.privateKeyDataProvider = privateKeyDataProvider;
	}

	static PrivateKeyDataProvider getPrivateKeyDataProvider()
	{
		return LicenseCreatorProperties.privateKeyDataProvider;
	}

	/**
	 * Sets the provider of the password for decrypting the license key.<br />
	 * <br />
	 * This field is <b>required</b>.
	 *
	 * @param passwordProvider The provider of the password for decrypting the license key
	 */
	public static void setPasswordProvider(PasswordProvider passwordProvider)
	{
		LicenseCreatorProperties.passwordProvider = passwordProvider;
	}

	static PasswordProvider getPasswordProvider()
	{
		return LicenseCreatorProperties.passwordProvider;
	}

	/**
	 * This class cannot be instantiated.
	 */
	private LicenseCreatorProperties()
	{
		throw new RuntimeException("This class cannot be instantiated.");
	}
}
