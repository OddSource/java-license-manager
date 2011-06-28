/*
 * AbstractTextInterfaceDevice.java from LicenseManager modified Tuesday, June 28, 2011 11:34:10 CDT (-0500).
 *
 * Copyright 2010-2011 the original author or authors.
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
public abstract class AbstractTextInterfaceDevice implements TextInterfaceDevice
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
}
