/*
 * MockPasswordProvider.java from LicenseManager modified Thursday, January 24, 2013 16:03:03 CST (-0600).
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

package net.nicholaswilliams.java.licensing.mock;

import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;

public class MockPasswordProvider implements PasswordProvider
{
    public char[] getPassword()
    {
        return new char[] {
            's', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'
        };
    }
}
