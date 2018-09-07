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
package ai.instance.tiamatStrongHold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

/**
 * @author Cheatkiller
 * @modified Luzien
 */
@AIName("brigadegeneraltahabata")
public class BrigadeGeneralTahabataAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private AtomicBoolean isEndPiercingStrike = new AtomicBoolean(true);
	private Future<?> piercingStrikeTask;
	private AtomicBoolean isEndFireStorm = new AtomicBoolean(true);
	private Future<?> fireStormTask;
	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false) && getPosition().getWorldMapInstance().getDoors().get(610) != null) {
			getPosition().getWorldMapInstance().getDoors().get(610).setOpen(false);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void startPiercingStrikeTask() {
		piercingStrikeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelPiercingStrike();
				}
				else {
					startPiercingStrikeEvent();
				}
			}
		}, 15000, 20000);
	}

	private void startFireStormTask() {
		fireStormTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelFireStorm();
				}
				else {
					startFireStormEvent();
				}
			}
		}, 10000, 20000);
	}

	private void startFireStormEvent() {
		if (getPosition().getWorldMapInstance().getNpc(283045) == null) {
			AI2Actions.useSkill(this, 20758);
			rndSpawn(283045, Rnd.get(1, 4));
		}
	}

	private void startPiercingStrikeEvent() {
		teleportRandomPlayer();
		Skill skill = SkillEngine.getInstance().getSkill(getOwner(), 20754, 60, getOwner());
		if (skill != null) {
			skill.useNoAnimationSkill();
			if (!skill.getEffectedList().isEmpty()) {
				useMultiSkill();
			}
		}
	}

	private void useMultiSkill() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.useSkill(BrigadeGeneralTahabataAI2.this, 20755);
			}
		}, 3000);
	}

	private void teleportRandomPlayer() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 40)) {
				players.add(player);
			}
		}
		Player target = !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
		AI2Actions.targetCreature(this, target);
		World.getInstance().updatePosition(getOwner(), target.getX(), target.getY(), target.getZ(), (byte) 0);
		PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
	}

	private void cancelPiercingStrike() {
		if (piercingStrikeTask != null && !piercingStrikeTask.isCancelled()) {
			piercingStrikeTask.cancel(true);
		}
	}

	private void cancelFireStorm() {
		if (fireStormTask != null && !fireStormTask.isCancelled()) {
			fireStormTask.cancel(true);
		}
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 96:
						lavaEruptionEvent(283116);
						if (isEndPiercingStrike.compareAndSet(true, false)) {
							startPiercingStrikeTask();
						}
						break;
					case 75:
						AI2Actions.useSkill(this, 20761);
						if (isEndFireStorm.compareAndSet(true, false)) {
							startFireStormTask();
						}
						break;
					case 60:
						AI2Actions.useSkill(this, 20761);
						break;
					case 55:
						lavaEruptionEvent(283118);
						break;
					case 40:
					case 25:
						AI2Actions.useSkill(this, 20761);
						break;
					case 20:
						cancelFireStorm();
						cancelPiercingStrike();
						lavaEruptionEvent(283120);
						break;
					case 10:
						AI2Actions.useSkill(this, 20942);
						break;
					case 7:
						AI2Actions.useSkill(this, 20883);
						spawn(283045, 679.88f, 1068.88f, 497.88f, (byte) 0);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void lavaEruptionEvent(final int floorId) {
		AI2Actions.targetSelf(BrigadeGeneralTahabataAI2.this);
		AI2Actions.useSkill(BrigadeGeneralTahabataAI2.this, 20756);
		if (getPosition().getWorldMapInstance().getNpc(283051) == null) {
			spawn(283051, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		}
		spawnfloor(floorId);
	}

	private void spawnfloor(final int floor) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(floor, 679.88f, 1068.88f, 497.88f, (byte) 0);
				spawn(floor + 1, 679.88f, 1068.88f, 497.88f, (byte) 0);
			}
		}, 10000);
	}

	private void rndSpawn(int npcId, int count) {
		for (int i = 0; i < count; i++) {
			SpawnTemplate template = rndSpawnInRange(npcId, 10);
			SpawnEngine.spawnObject(template, getPosition().getInstanceId());
		}
	}

	private SpawnTemplate rndSpawnInRange(int npcId, int dist) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * dist);
		float y1 = (float) (Math.sin(Math.PI * direction) * dist);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), getPosition().getHeading());
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 96, 75, 60, 55, 40, 25, 20, 10, 7 });
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	private void deleteAdds() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(283116));
		deleteNpcs(instance.getNpcs(283118));
		deleteNpcs(instance.getNpcs(283120));
		deleteNpcs(instance.getNpcs(283051));
		deleteNpcs(instance.getNpcs(283045));
		deleteNpcs(instance.getNpcs(283257));
		deleteNpcs(instance.getNpcs(283237));
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		isHome.set(true);
		getPosition().getWorldMapInstance().getDoors().get(610).setOpen(true);
		super.handleBackHome();
		getEffectController().removeEffect(20942);
		deleteAdds();
		cancelPiercingStrike();
		cancelFireStorm();
		isEndPiercingStrike.set(true);
		isEndFireStorm.set(true);
	}

	@Override
	protected void handleDied() {
		percents.clear();
		getPosition().getWorldMapInstance().getDoors().get(610).setOpen(true);
		super.handleDied();
		deleteAdds();
		cancelPiercingStrike();
		cancelFireStorm();
	}
}
