/*
 * TestImmutableListIterator.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.immutable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for ImmutableListIterator.
 */
public class TestImmutableListIterator
{
	private ImmutableListIterator<String> iterator;

	private MockValidObject valid;

	public TestImmutableListIterator()
	{
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("MyString1");
		temp.add("YourString2");
		temp.add("HisString3");
		temp.add("HerString4");

		this.valid = new MockValidObject();

		this.iterator = new ImmutableListIterator<String>(temp.listIterator(), this.valid);
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

	@Test(expected=UnsupportedOperationException.class)
	public void testAddNotAllowed()
	{
		this.iterator.add("Test");
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveNotAllowed()
	{
		this.iterator.remove();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testSetNotAllowed()
	{
		this.iterator.set("Test");
	}

	@Test
	public void testNext01()
	{
		this.valid.valid = true;

		String[] array = new String[] { "MyString1", "YourString2", "HisString3", "HerString4" };
		int i = 0;

		while(this.iterator.hasNext())
		{
			assertEquals("The index is not correct.", i, this.iterator.nextIndex());
			
			String string = this.iterator.next();

			assertNotNull("This string should not be null " + i, string);
			assertEquals("This string is not correct " + i, array[i++], string);
		}
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testNext02()
	{
		this.valid.valid = false;
		this.iterator.hasNext();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testNext03()
	{
		this.valid.valid = false;
		this.iterator.hasNext();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testNext04()
	{
		try {
			this.valid.valid = true;
			this.iterator.hasNext();
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		this.valid.valid = false;
		this.iterator.next();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testNext05()
	{
		try {
			this.valid.valid = true;
			this.iterator.hasNext();
			this.iterator.next();
			this.iterator.hasNext();
			this.iterator.next();
			this.iterator.hasNext();
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		this.valid.valid = false;
		this.iterator.next();
	}

	@Test
	public void testPrevious01()
	{
		this.valid.valid = true;

		String[] array = new String[] { "MyString1", "YourString2", "HisString3", "HerString4" };
		int i = 4;

		while(this.iterator.hasPrevious())
		{
			assertEquals("The index is not correct.", i, this.iterator.previousIndex());

			String string = this.iterator.previous();

			assertNotNull("This string should not be null " + i, string);
			assertEquals("This string is not correct " + i, array[i--], string);
		}
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testPrevious02()
	{
		this.valid.valid = false;
		this.iterator.hasPrevious();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testPrevious03()
	{
		this.valid.valid = false;
		this.iterator.previous();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testPrevious04()
	{
		try {
			this.valid.valid = true;
			this.iterator.hasPrevious();
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		this.valid.valid = false;
		this.iterator.previous();
	}

	@Test(expected=ImmutableModifiedThroughReflectionException.class)
	public void testPrevious05()
	{
		try {
			this.valid.valid = true;
			this.iterator.next();
			this.iterator.next();
			this.iterator.next();
			this.iterator.next();
			this.iterator.hasPrevious();
			this.iterator.previous();
			this.iterator.hasPrevious();
			this.iterator.previous();
			this.iterator.hasPrevious();
			this.iterator.previous();
		} catch(Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		this.valid.valid = false;
		this.iterator.previous();
	}
}