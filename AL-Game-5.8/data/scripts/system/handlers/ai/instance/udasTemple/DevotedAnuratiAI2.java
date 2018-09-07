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
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import ai.AggressiveNpcAI2;

/**
 * Devoted Anurati BossScript
 *
 * @author Antraxx
 */
@AIName("devotedanurati")
public class DevotedAnuratiAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private final List<Integer> spawnedNpc = new ArrayList<Integer>();
	private boolean canThink = true;

	@Override
	public boolean canThink() {
		return canThink;
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 80, 65, 45, 35, 25, 15, 5 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 80:
						spawnHelpersTask(80);
						break;
					case 65:
						spawnHelpersTask(65);
						break;
					case 45:
						spawnHelpersTask(45);
						break;
					case 35:
						spawnHelpersTask(35);
						break;
					case 25:
						spawnHelpersTask(25);
						break;
					case 15:
						spawnHelpersTask(15);
						break;
					case 5:
						spawnHelpersTask(5);
						break;
				}
				percents.remove(percent);
				break;
			}
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
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500031, getObjectId(), 0, 0);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		removeHelpersSpawn();
		addPercent();
		isHome.set(true);
		canThink = true;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		removeHelpersSpawn();
		percents.clear();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500033, getObjectId(), 0, 0);
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
	 * @param percent
	 */
	private void spawnHelpersTask(final int percent) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500032, getObjectId(), 0, 0);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isHome.equals(true) || isAlreadyDead()) {
					return;
				}
				canThink = false;
				setStateIfNot(AIState.WALKING);
				AI2Actions.useSkill(DevotedAnuratiAI2.this, 18745); // umwerfender schock
			}
		}, 2000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
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
		}, 6000);

	}

	/**
	 * Spawn Anuratis Helpers
	 * <p/>
	 * movement must be better adapted
	 *
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param h
	 * @param count
	 * @param action
	 */
	protected void spawnHelpers(int npcId, float x, float y, float z, byte h, int count, int action) {
		if (isHome.equals(true) || isAlreadyDead()) {
			return;
		}
		for (int i = 0; i < count; i++) {
			SpawnTemplate summon = SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, x, y, z, h);
			VisibleObject npc = SpawnEngine.spawnObject(summon, getPosition().getInstanceId());
			PacketSendUtility.broadcastPacket(getOwner(), new SM_NPC_INFO((Npc) npc, ""));
			addHelpersSpawn(npc.getObjectId());
			switch (action) {
				case 1:
					moveToForward((Npc) npc, 633.7391f, 455.4411f, 135.82466f, false);
					break;
				case 2:
					moveToForward((Npc) npc, 639.51807f, 455.4411f, 135.82466f, false);
					break;
				case 3:
					moveToForward((Npc) npc, 636.39575f, 455.4411f, 135.82466f, false);
					break;
			}
		}
	}

	protected void addHelpersSpawn(int objId) {
		synchronized (spawnedNpc) {
			spawnedNpc.add(objId);
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

	/**
	 * @param despawn
	 */
	private void moveToForward(final Npc npc, float x, float y, float z, boolean despawn) {
		setStateIfNot(AIState.WALKING);
		npc.setState(CreatureState.ACTIVE.getId());
		npc.getMoveController().moveToPoint(x, y, z);
		npc.setVisualState(CreatureVisualState.VISIBLE);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
}
