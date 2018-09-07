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
package com.aionemu.gameserver.model.templates.spawns.siegespawns;

import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SiegeSpawnType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author xTz
 */
public class SiegeSpawnTemplate extends SpawnTemplate {

	private int siegeId;
	private SiegeRace siegeRace;
	private SiegeSpawnType siegeSpawnType;
	private SiegeModType siegeModType;

	public SiegeSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}

	public SiegeSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int staticId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, staticId, fly);
	}

	public int getSiegeId() {
		return siegeId;
	}

	public SiegeRace getSiegeRace() {
		return siegeRace;
	}

	public SiegeSpawnType getSiegeSpawnType() {
		return siegeSpawnType;
	}

	public SiegeModType getSiegeModType() {
		return siegeModType;
	}

	public void setSiegeId(int siegeId) {
		this.siegeId = siegeId;
	}

	public void setSiegeRace(SiegeRace siegeRace) {
		this.siegeRace = siegeRace;
	}

	public void setSiegeSpawnType(SiegeSpawnType siegeSpawnType) {
		this.siegeSpawnType = siegeSpawnType;
	}

	public void setSiegeModType(SiegeModType siegeModType) {
		this.siegeModType = siegeModType;
	}

	public final boolean isPeace() {
		return siegeModType.equals(SiegeModType.PEACE);
	}

	public final boolean isSiege() {
		return siegeModType.equals(SiegeModType.SIEGE);
	}

	public final boolean isAssault() {
		return siegeModType.equals(SiegeModType.ASSAULT);
	}
}
