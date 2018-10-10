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

package com.aionemu.commons.configuration.transformers;

import java.lang.reflect.Field;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;

/**
 * Transformes decimal that is represented as string to double
 * 
 * @author SoulKeeper
 */
public class DoubleTransformer implements PropertyTransformer<Double> {

	/**
	 * Shared instance of this transformer. It's thread-safe so no need of
	 * multiple instances
	 */
	public static final DoubleTransformer SHARED_INSTANCE = new DoubleTransformer();

	/**
	 * Transforms string to required double
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @return Double that represents transformed string
	 * @throws TransformationException
	 *             if something went wrong
	 */
	@Override
	public Double transform(String value, Field field) throws TransformationException {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			throw new TransformationException(e);
		}
	}
}
