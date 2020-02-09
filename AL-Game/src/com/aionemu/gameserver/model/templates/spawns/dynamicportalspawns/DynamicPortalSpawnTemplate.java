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
package com.aionemu.gameserver.model.templates.spawns.dynamicportalspawns;

import com.aionemu.gameserver.model.dynamicportal.DynamicPortalStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author Falke_34
 */
public class DynamicPortalSpawnTemplate extends SpawnTemplate {

	private int id;
	private DynamicPortalStateType dynamicPortalType;
	
	public DynamicPortalSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}
	
	public DynamicPortalSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}
	
	public int getId() {
		return id;
	}
	
	public DynamicPortalStateType getDStateType() {
		return dynamicPortalType;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDStateType(DynamicPortalStateType dynamicPortalType) {
		this.dynamicPortalType = dynamicPortalType;
	}
	
	public final boolean isDynamicPortalOpen() {
		return dynamicPortalType.equals(DynamicPortalStateType.OPEN);
	}
	
	public final boolean isDynamicPortalClosed() {
		return dynamicPortalType.equals(DynamicPortalStateType.CLOSED);
	}
}
