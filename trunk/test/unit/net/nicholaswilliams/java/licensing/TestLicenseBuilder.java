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

package net.nicholaswilliams.java.licensing;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.security.auth.x500.X500Principal;

import static org.junit.Assert.*;

/**
 * Test class for License.Builder.
 */
public class TestLicenseBuilder
{
	public TestLicenseBuilder()
	{
		
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{

	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
		
	}

	@Before
	public void setUp()
	{
		
	}

	@After
	public void tearDown()
	{
		
	}

	@Test
	public void testBuild01()
	{
		License license = new License(new License.Builder().
						withIssuer(new X500Principal("CN=Nick Williams, C=US, ST=TN")).
						withHolder(new X500Principal("CN=Tim Williams, C=US, ST=AL")).
						withIssueDate(1234567890000L).
						withGoodAfterDate(1199145600000L).
						withGoodBeforeDate(1924991999000L).
						withNumberOfLicenses(15).
						withFeature("FEATURE1").
						withFeature("FEATURE3")
		);

		assertEquals("The issuer is not correct.",
				new X500Principal("CN=Nick Williams, C=US, ST=TN"),
				license.getIssuer());
		assertEquals("The holder is not correct.",
				new X500Principal("CN=Tim Williams, C=US, ST=AL"),
				license.getHolder());
		assertEquals("The issue date is not correct.", 1234567890000L, license.getIssueDate());
		assertEquals("The after date is not correct.", 1199145600000L, license.getGoodAfterDate());
		assertEquals("The before date is not correct.", 1924991999000L, license.getGoodBeforeDate());
		assertEquals("The number of licenses is not correct.", 15, license.getNumberOfLicenses());
		assertTrue("Feature mismatch #1.", license.hasLicenseForAllFeatures("FEATURE1"));
		assertTrue("Feature mismatch #2.", license.hasLicenseForAllFeatures("FEATURE3"));
		assertFalse("Feature mismatch #3.", license.hasLicenseForAllFeatures("FEATURE2"));
	}

	@Test
	public void testBuild02()
	{
		License license = new License(new License.Builder().
						withIssuer(new X500Principal("CN=UUID-5581CAD6-A0C5-1A88-C32B6117B700B53F, C=US, ST=TN")).
						withHolder(new X500Principal("CN=UUID-5581CAE6-0232-5353-1DC9AF6A79FE7106, C=US, ST=AL")).
						withIssueDate(1285629859000L).
						withGoodAfterDate(1136073600000L).
						withGoodBeforeDate(1230767999000L).
						withNumberOfLicenses(5).
						withFeature("FEATURE2")
		);

		assertEquals("The issuer is not correct.",
				new X500Principal("CN=UUID-5581CAD6-A0C5-1A88-C32B6117B700B53F, C=US, ST=TN"),
				license.getIssuer());
		assertEquals("The holder is not correct.",
				new X500Principal("CN=UUID-5581CAE6-0232-5353-1DC9AF6A79FE7106, C=US, ST=AL"),
				license.getHolder());
		assertEquals("The issue date is not correct.", 1285629859000L, license.getIssueDate());
		assertEquals("The after date is not correct.", 1136073600000L, license.getGoodAfterDate());
		assertEquals("The before date is not correct.", 1230767999000L, license.getGoodBeforeDate());
		assertEquals("The number of licenses is not correct.", 5, license.getNumberOfLicenses());
		assertTrue("Feature mismatch #1.", license.hasLicenseForAllFeatures("FEATURE2"));
		assertFalse("Feature mismatch #2.", license.hasLicenseForAllFeatures("FEATURE1"));
		assertFalse("Feature mismatch #3.", license.hasLicenseForAllFeatures("FEATURE3"));
	}
}