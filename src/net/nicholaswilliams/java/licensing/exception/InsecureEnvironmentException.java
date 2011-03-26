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

package net.nicholaswilliams.java.licensing.exception;

/**
 * Thrown when a security manager is already installed that allows reflection access but doesn't allow a more secure
 * security manager to be installed.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 * @see net.nicholaswilliams.java.licensing.LicenseSecurityManager
 */
@SuppressWarnings("unused")
public class InsecureEnvironmentException extends Error
{
	public InsecureEnvironmentException(String message, Throwable cause)
	{
		super("The license manager was activated in an insecure environment. " + message, cause);
	}

	public InsecureEnvironmentException(SecurityException cause)
	{
		super("The license manager was activated in an insecure environment. A security manager has already been " +
				  "installed, but it allows reflection access to the license cache and doesn't allow a new security " +
				  "manager to be installed.", cause);
	}
}
