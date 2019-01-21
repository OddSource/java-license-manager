/*
 * LicenseValidator.java from LicenseManager modified Tuesday, February 21, 2012 10:59:35 CST (-0600).
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

import io.oddsource.java.licensing.exception.ExpiredLicenseException;
import io.oddsource.java.licensing.exception.InvalidLicenseException;

/**
 * Specifies an interface for validating licenses. Users of License Manager do not have to implement this interface.
 * If it is not implemented, it is assumed that all licenses are valid. Users are encouraged, however, to implement
 * interface.<br />
 * <br />
 * There is a default implementation, {@link DefaultLicenseValidator}, that ensures the current date is between the
 * license's good-after and good-before dates (the license has taken effect and hasn't expired).
 *
 * @author Nick Williams
 * @version 1.0.1
 * @since 1.0.0
 */
public interface LicenseValidator
{
    /**
     * Validates the license provided and throws an exception if the license is invalid for any reason
     * (expired, not who it belongs to, etc.).
     *
     * @param license The license to validate
     * @throws InvalidLicenseException when the license is invalid for any reason; the implementer is required to provide adequate description in this exception to indicate why the license is invalid; extending the exception is encouraged.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public void validateLicense(License license) throws InvalidLicenseException;
}
