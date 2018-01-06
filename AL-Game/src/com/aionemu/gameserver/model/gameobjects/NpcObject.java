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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.HousingNpc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

/**
 * @author Rolandas
 */
public class NpcObject extends HouseObject<HousingNpc> {

	Npc npc = null;

	public NpcObject(House owner, int objId, int templateId) {
		super(owner, objId, templateId);
	}

	@Override
	public void onUse(Player player) {
		// TODO: Talk ?
	}

	@Override
	public synchronized void spawn() {
		super.spawn();
		if (npc == null) {
			HousingNpc template = getObjectTemplate();
			SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(getOwnerHouse().getWorldId(), template.getNpcId(), getX(), getY(), getZ(), getHeading());
			npc = (Npc) SpawnEngine.spawnObject(spawn, getOwnerHouse().getInstanceId());
		}
	}

	@Override
	public synchronized void onDespawn() {
		super.onDespawn();
		if (npc != null) {
			npc.getController().onDelete();
			npc = null;
		}
	}

	@Override
	public synchronized boolean canExpireNow() {
		if (npc == null) {
			return true;
		}
		return npc.getTarget() == null;
	}

	public int getNpcObjectId() {
		return npc == null ? 0 : npc.getObjectId();
	}
}
