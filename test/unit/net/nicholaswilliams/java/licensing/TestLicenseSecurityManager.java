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

import net.nicholaswilliams.java.mock.StateFlag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Member;
import java.lang.reflect.ReflectPermission;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Permission;

import static org.junit.Assert.*;

/**
 * Test class for LicenseSecurityManager.
 */
public class TestLicenseSecurityManager
{
	private LicenseSecurityManager manager;

	@Before
	public void setUp()
	{
		this.manager = LicenseSecurityManager.getInstance();
	}

	@After
	public void tearDown()
	{

	}

	@Test(expected=IllegalArgumentException.class)
	public void testSecurityManagerIsSuitableReplacement01()
	{
		LicenseSecurityManager.securityManagerIsSuitableReplacement(null);
	}

	@Test
	public void testSecurityManagerIsSuitableReplacement02()
	{
		final StateFlag checkMemberAccess1 = new StateFlag();

		assertFalse("The security manager should not be suitable.",
					LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager() {
						@Override
						public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
						{
							if(checkMemberAccess1.state)
								fail("checkMemberAccess should not have been called twice.");
							checkMemberAccess1.state = true;

							assertSame("The class is not correct.", License.class, reflectionClass);
							assertEquals("The access type is not correct.", Member.DECLARED, memberAccessType);
						}
						@Override
						public void checkPermission(Permission permission)
						{
							fail("checkPermission should not have been called.");
						}
					})
		);

