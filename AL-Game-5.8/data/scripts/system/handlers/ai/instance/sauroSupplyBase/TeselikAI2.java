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
package ai.instance.sauroSupplyBase;

import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

@AIName("teselik")
public class TeselikAI2 extends AggressiveNpcAI2 {

	private int stage = 0;
	private boolean isStart = false;
	@SuppressWarnings("unused")
	private Future<?> enrageTask;

	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		wakeUp();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		wakeUp();
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void wakeUp() {
		isStart = true;
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 90 && stage < 1) {
			stage1();
			stage = 1;
		}
	}

	private void stage1() {
		int delay = 50000;
		if (isAlreadyDead() || !isStart)
			return;
		else {
			SkillEngine.getInstance().getSkill(getOwner(), 20657, 45, getOwner()).useNoAnimationSkill();
			switch (Rnd.get(1, 2)) {
				case 1:
					random();
					break;
				case 2:
					random2();
					break;
			}
			scheduleDelayStage1(delay);
		}
	}

	private void random() {
		if (!isAlreadyDead()) {
			enrageTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(284455, 472.12497f, 344.17401f, 181.625f, (byte) 0);
						spawn(284455, 485.1312f, 344.20688f, 181.875f, (byte) 0);
					}
				}
			}, 3000);
		}
	}

	private void random2() {
		if (!isAlreadyDead()) {
			enrageTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(284455, 472.12497f, 328.17401f, 181.625f, (byte) 0);
						spawn(284455, 487.1312f, 327.20688f, 181.875f, (byte) 0);
					}
				}
			}, 3000);
		}
	}

	private void scheduleDelayStage1(int delay) {
		if (!isStart && !isAlreadyDead())
			return;
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					stage1();
				}
			}, delay);
		}
	}

	private void despawnNpcs(int npcId) {
		List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		despawnNpcs(284455);
		isStart = false;
		stage = 0;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		despawnNpcs(284455);
		isStart = false;
		stage = 0;
	}

}
