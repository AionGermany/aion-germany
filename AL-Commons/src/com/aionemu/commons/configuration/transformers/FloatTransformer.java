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
 * Thransforms string that represents float in decimal format
 * 
 * @author SoulKeeper
 */
public class FloatTransformer implements PropertyTransformer<Float> {

	/**
	 * Shared instance of this transformer. It's thread-safe so no need of
	 * multiple instances
	 */
	public static final FloatTransformer SHARED_INSTANCE = new FloatTransformer();

	/**
	 * Thransforms string to float
	 * 
	 * @param value
	 *            value that will be transformed
	 * @param field
	 *            value will be assigned to this field
	 * @return Float that represents value
	 * @throws TransformationException
	 *             if something went wrong
	 */
	@Override
	public Float transform(String value, Field field) throws TransformationException {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			throw new TransformationException(e);
		}
	}
}
