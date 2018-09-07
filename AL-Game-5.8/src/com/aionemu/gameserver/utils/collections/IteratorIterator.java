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
package com.aionemu.gameserver.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is representing an iterator, that is used to iterate through the collection that has format Iterable&lt;Iterable&lt;V&gt;&gt;.<br>
 * <p/>
 * 
 * <pre>
 * &lt;code&gt;
 * Usage:&lt;br&gt;
 * List&lt;List&lt;Integer&gt;&gt; someList = ....
 * IteratorIterator&lt;Integer&gt; iterator = new IteratorIterator&lt;Integer&gt;(someList)
 *
 * OR:
 *
 * Map&lt;Integer, Set&lt;SomeClass&gt;&gt; mapOfSets = ....
 * IteratorIterator&lt;SomeCLass&gt; iterator = new IteratorIterator&lt;SomeClass&gt;(mapsOfSets.values());
 * &lt;/code&gt;
 * </pre>
 * <p/>
 * This iterator is not thread-safe. <br>
 * This iterator omits null values for first level collection, which means that if we have:
 * <p/>
 * 
 * <pre>
 * &lt;code&gt;
 * Set&lt;Set&lt;Integer&gt;&gt; setOfSets = ....
 * setOfSets.add(null);
 * setOfSets.add(someSetOfIntegers); // Where someSetsOfIntegers is a set containing 1 and 2
 *
 * IteratorIterator&lt;Integer&gt; it = new IteratorIterator&lt;Integer&gt;(setOfSets);
 * &lt;/code&gt;
 * </pre>
 * <p/>
 * This <code>it</code> iterator will return only 2 values ( 1 and 2 )
 *
 * @param <V>
 *            Type of the values over which this iterator iterates
 * @author Luno
 */
public class IteratorIterator<V> implements Iterator<V> {

	/**
	 * 1st Level iterator
	 */
	private Iterator<? extends Iterable<V>> firstLevelIterator;
	/**
	 * 2nd level iterator
	 */
	private Iterator<V> secondLevelIterator;

	/**
	 * Constructor of <tt>IteratorIterator</tt>
	 *
	 * @param itit
	 *            an Iterator that iterate over Iterable<Value>
	 */
	public IteratorIterator(Iterable<? extends Iterable<V>> itit) {
		this.firstLevelIterator = itit.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (secondLevelIterator != null && secondLevelIterator.hasNext()) {
			return true;
		}

		while (firstLevelIterator.hasNext()) {
			Iterable<V> iterable = firstLevelIterator.next();

			if (iterable != null) {
				secondLevelIterator = iterable.iterator();

				if (secondLevelIterator.hasNext()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns next value of collection.<br>
	 * If there is no next value, then {@link NoSuchElementException} thrown.
	 */
	@Override
	public V next() {
		if (secondLevelIterator == null || !secondLevelIterator.hasNext()) {
			throw new NoSuchElementException();
		}
		return secondLevelIterator.next();
	}

	/**
	 * <font color="red"><b>NOT IMPLEMENTED</b></font>
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("This operation is not supported.");
	}
}
