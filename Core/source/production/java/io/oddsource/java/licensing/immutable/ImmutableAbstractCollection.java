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

import java.io.Serializable;
import java.util.Collection;

/**
 * Wraps a collection such that it cannot be modified. There is some overhead
 * associated with this due to verification of hash codes on every call to
 * prevent tampering with via reflection, but this is well worth it if your goal
 * is security and you truly need an unmodifiable collection.
 *
 * @param <E> Any object
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ImmutableAbstractCollection<E> extends ValidObject
    implements Immutable, Collection<E>, Serializable
{
    protected final static String modificationProhibited = "This collection cannot be modified.";

    private final static long serialVersionUID = -4187794066638697055L;

    protected final Collection<E> internalCollection;

    private final int internalSize;

    private final int internalHashCode;

    /**
     * Constructor that wraps (not copies).
     *
     * @param collection The collection to decorate, must not be null
     *
     * @throws IllegalArgumentException if collection is null
     */
    protected ImmutableAbstractCollection(final Collection<E> collection)
    {
        if(collection == null)
        {
            throw new IllegalArgumentException("Parameter collection must not be null.");
        }

        this.internalCollection = collection;
        this.internalSize = this.internalCollection.size();
        this.internalHashCode = this.internalCollection.hashCode();
    }

    /**
     * Checks the validity of this object, and throws an
     * {@link ImmutableModifiedThroughReflectionError} if that check fails.
     *
     * @throws ImmutableModifiedThroughReflectionError if the validity check fails.
     */
    @Override
    protected final void checkValidity()
    {
        if(
            this.internalSize != this.internalCollection.size() ||
            this.internalHashCode != this.internalCollection.hashCode()
        )
        {
            throw new ImmutableModifiedThroughReflectionError();
        }
    }

    @Override
    public final boolean equals(final Object o)
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return o == this || (
                o instanceof ImmutableAbstractCollection &&
                this.internalCollection.equals(
                    ((ImmutableAbstractCollection) o).internalCollection
                )
            );
        }
    }

    @Override
    public final int hashCode()
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalHashCode;
        }
    }

    @Override
    public final boolean contains(final Object object)
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            try
            {
                return this.internalCollection.contains(object);
            }
            catch(final ClassCastException e)
            {
                return false;
            }
        }
    }

    @Override
    public final boolean containsAll(final Collection<?> c)
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalCollection.containsAll(c);
        }
    }

    @Override
    public final boolean isEmpty()
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalCollection.isEmpty();
        }
    }

    @Override
    public final ImmutableIterator<E> iterator()
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return new ImmutableIterator<>(this.internalCollection.iterator(), this);
        }
    }

    @Override
    public final int size()
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalCollection.size();
        }
    }

    @Override
    public final Object[] toArray()
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalCollection.toArray();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public final <T> T[] toArray(final T[] prototype)
    {
        synchronized(this.internalCollection)
        {
            this.checkValidity();
            return this.internalCollection.toArray(prototype);
        }
    }

    @Override
    public final String toString()
    {
        return this.internalCollection.toString();
    }

    @Override
    public final boolean add(final E e)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final boolean addAll(final Collection<? extends E> c)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final void clear()
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final boolean remove(final Object o)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final boolean removeAll(final Collection<?> c)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final boolean retainAll(final Collection<?> c)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }
}
