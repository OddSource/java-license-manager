/*
 * TestImmutableArrayList.java from LicenseManager modified Monday, March 5, 2012 19:01:26 CST (-0600).
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

package net.nicholaswilliams.java.licensing.immutable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for ImmutableArrayList.
 */
public class TestImmutableArrayList
{
	private ImmutableArrayList<String> list;

	@Before
	public void setUp()
	{
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("MyString1");
		temp.add("YourString2");
		temp.add("HisString3");
		temp.add("HerString4");

		this.list = new ImmutableArrayList<String>(temp);
	}

	@After
	public void tearDown()
	{
		
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getInternal()
	{
		try
		{
			Field internalHashSet = this.list.getClass().getDeclaredField("internalList");
			internalHashSet.setAccessible(true);
			return (ArrayList<String>)internalHashSet.get(this.list);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testConstruct01() throws Exception
	{
		ArrayList<String> internalList = this.getInternal();

		assertNotNull("The internal collection should not be null.", this.list.internalCollection);
		assertNotNull("The internal array list should not be null.", internalList);
		assertSame("The objects should be the same.", this.list.internalCollection, internalList);

		Field field = ImmutableAbstractCollection.class.getDeclaredField("internalHashCode");
		field.setAccessible(true);

		int hashCode = field.getInt(this.list);

		assertEquals("The hash code is not correct.", internalList.hashCode(), hashCode);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddNotAllowed()
	{
		this.list.add(2, "Test");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddAllNotAllowed()
	{
		this.list.addAll(2, Arrays.asList("Test1", "Test2", "Test3"));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveNotAllowed()
	{
		this.list.remove("MyString1");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveByIntNotAllowed()
	{
		this.list.remove(1);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveAllNotAllowed()
	{
		this.list.removeAll(Arrays.asList("MyString1", "HerString4"));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSetNotAllowed()
	{
		this.list.set(1, "MyString1");
	}

	@Test
	public void testClone01()
	{
		ImmutableArrayList<String> test = this.list.clone();

		assertFalse("The lists should not be the same objects.", test == this.list);
		assertEquals("The lists should be equal.", this.list, test);
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testClone02()
	{
		this.getInternal().remove(2);
		this.list.clone();
	}

	@Test
	public void testGet01()
	{
		assertEquals("The value is not correct.", "YourString2", this.list.get(1));
	}

	@Test
	public void testGet02()
	{
		assertEquals("The value is not correct.", "HerString4", this.list.get(3));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGet03()
	{
		this.list.get(4);
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testGet04()
	{
		this.getInternal().add(null);
		this.list.get(1);
	}

	@Test
	public void testIndexOf01()
	{
		assertEquals("The index is not correct.", 0, this.list.indexOf("MyString1"));
	}

	@Test
	public void testIndexOf02()
	{
		assertEquals("The index is not correct.", 2, this.list.indexOf("HisString3"));
	}

	@Test
	public void testIndexOf03()
	{
		assertEquals("The index is not correct.", -1, this.list.indexOf("HisString4"));
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testIndexOf04()
	{
		this.getInternal().add("Test");
		this.list.indexOf("Hello");
	}

	@Test
	public void testLastIndexOf01()
	{
		assertEquals("The index is not correct.", 1, this.list.lastIndexOf("YourString2"));
	}

	@Test
	public void testLastIndexOf02()
	{
		assertEquals("The index is not correct.", 3, this.list.lastIndexOf("HerString4"));
	}

	@Test
	public void testLastIndexOf03()
	{
		assertEquals("The index is not correct.", -1, this.list.lastIndexOf("HisString4"));
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testLastIndexOf04()
	{
		this.getInternal().remove("HerString4");
		this.list.lastIndexOf("Hello");
	}

	@Test
	@SuppressWarnings("WhileLoopReplaceableByForEach")
	public void testListIterator101()
	{
		int i = 0;

		String[] array = new String[] { "MyString1", "YourString2", "HisString3", "HerString4" };

		ImmutableListIterator<String> iterator = this.list.listIterator();
		while(iterator.hasNext())
		{
			int index = iterator.nextIndex();
			assertEquals("The index is not correct " + i, i, index);

			String string = iterator.next();

			assertEquals("The strings should match " + i, this.list.get(index), string);

			assertTrue("The iterator has gone too far " + i, i < 4);
			assertNotNull("None of the strings should be null " + i, string);
			assertEquals("The string is not correct " + i, array[i++], string);
		}
	}

	@Test
	public void testListIterator102()
	{
		int i = 0;

		String[] array = new String[] { "MyString1", "YourString2", "HisString3", "HerString4" };

		for(String string : this.list)
		{
			assertTrue("The iterator has gone too far " + i, i < 4);
			assertNotNull("None of the strings should be null " + i, string);
			assertEquals("The string is not correct " + i, array[i++], string);
		}
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testListIterator103()
	{
		this.getInternal().remove("MyString1");
		this.list.listIterator();
	}

	@Test
	public void testListIterator201()
	{
		int i = 3;

		String[] array = new String[] { "MyString1", "YourString2", "HisString3", "HerString4" };

		ImmutableListIterator<String> iterator = this.list.listIterator(4);
		while(iterator.hasPrevious())
		{
			int index = iterator.previousIndex();
			assertEquals("The index is not correct " + i, i, index);

			String string = iterator.previous();

			assertEquals("The strings should match " + i, this.list.get(index), string);

			assertTrue("The iterator has gone too far " + i, i < 4);
			assertNotNull("None of the strings should be null " + i, string);
			assertEquals("The string is not correct " + i, array[i--], string);
		}
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testListIterator202()
	{
		this.getInternal().remove("MyString1");
		this.list.listIterator(2);
	}

	@Test
	public void testSubList01()
	{
		ImmutableArrayList<String> test = this.list.subList(1, 3);

		assertFalse("The objects should be different.", test == this.list);
		assertEquals("The list is the wrong side.", 2, test.size());
		assertEquals("String 1 is not correct.", "YourString2", test.get(0));
		assertEquals("String 2 is not correct.", "HisString3", test.get(1));
	}

	@Test
	public void testSubList02()
	{
		ImmutableArrayList<String> test = this.list.subList(2, 4);

		assertFalse("The objects should be different.", test == this.list);
		assertEquals("The list is the wrong side.", 2, test.size());
		assertEquals("String 1 is not correct.", "HisString3", test.get(0));
		assertEquals("String 2 is not correct.", "HerString4", test.get(1));
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testSubList03()
	{
		this.getInternal().add("Test");
		this.list.subList(2, 4);
	}
}