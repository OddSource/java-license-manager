/*
 * TestImmutableLinkedHashSet.java from LicenseManager modified Tuesday, February 21, 2012 10:59:35 CST (-0600).
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

package io.oddsource.java.licensing.immutable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

/**
 * Test class for ImmutableLinkedHashSet and ImmutableAbstractCollection.
 */
public class TestImmutableLinkedHashSet
{
    private ImmutableLinkedHashSet<String> set;

    public TestImmutableLinkedHashSet()
    {
        LinkedHashSet<String> temp = new LinkedHashSet<String>();
        temp.add("MyString1");
        temp.add("YourString2");
        temp.add("HisString3");
        temp.add("HerString4");

        this.set = new ImmutableLinkedHashSet<String>(temp);
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> getInternal()
    {
        try
        {
            Field internalHashSet = this.set.getClass().getDeclaredField("internalSet");
            internalHashSet.setAccessible(true);
            return (HashSet<String>)internalHashSet.get(this.set);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
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
    public void testGetByIndex01()
    {
        assertEquals("The value is not correct.", "YourString2", this.set.get(1));
    }

    @Test
    public void testGetByIndex02()
    {
        assertEquals("The value is not correct.", "HerString4", this.set.get(3));
    }

    @Test
    public void testGetByValue01()
    {
        assertEquals("The value is not correct.", "MyString1", this.set.get("MyString1"));
    }

    @Test
    public void testGetByValue02()
    {
        assertEquals("The value is not correct.", "HisString3", this.set.get("HisString3"));
    }

    @Test
    public void testConstruct() throws Exception
    {
        HashSet<String> internalSet = this.getInternal();

        assertNotNull("The internal collection should not be null.", this.set.internalCollection);
        assertNotNull("The internal hash set should not be null.", internalSet);
        assertSame("The objects should be the same.", this.set.internalCollection, internalSet);

        Field field = ImmutableAbstractCollection.class.getDeclaredField("internalHashCode");
        field.setAccessible(true);

        int hashCode = field.getInt(this.set);

        assertEquals("The hash code is not correct.", internalSet.hashCode(), hashCode);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddNotAllowed()
    {
        this.set.add("AnyString");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddAllNotAllowed()
    {
        this.set.addAll(new HashSet<String>());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testClearNotAllowed()
    {
        this.set.clear();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveNotAllowed()
    {
        this.set.remove("AnyString");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveAllNotAllowed()
    {
        this.set.removeAll(new HashSet<String>());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRetainAllNotAllowed()
    {
        this.set.retainAll(new HashSet<String>());
    }

    @Test
    public void testHashCode01() throws Exception
    {
        Field field = ImmutableAbstractCollection.class.getDeclaredField("internalHashCode");
        field.setAccessible(true);

        int hashCode = field.getInt(this.set);

        assertEquals("The hash code is not correct.", hashCode, this.set.hashCode());
    }

    @Test
    public void testHashCode02()
    {
        HashSet<String> temp = new HashSet<String>();
        temp.add("MyString1");
        temp.add("YourString2");
        temp.add("HisString3");
        temp.add("HerString4");

        ImmutableLinkedHashSet<String> test = new ImmutableLinkedHashSet<String>(temp);

        assertEquals("The hash codes should be equal.", test.hashCode(), this.set.hashCode());
    }

    @Test
    public void testHashCode03()
    {
        HashSet<String> temp = new HashSet<String>();
        temp.add("MyString1");
        temp.add("YourString2");
        temp.add("HisString3");
        temp.add("HerString5");

        ImmutableLinkedHashSet<String> test = new ImmutableLinkedHashSet<String>(temp);

        assertFalse("The hash codes should not be equal.", test.hashCode() == this.set.hashCode());
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testHashCode04()
    {
        this.getInternal().add("Test");
        this.set.hashCode();
    }

    @Test
    public void testEquals01()
    {
        HashSet<String> temp = new HashSet<String>();
        temp.add("MyString1");
        temp.add("YourString2");
        temp.add("HisString3");
        temp.add("HerString4");

        ImmutableLinkedHashSet<String> test = new ImmutableLinkedHashSet<String>(temp);

        assertEquals("The sets should be equal.", test, this.set);
    }

    @Test
    public void testEquals02()
    {
        HashSet<String> temp = new HashSet<String>();
        temp.add("MyString1");
        temp.add("YourString2");
        temp.add("HisString3");
        temp.add("HerString5");

        ImmutableLinkedHashSet<String> test = new ImmutableLinkedHashSet<String>(temp);

        assertFalse("The sets should not be equal.", test.equals(this.set));
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testEquals03()
    {
        this.getInternal().add("Test");
        this.set.equals(this.set);
    }

    @Test
    public void testClone01()
    {
        ImmutableLinkedHashSet<String> test = this.set.clone();

        assertFalse("The sets should not be the same objects.", test == this.set);
        assertEquals("The sets should be equal.", this.set, test);
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testClone02()
    {
        this.getInternal().add(null);
        this.set.clone();
    }

    @Test
    public void testContains01()
    {
        assertTrue("The set should contain this string.", this.set.contains("MyString1"));
    }

    @Test
    public void testContains02()
    {
        assertFalse("The set should not contain this string.", this.set.contains("YourString1"));
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testContains03()
    {
        this.getInternal().add("Test");
        this.set.contains("Test");
    }

    @Test
    public void testContainsAll01()
    {
        HashSet<String> test = new HashSet<String>();
        test.add("MyString1");
        test.add("YourString2");

        assertTrue("The set should contain all of these strings.", this.set.containsAll(test));
    }

    @Test
    public void testContainsAll02()
    {
        HashSet<String> test = new HashSet<String>();
        test.add("MyString1");
        test.add("YourString3");

        assertFalse("The set should not contain all of these strings.", this.set.containsAll(test));
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testContainsAll03()
    {
        this.getInternal().add(null);

        this.set.containsAll(this.set);
    }

    @Test
    public void testIsEmpty01()
    {
        assertFalse("This set should not be empty.", this.set.isEmpty());
    }

    @Test
    public void testIsEmpty02()
    {
        assertTrue("The set should not be empty.",
                new ImmutableLinkedHashSet<String>(new HashSet<String>()).isEmpty());
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testIsEmpty03()
    {
        this.getInternal().clear();
        this.set.isEmpty();
    }

    @Test
    @SuppressWarnings("WhileLoopReplaceableByForEach")
    public void testIterator01()
    {
        int i = 0;

        ImmutableIterator<String> iterator = this.set.iterator();
        while(iterator.hasNext())
        {
            String string = iterator.next();

            assertNotNull("None of the strings should be null.", string);
            assertTrue(
                    "Expected MyString1 or YourString2 or HisString3 or HerString4 but got " + string + ".",
                    string.equals("MyString1") ||
                            string.equals("YourString2") ||
                            string.equals("HisString3") ||
                            string.equals("HerString4")
            );

            i++;
        }

        assertEquals("The iterator should have iterated four times.", 4, i);
    }

    @Test
    public void testIterator02()
    {
        int i = 0;

        for(String string : this.set)
        {
            assertNotNull("None of the strings should be null.", string);
            assertTrue(
                    "Expected MyString1 or YourString2 or HisString3 or HerString4 but got " + string + ".",
                    string.equals("MyString1") ||
                            string.equals("YourString2") ||
                            string.equals("HisString3") ||
                            string.equals("HerString4")
            );

            i++;
        }

        assertEquals("The iterator should have iterated four times.", 4, i);
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testIterator03()
    {
        this.getInternal().remove("MyString1");
        this.set.iterator();
    }

    @Test
    public void testSize01()
    {
        assertEquals("The size is not correct.", 4, this.set.size());
    }

    @Test
    public void testSize02()
    {
        HashSet<String> test = new HashSet<String>();
        test.add("MyString1");
        test.add("YourString2");

        assertEquals("The size is not correct.", 2, new ImmutableLinkedHashSet<String>(test).size());
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testSize03()
    {
        this.getInternal().add(null);
        this.set.size();
    }

    @Test
    public void testToObjectArray01()
    {
        Object[] array = this.set.toArray();

        assertNotNull("The array should not be null.", array);
        assertEquals("The array length is not correct.", 4, array.length);
    }

    @Test
    public void testToObjectArray02()
    {
        HashSet<String> test = new HashSet<String>();
        test.add("MyString1");
        test.add("HisString3");

        Object[] array = new ImmutableLinkedHashSet<String>(test).toArray();

        assertNotNull("The array should not be null.", array);
        assertEquals("The array length is not correct.", 2, array.length);
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    public void testToObjectArray03()
    {
        this.getInternal().add("Test");
        this.set.toArray();
    }

    @Test
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public void testToPrototypedArray01()
    {
        String[] array = this.set.toArray(new String[0]);

        assertNotNull("The array should not be null.", array);
        assertEquals("The array length is not correct.", 4, array.length);
    }

    @Test
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public void testToPrototypedArray02()
    {
        HashSet<String> test = new HashSet<String>();
        test.add("MyString1");
        test.add("HisString3");

        String[] array = new ImmutableLinkedHashSet<String>(test).toArray(new String[0]);

        assertNotNull("The array should not be null.", array);
        assertEquals("The array length is not correct.", 2, array.length);
    }

    @Test(expected=ImmutableModifiedThroughReflectionException.class)
    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public void testToPrototypedArray03()
    {
        this.getInternal().add(null);
        this.set.toArray(new String[0]);
    }
}