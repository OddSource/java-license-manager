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
package io.oddsource.java.licensing.licensor.interfaces.cli.spi;

import java.io.IOError;
import java.io.InputStream;
import java.io.PrintStream;

import io.oddsource.java.licensing.licensor.interfaces.spi.AbstractPasswordPrompter;
import io.oddsource.java.licensing.licensor.interfaces.spi.OutputDevice;

/**
 * An abstract implementation of {@link TextInterfaceDevice} that implements any common utilities among all interface
 * devices.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractTextInterfaceDevice extends AbstractPasswordPrompter
    implements TextInterfaceDevice, OutputDevice
{
    private static final String[] FIRST_TEN_NUMBERS = new String[] {
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
    };

    private static final short WRITTEN_NUMBER_STRING_BOUNDARY_BOTTOM = 0;

    private static final short WRITTEN_NUMBER_STRING_BOUNDARY_TOP = 10;

    private final InputStream in;

    private final PrintStream out;

    private final PrintStream err;

    /**
     * Constructor.
     *
     * @param inputStream The standard-in stream
     * @param outputStream The standard-out stream
     * @param errorStream The standard-err stream
     */
    public AbstractTextInterfaceDevice(
        final InputStream inputStream,
        final PrintStream outputStream,
        final PrintStream errorStream
    )
    {
        this.in = inputStream;
        this.out = outputStream;
        this.err = errorStream;
    }

    /**
     * Terminates the currently running application normally (with a status code of zero). Equivalent to calling
     * (and, if fact, calls) {@link #exit(int)} with an {@code exitCode} of 0.
     */
    @Override
    public void exit()
    {
        this.exit(0);
    }

    /**
     * Prints a {@code char} to standard-out.
     *
     * @param c The {@code char} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOut(final char c) throws IOError
    {
        this.out.print(c);
    }

    /**
     * Prints a {@code String} to standard-out.
     *
     * @param s The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOut(final String s) throws IOError
    {
        this.out.print(s);
    }

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOut(final Object o) throws IOError
    {
        this.out.print(o);
    }

    /**
     * Prints a {@code char} to standard-err.
     *
     * @param c The {@code char} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErr(final char c) throws IOError
    {
        this.err.print(c);
    }

    /**
     * Prints a {@code String} to standard-err.
     *
     * @param s The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErr(final String s) throws IOError
    {
        this.err.print(s);
    }

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErr(final Object o) throws IOError
    {
        this.err.print(o);
    }

    /**
     * Terminates the current standard-out line by writing the line separator string.
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn() throws IOError
    {
        this.out.println();
    }

    /**
     * Prints a {@code char} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param c The {@code char} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(final char c) throws IOError
    {
        this.out.println(c);
    }

    /**
     * Prints a {@code String} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(final String s) throws IOError
    {
        this.out.println(s);
    }

    /**
     * Prints an {@code Object} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(final Object o) throws IOError
    {
        this.out.println(o);
    }

    /**
     * Prints a {@code String} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param message The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void outputMessage(final String message) throws IOError
    {
        this.printOutLn(message);
    }

    /**
     * Terminates the current standard-err line by writing the line separator string.
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn() throws IOError
    {
        this.err.println();
    }

    /**
     * Prints a {@code char} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param c The {@code char} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(final char c) throws IOError
    {
        this.err.println(c);
    }

    /**
     * Prints a {@code String} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(final String s) throws IOError
    {
        this.err.println(s);
    }

    /**
     * Prints an {@code Object} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(final Object o) throws IOError
    {
        this.err.println(o);
    }

    /**
     * Prints a {@code String} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param message The {@code String} to be printed
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public void outputErrorMessage(final String message) throws IOError
    {
        this.printErrLn(message);
    }

    /**
     * Gets the standard input stream.
     *
     * @return the standard input stream.
     */
    @Override
    public InputStream in()
    {
        return this.in;
    }

    /**
     * Gets the standard output stream.
     *
     * @return the standard output stream.
     */
    @Override
    public PrintStream out()
    {
        return this.out;
    }

    /**
     * Gets the standard error stream.
     *
     * @return the standard error stream.
     */
    @Override
    public PrintStream err()
    {
        return this.err;
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
     * @param what What we're prompting for a password for
     *
     * @return the valid, confirmed password entered by the user.
     *
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public char[] promptForValidPassword(final int minLength, final int maxLength, final String what) throws IOError
    {
        final String minString = AbstractTextInterfaceDevice.getNumberString(minLength);

        final String maxString = AbstractTextInterfaceDevice.getNumberString(maxLength);

        return this.promptForValidPassword(
            minLength,
            maxLength,
            "Enter pass phrase to encrypt " + what + ": ",
            "Verifying - Reenter pass phrase to encrypt " + what + ": ",
            "The password must be at least " + minString + " characters and no more than " + maxString +
                " characters long.",
            "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
            this
        );
    }

    private static String getNumberString(final int number)
    {
        return (
            number < AbstractTextInterfaceDevice.WRITTEN_NUMBER_STRING_BOUNDARY_BOTTOM ||
            number > AbstractTextInterfaceDevice.WRITTEN_NUMBER_STRING_BOUNDARY_TOP
        ) ? "" + number : AbstractTextInterfaceDevice.FIRST_TEN_NUMBERS[number];
    }
}
