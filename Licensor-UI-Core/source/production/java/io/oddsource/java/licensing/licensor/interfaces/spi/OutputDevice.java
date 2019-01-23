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

/**
 * Marker for a class capable of outputting simple messages.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public interface OutputDevice
{
    public void outputMessage(String message) throws IOError;

    public void outputErrorMessage(String message) throws IOError;
}
