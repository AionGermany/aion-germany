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
package com.aionemu.gameserver.geoEngine.math;

import com.aionemu.gameserver.configs.main.GeoDataConfig;

import javolution.context.ObjectFactory;
import javolution.lang.Reusable;

/**
 * @author MrPoke
 */
public class Array3f implements Reusable {

	@SuppressWarnings("rawtypes")
	private static final ObjectFactory FACTORY = new ObjectFactory() {

		@Override
		public Object create() {
			return new Array3f();
		}
	};
	public float a = 0;
	public float b = 0;
	public float c = 0;

	@Override
	public void reset() {
		a = 0;
		b = 0;
		c = 0;
	}

	/**
	 * Returns a new, preallocated or {@link #recycle recycled} text builder (on the stack when executing in a {@link javolution.context.StackContext StackContext}).
	 *
	 * @return a new, preallocated or recycled text builder instance.
	 */
	public static Array3f newInstance() {
		if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
			return (Array3f) FACTORY.object();
		}
		else {
			return new Array3f();
		}
	}

	/**
	 * Recycles a text builder {@link #newInstance() instance} immediately (on the stack when executing in a {@link javolution.context.StackContext StackContext}).
	 */
	@SuppressWarnings("unchecked")
	public static void recycle(Array3f instance) {
		if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
			FACTORY.recycle(instance);
		}
		else {
			instance = null;
		}
	}
}
