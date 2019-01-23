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
package io.oddsource.java.licensing.immutable;

/**
 * This class specifies an interface for checking the validity of an object. It
 * is specified as an abstract class instead of an interface so that the
 * implementing classes can keep the target method protected.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class ValidObject
{
    /**
     * Checks the validity of this object, and throws an
     * {@link ImmutableModifiedThroughReflectionError} if that check fails.
     *
     * @throws ImmutableModifiedThroughReflectionError if the validity check fails.
     */
    protected abstract void checkValidity();
}
