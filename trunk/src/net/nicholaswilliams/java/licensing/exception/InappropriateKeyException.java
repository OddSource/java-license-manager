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
 * This exception is thrown when the specified key is inappropriate for the
 * given cipher.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class InappropriateKeyException extends RuntimeException
{
	public InappropriateKeyException()
	{
		super("The specified key is inappropriate for the cipher.");
	}

	public InappropriateKeyException(String message)
	{
		super(message);
	}

	public InappropriateKeyException(Throwable cause)
	{
		super("The specified key is inappropriate for the cipher.", cause);
	}

	public InappropriateKeyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
