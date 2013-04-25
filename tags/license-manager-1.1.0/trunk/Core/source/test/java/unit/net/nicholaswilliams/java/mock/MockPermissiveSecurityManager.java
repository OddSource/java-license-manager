/*
 * MockPermissiveSecurityManager.java from LicenseManager modified Thursday, January 24, 2013 16:33:30 CST (-0600).
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

package net.nicholaswilliams.java.mock;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class MockPermissiveSecurityManager extends SecurityManager
{
	@Override
	public void checkPermission(Permission permission)
	{
	}

	@Override
	public void checkPermission(Permission permission, Object o)
	{
	}

	@Override
	public void checkCreateClassLoader()
	{
	}

	@Override
	public void checkAccess(Thread thread)
	{
	}

	@Override
	public void checkAccess(ThreadGroup threadGroup)
	{
	}

	@Override
	public void checkExit(int i)
	{
	}

	@Override
	public void checkExec(String s)
	{
	}

	@Override
	public void checkLink(String s)
	{
	}

	@Override
	public void checkRead(FileDescriptor fileDescriptor)
	{
	}

	@Override
	public void checkRead(String s)
	{
	}

	@Override
	public void checkRead(String s, Object o)
	{
	}

	@Override
	public void checkWrite(FileDescriptor fileDescriptor)
	{
	}

	@Override
	public void checkWrite(String s)
	{
	}

	@Override
	public void checkDelete(String s)
	{
	}

	@Override
	public void checkConnect(String s, int i)
	{
	}

	@Override
	public void checkConnect(String s, int i, Object o)
	{
	}

	@Override
	public void checkListen(int i)
	{
	}

	@Override
	public void checkAccept(String s, int i)
	{
	}

	@Override
	public void checkMulticast(InetAddress inetAddress)
	{
	}

	@Override
	public void checkPropertiesAccess()
	{
	}

	@Override
	public void checkPropertyAccess(String s)
	{
	}

	@Override
	public boolean checkTopLevelWindow(Object o)
	{
		return true;
	}

	@Override
	public void checkPrintJobAccess()
	{
	}

	@Override
	public void checkSystemClipboardAccess()
	{
	}

	@Override
	public void checkAwtEventQueueAccess()
	{
	}

	@Override
	public void checkPackageAccess(String s)
	{
	}

	@Override
	public void checkPackageDefinition(String s)
	{
	}

	@Override
	public void checkSetFactory()
	{
	}

	@Override
	public void checkMemberAccess(Class<?> aClass, int i)
	{
	}

	@Override
	public void checkSecurityAccess(String s)
	{
	}
}
