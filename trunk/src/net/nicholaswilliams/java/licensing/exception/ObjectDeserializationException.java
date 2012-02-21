/*
 * ObjectDeserializationException.java from LicenseManager modified Tuesday, June 28, 2011 11:34:10 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.exception;

/**
 * This class is thrown when an error occurs while reading an object from a
 * byte array. The general form of this error indicates an I/O problem.
 * Sub-classes of this exception indicate more specific problems.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ObjectDeserializationException extends RuntimeException
{
	public ObjectDeserializationException()
	{
		super("An error occurred while reading the object from the byte array.");
	}

	public ObjectDeserializationException(String message)
	{
		super(message);
	}

	public ObjectDeserializationException(Throwable cause)
	{
		super("An error occurred while reading the object from the byte array.", cause);
	}

	public ObjectDeserializationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}