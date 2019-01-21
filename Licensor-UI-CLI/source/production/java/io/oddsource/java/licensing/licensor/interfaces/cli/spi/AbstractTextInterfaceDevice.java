/*
 * AbstractTextInterfaceDevice.java from LicenseManager modified Thursday, January 24, 2013 14:51:21 CST (-0600).
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

package io.oddsource.java.licensing.licensor.interfaces.cli.spi;

import io.oddsource.java.licensing.licensor.interfaces.spi.AbstractPasswordPrompter;
import io.oddsource.java.licensing.licensor.interfaces.spi.OutputDevice;
import io.oddsource.java.licensing.licensor.interfaces.spi.PasswordPrompter;

import java.io.IOError;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * An abstract implementation of {@link TextInterfaceDevice} that implements any common utilities among all interface
 * devices.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractTextInterfaceDevice extends AbstractPasswordPrompter
        implements TextInterfaceDevice, PasswordPrompter, OutputDevice
{
    protected final InputStream in;

    protected final PrintStream out;

    protected final PrintStream err;

    public AbstractTextInterfaceDevice(InputStream inputStream, PrintStream outputStream, PrintStream errorStream)
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
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOut(char c) throws IOError
    {
        this.out.print(c);
    }

    /**
     * Prints a {@code String} to standard-out.
     *
     * @param s The {@code String} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOut(String s) throws IOError
    {
        this.out.print(s);
    }

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOut(Object o) throws IOError
    {
        this.out.print(o);
    }

    /**
     * Prints a {@code char} to standard-err.
     *
     * @param c The {@code char} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErr(char c) throws IOError
    {
        this.err.print(c);
    }

    /**
     * Prints a {@code String} to standard-err.
     *
     * @param s The {@code String} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErr(String s) throws IOError
    {
        this.err.print(s);
    }

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErr(Object o) throws IOError
    {
        this.err.print(o);
    }

    /**
     * Terminates the current standard-out line by writing the line separator string.
     *
     * @throws java.io.IOError if an I/O error occurs.
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
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(char c) throws IOError
    {
        this.out.println(c);
    }

    /**
     * Prints a {@code String} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(String s) throws IOError
    {
        this.out.println(s);
    }

    @Override
    public void outputMessage(String message) throws IOError
    {
        this.printOutLn(message);
    }

    /**
     * Prints an {@code Object} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printOutLn(Object o) throws IOError
    {
        this.out.println(o);
    }

    /**
     * Terminates the current standard-err line by writing the line separator string.
     *
     * @throws java.io.IOError if an I/O error occurs.
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
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(char c) throws IOError
    {
        this.err.println(c);
    }

    /**
     * Prints a {@code String} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(String s) throws IOError
    {
        this.err.println(s);
    }

    @Override
    public void outputErrorMessage(String message) throws IOError
    {
        this.printErrLn(message);
    }

    /**
     * Prints an {@code Object} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public void printErrLn(Object o) throws IOError
    {
        this.err.println(o);
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
     * passwords match.<br />
     * <br />
     * This method should continue to ask for the password over and over again until a valid password is entered and
     * confirmed or the user cancels the operation.
     *
     * @param minLength The minimum legal length for the password
     * @param maxLength The maximum legal length for the password
     * @param what What we're prompting for a password for
     * @return the valid, confirmed password entered by the user.
     * @throws IOError if an I/O error occurs.
     */
    @Override
    public char[] promptForValidPassword(int minLength, int maxLength, String what) throws IOError
    {
        String minString = AbstractTextInterfaceDevice.getNumberString(minLength);

        String maxString = AbstractTextInterfaceDevice.getNumberString(maxLength);

        return this.promptForValidPassword(minLength, maxLength,
                                           "Enter pass phrase to encrypt " + what + ": ",
                                           "Verifying - Reenter pass phrase to encrypt " + what + ": ",
                                           "The password must be at least " + minString +
                                                       " characters and no more than " + maxString + " characters long.",
                                           "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                           this);
    }

    private static final String[] FIRST_TEN_NUMBERS = new String[] { "zero", "one", "two", "three", "four", "five",
                                                                     "six", "seven", "eight", "nine", "ten" };

    private static String getNumberString(int number)
    {
        return number < 0 || number > 10 ? "" + number : AbstractTextInterfaceDevice.FIRST_TEN_NUMBERS[number];
    }
}
