/*
 * ImmutableArrayList.java from LicenseManager modified Tuesday, June 28, 2011 11:34:11 CDT (-0500).
 *
 * Copyright 2010-2011 the original author or authors.
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

package net.nicholaswilliams.java.licensing.immutable;

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
	 * @throws IllegalArgumentException if list is null
	 */
	public ImmutableArrayList(List<E> list)
	{
		super(new ArrayList<E>(list));

		this.internalList = (ArrayList<E>)this.internalCollection;
		this.internalList.trimToSize();
	}

	@Override
	@SuppressWarnings({"unchecked", "CloneDoesntCallSuperClone"})
	public final ImmutableArrayList<E> clone()
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return new ImmutableArrayList<E>((List<E>)this.internalList.clone());
		}
	}

	@Override
	public final E get(int index)
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return this.internalList.get(index);
		}
	}

	@Override
	public final int indexOf(Object o)
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return this.internalList.indexOf(o);
		}
	}

	@Override
	public final int lastIndexOf(Object o)
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return this.internalList.lastIndexOf(o);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final ImmutableListIterator<E> listIterator()
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return new ImmutableListIterator(this.internalList.listIterator(), this);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final ImmutableListIterator<E> listIterator(int index)
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			return new ImmutableListIterator(this.internalList.listIterator(index), this);
		}
	}

	@Override
	public final ImmutableArrayList<E> subList(int fromIndex, int toIndex)
	{
		synchronized(this.internalList)
		{
			this.checkValidity();
			List<E> subList = this.internalList.subList(fromIndex, toIndex);
			return new ImmutableArrayList<E>(subList);
		}
	}
	
	@Override
	public final void add(int index, E e)
	{
		throw new UnsupportedOperationException("This list cannot be modified.");
	}

	@Override
	public final boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("This list cannot be modified.");
	}

	@Override
	public final E remove(int index)
	{
		throw new UnsupportedOperationException("This list cannot be modified.");
	}

	@Override
	public final E set(int index, E e)
	{
		throw new UnsupportedOperationException("This list cannot be modified.");
	}
}
