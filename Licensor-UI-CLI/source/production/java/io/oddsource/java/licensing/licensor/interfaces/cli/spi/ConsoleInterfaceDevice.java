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

import java.io.Console;
import java.io.IOError;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.IllegalFormatException;

/**
 * The default implementation of {@link TextInterfaceDevice}. This is the only implementation currently used by the
 * LicenseManager in production environments, and wraps all of the methods in {@link java.io.Console} and the exit
 * method in {@link java.lang.Runtime}.<br />
 * <br />
 * Much of the documentation for the methods in this class was derived from the documentation for
 * {@link java.io.Console} and {@link java.lang.Runtime} because this implementation wraps the methods of those
 * classes.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConsoleInterfaceDevice extends AbstractTextInterfaceDevice
{
    private final Console console;

    private final Runtime runtime;

    public ConsoleInterfaceDevice()
    {
        super(System.in, System.out, System.err);

        this.console = System.console();
        this.runtime = Runtime.getRuntime();
    }

    /**
     * Terminates the currently running application. The argument serves as a status code; by convention, a nonzero
     * status code indicates abnormal termination.<br />
     * <br />
     * Note: Wraps {@link java.lang.Runtime#exit(int)}.
     *
     * @param exitCode The exit status to exit with
     */
    @Override
    public void exit(int exitCode)
    {
        this.runtime.exit(exitCode);
    }

    /**
     * Registers a new application shutdown hook. For the standard behavior, implemented by this implementation,
     * see the documentation for {@link Runtime#addShutdownHook(Thread)}.<br />
     * <br />
     * Note: Wraps {@link java.lang.Runtime#addShutdownHook(Thread)}.
     *
     * @param hook An initialized but unstarted Thread object
     * @throws IllegalArgumentException if the specified hook has already been registered, or if it can be determined that the hook is already running or has already been run.
     * @throws IllegalStateException if the application is already in the process of shutting down.
     * @throws SecurityException if registering shutdown hooks is forbidden.
     */
    @Override
    public void registerShutdownHook(Thread hook)
            throws IllegalArgumentException, IllegalStateException, SecurityException
    {
        this.runtime.addShutdownHook(hook);
    }

    /**
     * De-registers a previously-registered application shutdown hook.<br />
     * <br />
     * Note: Wraps {@link java.lang.Runtime#removeShutdownHook(Thread)}.
     *
     * @param hook The hook to remove
     * @return {@code true} if the specified hook had previously been registered and was successfully de-registered, {@code false} otherwise
     * @throws IllegalStateException if the virtual machine is already in the process of shutting down.
     * @throws SecurityException if registering shutdown hooks is forbidden.
     */
    @Override
    public boolean unregisterShutdownHook(Thread hook) throws IllegalStateException, SecurityException
    {
        return this.runtime.removeShutdownHook(hook);
    }

    /**
     * Writes a formatted string to this device's output stream using the specified format string and arguments.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#format(String, Object...)}.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return this device.
     * @throws java.util.IllegalFormatException - if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     */
    @Override
    public TextInterfaceDevice format(String format, Object... arguments) throws IllegalFormatException
    {
        this.console.format(format, arguments);
        return this;
    }

    /**
     * A convenience method to write a formatted string to this device's output stream using the specified format
     * string and arguments.<br />
     * <br />
     * An invocation of this method of the form {@code device.printf(format, args)} behaves in exactly the same way as
     * the invocation of {@code device.format(format, args)}.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#printf(String, Object...)}.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return this device.
     * @throws java.util.IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public TextInterfaceDevice printf(String format, Object... arguments) throws IllegalFormatException, IOError
    {
        this.console.printf(format, arguments);
        return this;
    }

    /**
     * Flushes this device by writing any buffered output to the underlying stream.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#flush()}.
     */
    @Override
    public void flush()
    {
        this.console.flush();
    }

    /**
     * Reads a single line of text from the device.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#readLine()}.
     *
     * @return a string containing the line read from the interface device input, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public String readLine() throws IOError
    {
        return this.console.readLine();
    }

    /**
     * Provides a formatted prompt, then reads a single line of text from the device.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#readLine(String, Object...)}.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return a string containing the line read from the interface device input, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws java.util.IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public String readLine(String format, Object... arguments) throws IllegalFormatException, IOError
    {
        return this.console.readLine(format, arguments);
    }

    /**
     * Reads a password or passphrase from the device with echoing disabled.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#readPassword()}.
     *
     * @return a character array containing the password or passphrase read from the device, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public char[] readPassword() throws IOError
    {
        return this.console.readPassword();
    }

    /**
     * Provides a formatted prompt, then reads a password or passphrase from the device with echoing disabled.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#readPassword(String, Object...)}.
     *
     * @param format A format string as described in {@link java.util.Formatter #syntax}
     * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
     * @return a character array containing the password or passphrase read from the device, not including any line-termination characters, or {@code null} if an end of stream has been reached.
     * @throws java.util.IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
     * @throws java.io.IOError if an I/O error occurs.
     */
    @Override
    public char[] readPassword(String format, Object... arguments) throws IllegalFormatException, IOError
    {
        return this.console.readPassword(format, arguments);
    }

    /**
     * Retrieves the unique Reader object associated with this interface device. For more information regarding the
     * uses of this method, see the documentation for {@link java.io.Console#reader()}.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#reader()}.
     *
     * @return the reader associated with this interface.
     */
    @Override
    public Reader getReader()
    {
        return this.console.reader();
    }

    /**
     * Retrieves the unique PrintWriter object associated with this interface device.<br />
     * <br />
     * Note: Wraps {@link java.io.Console#writer()}.
     *
     * @return the print writer associated with this interface.
     */
    @Override
    public PrintWriter getWriter()
    {
        return this.console.writer();
    }

    /**
     * Gets the console object in use by this device.
     *
     * @return the console object in use by this device.
     */
    public Console getConsole()
    {
        return this.console;
    }

    /**
     * Gets the runtime object in use by this device.
     *
     * @return the runtime object in use by this device.
     */
    public Runtime getRuntime()
    {
        return this.runtime;
    }
}
