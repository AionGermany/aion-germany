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
package com.aionemu.gameserver.model.templates.spawns.landingspecialspawns;

import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

public class LandingSpecialSpawnTemplate extends SpawnTemplate {

	private int id;
	private LandingSpecialStateType landingSpecialType;

	public LandingSpecialSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}

	public LandingSpecialSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}

	public int getId() {
		return id;
	}

	public LandingSpecialStateType getFStateType() {
		return landingSpecialType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFStateType(LandingSpecialStateType landingSpecialType) {
		this.landingSpecialType = landingSpecialType;
	}

	public final boolean isSpecialLandingSpawn() {
		return landingSpecialType.equals(LandingSpecialStateType.SPAWN);
	}

	public final boolean isSpecialLandingDespawn() {
		return landingSpecialType.equals(LandingSpecialStateType.DESPAWN);
	}
}
