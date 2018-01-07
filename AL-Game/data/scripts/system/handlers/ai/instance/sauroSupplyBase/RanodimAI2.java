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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

@AIName("ranodim")
public class RanodimAI2 extends AggressiveNpcAI2 {

	private int stage = 0;
	private boolean isStart = false;

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
		if (hpPercentage <= 50 && stage < 1) {
			stage1();
			stage = 1;
		}
	}

	private void stage1() {
		int delay = 25000;
		if (isAlreadyDead() || !isStart)
			return;
		else {
			SkillEngine.getInstance().getSkill(getOwner(), 20702, 45, getOwner()).useNoAnimationSkill();
			scheduleDelayStage1(delay);
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

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isStart = false;
		stage = 0;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		isStart = false;
		stage = 0;
	}
}
