/*
 * TestSignedLicense.java from LicenseManager modified Friday, June 1, 2012 15:54:15 CDT (-0500).
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

package net.nicholaswilliams.java.licensing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SignedLicense.
 */
public class TestSignedLicense
{
	private SignedLicense license;

	@Before
	public void setUp()
	{
		this.license = new SignedLicense(new byte[]{ 0x29, 0x7F, 0x3C }, new byte[]{ 0x01, 0x02, 0x77, 0x40, 0x0F });
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testLicenseContent()
	{
		assertArrayEquals("The license content is not correct.", new byte[]{ 0x29, 0x7F, 0x3C },
						  this.license.getLicenseContent());
	}

	@Test
	public void testSignatureContent()
	{
		assertArrayEquals("The signature content is not correct.", new byte[]{ 0x01, 0x02, 0x77, 0x40, 0x0F },
						  this.license.getSignatureContent());
	}

	@Test
	public void testDeserialization() throws ClassNotFoundException
	{
		Class.forName("net.nicholaswilliams.java.licensing.LicenseSecurityManager");

		byte[] data = new ObjectSerializer().writeObject(this.license);

		SignedLicense signedLicense = new ObjectSerializer().readObject(SignedLicense.class, data);

		assertArrayEquals("The license content is not correct.",
						  this.license.getLicenseContent(), signedLicense.getLicenseContent());
		assertArrayEquals("The signature content is not correct.",
						  this.license.getSignatureContent(), signedLicense.getSignatureContent());
	}
}