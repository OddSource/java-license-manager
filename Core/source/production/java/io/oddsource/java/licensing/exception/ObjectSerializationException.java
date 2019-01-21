/*
 * ObjectSerializationException.java from LicenseManager modified Friday, September 21, 2012 07:46:54 CDT (-0500).
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

package io.oddsource.java.licensing.exception;

/**
 * This class is thrown when an I/O error occurs while writing an object to a
 * byte array.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ObjectSerializationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public ObjectSerializationException()
    {
        super("An I/O error occurred while writing the object to the byte array.");
    }

    public ObjectSerializationException(String message)
    {
        super(message);
    }

    public ObjectSerializationException(Throwable cause)
    {
        super("An I/O error occurred while writing the object to the byte array.", cause);
    }

    public ObjectSerializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
