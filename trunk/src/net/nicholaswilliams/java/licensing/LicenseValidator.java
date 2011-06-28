/*
 * LicenseValidator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
 *
 * Copyright 2010-2011 the original author or authors.
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

import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;

/**
 * Specifies an interface for validating licenses. Users of License Manager do not have to implement this interface.
 * If it is not implemented, it is assumed that all licenses are valid. Users are encouraged, however, to implement
 * interface.<br />
 * <br />
 * There is a default implementation, {@link DefaultLicenseValidator}, that ensures the current date is between the
 * license's good-after and good-before dates (the license has taken effect and hasn't expired).
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public interface LicenseValidator
{
	/**
	 * Validates the license provided and throws an exception if the license is invalid for any reason
	 * (expired, not who it belongs to, etc.).
	 *
	 * @param license The license to validate
	 * @throws InvalidLicenseException when the license is invalid for any reason; the implementer is required to provide adequate description in this exception to indicate why the license is invalid; extending the exception is encouraged
	 * @see InvalidLicenseException
	 * @see net.nicholaswilliams.java.licensing.exception.ExpiredLicenseException
	 */
	public void validateLicense(License license) throws InvalidLicenseException;
}
