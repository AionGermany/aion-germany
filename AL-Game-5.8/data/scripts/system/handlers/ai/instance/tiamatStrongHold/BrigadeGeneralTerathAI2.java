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
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

/**
 * @author Cheatkiller
 */
@AIName("brigadegeneralterath")
public class BrigadeGeneralTerathAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private List<Integer> percents = new ArrayList<Integer>();
	private Future<?> skillTask;
	private boolean canThink = true;
	private Npc aethericField;
	private boolean isGravityEvent;
	private boolean isFinalBuff;

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (aethericField == null) {
			aethericField = (Npc) spawn(730692, 1030.08f, 1030.08f, 1030.08f, (byte) 0);
			getPosition().getWorldMapInstance().getDoors().get(706).setOpen(false);
		}
		if (isHome.compareAndSet(true, false) && !isGravityEvent) {
			startSkillTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
		if (!isFinalBuff && getOwner().getLifeStats().getHpPercentage() <= 25) {
			isFinalBuff = true;
			AI2Actions.useSkill(this, 20942);
		}
	}

	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				}
				else {
					gravityDistortionEvent();
				}
			}
		}, 5000, 30000);
	}

	private void cancelskillTask() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
	}

	private void gravityDistortionEvent() {
		SkillEngine.getInstance().getSkill(getOwner(), 20739, 55, getOwner()).useNoAnimationSkill();
		spawn(283096, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		spawn(283097, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		spawn(283098, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 20741, 55, getOwner()).useNoAnimationSkill();
			}
		}, 5000);
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent && !isGravityEvent) {
				percents.remove(percent);
				canThink = false;
				isGravityEvent = true;
				cancelskillTask();
				spawn(283558, 1056.8f, 297.6f, 409.9f, (byte) 0);
				spawn(283558, 1002.07f, 297.4f, 409.85f, (byte) 0);
				SkillEngine.getInstance().getSkill(getOwner(), 20737, 55, getOwner()).useNoAnimationSkill();
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						EmoteManager.emoteStopAttacking(getOwner());
						setStateIfNot(AIState.WALKING);
						getOwner().getMoveController().moveToPoint(getOwner().getSpawn().getX(), getOwner().getSpawn().getY(), getOwner().getSpawn().getZ());
						WalkManager.startWalking(BrigadeGeneralTerathAI2.this);
						getOwner().setState(1);
						PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getOwner().getObjectId()));
					}
				}, 4000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(283110, 1029.9f, 297.26f, 409.08f, (byte) 0);
						spawn(283109, 1029.93f, 297.31f, 409.08f, (byte) 0);
					}
				}, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						WorldMapInstance instance = getPosition().getWorldMapInstance();
						deleteNpcs(instance.getNpcs(283558));
						deleteNpcs(instance.getNpcs(283110));
						deleteNpcs(instance.getNpcs(283109));
						getEffectController().removeEffect(20737);
						canThink = true;
						isGravityEvent = false;
						startSkillTask();
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
							setStateIfNot(AIState.WALKING);
							getOwner().setState(1);
							getOwner().getMoveController().moveToTargetObject();
							PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getOwner().getObjectId()));
						}
					}
				}, 30000);
			}
			break;
		}
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		cancelskillTask();
		aethericField.getController().onDelete();
		getPosition().getWorldMapInstance().getDoors().get(706).setOpen(true);
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		addPercent();
		isFinalBuff = false;
		cancelskillTask();
		isGravityEvent = false;
		canThink = true;
		isHome.set(true);
		aethericField.getController().onDelete();
		getPosition().getWorldMapInstance().getDoors().get(706).setOpen(true);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelskillTask();
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 90, 70, 50, 30, 25 });
	}

	@Override
	public boolean canThink() {
		return canThink;
	}
}
