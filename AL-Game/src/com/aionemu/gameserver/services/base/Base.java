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
package com.aionemu.gameserver.services.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.configs.main.BaseConfig;
import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.MapRegion;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * @author Source
 */
public class Base<BL extends BaseLocation> {

	private Future<?> startAssault, stopAssault;
	private final BL baseLocation;
	private List<Race> list = new ArrayList<>();
	private final BossDeathListener bossDeathListener = new BossDeathListener(this);
	private List<Npc> attackers = new ArrayList<>();
	private final AtomicBoolean finished = new AtomicBoolean();
	private boolean started;
	private Npc boss, flag;

	public Base(BL baseLocation) {
		list.add(Race.ASMODIANS);
		list.add(Race.ELYOS);
		list.add(Race.NPC);
		this.baseLocation = baseLocation;
	}

	public final void start() {

		boolean doubleStart = false;

		synchronized (this) {
			if (started) {
				doubleStart = true;
			}
			else {
				started = true;
			}
		}

		if (!doubleStart) {
			spawn();
		}
	}

	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			if (getBoss() != null) {
				rmvBossListener();
			}
			despawn();
		}
	}

	private List<SpawnGroup2> getBaseSpawns() {
		List<SpawnGroup2> spawns = DataManager.SPAWNS_DATA2.getBaseSpawnsByLocId(getId());

		if (spawns == null) {
			throw new NullPointerException("No spawns for base:" + getId());
		}

		return spawns;
	}

	protected void spawn() {
		for (SpawnGroup2 group : getBaseSpawns()) {
			for (SpawnTemplate spawn : group.getSpawnTemplates()) {
				final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
				if (template.getBaseRace().equals(getRace())) {
					if (template.getHandlerType() == null) {
						Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
						NpcTemplate npcTemplate = npc.getObjectTemplate();
						if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG)) {
							setFlag(npc);
							MapRegion mr = npc.getPosition().getMapRegion();
							mr.activate();
						}
					}
				}
			}
		}

		delayedAssault();
		delayedSpawn(getRace());
	}

	private void delayedAssault() {
		startAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				chooseAttackersRace();
				sendLDF4AdvanceMsgKiller(getId()); // Akaron Base Message
			}
		}, Rnd.get(BaseConfig.ASSAULT_MIN_DELAY, BaseConfig.ASSAULT_MAX_DELAY) * 60000); // Randomly every 15 - 20 min start assault
	}

	// Akaron Base Message
	public boolean sendLDF4AdvanceMsgKiller(int id) {
		switch (id) {
			case 100:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v13);
					}
				});
				return true;
			case 101:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v04);
					}
				});
				return true;
			case 102:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v12);
					}
				});
				return true;
			case 103:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v03);
					}
				});
				return true;
			case 104:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v06);
					}
				});
				return true;
			case 105:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v05);
					}
				});
				return true;
			case 106:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v01);
					}
				});
				return true;
			case 107:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v09);
					}
				});
				return true;
			case 108:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v11);
					}
				});
				return true;
			case 109:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v10);
					}
				});
				return true;
			case 110:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v07);
					}
				});
				return true;
			case 111:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v02);
					}
				});
				return true;
			case 112:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v08);
					}
				});
				return true;
			default:
				return false;
		}
	}

	private void delayedSpawn(final Race race) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (getRace().equals(race) && getBoss() == null) {
					spawnBoss();
				}
			}
		}, Rnd.get(BaseConfig.BOSS_SPAWN_MIN_DELAY, BaseConfig.BOSS_SPAWN_MAX_DELAY) * 60000); // Boss spawn between 30 min and 4 hours delay on retail
	}

	protected void spawnBoss() {
		for (SpawnGroup2 group : getBaseSpawns()) {
			for (SpawnTemplate spawn : group.getSpawnTemplates()) {
				final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
				if (template.getBaseRace().equals(getRace())) {
					if (template.getHandlerType() != null && template.getHandlerType().equals(SpawnHandlerType.BOSS)) {
						Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
						setBoss(npc);
						addBossListeners();
					}
				}
			}
		}
	}

	protected void chooseAttackersRace() {
		AtomicBoolean next = new AtomicBoolean(Math.random() < 0.5);
		for (Race race : list) {
			if (race == null) {
				throw new NullPointerException("Base:" + race + " race is null chooseAttackersRace!");
			}
			else if (!race.equals(getRace())) {
				if (next.compareAndSet(true, false)) {
					continue;
				}
				spawnAttackers(race);
			}
		}
	}

	public void spawnAttackers(Race race) {
		if (getFlag() == null && !isNoFlag(getId())) {
			throw new NullPointerException("Base:" + getId() + " flag is null!");
		}
		else if (!getFlag().getPosition().getMapRegion().isMapRegionActive()) {
			// 20% chance to capture base in not active region by invaders assault
			Race CurrentRace = getFlag().getRace();
			if (Math.random() < 0.2 && !race.equals(CurrentRace)) {
				BaseService.getInstance().capture(getId(), race);
			}
			else {
				// Next attack
				delayedAssault();
			}
			return;
		}

		if (!isAttacked()) {
			despawnAttackers();

			for (SpawnGroup2 group : getBaseSpawns()) {
				for (SpawnTemplate spawn : group.getSpawnTemplates()) {
					final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
					if (template.getBaseRace().equals(race)) {
						if (template.getHandlerType() != null && template.getHandlerType().equals(SpawnHandlerType.ATTACKER)) {
							Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
							getAttackers().add(npc);
						}
					}
				}
			}

			// Since patch 4.7 in kaldor are siege important bases that only have balaur attackers for back occupying.
			if (getAttackers().isEmpty() && !isOnlyBalaur(getId())) {
				throw new NullPointerException("No attackers was found for base:" + getId());
			}
			else {
				stopAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnAttackers();

						// Next attack
						delayedAssault();
					}
				}, 5 * 60000); // After 5 min attackers despawned
			}
		}
	}

	public boolean isOnlyBalaur(int id) {
		switch (id) {
			case 90: // Stonereach Outpost
				return true;
			case 91: // Flamecrest Outpost
				return true;
			case 134: // Rattlefrost Outpost
				return true;
			case 135: // Sliversleet Outpost
				return true;
			case 136: // Coldforge Outpost
				return true;
			case 137: // Shimmerfrost Outpost
				return true;
			case 138: // Icehowl Outpost
				return true;
			case 139: // Chillhaunt Outpost
				return true;
			case 140: // Wildersage Artifact Outpost
				return true;
			case 141: // Dauntless Artifact Outpost
				return true;
			case 142: // Anchorbrak Artifact Outpost
				return true;
			case 143: // Brokenblade Artifact Outpost
				return true;
			case 144: // Sootguzzle Outpost
				return true;
			case 145: // Flameruin Outpost
				return true;
			case 146: // Stokebellow Outpost
				return true;
			case 147: // Blazerack Outpost
				return true;
			case 148: // Smoldergeist Outpost
				return true;
			case 149: // Moltenspike Outpost
				return true;
			default:
				return false;
		}
	}

	public boolean isNoFlag(int id) {
		switch (id) {
			case 140: // Wildersage Artifact Outpost
				return true;
			case 141: // Dauntless Artifact Outpost
				return true;
			case 142: // Anchorbrak Artifact Outpost
				return true;
			case 143: // Brokenblade Artifact Outpost
				return true;
			default:
				return false;
		}
	}

	public boolean isAttacked() {
		for (Npc attacker : getAttackers()) {
			if (!attacker.getLifeStats().isAlreadyDead()) {
				return true;
			}
		}
		return false;
	}

	protected void despawn() {
		setFlag(null);

		FastList<Npc> spawned = World.getInstance().getBaseSpawns(getId());
		if (spawned != null) {
			for (Npc npc : spawned) {
				npc.getController().onDelete();
			}
		}

		if (startAssault != null) {
			startAssault.cancel(true);
		}

		if (stopAssault != null) {
			stopAssault.cancel(true);
			despawnAttackers();
		}
	}

	protected void despawnAttackers() {
		NpcController controller;
		for (Npc attacker : getAttackers()) {
			controller = attacker.getController();
			if (null != controller)// despawn fix
			{
				controller.cancelTask(TaskId.RESPAWN);
				controller.onDelete();
			}
		}
		getAttackers().clear();
	}

	protected void addBossListeners() {
		AbstractAI ai = (AbstractAI) getBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.addCallback(getBossListener());
	}

	protected void rmvBossListener() {
		AbstractAI ai = (AbstractAI) getBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.removeCallback(getBossListener());
	}

	public Npc getFlag() {
		return flag;
	}

	public void setFlag(Npc flag) {
		this.flag = flag;
	}

	public Npc getBoss() {
		return boss;
	}

	public void setBoss(Npc boss) {
		this.boss = boss;
	}

	public BossDeathListener getBossListener() {
		return bossDeathListener;
	}

	public boolean isFinished() {
		return finished.get();
	}

	public BL getBaseLocation() {
		return baseLocation;
	}

	public int getId() {
		return baseLocation.getId();
	}

	public Race getRace() {
		return baseLocation.getRace();
	}

	public void setRace(Race race) {
		baseLocation.setRace(race);
	}

	public List<Npc> getAttackers() {
		return attackers;
	}
}
