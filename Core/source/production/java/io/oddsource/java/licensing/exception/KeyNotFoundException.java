/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing.exception;

/**
 * This is thrown when the key (file) could not be found.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class KeyNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public KeyNotFoundException()
    {
        super("The key file could not be found.");
    }

    /**
     * Constructor.
     *
     * @param message The message
     */
    public KeyNotFoundException(final String message)
    {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause
     */
    public KeyNotFoundException(final Throwable cause)
    {
        super("The key file could not be found.", cause);
    }

    /**
     * Constructor.
     *
     * @param message The message
     * @param cause The cause
     */
    public KeyNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
