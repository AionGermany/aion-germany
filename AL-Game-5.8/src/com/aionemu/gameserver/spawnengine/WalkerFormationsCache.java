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
package com.aionemu.gameserver.spawnengine;

import java.util.Map;

import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

/**
 * @author Rolandas
 */
class WalkerFormationsCache {

	private static Map<Integer, WorldWalkerFormations> formations = PlatformDependent.newConcurrentHashMap();

	private WalkerFormationsCache() {
	}

	protected static InstanceWalkerFormations getInstanceFormations(int worldId, int instanceId) {
		WorldWalkerFormations wwf = formations.get(worldId);
		if (wwf == null) {
			wwf = new WorldWalkerFormations();
			formations.put(worldId, wwf);
		}
		return wwf.getInstanceFormations(instanceId);
	}

	protected static void onInstanceDestroy(int worldId, int instanceId) {
		getInstanceFormations(worldId, instanceId).onInstanceDestroy();
	}
}
