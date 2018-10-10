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

package com.aionemu.commons.objects.filter;

/**
 * Object filter interface.
 * <p/>
 * Instances of classes that implement this interface are used to filter
 * objects.
 * <p/>
 * Created on: 31.03.2009 10:07:12
 * 
 * @author Aquanox
 * @param <T>
 */
public interface ObjectFilter<T> {

	/**
	 * Tests whether or not the specified <code>Object</code> should be included
	 * in a object list.
	 * 
	 * @param object
	 *            The object to be tested
	 * @return <code>true</code> if and only if <code>object</code> should be
	 *         included
	 */
	boolean acceptObject(T object);
}
