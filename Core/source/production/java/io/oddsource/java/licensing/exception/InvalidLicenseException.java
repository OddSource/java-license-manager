/*
 * InvalidLicenseException.java from LicenseManager modified Friday, September 21, 2012 07:46:54 CDT (-0500).
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

package io.oddsource.java.licensing.exception;

import io.oddsource.java.licensing.LicenseValidator;

/**
 * This exception is thrown whenever a license is validated that isn't valid for some reason.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 * @see LicenseValidator
 */
@SuppressWarnings("unused")
public class InvalidLicenseException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InvalidLicenseException()
    {
        super("The license is not valid.");
    }

    public InvalidLicenseException(String message)
    {
        super(message);
    }

    public InvalidLicenseException(Throwable cause)
    {
        super("The license is not valid.", cause);
    }

    public InvalidLicenseException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
