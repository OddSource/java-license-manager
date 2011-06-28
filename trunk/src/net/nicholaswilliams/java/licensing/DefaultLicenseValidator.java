/*
 * DefaultLicenseValidator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

import net.nicholaswilliams.java.licensing.exception.ExpiredLicenseException;
import net.nicholaswilliams.java.licensing.exception.InvalidLicenseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A default implementation of {@link LicenseValidator}, which simply checks that the license is active and not expired.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultLicenseValidator implements LicenseValidator
{
	/**
	 * Ensures the current date is between the license's good-after and good-before dates (the license
	 * has taken effect and hasn't expired).
	 *
	 * @param license The license to validate
	 * @throws net.nicholaswilliams.java.licensing.exception.InvalidLicenseException when the license is invalid for any reason; the implementer is required to provide adequate description in this exception to indicate why the license is invalid; extending the exception is encouraged
	 * @see InvalidLicenseException
	 * @see ExpiredLicenseException
	 */
	@Override
	public void validateLicense(License license) throws InvalidLicenseException
	{
		long time = Calendar.getInstance().getTimeInMillis();
		if(license.getGoodAfterDate() > time)
			throw new InvalidLicenseException("The " + this.getLicenseDescription(license) + " does not take effect until " + this.getFormattedDate(license.getGoodAfterDate()) + ".");
		if(license.getGoodBeforeDate() < time)
			throw new ExpiredLicenseException("The " + this.getLicenseDescription(license) + " expired on " + this.getFormattedDate(license.getGoodAfterDate()) + ".");
	}

	public String getLicenseDescription(License license)
	{
		return license.getSubject() + " license for " + license.getHolder();
	}

	public String getFormattedDate(long time)
	{
		return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z (Z)").format(new Date(time));
	}
}
