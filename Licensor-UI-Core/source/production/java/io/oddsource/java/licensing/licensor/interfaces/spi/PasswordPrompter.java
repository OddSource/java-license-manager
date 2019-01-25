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
package io.oddsource.java.licensing.licensor.interfaces.spi;

import java.io.IOError;
import java.util.IllegalFormatException;

/**
 * Interface for prompting for passwords.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PasswordPrompter
{
    /**
     * Reads a password or passphrase from the device with echoing disabled.
     *
     * @return a character array containing the password or passphrase read from the device, not including any
     *     line-termination characters, or {@code null} if an end of stream has been reached.
     *
     * @throws IOError if an I/O error occurs.
     */
    public abstract char[] readPassword() throws IOError;

    /**
     * Provides a formatted prompt, then reads a password or passphrase from the device with echoing disabled.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more
     *     arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and
     *     may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined
     *     by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     *
     * @return a character array containing the password or passphrase read from the device, not including any
     *     line-termination characters, or {@code null} if an end of stream has been reached.
     *
     * @throws IllegalFormatException if a format string contains an illegal syntax, a format specifier that is
     *     incompatible with the given arguments, insufficient arguments given the format string, or other illegal
     *     conditions. For specification of all possible formatting errors, see the Details section of the formatter
     *     class specification.
     * @throws IOError if an I/O error occurs.
     */
    public abstract char[] readPassword(String format, Object... arguments) throws IllegalFormatException, IOError;

    /**
     * Checks whether the two passwords match.
     *
     * @param password1 The entered password
     * @param password2 The confirmed password
     *
     * @return {@code true} if the passwords match, {@code false} otherwise.
     */
    public abstract boolean passwordsMatch(char[] password1, char[] password2);

    /**
     * Prompts for a valid password by asking for the password using {@link #readPassword(String, Object...)}, checking
     * its length against the constraints, asking for the password to be confirmed, and checking to make sure the
     * passwords match.<br>
     * <br>
     * This method should continue to ask for the password over and over again until a valid password is entered and
     * confirmed or the user cancels the operation.
     *
     * @param minLength The minimum legal length for the password
     * @param maxLength The maximum legal length for the password
     * @param promptMessage The message to prompt for the password initially (the message should not include format
     *     specifiers)
     * @param promptConfirmMessage The message to prompt to confirm the password (the message should not include
     *     format specifiers)
     * @param lengthError The message to display if the password does not meet the minimum or maximum length
     *     requirements (the message should not include format specifiers)
     * @param matchError The message to display if the two entered passwords do not match (the message should not
     *     include format specifiers)
     * @param device The device that should be used to output messages and errors
     *
     * @return the valid, confirmed password entered by the user.
     *
     * @throws IOError if an I/O error occurs.
     */
    public abstract char[] promptForValidPassword(
        int minLength,
        int maxLength,
        String promptMessage,
        String promptConfirmMessage,
        String lengthError,
        String matchError,
        OutputDevice device
    )
        throws IOError;

    /**
     * Prompts for a valid password by asking for the password using {@link #readPassword(String, Object...)}, checking
     * its length against the constraints, asking for the password to be confirmed, and checking to make sure the
     * passwords match.<br>
     * <br>
     * This method should continue to ask for the password over and over again until a valid password is entered and
     * confirmed or the user cancels the operation.
     *
     * @param minLength The minimum legal length for the password
     * @param maxLength The maximum legal length for the password
     * @param what What we're prompting for a password for
     *
     * @return the valid, confirmed password entered by the user.
     *
     * @throws IOError if an I/O error occurs.
     */
    public abstract char[] promptForValidPassword(int minLength, int maxLength, String what) throws IOError;
}
