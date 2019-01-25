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

import java.util.Iterator;

/**
 * Wraps an iterator such that it cannot be modified.
 *
 * @param <E> Any object
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImmutableIterator<E> implements Immutable, Iterator<E>
{
    private final Iterator<E> internal;

    private final ValidObject validObject;

    ImmutableIterator(final Iterator<E> iterator, final ValidObject validObject)
    {
        this.internal = iterator;
        this.validObject = validObject;
    }

    @Override
    public boolean hasNext()
    {
        synchronized(this.validObject)
        {
            this.validObject.checkValidity();
            return this.internal.hasNext();
        }
    }

    @Override
    public E next()
    {
        synchronized(this.validObject)
        {
            this.validObject.checkValidity();
            return this.internal.next();
        }
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("This iterator cannot be modified.");
    }
}