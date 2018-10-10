/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.commons.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

import javolution.util.FastCollection;
import javolution.util.FastCollection.Record;

/**
 * @author NB4L1
 */
@SuppressWarnings("unchecked")
public abstract class AEFastCollection<E> implements Collection<E> {

	public abstract Record head();

	public abstract Record tail();

	public abstract E valueOf(Record record);

	public abstract void delete(Record record);

	public abstract void delete(Record record, E value);

	public final E getFirst() {
		final Record first = head().getNext();
		if (first == tail())
			return null;

		return valueOf(first);
	}

	public final E getLast() {
		final Record last = tail().getPrevious();
		if (last == head())
			return null;

		return valueOf(last);
	}

	public final E removeFirst() {
		final Record first = head().getNext();
		if (first == tail())
			return null;

		final E value = valueOf(first);
		delete(first, value);
		return value;
	}

	public final E removeLast() {
		final Record last = tail().getPrevious();
		if (last == head())
			return null;

		final E value = valueOf(last);
		delete(last, value);
		return value;
	}

	public boolean addAll(E[] c) {
		boolean modified = false;

		for (E e : c)
			if (add(e))
				modified = true;

		return modified;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll((Iterable<? extends E>) c);
	}

	public boolean addAll(Iterable<? extends E> c) {
		if (c instanceof RandomAccess && c instanceof List<?>)
			return addAll((List<? extends E>) c);

		if (c instanceof FastCollection<?>)
			return addAll((FastCollection<? extends E>) c);

		if (c instanceof AEFastCollection<?>)
			return addAll((AEFastCollection<? extends E>) c);

		boolean modified = false;

		for (E e : c)
			if (add(e))
				modified = true;

		return modified;
	}

	private boolean addAll(AEFastCollection<? extends E> c) {
		boolean modified = false;

		for (Record r = c.head(), end = c.tail(); (r = r.getNext()) != end;)
			if (add(c.valueOf(r)))
				modified = true;

		return modified;
	}

	private boolean addAll(FastCollection<? extends E> c) {
		boolean modified = false;

		for (Record r = c.head(), end = c.tail(); (r = r.getNext()) != end;)
			if (add(c.valueOf(r)))
				modified = true;

		return modified;
	}

	private boolean addAll(List<? extends E> c) {
		boolean modified = false;

		for (int i = 0, size = c.size(); i < size;)
			if (add(c.get(i++)))
				modified = true;

		return modified;
	}

	public boolean containsAll(Object[] c) {
		for (Object obj : c)
			if (!contains(obj))
				return false;

		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return containsAll((Iterable<? extends E>) c);
	}

	public boolean containsAll(Iterable<?> c) {
		if (c instanceof RandomAccess && c instanceof List<?>)
			return containsAll((List<?>) c);

		if (c instanceof FastCollection<?>)
			return containsAll((FastCollection<?>) c);

		if (c instanceof AEFastCollection<?>)
			return containsAll((AEFastCollection<?>) c);

		for (Object obj : c)
			if (!contains(obj))
				return false;

		return true;
	}

	private boolean containsAll(AEFastCollection<?> c) {
		for (Record r = c.head(), end = c.tail(); (r = r.getNext()) != end;)
			if (!contains(c.valueOf(r)))
				return false;

		return true;
	}

	private boolean containsAll(FastCollection<?> c) {
		for (Record r = c.head(), end = c.tail(); (r = r.getNext()) != end;)
			if (!contains(c.valueOf(r)))
				return false;

		return true;
	}

	private boolean containsAll(List<?> c) {
		for (int i = 0, size = c.size(); i < size;)
			if (!contains(c.get(i++)))
				return false;

		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;

		for (Record head = head(), r = tail().getPrevious(), previous; r != head; r = previous) {
			previous = r.getPrevious();
			if (c.contains(valueOf(r))) {
				delete(r);
				modified = true;
			}
		}

		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;

		for (Record head = head(), r = tail().getPrevious(), previous; r != head; r = previous) {
			previous = r.getPrevious();
			if (!c.contains(valueOf(r))) {
				delete(r);
				modified = true;
			}
		}

		return modified;
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[size()]);
	}

	@Override
	public <T> T[] toArray(T[] array) {
		int size = size();

		if (array.length != size)
			array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);

		if (size == 0 && array.length == 0)
			return array;

		int i = 0;
		for (Record r = head(), end = tail(); (r = r.getNext()) != end;)
			array[i++] = (T) valueOf(r);

		return array;
	}
}
