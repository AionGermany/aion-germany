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
package ai.instance.udasTemple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import ai.AggressiveNpcAI2;

/**
 * Lord Vallakhan BossScript
 *
 * @author Antraxx
 */
@AIName("lordvallakhan")
public class LordVallakhanAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private Future<?> taskEinsturz;
	private final List<Integer> spawnedNpc = new ArrayList<Integer>();
	private boolean canThink = true;

	@Override
	public boolean canThink() {
		return canThink;
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 99, 75, 30, 10 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 99:
						spawnHelpers(281384, 1, 3);
						break;
					case 75:
						spawnHelpers(281524, 2, 3);
						break;
					case 30:
						spawnHelpers(281524, 2, 3);
						break;
					case 10:
						spawnHelpers(281524, 2, 3);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void taskEinsturzStart() {
		taskEinsturz = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isHome.equals(true) || isAlreadyDead()) {
					taskEinsturzStop();
					return;
				}

				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500023, getObjectId(), 0, 0);

				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (isHome.equals(true) || isAlreadyDead()) {
							return;
						}
						canThink = false;
						setStateIfNot(AIState.WALKING);
						AI2Actions.useSkill(LordVallakhanAI2.this, 18599);
					}
				}, 2000);

				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// normal weiter
						if (isHome.equals(true) || isAlreadyDead()) {
							return;
						}
						canThink = true;
						Creature creature = getAggroList().getMostHated();
						if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
							setStateIfNot(AIState.FIGHT);
							think();
						}
						else {
							getMoveController().abortMove();
							getOwner().setTarget(creature);
							getOwner().getGameStats().renewLastAttackTime();
							getOwner().getGameStats().renewLastAttackedTime();
							getOwner().getGameStats().renewLastChangeTargetTime();
							getOwner().getGameStats().renewLastSkillTime();
							setStateIfNot(AIState.FIGHT);
							handleMoveValidate();
						}
					}
				}, 8500);
			}
		}, 60000, 60000);
	}

	private void taskEinsturzStop() {
		if ((taskEinsturz != null) && !taskEinsturz.isDone()) {
			taskEinsturz.cancel(true);
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500022, getObjectId(), 0, 0);
			taskEinsturzStart();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		synchronized (spawnedNpc) {
			removeHelpersSpawn();
		}
		addPercent();
		taskEinsturzStop();
		isHome.set(true);
		canThink = true;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		removeHelpersSpawn();
		percents.clear();
		taskEinsturzStop();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500024, getObjectId(), 0, 0);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		synchronized (spawnedNpc) {
			removeHelpersSpawn();
		}
		percents.clear();
	}

	/**
	 * Spawn Helpers in Range
	 *
	 * @param npcId
	 * @param count
	 * @param distance
	 */
	protected void spawnHelpers(int npcId, int count, int distance) {
		if (!isAlreadyDead()) {
			for (int i = 0; i < count; i++) {
				SpawnTemplate summon = null;
				summon = rndSpawnInRange(npcId, distance);
				VisibleObject npc = SpawnEngine.spawnObject(summon, getPosition().getInstanceId());
				PacketSendUtility.broadcastPacket(getOwner(), new SM_NPC_INFO((Npc) npc, ""));
				synchronized (spawnedNpc) {
					spawnedNpc.add(npc.getObjectId());
				}
			}
		}
	}

	/**
	 * Remove Helpers
	 */
	private void removeHelpersSpawn() {
		for (Integer object : spawnedNpc) {
			VisibleObject npc = World.getInstance().findVisibleObject(object);
			if (npc != null && npc.isSpawned()) {
				npc.getController().onDelete();
			}
		}
		spawnedNpc.clear();
	}

	protected SpawnTemplate rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x = (float) (Math.cos(Math.PI * direction) * distance);
		float y = (float) (Math.sin(Math.PI * direction) * distance);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x, getPosition().getY() + y, getPosition().getZ(), getPosition().getHeading());
	}
}
