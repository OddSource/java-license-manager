/*
 * ImmutableModifiedThroughReflectionException.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
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

/**
 * This exception is thrown when an unmodifiable list is modified illegally
 * through exception.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ImmutableModifiedThroughReflectionException extends Error
{
	public ImmutableModifiedThroughReflectionException()
	{
		super("This immutable object appears to have been modified through reflection.");
	}

	public ImmutableModifiedThroughReflectionException(String message)
	{
		super(message);
	}

	public ImmutableModifiedThroughReflectionException(Throwable cause)
	{
		super("This immutable object appears to have been modified through reflection.", cause);
	}

	public ImmutableModifiedThroughReflectionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
