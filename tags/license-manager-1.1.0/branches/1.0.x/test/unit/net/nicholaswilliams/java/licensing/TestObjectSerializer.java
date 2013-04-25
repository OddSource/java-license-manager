/*
 * TestObjectSerializer.java from LicenseManager modified Friday, September 21, 2012 07:37:45 CDT (-0500).
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

import net.nicholaswilliams.java.licensing.exception.ObjectTypeNotExpectedException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for ObjectSerializer.
 */
public class TestObjectSerializer
{
	ObjectSerializer serializer;

	public TestObjectSerializer()
	{
		this.serializer = new ObjectSerializer();
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
	public void testWriteObject1() throws Exception
	{
		MockTestObject1 object = new MockTestObject1();

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MockTestObject1.class, input.getClass());

		MockTestObject1 actual = (MockTestObject1)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}
	
	@Test
	public void testWriteObject2() throws Exception
	{
		MockTestObject1 object = new MockTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MockTestObject1.class, input.getClass());

		MockTestObject1 actual = (MockTestObject1)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test
	public void testWriteObject3() throws Exception
	{
		MockTestObject2 object = new MockTestObject2();

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MockTestObject2.class, input.getClass());

		MockTestObject2 actual = (MockTestObject2)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test
	public void testWriteObject4() throws Exception
	{
		MockTestObject2 object = new MockTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MockTestObject2.class, input.getClass());

		MockTestObject2 actual = (MockTestObject2)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test(expected=ObjectTypeNotExpectedException.class)
	public void testReadObject1() throws Exception
	{
		MockTestObject1 object = new MockTestObject1();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		this.serializer.readObject(MockTestObject2.class, data);
	}

	@Test
	public void testReadObject2() throws Exception
	{
		MockTestObject1 object = new MockTestObject1();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MockTestObject1 returned = this.serializer.readObject(MockTestObject1.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}
	
	@Test
	public void testReadObject3() throws Exception
	{
		MockTestObject1 object = new MockTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MockTestObject1 returned = this.serializer.readObject(MockTestObject1.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test(expected=ObjectTypeNotExpectedException.class)
	public void testReadObject4() throws Exception
	{
		MockTestObject2 object = new MockTestObject2();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		this.serializer.readObject(MockTestObject1.class, data);
	}

	@Test
	public void testReadObject5() throws Exception
	{
		MockTestObject2 object = new MockTestObject2();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MockTestObject2 returned = this.serializer.readObject(MockTestObject2.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test
	public void testReadObject6() throws Exception
	{
		MockTestObject2 object = new MockTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MockTestObject2 returned = this.serializer.readObject(MockTestObject2.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test
	public void testBoth1()
	{
		MockTestObject1 object = new MockTestObject1();

		byte[] data = this.serializer.writeObject(object);

		MockTestObject1 returned = this.serializer.readObject(MockTestObject1.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth2()
	{
		MockTestObject1 object = new MockTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		byte[] data = this.serializer.writeObject(object);

		MockTestObject1 returned = this.serializer.readObject(MockTestObject1.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth3()
	{
		MockTestObject2 object = new MockTestObject2();

		byte[] data = this.serializer.writeObject(object);

		MockTestObject2 returned = this.serializer.readObject(MockTestObject2.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth4()
	{
		MockTestObject2 object = new MockTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		byte[] data = this.serializer.writeObject(object);

		MockTestObject2 returned = this.serializer.readObject(MockTestObject2.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}
}

class MockTestObject1 implements Serializable
{
	private static final long serialVersionUID = 1L;

	boolean coolTest = false;

	byte[] myArray = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13 };

	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof MockTestObject1 ))
			return false;
		MockTestObject1 test = (MockTestObject1)o;
		return test.coolTest == this.coolTest && Arrays.equals(test.myArray, this.myArray);
	}
}

class MockTestObject2 implements Serializable
{
	private static final long serialVersionUID = 1L;

	String aString = "Hello, world";

	char[] password = new char[] { 'a', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof MockTestObject2 ))
			return false;
		MockTestObject2 test = (MockTestObject2)o;
		return Arrays.equals(test.password, this.password) && (
				(this.aString == null && test.aString == null) ||
				(this.aString != null && this.aString.equals(test.aString))
		);
	}
}