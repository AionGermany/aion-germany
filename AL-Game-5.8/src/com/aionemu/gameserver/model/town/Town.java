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
package com.aionemu.gameserver.model.town;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOWNS_LIST;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author ViAl
 */
public class Town {

	private int id;
	private int level;
	private int points;
	private Timestamp levelUpDate;
	private Race race;
	private PersistentState persistentState;
	private List<Npc> spawnedNpcs;

	/**
	 * Used only from DAO.
	 *
	 * @param id
	 * @param level
	 * @param points
	 */
	public Town(int id, int level, int points, Race race, Timestamp levelUpDate) {
		this.id = id;
		this.level = level;
		this.points = points;
		this.levelUpDate = levelUpDate;
		this.race = race;
		this.persistentState = PersistentState.UPDATED;
		this.spawnedNpcs = new ArrayList<Npc>();
		spawnNewObjects();
	}

	/**
	 * Used for initial import from house templates.
	 *
	 * @param id
	 */
	public Town(int id, Race race) {
		this(id, 1, 0, race, new Timestamp(60000));
		this.persistentState = PersistentState.NEW;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getPoints() {
		return points;
	}

	public synchronized void increasePoints(int amount) {
		switch (this.level) {
			case 1:
				if (this.points + amount >= 1000) {
					increaseLevel();
				}
				break;
			case 2:
				if (this.points + amount >= 2000) {
					increaseLevel();
				}
				break;
			case 3:
				if (this.points + amount >= 3000) {
					increaseLevel();
				}
				break;
			case 4:
				if (this.points + amount >= 4000) {
					increaseLevel();
				}
				break;
		}
		this.points += amount;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	private void increaseLevel() {
		this.level++;
		this.levelUpDate.setTime(System.currentTimeMillis());
		broadcastUpdate();
		despawnOldObjects();
		spawnNewObjects();
	}

	private void broadcastUpdate() {
		Map<Integer, Town> data = new HashMap<Integer, Town>(1);
		data.put(this.id, this);
		final SM_TOWNS_LIST packet = new SM_TOWNS_LIST(data);
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.getRace() == race) {
					PacketSendUtility.sendPacket(player, packet);
				}
			}
		});
	}

	private void spawnNewObjects() {
		List<Spawn> newSpawns = DataManager.TOWN_SPAWNS_DATA.getSpawns(this.id, this.level);
		int worldId = DataManager.TOWN_SPAWNS_DATA.getWorldIdForTown(this.id);
		for (Spawn spawn : newSpawns) {
			for (SpawnSpotTemplate sst : spawn.getSpawnSpotTemplates()) {
				SpawnTemplate spawnTemplate = SpawnEngine.addNewSpawn(worldId, spawn.getNpcId(), sst.getX(), sst.getY(), sst.getZ(), sst.getHeading(), spawn.getRespawnTime());
				spawnTemplate.setStaticId(sst.getStaticId());
				spawnTemplate.setRandomWalk(0);
				VisibleObject object = SpawnEngine.spawnObject(spawnTemplate, 1);
				if (object instanceof Npc) {
					((Npc) object).setTownId(this.id);
					spawnedNpcs.add((Npc) object);
				}
			}
		}
	}

	private void despawnOldObjects() {
		for (Npc npc : spawnedNpcs) {
			npc.getController().delete();
		}
		spawnedNpcs.clear();
	}

	public Race getRace() {
		return this.race;
	}

	public Timestamp getLevelUpDate() {
		return levelUpDate;
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState state) {
		if (this.persistentState == PersistentState.NEW && state == PersistentState.UPDATE_REQUIRED) {
			return;
		}
		else {
			this.persistentState = state;
		}
	}
}
