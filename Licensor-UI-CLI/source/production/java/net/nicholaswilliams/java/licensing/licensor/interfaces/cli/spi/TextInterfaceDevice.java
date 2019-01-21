/*
 * TextInterfaceDevice.java from LicenseManager modified Thursday, January 24, 2013 14:51:21 CST (-0600).
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.cli.spi;

import net.nicholaswilliams.java.licensing.licensor.interfaces.spi.OutputDevice;
import net.nicholaswilliams.java.licensing.licensor.interfaces.spi.PasswordPrompter;

import java.io.Flushable;
import java.io.IOError;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.IllegalFormatException;

/**
 * Specifies an interface for interacting with text devices in order to abstract the details of interaction away from
 * the application code. In nearly all circumstances, the implementation used will be the
 * {@link ConsoleInterfaceDevice}.<br />
 * <br />
 * Much of the documentation for the methods in this class was derived from the documentation for
 * {@link java.io.Console} and {@link java.lang.Runtime} because the default and intended implementation wraps the
 * methods of those classes.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TextInterfaceDevice extends PasswordPrompter, Flushable, OutputDevice
{
    public static final TextInterfaceDevice CONSOLE = new ConsoleInterfaceDevice();

    /**
     * Registers a new application shutdown hook. For the standard behavior, implemented by the default implementation,
     * see the documentation for {@link java.lang.Runtime#addShutdownHook(Thread)}.
     *
     * @param hook An initialized but unstarted Thread object
     * @throws IllegalArgumentException if the specified hook has already been registered, or if it can be determined that the hook is already running or has already been run.
     * @throws IllegalStateException if the application is already in the process of shutting down.
     * @throws SecurityException if registering shutdown hooks is forbidden.
     */
    public void registerShutdownHook(Thread hook)
            throws IllegalArgumentException, IllegalStateException, SecurityException;

    /**
     * De-registers a previously-registered application shutdown hook.
     *
     * @param hook The hook to remove
     * @return {@code true} if the specified hook had previously been registered and was successfully de-registered, {@code false} otherwise
     * @throws IllegalStateException if the virtual machine is already in the process of shutting down.
     * @throws SecurityException if registering shutdown hooks is forbidden.
     */
    public boolean unregisterShutdownHook(Thread hook) throws IllegalStateException, SecurityException;

    /**
     * Terminates the currently running application normally (with a status code of zero).
     */
    public void exit();

    /**
     * Terminates the currently running application. The argument serves as a status code; by convention, a nonzero
     * status code indicates abnormal termination.
     *
     * @param exitCode The exit status to exit with
     */
    public void exit(int exitCode);

    /**
     * Writes a formatted string to this device's output stream using the specified format string and arguments.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return this device.
     * @throws IllegalFormatException - if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     */
    public TextInterfaceDevice format(String format, Object... arguments) throws IllegalFormatException;

    /**
     * A convenience method to write a formatted string to this device's output stream using the specified format
     * string and arguments.<br />
     * <br />
     * An invocation of this method of the form {@code device.printf(format, args)} behaves in exactly the same way as
     * the invocation of {@code device.format(format, args)}.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return this device.
     * @throws IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws IOError if an I/O error occurs.
     */
    public TextInterfaceDevice printf(String format, Object... arguments) throws IllegalFormatException, IOError;

    /**
     * Reads a single line of text from the device.
     *
     * @return a string containing the line read from the interface device input, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws IOError if an I/O error occurs.
     */
    public String readLine() throws IOError;

    /**
     * Provides a formatted prompt, then reads a single line of text from the device.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return a string containing the line read from the interface device input, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws IOError if an I/O error occurs.
     */
    public String readLine(String format, Object... arguments) throws IllegalFormatException, IOError;

    /**
     * Reads a password or passphrase from the device with echoing disabled.
     *
     * @return a character array containing the password or passphrase read from the device, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws IOError if an I/O error occurs.
     */
    public char[] readPassword() throws IOError;

    /**
     * Provides a formatted prompt, then reads a password or passphrase from the device with echoing disabled.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return a character array containing the password or passphrase read from the device, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws IOError if an I/O error occurs.
     */
    public char[] readPassword(String format, Object... arguments) throws IllegalFormatException, IOError;

    /**
     * Prints a {@code char} to standard-out.
     *
     * @param c The {@code char} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOut(char c) throws IOError;

    /**
     * Prints a {@code String} to standard-out.
     *
     * @param s The {@code String} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOut(String s) throws IOError;

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOut(Object o) throws IOError;

    /**
     * Prints a {@code char} to standard-err.
     *
     * @param c The {@code char} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErr(char c) throws IOError;

    /**
     * Prints a {@code String} to standard-err.
     *
     * @param s The {@code String} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErr(String s) throws IOError;

    /**
     * Prints an {@code Object} to standard-out.
     *
     * @param o The {@code Object} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErr(Object o) throws IOError;

    /**
     * Terminates the current standard-out line by writing the line separator string.
     *
     * @throws IOError if an I/O error occurs.
     */
    public void printOutLn() throws IOError;

    /**
     * Prints a {@code char} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param c The {@code char} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOutLn(char c) throws IOError;

    /**
     * Prints a {@code String} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOutLn(String s) throws IOError;

    /**
     * Prints an {@code Object} to standard-out, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printOutLn(Object o) throws IOError;

    /**
     * Terminates the current standard-err line by writing the line separator string.
     *
     * @throws IOError if an I/O error occurs.
     */
    public void printErrLn() throws IOError;

    /**
     * Prints a {@code char} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param c The {@code char} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErrLn(char c) throws IOError;

    /**
     * Prints a {@code String} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param s The {@code String} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErrLn(String s) throws IOError;

    /**
     * Prints an {@code Object} to standard-err, then terminates the current line by writing the line separator string.
     *
     * @param o The {@code Object} to be printed
     * @throws IOError if an I/O error occurs.
     */
    public void printErrLn(Object o) throws IOError;

    /**
     * Retrieves the unique Reader object associated with this interface device. For more information regarding the
     * uses of this method, see the documentation for {@link java.io.Console#reader()}.
     *
     * @return the reader associated with this interface.
     */
    public Reader getReader();

    /**
     * Retrieves the unique PrintWriter object associated with this interface device.
     *
     * @return the print writer associated with this interface.
     */
    public PrintWriter getWriter();

    /**
     * Gets the standard input stream.
     *
     * @return the standard input stream.
     */
    public InputStream in();

    /**
     * Gets the standard output stream.
     *
     * @return the standard output stream.
     */
    public PrintStream out();

    /**
     * Gets the standard error stream.
     *
     * @return the standard error stream.
     */
    public PrintStream err();
}
