/*
 * LicenseSecurityManager.java from LicenseManager modified Saturday, May 19, 2012 09:07:08 CDT (-0500).
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

import net.nicholaswilliams.java.licensing.exception.InsecureEnvironmentException;

import java.io.FileDescriptor;
import java.lang.reflect.Member;
import java.net.InetAddress;
import java.security.Permission;

/**
 * This security manager is one of the most integral pieces to the license manager. It prevents reflection attacks from
 * disabling or compromising the security features in this product.<br />
 * <br />
 * When the security manager is initialized, it first checks if a different security manager is already installed in
 * this JVM. If no security manager is installed already, then this security manager installs itself.<br />
 * <br />
 * If another security manager is already installed, this checks to make sure it prevents reflection attacks against
 * critical LicenseManager classes. If it prevents attacks, it is a suitable security manager and allowed to remain. If
 * it does not prevent attacks, this attempts to override the currently installed security manager and install itself.
 * If the existing security manager prevents this from installing itself, an {@link InsecureEnvironmentException} is
 * thrown and the LicenseManager fails to start.<br />
 * <br />
 * When this security manager installs itself over another, it will nest the other security manager within itself and
 * call all appropriate checking methods on that other security manager after this manager performs its analog
 * checks.<br />
 * <br />
 * When reflection is used to access non-public methods, fields, classes or interfaces, the JVM first consults the
 * installed security manager to ensure that the access is permitted. This security manager throws an exception if the
 * protected or private object being accessed via reflection belongs to the security manager package.<br />
 * <br />
 * Finally, this security manager will prevent other security managers from installing themselves over this one, so
 * that these security measures are not compromised.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
final class LicenseSecurityManager extends SecurityManager
{
	private static LicenseSecurityManager instance;

	private static final RuntimePermission CHECK_MEMBER_ACCESS_PERMISSION =
			new RuntimePermission("accessDeclaredMembers");

	private static final RuntimePermission SET_SECURITY_MANAGER_PERMISSION =
			new RuntimePermission("setSecurityManager");

	static
	{
		SecurityManager manager = System.getSecurityManager();
		if(manager == null)
		{
			// install the security manager
			LicenseSecurityManager.installSecurityManagerWithParent(null);
		}
		else if(!manager.getClass().equals(LicenseSecurityManager.class))
		{
			if(!LicenseSecurityManager.securityManagerIsSuitableReplacement(manager))
			{
				// if it's not a suitable replacement, reset the security manager
				LicenseSecurityManager.installSecurityManagerWithParent(manager);
			}
		}
	}

	protected static boolean securityManagerIsSuitableReplacement(SecurityManager securityManager)
	{
		if(securityManager == null)
			throw new IllegalArgumentException("Parameter securityManager cannot be null!");

		// Make sure we can't call java.lang.Class#getDeclared*() on License
		try
		{
			securityManager.checkMemberAccess(License.class, Member.DECLARED);
			return false;
		}
		catch(SecurityException ignore)
		{
			// this is a good thing
		}

		// Make sure we can't call java.lang.Class#getDeclared*() on LicenseManager
		try
		{
			securityManager.checkMemberAccess(LicenseManager.class, Member.DECLARED);
			return false;
		}
		catch(SecurityException ignore)
		{
			// this is a good thing
		}

		// Make sure we can't call java.lang.System#setSecurityManager()
		try
		{
			securityManager.checkPermission(LicenseSecurityManager.SET_SECURITY_MANAGER_PERMISSION);
			return false;
		}
		catch(SecurityException ignore)
		{
			// this is a good thing
		}

		return true;
	}

	private static void installSecurityManagerWithParent(SecurityManager parent)
	{
		try
		{
			// install the security manager
			LicenseSecurityManager.instance = new LicenseSecurityManager(parent);
			System.setSecurityManager(LicenseSecurityManager.instance);
		}
		catch(SecurityException e)
		{
			// since we can't install the security manager, indicate that the environment is insecure
			throw new InsecureEnvironmentException(e);
		}
	}

	private final SecurityManager next;

	private LicenseSecurityManager(SecurityManager next)
	{
		super();

		this.next = next;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
	{
		if(reflectionClass == null)
			throw new IllegalArgumentException("Parameter reflectionClass cannot be null.");

		this.inCheck = true;

		try
		{
			if(memberAccessType != Member.PUBLIC)
			{
				Package packageObject = reflectionClass.getPackage();
				if(
						packageObject != null &&
						packageObject.getName().startsWith("net.nicholaswilliams.java.licensing") &&
						!reflectionClass.equals(FeatureRestriction.class)
				)
				{
					throw new SecurityException("Reflection access to non-public members of LicenseManager classes prohibited.");
				}

				if(reflectionClass == java.lang.Class.class || reflectionClass == java.lang.System.class)
				{
					Class stack[] = getClassContext();
					if(stack.length < 4 || !stack[3].getPackage().getName().startsWith("java."))
						throw new SecurityException("Reflection access to non-public members of java.lang.Class and java.lang.System prohibited.");
				}

				if(this.next != null)
				{
					/*
					 * Per Java SE 6 documentation for java.lang.SecurityManager#checkMemberAccess: If this method is
					 * overridden, then a call to super.checkMemberAccess cannot be made, as the default implementation
					 * of checkMemberAccess relies on the code being checked being at a stack depth of 4. So, we
					 * copy-and-paste the implementation from Java SE 6.
					 *
					 * this.next.checkMemberAccess(reflectionClass, memberAccessType);
					 *
					 * Copyright 1994-2007 Sun Microsystems, Inc.  All Rights Reserved.
					 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
					 *
					 * This code is free software; you can redistribute it and/or modify it
					 * under the terms of the GNU General Public License version 2 only, as
					 * published by the Free Software Foundation.  Sun designates this
					 * particular file as subject to the "Classpath" exception as provided
					 * by Sun in the LICENSE file that accompanied this code.
					 *
					 * This code is distributed in the hope that it will be useful, but WITHOUT
					 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
					 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
					 * version 2 for more details (a copy is included in the LICENSE file that
					 * accompanied this code).
					 *
					 * You should have received a copy of the GNU General Public License version
					 * 2 along with this work; if not, write to the Free Software Foundation,
					 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
					 *
					 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
					 * CA 95054 USA or visit www.sun.com if you need additional information or
					 * have any questions.
					 */

					Class stack[] = getClassContext();
					/*
					 * stack depth of 4 should be the caller of one of the
					 * methods in java.lang.Class that invoke checkMember
					 * access. The stack should look like:
					 *
					 * someCaller                        [3]
					 * java.lang.Class.someReflectionAPI [2]
					 * java.lang.Class.checkMemberAccess [1]
					 * SecurityManager.checkMemberAccess [0]
					 *
					 */
					if((stack.length < 4) || (stack[3].getClassLoader() != reflectionClass.getClassLoader()))
					{
						this.checkPermission(LicenseSecurityManager.CHECK_MEMBER_ACCESS_PERMISSION);
					}
				}
			}
		}
		finally
		{
			this.inCheck = false;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void checkPermission(Permission permission)
	{
		this.inCheck = true;

		try
		{
			if(permission.getName().equals("setSecurityManager"))
				throw new SecurityException("Setting a SecurityManager other than the LicenseSecurityManager is prohibited.");

			if(this.next != null)
				this.next.checkPermission(permission);
		}
		finally
		{
			this.inCheck = false;
		}
	}

	@Override
	public void checkPackageAccess(String packageName)
	{
		if(this.next != null)
			this.next.checkPackageAccess(packageName);
	}

	@Override
	public void checkPermission(Permission permission, Object object)
	{
		if(this.next != null)
			this.next.checkPermission(permission, object);
	}

	@Override
	public void checkCreateClassLoader()
	{
		if(this.next != null)
			this.next.checkCreateClassLoader();
	}

	@Override
	public void checkAccess(Thread thread)
	{
		if(this.next != null)
			this.next.checkAccess(thread);
	}

	@Override
	public void checkAccess(ThreadGroup threadGroup)
	{
		if(this.next != null)
			this.next.checkAccess(threadGroup);
	}

	@Override
	public void checkExit(int i)
	{
		if(this.next != null)
			this.next.checkExit(i);
	}

	@Override
	public void checkExec(String s)
	{
		if(this.next != null)
			this.next.checkExec(s);
	}

	@Override
	public void checkLink(String s)
	{
		if(this.next != null)
			this.next.checkLink(s);
	}

	@Override
	public void checkRead(FileDescriptor fileDescriptor)
	{
		if(this.next != null)
			this.next.checkRead(fileDescriptor);
	}

	@Override
	public void checkRead(String s)
	{
		if(this.next != null)
			this.next.checkRead(s);
	}

	@Override
	public void checkRead(String s, Object o)
	{
		if(this.next != null)
			this.next.checkRead(s);
	}

	@Override
	public void checkWrite(FileDescriptor fileDescriptor)
	{
		if(this.next != null)
			this.next.checkWrite(fileDescriptor);
	}

	@Override
	public void checkWrite(String s)
	{
		if(this.next != null)
			this.next.checkWrite(s);
	}

	@Override
	public void checkDelete(String s)
	{
		if(this.next != null)
			this.next.checkDelete(s);
	}

	@Override
	public void checkConnect(String s, int i)
	{
		if(this.next != null)
			this.next.checkConnect(s, i);
	}

	@Override
	public void checkConnect(String s, int i, Object o)
	{
		if(this.next != null)
			this.next.checkConnect(s, i, o);
	}

	@Override
	public void checkListen(int i)
	{
		if(this.next != null)
			this.next.checkListen(i);
	}

	@Override
	public void checkAccept(String s, int i)
	{
		if(this.next != null)
			this.next.checkAccept(s, i);
	}

	@Override
	public void checkMulticast(InetAddress inetAddress)
	{
		if(this.next != null)
			this.next.checkMulticast(inetAddress);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void checkMulticast(InetAddress inetAddress, byte b)
	{
		if(this.next != null)
			this.next.checkMulticast(inetAddress, b);
	}

	@Override
	public void checkPropertiesAccess()
	{
		if(this.next != null)
			this.next.checkPropertiesAccess();
	}

	@Override
	public void checkPropertyAccess(String s)
	{
		if(this.next != null)
			this.next.checkPropertyAccess(s);
	}

	@Override
	public void checkPrintJobAccess()
	{
		if(this.next != null)
			this.next.checkPrintJobAccess();
	}

	@Override
	public void checkSystemClipboardAccess()
	{
		if(this.next != null)
			this.next.checkSystemClipboardAccess();
	}

	@Override
	public void checkAwtEventQueueAccess()
	{
		if(this.next != null)
			this.next.checkAwtEventQueueAccess();
	}

	@Override
	public void checkPackageDefinition(String s)
	{
		if(this.next != null)
			this.next.checkPackageDefinition(s);
	}

	@Override
	public void checkSetFactory()
	{
		if(this.next != null)
			this.next.checkSetFactory();
	}

	@Override
	public void checkSecurityAccess(String s)
	{
		if(this.next != null)
			this.next.checkSecurityAccess(s);
	}

	@Override
	public boolean checkTopLevelWindow(Object window)
	{
		return this.next == null || this.next.checkTopLevelWindow(window);
	}

	@Override
	public ThreadGroup getThreadGroup()
	{
		return this.next != null ? this.next.getThreadGroup() : super.getThreadGroup();
	}

	@Override
	public Object getSecurityContext()
	{
		return this.next != null ? this.next.getSecurityContext() : super.getSecurityContext();
	}

	protected static LicenseSecurityManager getInstance()
	{
		return LicenseSecurityManager.instance;
	}
}
