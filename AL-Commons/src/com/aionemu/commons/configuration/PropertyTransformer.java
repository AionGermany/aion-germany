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

package com.aionemu.commons.configuration;

import java.lang.reflect.Field;

/**
 * This insterface represents property transformer, each transformer should
 * implement it.
 * 
 * @author SoulKeeper
 * @param <T>
 *            Type of returned value
 */
public interface PropertyTransformer<T> {

	/**
	 * This method actually transforms value to object instance
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @return result of transformation
	 * @throws TransformationException
	 *             if something went wrong
	 */
	public T transform(String value, Field field) throws TransformationException;
}
