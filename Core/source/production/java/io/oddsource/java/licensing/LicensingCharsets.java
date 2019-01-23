/*
 * LicensingCharsets.java from LicenseManager modified Wednesday, April 24, 2013 12:06:11 CDT (-0500).
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

package io.oddsource.java.licensing;

import java.nio.charset.Charset;

/**
 * Defines the default character set used in license serialization and deserialization.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.1.0
 */
public final class LicensingCharsets
{
    /**
     * All operations in License Manager use the universal UTF-8 character set.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * This class cannot be instantiated.
     */
    private LicensingCharsets()
    {
        throw new AssertionError("This class cannot be instantiated.");
    }
}
