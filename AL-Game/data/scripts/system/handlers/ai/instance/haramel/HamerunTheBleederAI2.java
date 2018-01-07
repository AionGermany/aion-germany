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
package ai.instance.haramel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @Author Majka Ajural
 */
@AIName("hamerun_the_bleeder")
public class HamerunTheBleederAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> taskAbilityDrain;
	private final int servant1Id = 282041; // Brainwashed Fighter
	private final int servant2Id = 282042; // Brainwashed MuMu Fighter
	protected List<Integer> percents = new ArrayList<Integer>();
	private boolean canThink = true;
	private Phase phase = Phase.ACTIVE;

	private enum Phase {

		ACTIVE,
		SERVANT,
	}

	@Override
	public boolean canThink() {
		return canThink;
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
			taskAbilityDrainStart();
		}

		callForHelp(10);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isHome.set(true);
		phase = Phase.ACTIVE;
		canThink = true;
		taskAbilityDrainStop();
		despawnServants();
		getEffectController().removeAllEffects(); // Remove all effects on owner
		addPercent();
	}

	@Override
	protected void handleDied() {
		super.handleDied();

		// Cancel owner's tasks
		taskAbilityDrainStop();

		// Destroys owner's spawns
		despawnServants();

		// Destroys owner
		AI2Actions.deleteOwner(this);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50 });
	}

	// Spawn both Servants of Hamerun
	private void spawnServants() {

		// Random shout of Hamerun to call his servants
		int[] hamerunServantShouts = { 1500102, 1500103 };
		NpcShoutsService.getInstance().sendMsg(getOwner(), hamerunServantShouts[Rnd.get(0, 1)], getObjectId(), 0, 0);

		// Spawns Brainwashed Fighter [ID: 282041] and Brainwashed MuMu Fighter [ID: 282042]
		WorldPosition npcPosition = getOwner().getPosition();
		float npcX = npcPosition.getX();
		float npcY = npcPosition.getY();
		float npcZ = npcPosition.getZ();

		Npc servant1spawn = (Npc) spawn(servant1Id, npcX - 2.0f, npcY + 2.0f, npcZ, (byte) 60); // Right
		Npc servant2spawn = (Npc) spawn(servant2Id, npcX + 2.0f, npcY - 2.0f, npcZ, (byte) 22); // Left

		// Sets target against player and attack it
		Creature player = getAggroList().getMostHated();
		servant1spawn.setTarget(player);
		servant1spawn.getController().attackTarget(player, 0);
		servant2spawn.setTarget(player);
		servant2spawn.getController().attackTarget(player, 0);

		// Sets owner's target as itself
		getOwner().setTarget(getOwner());
	}

	// Despawn servants; called on return home
	private void despawnServants() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(servant1Id));
		deleteNpcs(instance.getNpcs(servant2Id));
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private synchronized void checkPercentage(int hpPercentage) {

		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						// Spawning servants
						if (!isAlreadyDead() && phase.equals(Phase.ACTIVE)) {
							// Stops Ability Drain task to avoid to stop him without shield as actually two buffs cannot be got together
							taskAbilityDrainStop();

							taskHamerunHypnosisStart();
						}
						percents.remove(percent);
						break;
				}
			}
			break;
		}
	}

	private void taskAbilityDrainStop() {
		if ((taskAbilityDrain != null) && !taskAbilityDrain.isDone()) {
			taskAbilityDrain.cancel(true);
		}
	}

	private void taskAbilityDrainStart() {
		taskAbilityDrain = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					taskAbilityDrainStop();
				}
				else {
					if (!getOwner().isCasting() && phase.equals(Phase.ACTIVE)) {
						getOwner().getController().useSkill(19264, 11); // Ability Drain
					}
				}
			}
		}, 0, 10000);
	}

	// Starts when hp is below 50%
	private void taskHamerunHypnosisStart() {

		// Stops attacks
		phase = Phase.SERVANT;
		canThink = false;
		EmoteManager.emoteStopAttacking(getOwner());
		setStateIfNot(AIState.WALKING);

		// Casts the Hamerun's Hypnosis skill (ID: 19210)
		getOwner().getController().abortCast(); // Stops eventual cast skill
		SkillEngine.getInstance().getSkill(getOwner(), 19210, 1, getOwner()).useSkill();

		// Restarts fighting after the end of Hamerun's Hypnosis skill
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {

				if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.SERVANT)) {
					phase = Phase.ACTIVE;
					canThink = true;

					Creature player = getAggroList().getMostHated();
					if (player == null || player.getLifeStats().isAlreadyDead() || !getOwner().canSee(player)) {
						setStateIfNot(AIState.FIGHT);
						think();
					}
					else {
						getMoveController().abortMove();
						getOwner().setTarget(player);
						getOwner().getGameStats().renewLastAttackTime();
						getOwner().getGameStats().renewLastAttackedTime();
						getOwner().getGameStats().renewLastChangeTargetTime();
						getOwner().getGameStats().renewLastSkillTime();
						setStateIfNot(AIState.FIGHT);
						handleMoveValidate();
						taskAbilityDrainStart();
					}
				}
			}
		}, 18000);

		spawnServants();
	}
}
