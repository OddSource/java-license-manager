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
import java.util.Arrays;

/**
 * Abstract class for prompting for passwords.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractPasswordPrompter implements PasswordPrompter
{
    /**
     * Checks whether the two passwords match.
     *
     * @param password1 The entered password
     * @param password2 The confirmed password
     *
     * @return {@code true} if the passwords match, {@code false} otherwise.
     */
    @Override
    public boolean passwordsMatch(final char[] password1, final char[] password2)
    {
        return Arrays.equals(password1, password2);
    }

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
    @Override
    public char[] promptForValidPassword(
        final int minLength, final int maxLength, final String promptMessage,
        final String promptConfirmMessage, final String lengthError, final String matchError,
        final OutputDevice device
    )
        throws IOError
    {
        char[] password1 = null;
        char[] password2 = null;
        boolean passwordVerified = false;

        try
        {
            while(!passwordVerified)
            {
                password1 = this.readPassword(promptMessage);

                if(password1.length < minLength || password1.length > maxLength)
                {
                    device.outputErrorMessage(lengthError);
                    continue;
                }

                password2 = this.readPassword(promptConfirmMessage);

                passwordVerified = this.passwordsMatch(password1, password2);
                if(!passwordVerified)
                {
                    device.outputErrorMessage(matchError);
                }
            }

            return Arrays.copyOf(password1, password1.length);
        }
        finally
        {
            if(password1 != null)
            {
                Arrays.fill(password1, '\u0000');
            }
            if(password2 != null)
            {
                Arrays.fill(password2, '\u0000');
            }
        }
    }
}
