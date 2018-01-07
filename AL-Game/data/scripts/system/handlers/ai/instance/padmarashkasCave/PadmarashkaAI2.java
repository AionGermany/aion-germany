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
package ai.instance.padmarashkasCave;

import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI2;

/**
 * @author Ritsu, Luzien
 */
@AIName("padmarashka")
public class PadmarashkaAI2 extends AggressiveNpcAI2 {

	private int stage = 0;
	private boolean isStart = false;
	private boolean canThink = false;
	private Future<?> mainSkillTask;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		putToSleep();
	}

	@Override
	protected void handleAttack(Creature creature) {
		if (!this.getEffectController().hasAbnormalEffect(19186) && !isStart) {
			wakeUp();
		}
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		if (!this.getEffectController().hasAbnormalEffect(19186) && !isStart) {
			wakeUp();
		}
		super.handleCreatureAggro(creature);
	}

	private void wakeUp() {
		canThink = true;
		isStart = true;
		scheduleSpawnEntrance();
		startMainSkillTask();
	}

	private void startMainSkillTask() {
		mainSkillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), Rnd.get(2) == 0 ? 20401 : 20093, 48, getTarget()).useNoAnimationSkill();
			}
		}, 10000, 20000);
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 95 && stage < 1) {
			stage1();
			stage = 1;
		}
		if (hpPercentage <= 50 && stage < 2) {
			stage2();
			stage = 2;
		}
		if (hpPercentage <= 25 && stage < 3) {
			stage3();
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1401215); // Cave crumble
			stage = 3;
		}
	}

	private void scheduleSpawnEntrance() {
		int delay = 60000;
		if (isAlreadyDead() || !isStart) {
			return;
		}
		else {
			getOwner().clearAttackedCount();
			int count = Rnd.get(1, 3);
			for (int i = 0; i < count; i++) {
				int npcId = 282792 + Rnd.get(5);
				attackPlayer((Npc) spawn(npcId, 531.465f + Rnd.get(-5, 5), 316.921f + Rnd.get(-5, 5), 65.351f, (byte) 105));
			}
			scheduleDelayEntrance(delay);
		}
	}

	private void attackPlayer(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				npc.setTarget(getTarget());
				((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
				npc.setState(1);
				npc.getMoveController().moveToTargetObject();
				PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
			}
		}, 1000);
	}

	private void stage1() {
		int delay = 60000;
		if (isAlreadyDead() || !isStart) {
			return;
		}
		else {
			SkillEngine.getInstance().getSkill(getOwner(), 19177, 55, getOwner()).useNoAnimationSkill();
			switch (Rnd.get(1, 10)) {
				case 1:
					spawn(282613, 575.064f, 163.573f, 66.01f, (byte) 0);
					break;
				case 2:
					spawn(282613, 587.361f, 165.187f, 66.01f, (byte) 0);
					break;
				case 3:
					spawn(282613, 577.463f, 155.553f, 66.01f, (byte) 0);
					break;
				case 4:
					spawn(282613, 582.064f, 160.785f, 66.01f, (byte) 0);
					break;
				case 5:
					spawn(282613, 586.160f, 157.214f, 66.01f, (byte) 0);
					break;
				case 6:
					spawn(282613, 571.436f, 150.558f, 66.01f, (byte) 0);
					break;
				case 7:
					spawn(282613, 577.574f, 150.415f, 66.01f, (byte) 0);
					break;
				case 8:
					spawn(282613, 581.354f, 151.640f, 66.01f, (byte) 0);
					break;
				case 9:
					spawn(282613, 589.382f, 149.758f, 66.01f, (byte) 0);
					break;
				case 10:
					spawn(282613, 583.469f, 145.809f, 66.01f, (byte) 0);
					break;

			}
			scheduleDelayStage1(delay);
			if (getPosition().getWorldMapInstance().getNpc(218675) == null) {
				spawn(218675, 573.413f, 179.518f, 66.220f, (byte) 30);
			}
			else if (getPosition().getWorldMapInstance().getNpc(218676) == null) {
				spawn(218676, 581.827f, 179.587f, 66.250f, (byte) 30);
			}
		}
	}

	private void stage2() {
		int delay = 120000;
		if (isAlreadyDead() || !isStart) {
			return;
		}
		else {
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1401214); // Huge egg is revealed
			switch (Rnd.get(1, 4)) {
				case 1:
					spawn(282614, 510.12497f, 250.17401f, 66.625f, (byte) 0);
					break;
				case 2:
					spawn(282614, 530.1312f, 170.20688f, 65.875f, (byte) 0);
					break;
				case 3:
					spawn(282614, 610.32495f, 210.19055f, 66.125f, (byte) 0);
					break;
				case 4:
					spawn(282614, 590.1816f, 290.31775f, 66.75f, (byte) 0);
					break;
			}
			scheduleDelayStage2(delay);
		}
	}

	private void stage3() // Spawn Rock
	{
		SkillEngine.getInstance().getSkill(getOwner(), 19179, 55, getOwner()).useNoAnimationSkill();
		spawn(282140, 520.99585f, 270.23776f, 66.25f, (byte) 0);
		spawn(282140, 506.8137f, 238.60612f, 66.57414f, (byte) 0);
		spawn(282140, 495.94504f, 218.84671f, 67.58238f, (byte) 0);
		spawn(282140, 502.39307f, 196.61835f, 67.5513f, (byte) 0);
		spawn(282140, 510.88928f, 178.31491f, 66.869156f, (byte) 0);
		spawn(282140, 530.8308f, 160.27686f, 65.926926f, (byte) 0);
		spawn(282140, 551.6416f, 145.72856f, 67.93133f, (byte) 0);
		spawn(282140, 567.2481f, 162.51135f, 66.09399f, (byte) 0);
		spawn(282140, 585.17523f, 188.57863f, 66.125f, (byte) 0);
		spawn(282140, 611.51733f, 186.78618f, 66.25f, (byte) 0);
		spawn(282140, 624.74023f, 205.0654f, 66.255615f, (byte) 0);
		spawn(282140, 627.2288f, 228.05437f, 66.3268f, (byte) 0);
		spawn(282140, 619.18396f, 258.662f, 69.37874f, (byte) 0);
		spawn(282140, 604.1967f, 284.47342f, 66.71312f, (byte) 0);
		spawn(282140, 589.62775f, 293.01245f, 66.75f, (byte) 0);
		spawn(282140, 599.13367f, 264.5496f, 66.25f, (byte) 0);
		spawn(282140, 517.9813f, 220.19554f, 66.86278f, (byte) 0);
		spawn(282140, 533.5666f, 194.63768f, 66.125f, (byte) 0);
	}

	private void scheduleDelayStage2(int delay) {
		if (!isStart && !isAlreadyDead()) {
			return;
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					stage2();
				}
			}, delay);
		}
	}

	private void scheduleDelayStage1(int delay) {
		if (!isStart && !isAlreadyDead()) {
			return;
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					stage1();
				}
			}, delay);
		}
	}

	private void scheduleDelayEntrance(int delay) {
		if (!isStart && !isAlreadyDead()) {
			return;
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					scheduleSpawnEntrance();
				}
			}, delay);
		}
	}

	private void putToSleep() {
		SkillEngine.getInstance().getSkill(getOwner(), 19186, 55, getOwner()).useNoAnimationSkill();
		this.getEffectController().setAbnormal(AbnormalState.SLEEP.getId());
		canThink = false;
	}

	private void spawnPadmaProtector() {
		spawn(218670, 533.107f, 247.685f, 66.761f, (byte) 45);
		spawn(218671, 581.092f, 265.049f, 66.843f, (byte) 15);
		spawn(218673, 547.838f, 264.624f, 66.875f, (byte) 45);
		spawn(218674, 595.748f, 248.045f, 66.250f, (byte) 15);
	}

	private void deSpawnNpcs() {
		// Despawn Stage 3
		despawnNpcs(282140);
		// Despawn Stage 2
		despawnNpcs(282614);
		despawnNpcs(282712);
		despawnNpcs(282620);
		// Despawn Stage 1
		despawnNpcs(282613);
		despawnNpcs(218675);
		despawnNpcs(218676);
		despawnNpcs(282715);
		despawnNpcs(282716);
		// Despawn Entrance mobs
		despawnNpcs(282792);
		despawnNpcs(282793);
		despawnNpcs(282794);
		despawnNpcs(282795);
		despawnNpcs(282796);
		// Despawn Padma Protector
		despawnNpcs(218670);
		despawnNpcs(218671);
		despawnNpcs(218673);
		despawnNpcs(218674);
	}

	private void despawnNpcs(int npcId) {
		List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void cancelTask() {
		if (mainSkillTask != null && !mainSkillTask.isDone()) {
			mainSkillTask.cancel(true);
		}
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isStart = false;
		stage = 0;
		cancelTask();
		deSpawnNpcs();
		putToSleep();
		spawnPadmaProtector();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
		isStart = false;
		stage = 0;
		deSpawnNpcs();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTask();
		deSpawnNpcs();
	}

	@Override
	public boolean canThink() {
		return canThink;
	}
}
