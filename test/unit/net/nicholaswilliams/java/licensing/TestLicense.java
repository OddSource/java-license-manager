/*
 * TestLicense.java from LicenseManager modified Monday, February 13, 2012 23:37:18 CST (-0600).
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

import net.nicholaswilliams.java.licensing.immutable.ImmutableLinkedHashSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for License.
 */
public class TestLicense
{
	private License license;

	@Before
	public void setUp()
	{
		this.license = new License.Builder().
								withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
								withIssuer("CN=Nick Williams, C=US, ST=TN").
								withHolder("CN=Tim Williams, C=US, ST=AL").
								withSubject("Simple Product Name(TM)").
								withIssueDate(2348907324983L).
								withGoodAfterDate(2348907325000L).
								withGoodBeforeDate(2348917325000L).
								withNumberOfLicenses(57).
								withFeature("nickFeature1").
								withFeature("allisonFeature2", 2348917325000L).
								build();
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testProductKey01()
	{
		assertEquals("The product key is not correct.", "5565-1039-AF89-GGX7-TN31-14AL", this.license.getProductKey());
	}

	@Test
	public void testIssuer01()
	{
		assertEquals("The issuer is not correct.", "CN=Nick Williams, C=US, ST=TN", this.license.getIssuer());
	}

	@Test
	public void testHolder01()
	{
		assertEquals("The holder is not correct.", "CN=Tim Williams, C=US, ST=AL", this.license.getHolder());
	}

	@Test
	public void testSubject01()
	{
		assertEquals("The subject is not correct.", "Simple Product Name(TM)", this.license.getSubject());
	}

	@Test
	public void testIssueDate01()
	{
		assertEquals("The issue date is not correct.", 2348907324983L, this.license.getIssueDate());
	}

	@Test
	public void testGoodAfterDate01()
	{
		assertEquals("The good after date is not correct.", 2348907325000L, this.license.getGoodAfterDate());
	}

	@Test
	public void testGoodBeforeDate01()
	{
		assertEquals("The good before date is not correct.", 2348917325000L, this.license.getGoodBeforeDate());
	}

	@Test
	public void testNumberOfLicenses01()
	{
		assertEquals("The number of licenses is not correct.", 57, this.license.getNumberOfLicenses());
	}

	@Test
	public void testFeatures01()
	{
		ImmutableLinkedHashSet<License.Feature> features = this.license.getFeatures();

		assertEquals("The size of the features is not correct.", 2, features.size());
		assertNotNull("Feature 1 is missing.", features.get(0));
		assertEquals("Feature 1 is not correct.", "nickFeature1", features.get(0).getName());
		assertNotNull("Feature 2 is missing.", features.get(1));
		assertEquals("Feature 2 is not correct.", "allisonFeature2", features.get(1).getName());
		assertEquals("Feature 2 is not correct.", 2348917325000L, features.get(1).getGoodBeforeDate());
	}

	@Test
	public void testFeatures02()
	{
		assertTrue("Feature 1 is missing.", this.license.hasLicenseForAllFeatures("nickFeature1"));
		assertTrue("Feature 2 is missing.", this.license.hasLicenseForAllFeatures("allisonFeature2"));
	}

	@Test
	public void testFeatures03()
	{
		assertTrue("Result 1 is incorrect.", this.license.hasLicenseForAllFeatures("nickFeature1", "allisonFeature2"));
		assertFalse("Result 2 is incorrect.", this.license.hasLicenseForAllFeatures("timFeature1", "allisonFeature2"));
		assertFalse("Result 3 is incorrect.", this.license.hasLicenseForAllFeatures("nickFeature1", "timFeature2"));
		assertFalse("Result 4 is incorrect.", this.license.hasLicenseForAllFeatures("nickFeature1", "allisonFeature2", "timFeature3"));
		assertFalse("Result 5 is incorrect.", this.license.hasLicenseForAllFeatures("jeffFeature1", "timFeature2"));
		assertFalse("Result 6 is incorrect.", this.license.hasLicenseForAllFeatures("dogFeature1"));
	}

	@Test
	public void testFeatures04()
	{
		assertTrue("Result 1 is incorrect.", this.license.hasLicenseForAnyFeature("nickFeature1", "allisonFeature2"));
		assertTrue("Result 2 is incorrect.", this.license.hasLicenseForAnyFeature("timFeature1", "allisonFeature2"));
		assertTrue("Result 3 is incorrect.", this.license.hasLicenseForAnyFeature("nickFeature1", "timFeature2"));
		assertTrue("Result 4 is incorrect.", this.license.hasLicenseForAnyFeature("nickFeature1", "allisonFeature2", "timFeature3"));
		assertFalse("Result 5 is incorrect.", this.license.hasLicenseForAnyFeature("jeffFeature1", "timFeature2"));
		assertFalse("Result 6 is incorrect.", this.license.hasLicenseForAnyFeature("dogFeature1"));
		assertTrue("Result 7 is incorrect.", this.license.hasLicenseForAnyFeature("nickFeature1"));
		assertTrue("Result 8 is incorrect.", this.license.hasLicenseForAnyFeature("allisonFeature2"));
	}

	@Test
	public void testEqualsWithClone01()
	{
		License clone = this.license.clone();

		assertNotSame("The objects should not be the same.", this.license, clone);
		assertEquals("The objects should be equal.", this.license, clone);
		assertEquals("The hash codes should match.", this.license.hashCode(), clone.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate01()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("nickFeature1").
									withFeature("allisonFeature2", 2348917325000L).
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertEquals("The objects should be equal.", this.license, duplicate);
		assertEquals("The hash codes should match.", this.license.hashCode(), duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate02()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertTrue("The objects should be equal.", this.license.equals(duplicate));
		assertEquals("The hash codes should match.", this.license.hashCode(), duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate03()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate04()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325001L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate05()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(56).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate06()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907324999L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate07()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324984L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate08()
	{
		License duplicate = new License.Builder().
								withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
								withIssuer("CN=Nick Williams, C=US, ST=TN").
								withHolder("CN=Tim Williams, C=US, ST=TN").
								withSubject("Simple Product Name(TM)").
								withIssueDate(2348907324983L).
								withGoodAfterDate(2348907325000L).
								withGoodBeforeDate(2348917325000L).
								withNumberOfLicenses(57).
								withFeature("allisonFeature2", 2348917325000L).
								withFeature("nickFeature1").
								build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate09()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=AL").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2", 2348917325000L).
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate10()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature3").
									withFeature("nickFeature1").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate11()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AL").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("allisonFeature2").
									withFeature("nickFeature4").
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testEqualsWithDuplicate12()
	{
		License duplicate = new License.Builder().
									withProductKey("5565-1039-AF89-GGX7-TN31-14AM").
									withIssuer("CN=Nick Williams, C=US, ST=TN").
									withHolder("CN=Tim Williams, C=US, ST=AL").
									withSubject("Simple Product Name(TM)").
									withIssueDate(2348907324983L).
									withGoodAfterDate(2348907325000L).
									withGoodBeforeDate(2348917325000L).
									withNumberOfLicenses(57).
									withFeature("nickFeature1").
									withFeature("allisonFeature2", 2348917325000L).
									build();

		assertNotSame("The objects should not be the same.", this.license, duplicate);
		assertFalse("The objects should not be equal.", this.license.equals(duplicate));
		assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
	}

	@Test
	public void testToString()
	{
		assertEquals("The string was not correct.",
					 "[5565-1039-AF89-GGX7-TN31-14AL][CN=Tim Williams, C=US, ST=AL][CN=Nick Williams, C=US, ST=TN][Simple Product Name(TM)][2348907324983][2348907325000][2348917325000][57][nickFeature1"+(char)0x1F+"-1, allisonFeature2"+(char)0x1F+"2348917325000]",
					 this.license.toString());
	}

	@Test
	public void testSerialization()
	{
		assertEquals("The serialization was not correct.",
					 "[5565-1039-AF89-GGX7-TN31-14AL][CN=Tim Williams, C=US, ST=AL][CN=Nick Williams, C=US, ST=TN][Simple Product Name(TM)][2348907324983][2348907325000][2348917325000][57][nickFeature1"+(char)0x1F+"-1, allisonFeature2"+(char)0x1F+"2348917325000]",
					 new String(this.license.serialize()));
	}

	@Test
	public void testDeserialization01()
	{
		License license = License.deserialize(("[5565-1039-AF89-GGX7-TN31-14AL][CN=John E. Smith, C=CA, ST=QE][CN=OurCompany, C=US, ST=KY][Cool Product, by Company][14429073214631][1443907325000][1443917325000][12][fordFeature1"+(char)0x1F+"-1, chevyFeature2"+(char)0x1F+Long.MAX_VALUE+", hondaFeature3"+(char)0x1F+Long.MAX_VALUE+", toyotaFeature4"+(char)0x1F+"-1]").getBytes());

		assertEquals("The product key is not correct.", "5565-1039-AF89-GGX7-TN31-14AL", license.getProductKey());
		assertEquals("The holder is not correct.", "CN=John E. Smith, C=CA, ST=QE", license.getHolder());
		assertEquals("The issuer is not correct.", "CN=OurCompany, C=US, ST=KY", license.getIssuer());
		assertEquals("The company is not correct.", "Cool Product, by Company", license.getSubject());
		assertEquals("The issue date is not correct.", 14429073214631L, license.getIssueDate());
		assertEquals("The good after date is not correct.", 1443907325000L, license.getGoodAfterDate());
		assertEquals("The good before date is not correct.", 1443917325000L, license.getGoodBeforeDate());
		assertEquals("The number of licenses is not correct.", 12, license.getNumberOfLicenses());
		assertEquals("The number of features is not correct.", 4, license.getFeatures().size());
		assertTrue("Feature 1 is missing.", license.hasLicenseForAllFeatures("fordFeature1"));
		assertTrue("Feature 2 is missing.", license.hasLicenseForAllFeatures("chevyFeature2"));
		assertTrue("Feature 3 is missing.", license.hasLicenseForAllFeatures("hondaFeature3"));
		assertTrue("Feature 4 is missing.", license.hasLicenseForAllFeatures("toyotaFeature4"));
	}

	@Test
	public void testDeserialization02()
	{
		License license = License.deserialize(("[6575-TH0T-SNL5-7XGG-1099-1040][CN=John E. Smith, C=CA, ST=QE][CN=OurCompany, C=US, ST=KY][Cool Product, by Company][14429073214631][1443907325000][1443917325000][12][fordFeature1"+(char)0x1F+"-1, chevyFeature2"+(char)0x1F+Long.MAX_VALUE+", hondaFeature3"+(char)0x1F+"1234567890, toyotaFeature4"+(char)0x1F+"-1]").getBytes());

		assertEquals("The product key is not correct.", "6575-TH0T-SNL5-7XGG-1099-1040", license.getProductKey());
		assertEquals("The holder is not correct.", "CN=John E. Smith, C=CA, ST=QE", license.getHolder());
		assertEquals("The issuer is not correct.", "CN=OurCompany, C=US, ST=KY", license.getIssuer());
		assertEquals("The company is not correct.", "Cool Product, by Company", license.getSubject());
		assertEquals("The issue date is not correct.", 14429073214631L, license.getIssueDate());
		assertEquals("The good after date is not correct.", 1443907325000L, license.getGoodAfterDate());
		assertEquals("The good before date is not correct.", 1443917325000L, license.getGoodBeforeDate());
		assertEquals("The number of licenses is not correct.", 12, license.getNumberOfLicenses());
		assertEquals("The number of features is not correct.", 4, license.getFeatures().size());
		assertTrue("Feature 1 is missing.", license.hasLicenseForAllFeatures("fordFeature1"));
		assertTrue("Feature 2 is missing.", license.hasLicenseForAllFeatures("chevyFeature2"));
		assertNotNull("Feature 3 is missing.", license.getFeatures().get(2));
		assertEquals("Feature 3 is missing.", "hondaFeature3", license.getFeatures().get(2).getName());
		assertFalse("Feature 3 should be expired.", license.hasLicenseForAllFeatures("hondaFeature3"));
		assertTrue("Feature 4 is missing.", license.hasLicenseForAllFeatures("toyotaFeature4"));
	}
}