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
 * This exception is thrown when 2048-bit RSA security, which is required for
 * this library to run, is not supported with the current JVM.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class RSA2048NotSupportedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public RSA2048NotSupportedException()
    {
        super("2048-bit RSA Security is not supported on this system.");
    }

    public RSA2048NotSupportedException(String message)
    {
        super(message);
    }

    public RSA2048NotSupportedException(Throwable cause)
    {
        super("2048-bit RSA Security is not supported on this system.", cause);
    }

    public RSA2048NotSupportedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
