/*
 * TestImmutableAbstractCollection.java from LicenseManager modified Friday, September 21, 2012 07:46:54 CDT (-0500).
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

package io.oddsource.java.licensing.immutable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ImmutableAbstractCollection.
 */
public class TestImmutableAbstractCollection
{
    @Before
    public void setUp()
    {

    }

    @After
    public void tearDown()
    {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct01()
    {
        new ImmutableAbstractCollection<String>(null)
        {
            private static final long serialVersionUID = 1L;
        };
    }
}
