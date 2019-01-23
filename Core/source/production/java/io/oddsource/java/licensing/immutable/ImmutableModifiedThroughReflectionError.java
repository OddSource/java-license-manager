/*
 * ImmutableModifiedThroughReflectionError.java from LicenseManager modified Friday, September 21, 2012 07:46:54 CDT
 * (-0500).
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

package io.oddsource.java.licensing.immutable;

/**
 * This exception is thrown when an unmodifiable list is modified illegally
 * through exception.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ImmutableModifiedThroughReflectionError extends Error
{
    private static final long serialVersionUID = 1L;

    private static final String defaultMessage =
        "This immutable object appears to have been modified through reflection.";

    /**
     * Constructor.
     */
    public ImmutableModifiedThroughReflectionError()
    {
        super(ImmutableModifiedThroughReflectionError.defaultMessage);
    }

    /**
     * Constructor.
     *
     * @param message The message
     */
    public ImmutableModifiedThroughReflectionError(final String message)
    {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause
     */
    public ImmutableModifiedThroughReflectionError(final Throwable cause)
    {
        super(ImmutableModifiedThroughReflectionError.defaultMessage, cause);
    }

    /**
     * Constructor.
     *
     * @param message The message
     * @param cause The cause
     */
    public ImmutableModifiedThroughReflectionError(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
