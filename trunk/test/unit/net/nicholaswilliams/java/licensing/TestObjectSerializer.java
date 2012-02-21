/*
 * TestObjectSerializer.java from LicenseManager modified Tuesday, February 21, 2012 10:56:34 CST (-0600).
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
		MyTestObject1 object = new MyTestObject1();

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MyTestObject1.class, input.getClass());

		MyTestObject1 actual = (MyTestObject1)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}
	
	@Test
	public void testWriteObject2() throws Exception
	{
		MyTestObject1 object = new MyTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MyTestObject1.class, input.getClass());

		MyTestObject1 actual = (MyTestObject1)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test
	public void testWriteObject3() throws Exception
	{
		MyTestObject2 object = new MyTestObject2();

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MyTestObject2.class, input.getClass());

		MyTestObject2 actual = (MyTestObject2)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test
	public void testWriteObject4() throws Exception
	{
		MyTestObject2 object = new MyTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		byte[] data = this.serializer.writeObject(object);

		assertNotNull("The array should not be null.", data);
		assertTrue("The array shoudl not be empty.", data.length > 0);

		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		ObjectInputStream stream = new ObjectInputStream(bytes);

		Object input = stream.readObject();

		assertNotNull("The object should not be null.", input);
		assertEquals("The object is not the right kind of object.", MyTestObject2.class, input.getClass());

		MyTestObject2 actual = (MyTestObject2)input;
		assertFalse("The objects should not be the same object.", actual == object);
		assertEquals("The object is not correct.", object, actual);
	}

	@Test(expected=ObjectTypeNotExpectedException.class)
	public void testReadObject1() throws Exception
	{
		MyTestObject1 object = new MyTestObject1();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		this.serializer.readObject(MyTestObject2.class, data);
	}

	@Test
	public void testReadObject2() throws Exception
	{
		MyTestObject1 object = new MyTestObject1();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MyTestObject1 returned = this.serializer.readObject(MyTestObject1.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}
	
	@Test
	public void testReadObject3() throws Exception
	{
		MyTestObject1 object = new MyTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MyTestObject1 returned = this.serializer.readObject(MyTestObject1.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test(expected=ObjectTypeNotExpectedException.class)
	public void testReadObject4() throws Exception
	{
		MyTestObject2 object = new MyTestObject2();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		this.serializer.readObject(MyTestObject1.class, data);
	}

	@Test
	public void testReadObject5() throws Exception
	{
		MyTestObject2 object = new MyTestObject2();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MyTestObject2 returned = this.serializer.readObject(MyTestObject2.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test
	public void testReadObject6() throws Exception
	{
		MyTestObject2 object = new MyTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);

		stream.writeObject(object);
		stream.close();

		byte[] data = bytes.toByteArray();

		MyTestObject2 returned = this.serializer.readObject(MyTestObject2.class, data);

		assertNotNull("The returned object should not be null.", returned);
		assertFalse("The returned object should not be the same.", returned == object);
		assertEquals("The returned object should be equal.", object, returned);
	}

	@Test
	public void testBoth1()
	{
		MyTestObject1 object = new MyTestObject1();

		byte[] data = this.serializer.writeObject(object);

		MyTestObject1 returned = this.serializer.readObject(MyTestObject1.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth2()
	{
		MyTestObject1 object = new MyTestObject1();
		object.coolTest = true;
		Arrays.fill(object.myArray, (byte)12);

		byte[] data = this.serializer.writeObject(object);

		MyTestObject1 returned = this.serializer.readObject(MyTestObject1.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth3()
	{
		MyTestObject2 object = new MyTestObject2();

		byte[] data = this.serializer.writeObject(object);

		MyTestObject2 returned = this.serializer.readObject(MyTestObject2.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}

	@Test
	public void testBoth4()
	{
		MyTestObject2 object = new MyTestObject2();
		object.aString = "another test string";
		Arrays.fill(object.password, 'b');

		byte[] data = this.serializer.writeObject(object);

		MyTestObject2 returned = this.serializer.readObject(MyTestObject2.class, data);

		assertNotNull("The object should not be null.", returned);
		assertFalse("The object should not be the same.", returned == object);
		assertEquals("The object should be equal.", object, returned);
	}
}

class MyTestObject1 implements Serializable
{
	boolean coolTest = false;

	byte[] myArray = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13 };

	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof MyTestObject1))
			return false;
		MyTestObject1 test = (MyTestObject1)o;
		return test.coolTest == this.coolTest && Arrays.equals(test.myArray, this.myArray);
	}
}

class MyTestObject2 implements Serializable
{
	String aString = "Hello, world";

	char[] password = new char[] { 'a', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof MyTestObject2))
			return false;
		MyTestObject2 test = (MyTestObject2)o;
		return Arrays.equals(test.password, this.password) && (
				(this.aString == null && test.aString == null) ||
				(this.aString != null && this.aString.equals(test.aString))
		);
	}
}