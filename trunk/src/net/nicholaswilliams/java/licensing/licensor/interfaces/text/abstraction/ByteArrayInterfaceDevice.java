/*
 * ByteArrayInterfaceDevice.java from LicenseManager modified Tuesday, February 21, 2012 10:56:34 CST (-0600).
 *
 * Copyright 2010-2012 the original author or authors.
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.IllegalFormatException;

/**
 * An (incomplete) alternative implementation where everything happens via {@link java.io.ByteArrayInputStream}s and
 * {@link org.apache.commons.io.output.ByteArrayOutputStream}s. No general uses yet.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class ByteArrayInterfaceDevice extends AbstractTextInterfaceDevice
{
	private final ByteArrayInputStream inputStream;

	private final ByteArrayOutputStream outputStream;

	private final ByteArrayOutputStream errorOutputStream;

	public ByteArrayInterfaceDevice(byte[] inputBuffer)
	{
		super(
				new ByteArrayInputStream(inputBuffer),
				new PrintStream(new ByteArrayOutputStream()),
				new PrintStream(new ByteArrayOutputStream())
		);

		this.inputStream = (ByteArrayInputStream)this.in;

		try
		{
			Field field = java.io.FilterOutputStream.class.getDeclaredField("out");
			field.setAccessible(true);

			this.outputStream = (ByteArrayOutputStream)field.get(this.out);

			this.errorOutputStream = (ByteArrayOutputStream)field.get(this.err);
		}
		catch(NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers a new application shutdown hook. For the standard behavior, implemented by the default implementation,
	 * see the documentation for {@link Runtime#addShutdownHook(Thread)}.
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
		throw new UnsupportedOperationException("This method is not implemented yet.");
	}

	/**
	 * De-registers a previously-registered application shutdown hook.
	 *
	 * @param hook The hook to remove
	 * @return {@code true} if the specified hook had previously been registered and was successfully de-registered, {@code false} otherwise
	 * @throws IllegalStateException if the virtual machine is already in the process of shutting down.
	 * @throws SecurityException if registering shutdown hooks is forbidden.
	 */
	@Override
	public boolean unregisterShutdownHook(Thread hook) throws IllegalStateException, SecurityException
	{
		throw new UnsupportedOperationException("This method is not implemented yet.");
	}

	/**
	 * Terminates the currently running application. The argument serves as a status code; by convention, a nonzero
	 * status code indicates abnormal termination.
	 *
	 * @param exitCode The exit status to exit with
	 */
	@Override
	public void exit(int exitCode)
	{
		throw new UnsupportedOperationException("This method is not implemented yet.");
	}

	/**
	 * Writes a formatted string to this device's output stream using the specified format string and arguments.
	 *
	 * @param format A format string as described in {@link java.util.Formatter #syntax}
	 * @param arguments Arguments referenced by the format specifiers in the format string. If there are more arguments than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java Virtual Machine Specification. The behaviour on a null argument depends on the conversion.
	 * @return this device.
	 * @throws java.util.IllegalFormatException - if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
	 */
	@Override
	public TextInterfaceDevice format(String format, Object... arguments) throws IllegalFormatException
	{
		return null;
	}

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
	 * @throws java.util.IllegalFormatException if a format string contains an illegal syntax, a format specifier that is incompatible with the given arguments, insufficient arguments given the format string, or other illegal conditions. For specification of all possible formatting errors, see the Details section of the formatter class specification.
	 * @throws java.io.IOError if an I/O error occurs.
	 */
	@Override
	public TextInterfaceDevice printf(String format, Object... arguments) throws IllegalFormatException, IOError
	{
		return null;
	}

	/**
	 * Flushes this device by writing any buffered output to the underlying stream.<br />
	 * <br />
	 * Note: Wraps {@link java.io.Console#flush()}.
	 */
	@Override
	public void flush()
	{
		this.out.flush();
		this.err.flush();
	}

	/**
	 * Reads a single line of text from the device.
	 *
	 * @return a string containing the line read from the interface device input, not including any line-termination characters, or {@code null} if an end of stream has been reached.
	 * @throws java.io.IOError if an I/O error occurs.
	 */
	@Override
	public String readLine() throws IOError
	{
		return null;
	}

	/**
	 * Provides a formatted prompt, then reads a single line of text from the device.
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
		return null;
	}

	/**
	 * Reads a password or passphrase from the device with echoing disabled.
	 *
	 * @return a character array containing the password or passphrase read from the device, not including any line-termination characters, or {@code null} if an end of stream has been reached.
	 * @throws java.io.IOError if an I/O error occurs.
	 */
	@Override
	public char[] readPassword() throws IOError
	{
		return new char[0];
	}

	/**
	 * Provides a formatted prompt, then reads a password or passphrase from the device with echoing disabled.
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
		return new char[0];
	}

	/**
	 * Retrieves the unique Reader object associated with this interface device. For more information regarding the
	 * uses of this method, see the documentation for {@link java.io.Console#reader()}.
	 *
	 * @return the reader associated with this interface.
	 */
	@Override
	public Reader getReader()
	{
		return null;
	}

	/**
	 * Retrieves the unique PrintWriter object associated with this interface device.
	 *
	 * @return the print writer associated with this interface.
	 */
	@Override
	public PrintWriter getWriter()
	{
		return null;
	}

	/**
	 * Gets the input stream in use by this device.
	 *
	 * @return the input stream.
	 */
	public ByteArrayInputStream getInputStream()
	{
		return this.inputStream;
	}

	/**
	 * Gets the output stream in use by this device.
	 *
	 * @return the output stream.
	 */
	public ByteArrayOutputStream getOutputStream()
	{
		return this.outputStream;
	}

	/**
	 * Gets the error output stream in use by this device.
	 *
	 * @return the error output stream.
	 */
	public ByteArrayOutputStream getErrorOutputStream()
	{
		return this.errorOutputStream;
	}
}
