/*
 * ImmutableArrayList.java from LicenseManager modified Thursday, January 24, 2013 23:33:43 CST (-0600).
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Wraps a list such that it cannot be modified. There is some overhead
 * associated with this due to verification of hash codes on every call to
 * prevent tampering with via reflection, but this is well worth it if your goal
 * is security and you truly need an unmodifiable list.
 *
 * @param <E> Any object
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImmutableArrayList<E> extends ImmutableAbstractCollection<E>
    implements List<E>, Serializable, Cloneable
{
    private final static long serialVersionUID = -6912407141647481417L;

    private final ArrayList<E> internalList;

    /**
     * Constructor that copies.
     *
     * @param list the list to decorate, must not be null
     *
     * @throws IllegalArgumentException if list is null
     */
    public ImmutableArrayList(final List<E> list)
    {
        super(new ArrayList<>(list));

        this.internalList = (ArrayList<E>) this.internalCollection;
        this.internalList.trimToSize();
    }

    @Override
    @SuppressWarnings({"unchecked", "CloneDoesntCallSuperClone"})
    public final ImmutableArrayList<E> clone()
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return new ImmutableArrayList<>((List<E>) this.internalList.clone());
        }
    }

    @Override
    public final E get(final int index)
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return this.internalList.get(index);
        }
    }

    @Override
    public final int indexOf(final Object o)
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return this.internalList.indexOf(o);
        }
    }

    @Override
    public final int lastIndexOf(final Object o)
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return this.internalList.lastIndexOf(o);
        }
    }

    @Override
    public final ImmutableListIterator<E> listIterator()
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return new ImmutableListIterator<>(this.internalList.listIterator(), this);
        }
    }

    @Override
    public final ImmutableListIterator<E> listIterator(final int index)
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            return new ImmutableListIterator<>(this.internalList.listIterator(index), this);
        }
    }

    @Override
    public final ImmutableArrayList<E> subList(final int fromIndex, final int toIndex)
    {
        synchronized(this.internalList)
        {
            this.checkValidity();
            final List<E> subList = this.internalList.subList(fromIndex, toIndex);
            return new ImmutableArrayList<>(subList);
        }
    }

    @Override
    public final void add(final int index, final E e)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final boolean addAll(final int index, final Collection<? extends E> c)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final E remove(final int index)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }

    @Override
    public final E set(final int index, final E e)
    {
        throw new UnsupportedOperationException(ImmutableAbstractCollection.modificationProhibited);
    }
}
