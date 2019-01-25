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
package io.oddsource.java.licensing;

/**
 * This specifies an interface for providing and persisting the stored, still-encrypted license content and signature
 * object.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface LicenseProvider
{
    /**
     * Gets the stored, still-encrypted license content and signature from the persistence store.
     *
     * @param context The context for which to get the license
     *
     * @return the signed license object.
     */
    public abstract SignedLicense getLicense(Object context);
}
