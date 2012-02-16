/*
 * DeserializingLicenseProvider.java from LicenseManager modified Wednesday, February 15, 2012 22:59:20 CST (-0600).
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

/**
 * An abstract implementation of the {@link LicenseProvider} interface that assumes the license will be stored in
 * serialized form. Users need only implement the method returning the raw byte data of the serialized license in order
 * to complete this implementation.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class DeserializingLicenseProvider implements LicenseProvider
{
	/**
	 * Gets the stored, still-encrypted license content and signature from the persistence store.
	 *
	 * @param context The context for which to get the license
	 * @return the signed license object.
	 */
	@Override
	public final SignedLicense getLicense(Object context)
	{
		byte[] data = this.getLicenseData(context);

		return data == null ? null : new ObjectSerializer().readObject(SignedLicense.class, data);
	}

	/**
	 * Gets the stored, still-encrypted, still-serialized license content and signature from the persistence store. If
	 * no license is found, this method should return null (not an empty array).
	 *
	 * @param context The context for which to get the license
	 * @return the signed license data.
	 */
	protected abstract byte[] getLicenseData(Object context);
}
