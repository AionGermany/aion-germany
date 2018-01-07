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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

@AIName("rohuka")
public class CaptainRohukaAI2 extends AggressiveNpcAI2 {

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
		checkPercentage(getLifeStats().getHpPercentage());
		wakeUp();
	}

	private void wakeUp() {
		isStart = true;
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 50 && stage < 1) {
			stage1();
			stage = 1;
		}
		if (hpPercentage <= 45 && stage < 2) {
			stage2();
			stage = 2;
		}
		if (hpPercentage <= 25 && stage < 3) {
			stage3();
			stage = 3;
		}
	}

	private void stage1() {
		if (isAlreadyDead() || !isStart)
			return;
		else {
			SkillEngine.getInstance().getSkill(getOwner(), 21135, 50, getOwner()).useNoAnimationSkill();
		}
	}

	private void stage2() {
		int delay = 35000;
		if (isAlreadyDead() || !isStart)
			return;
		else {
			skill();
			scheduleDelayStage2(delay);
		}
	}

	private void skill() {
		SkillEngine.getInstance().getSkill(getOwner(), 18158, 100, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 18160, 100, getOwner()).useNoAnimationSkill();
			}
		}, 4000);
	}

	private void scheduleDelayStage2(int delay) {
		if (!isStart && !isAlreadyDead())
			return;
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					stage2();
				}
			}, delay);
		}
	}

	private void stage3() {
		int delay = 15000;
		if (isAlreadyDead() || !isStart)
			return;
		else
			scheduleDelayStage3(delay);
	}

	private void scheduleDelayStage3(int delay) {
		if (!isStart && !isAlreadyDead())
			return;
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					getRandomTarget();
					stage3();
				}

			}, delay);
		}
	}

	@Override
	public Player getRandomTarget() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 16))
				players.add(player);
		}
		if (players.isEmpty())
			return null;

		getAggroList().clear();
		getAggroList().startHate(players.get(Rnd.get(0, players.size() - 1)));
		return null;
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
