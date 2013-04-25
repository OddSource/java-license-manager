/*
 * TestCliOptionsBuilder.java from LicenseManager modified Thursday, January 24, 2013 15:08:50 CST (-0600).
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.cli.spi;

import org.apache.commons.cli.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for CliOptionsBuilder.
 */
public class TestCliOptionsBuilder
{
	private CliOptionsBuilder builder;

	@Before
	public void setUp()
	{
		this.builder = CliOptionsBuilder.get();
	}

	@After
	public void tearDown()
	{

	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateNoArg()
	{
		this.builder.create();
	}

	@Test
	public void testCreateCharArg()
	{
		Option option = this.builder.create('c');
		assertNotNull("The option should not be null.", option);
		assertEquals("The option is incorrect.", "c", option.getOpt());
	}

	@Test
	public void testCreateStringArg()
	{
		Option option = this.builder.create("test");
		assertNotNull("The option should not be null.", option);
		assertEquals("The option is incorrect.", "test", option.getOpt());
	}

	@Test
	public void testHasArg()
	{
		Option option = this.builder.hasArg().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasArg should be true.", option.hasArg());
	}

	@Test
	public void testHasArgBooleanArg()
	{
		Option option = this.builder.hasArg(false).create("test");
		assertNotNull("The option should not be null.", option);
		assertFalse("hasArg should be false.", option.hasArg());
	}

	@Test
	public void testHasArgs()
	{
		Option option = this.builder.hasArgs().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasArgs should be true.", option.hasArgs());
		assertEquals("getArgs is not correct.", -2, option.getArgs());
	}

	@Test
	public void testHasArgsIntArg()
	{
		Option option = this.builder.hasArgs(5).create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasArgs should be true.", option.hasArgs());
		assertEquals("getArgs is not correct.", 5, option.getArgs());
	}

	@Test
	public void testHasOptionalArg()
	{
		Option option = this.builder.hasOptionalArg().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasOptionalArg should be true.", option.hasOptionalArg());
	}

	@Test
	public void testHasOptionalArgs()
	{
		Option option = this.builder.hasOptionalArgs().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasOptionalArg should be true.", option.hasOptionalArg());
		assertEquals("getArgs is not correct.", -2, option.getArgs());
	}

	@Test
	public void testHasOptionalArgsIntArg()
	{
		Option option = this.builder.hasOptionalArgs(9).create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasOptionalArg should be true.", option.hasOptionalArg());
		assertEquals("getArgs is not correct.", 9, option.getArgs());
	}

	@Test
	public void testIsRequired()
	{
		Option option = this.builder.isRequired().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("isRequired should be true.", option.isRequired());
	}

	@Test
	public void testIsRequiredBooleanArg()
	{
		Option option = this.builder.isRequired(false).create("test");
		assertNotNull("The option should not be null.", option);
		assertFalse("isRequired should be false.", option.isRequired());
	}

	@Test
	public void testWithArgName()
	{
		Option option = this.builder.withArgName("argName01").create("test");
		assertNotNull("The option should not be null.", option);
		assertEquals("The arg name is not correct.", "argName01", option.getArgName());
	}

	@Test
	public void testWithDescription()
	{
		Option option = this.builder.withDescription("testDescription01").create("test");
		assertNotNull("The option should not be null.", option);
		assertEquals("The description is not correct.", "testDescription01", option.getDescription());
	}

	@Test
	public void testWithLongOpt()
	{
		Option option = this.builder.withLongOpt("longOpt01").create("test");
		assertNotNull("The option should not be null.", option);
		assertEquals("The long opt is not correct.", "longOpt01", option.getLongOpt());
	}

	@Test
	public void testWithType()
	{
		Option option = this.builder.withType(23498723497L).create("test");
		assertNotNull("The option should not be null.", option);
		assertEquals("getType is not correct.", 23498723497L, option.getType());
	}

	@Test
	public void testWithValueSeparator()
	{
		Option option = this.builder.withValueSeparator().create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasValueSeparator should be true.", option.hasValueSeparator());
		assertEquals("getValueSeparator is not correct.", '=', option.getValueSeparator());
	}

	@Test
	public void testWithValueSeparatorCharArg()
	{
		Option option = this.builder.withValueSeparator(':').create("test");
		assertNotNull("The option should not be null.", option);
		assertTrue("hasValueSeparator should be true.", option.hasValueSeparator());
		assertEquals("getValueSeparator is not correct.", ':', option.getValueSeparator());
	}
}