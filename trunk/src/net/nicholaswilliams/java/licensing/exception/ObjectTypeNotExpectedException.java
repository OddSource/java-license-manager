/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 * 
 *	  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.nicholaswilliams.java.licensing.exception;

/**
 * This class class is thrown when an object is deserialized that does not match
 * the type that was expected.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ObjectTypeNotExpectedException extends ObjectDeserializationException
{
	public ObjectTypeNotExpectedException()
	{
		super("The type of object read did not match the type expected.");
	}

	public ObjectTypeNotExpectedException(String message)
	{
		super(message);
	}

	public ObjectTypeNotExpectedException(String expectedType, String encounteredType)
	{
		super("While deserializing an object of expected type \"" + expectedType + "\", got an object of type \"" +
				encounteredType + "\" instead.");
	}

	public ObjectTypeNotExpectedException(Throwable cause)
	{
		super("The type of object read did not match the type expected.", cause);
	}

	public ObjectTypeNotExpectedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ObjectTypeNotExpectedException(String expectedType, String encounteredType, Throwable cause)
	{
		super("While deserializing an object of expected type \"" + expectedType + "\", got an object of type \"" +
				encounteredType + "\" instead.", cause);
	}
}
