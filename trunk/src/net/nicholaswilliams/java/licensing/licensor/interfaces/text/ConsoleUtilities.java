/*
 * ConsoleUtilities.java from LicenseManager modified Tuesday, February 21, 2012 10:56:33 CST (-0600).
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;

/**
 * Utilities used by the text interface classes.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
class ConsoleUtilities
{
	/**
	 * Sets up a shutdown hook to ensure that the text device always exits to a newline.
	 *
	 * @param interfaceDevice The interface device to configure
	 */
	public static void configureInterfaceDevice(TextInterfaceDevice interfaceDevice)
	{
		interfaceDevice.registerShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				System.out.println();
			}
		}));
	}
}
