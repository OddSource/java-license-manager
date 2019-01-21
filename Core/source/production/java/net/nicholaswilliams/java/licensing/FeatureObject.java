/*
 * FeatureObject.java from LicenseManager modified Saturday, May 19, 2012 09:14:18 CDT (-0500).
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

package net.nicholaswilliams.java.licensing;

/**
 * An interface that other objects can implement to indicate that they represent features. This can be especially
 * useful for decorating enums, for example, to represent valid features.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public interface FeatureObject
{
    public String getName();
}