		assertTrue("checkMemberAccess should have been called.", checkMemberAccess1.state);
	}

	@Test
	public void testSecurityManagerIsSuitableReplacement03()
	{
		final StateFlag checkMemberAccess1 = new StateFlag();
		final StateFlag checkMemberAccess2 = new StateFlag();

		assertFalse("The security manager should not be suitable.",
					LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager() {
						@Override
						public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
						{
							assertSame("The class is not correct.", checkMemberAccess1.state ? LicenseManager.class : License.class, reflectionClass);
							assertEquals("The access type is not correct.", Member.DECLARED, memberAccessType);

							if(checkMemberAccess1.state)
								checkMemberAccess2.state = true;
							checkMemberAccess1.state = true;

							if(reflectionClass == License.class)
								throw new SecurityException();
						}
						@Override
						public void checkPermission(Permission permission)
						{
							fail("checkPermission should not have been called.");
						}
					})
		);

		assertTrue("checkMemberAccess should have been called.", checkMemberAccess1.state);
		assertTrue("checkMemberAccess should have been called twice.", checkMemberAccess2.state);
	}

	@Test
	public void testSecurityManagerIsSuitableReplacement04()
	{
		final StateFlag checkMemberAccess1 = new StateFlag();
		final StateFlag checkMemberAccess2 = new StateFlag();
		final StateFlag checkPermission = new StateFlag();

		assertFalse("The security manager should not be suitable.",
					LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager() {
						@Override
						public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
						{
							assertSame("The class is not correct.", checkMemberAccess1.state ? LicenseManager.class : License.class, reflectionClass);
							assertEquals("The access type is not correct.", Member.DECLARED, memberAccessType);

							if(checkMemberAccess1.state)
								checkMemberAccess2.state = true;
							checkMemberAccess1.state = true;

							throw new SecurityException();
						}
						@Override
						public void checkPermission(Permission permission)
						{
							assertEquals("The permission object is not correct.", RuntimePermission.class, permission.getClass());
							assertEquals("The checked permission is not correct.", "setSecurityManager", permission.getName());

							checkPermission.state = true;
						}
					})
		);

		assertTrue("checkMemberAccess should have been called.", checkMemberAccess1.state);
		assertTrue("checkMemberAccess should have been called twice.", checkMemberAccess2.state);
		assertTrue("checkPermission should have been called.", checkPermission.state);
	}

	@Test
	public void testSecurityManagerIsSuitableReplacement05()
	{
		final StateFlag checkMemberAccess1 = new StateFlag();
		final StateFlag checkMemberAccess2 = new StateFlag();
		final StateFlag checkPermission = new StateFlag();

		assertTrue("The security manager should be suitable.",
				   LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager()
				   {
					   @Override
					   public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
					   {
						   assertSame("The class is not correct.",
									  checkMemberAccess1.state ? LicenseManager.class : License.class, reflectionClass);
						   assertEquals("The access type is not correct.", Member.DECLARED, memberAccessType);

						   if(checkMemberAccess1.state)
							   checkMemberAccess2.state = true;
						   checkMemberAccess1.state = true;

						   throw new SecurityException();
					   }

					   @Override
					   public void checkPermission(Permission permission)
					   {
						   assertEquals("The permission object is not correct.", RuntimePermission.class,
										permission.getClass());
						   assertEquals("The checked permission is not correct.", "setSecurityManager",
										permission.getName());

						   checkPermission.state = true;

						   throw new SecurityException();
					   }
				   })
				  );

		assertTrue("checkMemberAccess should have been called.", checkMemberAccess1.state);
		assertTrue("checkMemberAccess should have been called twice.", checkMemberAccess2.state);
		assertTrue("checkPermission should have been called.", checkPermission.state);
	}

	@Test(expected=SecurityException.class)
	public void testCheckPermission01()
	{
		this.manager.checkPermission(new RuntimePermission("setSecurityManager"));
	}

	@Test
	public void testCheckPermission02()
	{
		this.manager.checkPermission(new ReflectPermission("suppressAccessChecks"));
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess01()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.DataSignatureManager.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess02()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.License.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess03()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.LicenseManager.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess04()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.LicenseSecurityManager.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess05()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.ObjectSerializer.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess06()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.SignedLicense.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess07()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableAbstractCollection.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess08()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableArrayList.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess09()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableIterator.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess10()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableLinkedHashSet.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testCheckMemberAccess11()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableListIterator.class, Member.DECLARED);
	}

	@Test
	public void testCheckMemberAccess12()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.DataSignatureManager.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess13()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.License.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess14()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.LicenseManager.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess15()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.LicenseSecurityManager.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess16()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.ObjectSerializer.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess17()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.SignedLicense.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess18()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableAbstractCollection.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess19()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableArrayList.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess20()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableIterator.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess21()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableLinkedHashSet.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess22()
	{
		this.manager.checkMemberAccess(net.nicholaswilliams.java.licensing.immutable.ImmutableListIterator.class, Member.PUBLIC);
	}

	@Test
	public void testCheckMemberAccess23()
	{
		this.manager.checkMemberAccess(String.class, Member.DECLARED);
	}

	@Test
	public void testCheckMemberAccess24()
	{
		this.manager.checkMemberAccess(Test.class, Member.DECLARED);
	}

	@Test
	public void testCheckMemberAccess25()
	{
		this.manager.checkMemberAccess(Member.class, Member.DECLARED);
	}

	@Test
	public void testCheckMemberAccess26()
	{
		this.manager.checkMemberAccess(Object.class, Member.DECLARED);
	}

	@Test
	public void testCheckMemberAccess27()
	{
		this.manager.checkMemberAccess(java.util.ArrayList.class, Member.DECLARED);
	}

	@Test(expected=SecurityException.class)
	public void testPrivateReflection01() throws NoSuchFieldException
	{
		License.class.getDeclaredField("goodAfterDate");
	}

	@Test(expected=SecurityException.class)
	public void testPrivateReflection02() throws NoSuchFieldException
	{
		LicenseManager.class.getDeclaredField("licenseCache");
	}

	@Test(expected=SecurityException.class)
	public void testPrivateReflection03() throws NoSuchFieldException
	{
		LicenseManager.class.getDeclaredClasses();
	}

	@Test
	public void testSecurityManagerWasSet()
	{
		assertSame("The security manager mas not set correctly.", this.manager, System.getSecurityManager());
	}

	@Test(expected=SecurityException.class)
	public void testSetSecurityManager01()
	{
		System.setSecurityManager(null);
	}

	@Test(expected=SecurityException.class)
	public void testSetSecurityManager02()
	{
		System.setSecurityManager(this.manager);
	}

	@Test(expected=SecurityException.class)
	public void testReflectionOnClassClassBlocked() throws NoSuchMethodException
	{
		java.lang.Class.class.getDeclaredMethod("privateGetDeclaredMethods", boolean.class);
	}

	@Test(expected=SecurityException.class)
	public void testReflectionOnSecurityManagerBlocked() throws NoSuchFieldException
	{
		java.lang.System.class.getDeclaredField("security");
	}

	@Test
	public void testCheckPermission()
	{
		this.manager.checkPermission(new ReflectPermission("fakePermission"), new Object());
	}

	@Test
	public void testCheckExec()
	{
		this.manager.checkExec("test");
	}

	@Test
	public void testCheckLink()
	{
		this.manager.checkLink("test");
	}

	@Test
	public void testCheckRead()
	{
		this.manager.checkRead("test", new Object());
	}

	@Test
	public void testCheckDelete()
	{
		this.manager.checkDelete("test");
	}

	@Test
	public void testCheckConnect()
	{
		this.manager.checkConnect("test", 1, new Object());
	}

	@Test
	public void testCheckListen()
	{
		this.manager.checkListen(1);
	}

	@Test
	public void testCheckAccept()
	{
		this.manager.checkAccept("test", 1);
	}

	@Test
	public void testCheckMulticast() throws UnknownHostException
	{
		this.manager.checkMulticast(InetAddress.getLocalHost());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCheckMulticastWithByte() throws UnknownHostException
	{
		this.manager.checkMulticast(InetAddress.getLocalHost(), (byte)0x00);
	}

	@Test
	public void testCheckPropertiesAccess()
	{
		this.manager.checkPropertiesAccess();
	}

	@Test
	public void testCheckPrintJobAccess()
	{
		this.manager.checkPrintJobAccess();
	}

	@Test
	public void testCheckSystemClipBoardAccess()
	{
		this.manager.checkSystemClipboardAccess();
	}

	@Test
	public void testCheckAwtEventQueueAccess()
	{
		this.manager.checkAwtEventQueueAccess();
	}

	@Test
	public void testCheckPackageDefinition()
	{
		this.manager.checkPackageDefinition("test");
	}

	@Test
	public void testCheckSetFactory()
	{
		this.manager.checkSetFactory();
	}
}