/*
 * ImmutableLinkedHashSet.java from LicenseManager modified Tuesday, February 21, 2012 10:59:34 CST (-0600).
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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Wraps a set such that it cannot be modified. There is some overhead
 * associated with this due to verification of hash codes on every call to
 * prevent tampering with via reflection, but this is well worth it if your goal
 * is security and you truly need an unmodifiable set.
 *
 * @author Nick Williams
 * @version 1.5.0
 * @since 1.0.0
 */
public final class ImmutableLinkedHashSet<E> extends ImmutableAbstractCollection<E>
    implements Set<E>, Serializable, Cloneable
{
    private final static long serialVersionUID = 2284350955829958161L;

    private final LinkedHashSet<E> internalSet;

    private final ArrayList<E> internalList;

    /**
     * Constructor that copies.
     *
     * @param list the set to decorate, must not be null
     *
     * @throws IllegalArgumentException if list is null
     */
    public ImmutableLinkedHashSet(final Set<E> list)
    {
        super(new LinkedHashSet<>(list));

        this.internalSet = (LinkedHashSet<E>) this.internalCollection;
        this.internalList = new ArrayList<>(list);
    }

    @Override
    @SuppressWarnings({"unchecked", "CloneDoesntCallSuperClone"})
    public final ImmutableLinkedHashSet<E> clone()
    {
        synchronized(this.internalSet)
        {
            this.checkValidity();
            return new ImmutableLinkedHashSet<>((Set<E>) this.internalSet.clone());
        }
    }

    /**
     * Retrieves the indexed element specified.
     *
     * @param index The element to retrieve.
     *
     * @return The element requested.
     */
    public E get(final int index)
    {
        return index < 0 ? null : this.internalList.get(index);
    }

    /**
     * Retrieves the matching element specified.
     *
     * @param object The element to match.
     *
     * @return The element requested.
     */
    public E get(final E object)
    {
        return this.get(this.internalList.indexOf(object));
    }
}
