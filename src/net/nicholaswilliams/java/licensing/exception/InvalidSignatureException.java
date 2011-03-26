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
 * This is thrown when a signature is invalid and cannot be verified.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class InvalidSignatureException extends RuntimeException
{
	public InvalidSignatureException()
	{
		super("The signature provided is invalid and cannot be verified.");
	}

	public InvalidSignatureException(String message)
	{
		super(message);
	}

	public InvalidSignatureException(Throwable cause)
	{
		super("The signature provided is invalid and cannot be verified.", cause);
	}

	public InvalidSignatureException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
