/*
 * TestLicense.java from LicenseManager modified Thursday, January 24, 2013 16:04:29 CST (-0600).
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

import io.oddsource.java.licensing.immutable.ImmutableLinkedHashSet;
import io.oddsource.java.licensing.mock.MockFeatureObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
                                addFeature("nickFeature1").
                                addFeature("allisonFeature2", 2348917325000L).
                                build();
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void testConstructorParts01() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Constructor<License> constructor = License.class.getDeclaredConstructor(String[].class);
        constructor.setAccessible(true);

        try
        {
            String[] argument = null;
            constructor.newInstance(new Object[] { argument });
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testConstructorParts02() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Constructor<License> constructor = License.class.getDeclaredConstructor(String[].class);
        constructor.setAccessible(true);

        try
        {
            String[] argument = new String[] {};
            constructor.newInstance(new Object[] { argument });
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testConstructorParts03() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Constructor<License> constructor = License.class.getDeclaredConstructor(String[].class);
        constructor.setAccessible(true);

        try
        {
            String[] argument = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight"};
            constructor.newInstance(new Object[] { argument });
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testConstructorParts04() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Constructor<License> constructor = License.class.getDeclaredConstructor(String[].class);
        constructor.setAccessible(true);

        try
        {
            String[] argument = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
            constructor.newInstance(new Object[] { argument });
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
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

        assertTrue("Feature 1 is missing now.", this.license.hasLicenseForFeature("nickFeature1"));
        assertTrue("Feature 2 is missing now.", this.license.hasLicenseForFeature("allisonFeature2"));
        assertFalse("There's a phantom feature.", this.license.hasLicenseForFeature("fakeFeature3"));
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
    public void testFeatures05()
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
                                addFeature("goodFeature1").
                                addFeature("goodFeature2", 2348917325000L).
                                addFeature("expiredFeature3", 1L).
                                build();

        assertTrue("Feature 1 is missing now.", this.license.hasLicenseForFeature("goodFeature1"));
        assertTrue("Feature 2 is missing now.", this.license.hasLicenseForFeature("goodFeature2"));
        assertFalse("There's a phantom feature.", this.license.hasLicenseForFeature("expiredFeature3"));

        assertTrue("Error 1", this.license.hasLicenseForAnyFeature("goodFeature1", "expiredFeature3"));
        assertFalse("Error 2", this.license.hasLicenseForAnyFeature("expiredFeature3"));

        assertTrue("Error 3", this.license.hasLicenseForAllFeatures("goodFeature1", "goodFeature2"));
        assertFalse("Error 4", this.license.hasLicenseForAllFeatures("goodFeature1", "expiredFeature3"));
        assertFalse("Error 5", this.license.hasLicenseForAllFeatures("expiredFeature3"));
    }

    @Test
    public void testFeatures06()
    {
        assertTrue("Feature 1 is missing.",
                   this.license.hasLicenseForAllFeatures(new MockFeatureObject("nickFeature1")));
        assertTrue("Feature 2 is missing.",
                   this.license.hasLicenseForAllFeatures(new MockFeatureObject("allisonFeature2")));

        assertTrue("Feature 1 is missing now.",
                   this.license.hasLicenseForFeature(new MockFeatureObject("nickFeature1")));
        assertTrue("Feature 2 is missing now.",
                   this.license.hasLicenseForFeature(new MockFeatureObject("allisonFeature2")));
        assertFalse("There's a phantom feature.",
                    this.license.hasLicenseForFeature(new MockFeatureObject("fakeFeature3")));
    }

    @Test
    public void testFeatures07()
    {
        assertTrue("Result 1 is incorrect.", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("allisonFeature2")
        ));
        assertFalse("Result 2 is incorrect.", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("timFeature1"), new MockFeatureObject("allisonFeature2")
        ));
        assertFalse("Result 3 is incorrect.", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("timFeature2")
        ));
        assertFalse("Result 4 is incorrect.", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("allisonFeature2"),
                new MockFeatureObject("timFeature3")
        ));
        assertFalse("Result 5 is incorrect.", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("jeffFeature1"), new MockFeatureObject("timFeature2")
        ));
        assertFalse("Result 6 is incorrect.",
                    this.license.hasLicenseForAllFeatures(new MockFeatureObject("dogFeature1")));
    }

    @Test
    public void testFeatures08()
    {
        assertTrue("Result 1 is incorrect.", this.license.hasLicenseForAnyFeature(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("allisonFeature2")
        ));
        assertTrue("Result 2 is incorrect.", this.license.hasLicenseForAnyFeature(
                new MockFeatureObject("timFeature1"), new MockFeatureObject("allisonFeature2")
        ));
        assertTrue("Result 3 is incorrect.", this.license.hasLicenseForAnyFeature(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("timFeature2")
        ));
        assertTrue("Result 4 is incorrect.", this.license.hasLicenseForAnyFeature(
                new MockFeatureObject("nickFeature1"), new MockFeatureObject("allisonFeature2"),
                new MockFeatureObject("timFeature3")
        ));
        assertFalse("Result 5 is incorrect.", this.license.hasLicenseForAnyFeature(
                new MockFeatureObject("jeffFeature1"), new MockFeatureObject("timFeature2")
        ));
        assertFalse("Result 6 is incorrect.",
                    this.license.hasLicenseForAnyFeature(new MockFeatureObject("dogFeature1")));
        assertTrue("Result 7 is incorrect.",
                   this.license.hasLicenseForAnyFeature(new MockFeatureObject("nickFeature1")));
        assertTrue("Result 8 is incorrect.",
                   this.license.hasLicenseForAnyFeature(new MockFeatureObject("allisonFeature2")));
    }

    @Test
    public void testFeatures09()
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
                                addFeature("goodFeature1").
                                addFeature("goodFeature2", 2348917325000L).
                                addFeature("expiredFeature3", 1L).
                                build();

        assertTrue("Feature 1 is missing now.",
                   this.license.hasLicenseForFeature(new MockFeatureObject("goodFeature1")));
        assertTrue("Feature 2 is missing now.",
                   this.license.hasLicenseForFeature(new MockFeatureObject("goodFeature2")));
        assertFalse("There's a phantom feature.",
                    this.license.hasLicenseForFeature(new MockFeatureObject("expiredFeature3")));

        assertTrue("Error 1", this.license.hasLicenseForAnyFeature(new MockFeatureObject(
                "goodFeature1"), new MockFeatureObject("expiredFeature3")
        ));
        assertFalse("Error 2", this.license.hasLicenseForAnyFeature(new MockFeatureObject("expiredFeature3")));

        assertTrue("Error 3", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("goodFeature1"), new MockFeatureObject("goodFeature2")
        ));
        assertFalse("Error 4", this.license.hasLicenseForAllFeatures(
                new MockFeatureObject("goodFeature1"), new MockFeatureObject("expiredFeature3")
        ));
        assertFalse("Error 5",
                    this.license.hasLicenseForAllFeatures(new MockFeatureObject("expiredFeature3")));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEqualsWithNullObject01()
    {
        assertFalse("The objects should not be equal.", this.license.equals(null));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testEqualsWithNonLicense01()
    {
        assertFalse("The objects should not be equal.", this.license.equals("Hello"));
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
                                    addFeature("nickFeature1").
                                    addFeature("allisonFeature2", 2348917325000L).
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                addFeature("allisonFeature2", 2348917325000L).
                                addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2", 2348917325000L).
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature3").
                                    addFeature("nickFeature1").
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
                                    addFeature("allisonFeature2").
                                    addFeature("nickFeature4").
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
                                    addFeature("nickFeature1").
                                    addFeature("allisonFeature2", 2348917325000L).
                                    build();

        assertNotSame("The objects should not be the same.", this.license, duplicate);
        assertFalse("The objects should not be equal.", this.license.equals(duplicate));
        assertFalse("The hash codes should not match.", this.license.hashCode() == duplicate.hashCode());
    }

    @Test
    public void testEqualsWithSimpleBlankLicense01()
    {
        License simple = new License.Builder().build();

        assertNotSame("The objects should not be the same.", this.license, simple);
        assertFalse("The objects should not be equal.", this.license.equals(simple));
        assertFalse("The hash codes should not match.", this.license.hashCode() == simple.hashCode());
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
        License license = License.deserialize(
                ( "[5565-1039-AF89-GGX7-TN31-14AL][CN=John E. Smith, C=CA, ST=QE][CN=OurCompany, C=US, ST=KY][Cool Product, by Company][14429073214631][1443907325000][1443917325000][12][fordFeature1" +
                  (char) 0x1F + "-1, chevyFeature2" + (char) 0x1F + Long.MAX_VALUE + ", hondaFeature3" + (char) 0x1F +
                  Long.MAX_VALUE + ", toyotaFeature4" + (char) 0x1F + "-1]" ).getBytes());

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
        License license = License.deserialize(
                ( "[6575-TH0T-SNL5-7XGG-1099-1040][CN=John E. Smith, C=CA, ST=QE][CN=OurCompany, C=US, ST=KY][Cool Product, by Company][14429073214631][1443907325000][1443917325000][12][fordFeature1" +
                  (char) 0x1F + "-1, chevyFeature2" + (char) 0x1F + Long.MAX_VALUE + ", hondaFeature3" + (char) 0x1F +
                  "1234567890, toyotaFeature4" + (char) 0x1F + "-1]" ).getBytes());

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

    @Test
    public void testDeserialization03()
    {
        License license = License.deserialize(
                ( "[][][][][0][0][0][0][]" ).getBytes());

        assertEquals("The product key is not correct.", "", license.getProductKey());
        assertEquals("The holder is not correct.", "", license.getHolder());
        assertEquals("The issuer is not correct.", "", license.getIssuer());
        assertEquals("The company is not correct.", "", license.getSubject());
        assertEquals("The issue date is not correct.", 0L, license.getIssueDate());
        assertEquals("The good after date is not correct.", 0L, license.getGoodAfterDate());
        assertEquals("The good before date is not correct.", 0L, license.getGoodBeforeDate());
        assertEquals("The number of licenses is not correct.", 0, license.getNumberOfLicenses());
        assertEquals("The number of features is not correct.", 0, license.getFeatures().size());
    }

    @Test
    public void testLicenseFeatureFromString01() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        try
        {
            fromString.invoke(null, new Object[] { null });
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testLicenseFeatureFromString02() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        try
        {
            fromString.invoke(null, "one");
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testLicenseFeatureConstructor03() throws NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        try
        {
            fromString.invoke(null, "one" + (char) 0x1F + "2" + (char) 0x1F + "three");
            fail("Expected exception IllegalArgumentException.");
        }
        catch(InvocationTargetException e)
        {
            assertNotNull("The cause should not be null.", e.getCause());
            assertSame("The cause is not correct.", IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testLicenseFeatureEquals01() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        License.Feature feature1 = (License.Feature)fromString.invoke(null, "one" + (char)0x1F + "2");

        assertFalse("Equals should return false.", feature1.equals(null));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testLicenseFeatureEquals02() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        License.Feature feature1 = (License.Feature)fromString.invoke(null, "one" + (char)0x1F + "2");

        assertFalse("Equals should return false.", feature1.equals("one" + (char)0x1F + "2"));
    }

    @Test
    public void testLicenseFeatureEquals03() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        License.Feature feature1 = (License.Feature)fromString.invoke(null, "one" + (char)0x1F + "2");
        License.Feature feature2 = (License.Feature)fromString.invoke(null, "one" + (char)0x1F + "5");

        assertNotSame("The objects should not be the same.", feature1, feature2);
        assertEquals("The objects should be equal.", feature1, feature2);
        assertEquals("The hash codes should be equal.", feature1.hashCode(), feature2.hashCode());
    }

    @Test
    public void testLicenseFeatureEquals04() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Method fromString = License.Feature.class.getDeclaredMethod("fromString", String.class);
        fromString.setAccessible(true);

        License.Feature feature1 = (License.Feature)fromString.invoke(null, "one" + (char)0x1F + "2");
        License.Feature feature2 = (License.Feature)fromString.invoke(null, "three" + (char)0x1F + "5");

        assertNotSame("The objects should not be the same.", feature1, feature2);
        assertFalse("The objects should not be equal.", feature1.equals(feature2));
        assertFalse("The hash codes should not be equal.", feature1.hashCode() == feature2.hashCode());
    }
}