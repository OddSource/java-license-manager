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
import static org.junit.Assert.*;

import net.nicholaswilliams.java.licensing.licensor.X500PrincipalBuilder;
import org.junit.*;

/**
 * Test class for X500PrincipalBuilder.
 */
public class TestX500PrincipalBuilder
{
	private X500PrincipalBuilder builder;

	public TestX500PrincipalBuilder()
	{
		this.builder = new X500PrincipalBuilder();
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
	public void testGenerate01()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams"),
				principal
		);
	}

	@Test
	public void testGenerate02()
	{
		X500Principal principal = this.builder.
				withCommonName("Tim Williams").withOrganization("DNSCrawler").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Tim Williams, O=DNSCrawler",
				principal.toString());
		
		assertEquals("The principals should match.",
				new X500Principal("CN=Tim Williams, O=DNSCrawler"),
				principal
		);
	}

	@Test
	public void testGenerate03()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").withOrganization("JavaPros").
				withOrganizationalUnit("Licensing").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams, O=JavaPros, OU=Licensing",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams, O=JavaPros, OU=Licensing"),
				principal
		);
	}

	@Test
	public void testGenerate04()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").withOrganization("JavaPros").
				withOrganizationalUnit("R&D").withCountry("CA").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams, O=JavaPros, OU=R&D, C=CA",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams, O=JavaPros, OU=R&D, C=CA"),
				principal
		);
	}

	@Test
	public void testGenerate05()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").withOrganization("JavaPros").
				withOrganizationalUnit("R&D").withCountry("US").
				withStateOrProvince("AL").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=AL",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=AL"),
				principal
		);
	}

	@Test
	public void testGenerate06()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").withOrganization("JavaPros").
				withOrganizationalUnit("R&D").withCountry("US").
				withStateOrProvince("TN").withStreetAddress("123 Acorn Ave").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=TN, STREET=123 Acorn Ave",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=TN, STREET=123 Acorn Ave"),
				principal
		);
	}

	@Test
	public void testGenerate07()
	{
		X500Principal principal = this.builder.
				withCommonName("Nick Williams").withOrganization("JavaPros").
				withOrganizationalUnit("R&D").withCountry("US").
				withStateOrProvince("TN").withStreetAddress("321 Walnut Court").
				generate();

		assertEquals("The principal is not correct.",
				"CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=TN, STREET=321 Walnut Court",
				principal.toString());

		assertEquals("The principals should match.",
				new X500Principal("CN=Nick Williams, O=JavaPros, OU=R&D, C=US, ST=TN, STREET=321 Walnut Court"),
				principal
		);
	}
}