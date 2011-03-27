/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor;

import javax.security.auth.x500.X500Principal;

/**
 * This class builds {@code X500Principal}s.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class X500PrincipalBuilder
{
	private String commonName;

	private String organization;

	private String organizationalUnit;

	private String country;

	private String stateOrProvince;

	private String streetAddress;

	public X500PrincipalBuilder()
	{

	}

	public X500PrincipalBuilder withCommonName(String commonName)
	{
		this.commonName = commonName;
		return this;
	}

	public X500PrincipalBuilder withOrganization(String organization)
	{
		this.organization = organization;
		return this;
	}

	public X500PrincipalBuilder withOrganizationalUnit(String organizationalUnit)
	{
		this.organizationalUnit = organizationalUnit;
		return this;
	}

	public X500PrincipalBuilder withCountry(String country)
	{
		this.country = country;
		return this;
	}

	public X500PrincipalBuilder withStateOrProvince(String stateOrProvince)
	{
		this.stateOrProvince = stateOrProvince;
		return this;
	}

	public X500PrincipalBuilder withStreetAddress(String streetAddress)
	{
		this.streetAddress = streetAddress;
		return this;
	}

	public X500Principal generate()
	{
		StringBuilder name = new StringBuilder("CN=").append(this.commonName);
		
		if(this.organization != null && this.organization.trim().length() > 0)
			name.append(", O=").append(this.organization);
		
		if(this.organizationalUnit != null && this.organizationalUnit.trim().length() > 0)
			name.append(", OU=").append(this.organizationalUnit);
		
		if(this.country != null && this.country.trim().length() > 0)
			name.append(", C=").append(this.country);
		
		if(this.stateOrProvince != null && this.stateOrProvince.trim().length() > 0)
			name.append(", ST=").append(this.stateOrProvince);
		
		if(this.streetAddress != null && this.streetAddress.trim().length() > 0)
			name.append(", STREET=").append(this.streetAddress);
		
		return new X500Principal(name.toString());
	}
}
