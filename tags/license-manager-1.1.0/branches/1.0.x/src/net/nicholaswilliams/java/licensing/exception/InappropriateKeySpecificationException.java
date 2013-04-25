/*
 * InappropriateKeySpecificationException.java from LicenseManager modified Friday, September 21, 2012 07:37:46 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.exception;

/**
 * This exception is thrown when an inappropriate key specification is provided
 * for the key factory.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class InappropriateKeySpecificationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InappropriateKeySpecificationException()
	{
		super("The specified key specification is inappropriate for the factory.");
	}

	public InappropriateKeySpecificationException(String message)
	{
		super(message);
	}

	public InappropriateKeySpecificationException(Throwable cause)
	{
		super("The specified key specification is inappropriate for the factory.", cause);
	}

	public InappropriateKeySpecificationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
