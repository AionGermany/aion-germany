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
package ai.instance.beshmundirTemple;

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
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

/**
 * @author Antraxx
 */
@AIName("captainlakhara")
public class CaptainLakharaAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();
	private Future<?> taskRage;
	private Future<?> taskDivineGrasp;
	private boolean canThink = true;

	@Override
	public boolean canThink() {
		return canThink;
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 25, 10 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 25:
						NpcShoutsService.getInstance().sendMsg(getOwner(), 1500043, getObjectId(), 0, 1000);
						taskDivineGraspStop();
						break;
					case 10:
						taskRageStart();
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
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500042, getObjectId(), 0, 0);
			taskDivineGraspStart();
			callForHelp(36);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		taskDivineGraspStop();
		taskRageStop();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500044, getObjectId(), 0, 1000);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		taskDivineGraspStop();
		taskRageStop();
	}

	@Override
	protected void handleBackHome() {
		isHome.set(true);
		canThink = true;
		taskDivineGraspStop();
		taskRageStop();
		super.handleBackHome();
	}

	private void taskDivineGraspStart() {
		taskDivineGrasp = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					taskDivineGraspStop();
				}
				else {
					canThink = false;
					setStateIfNot(AIState.WALKING);

					// arms up pull after three seconds
					AI2Actions.useSkill(CaptainLakharaAI2.this, 18890);

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// pull player
							if (isHome.equals(true) || isAlreadyDead()) {
								taskDivineGraspStop();
								return;
							}
							AI2Actions.useSkill(CaptainLakharaAI2.this, 18995);
						}
					}, 4500);

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Drehschmetterschlag #1
							if (isHome.equals(true) || isAlreadyDead()) {
								taskDivineGraspStop();
								return;
							}
							AI2Actions.useSkill(CaptainLakharaAI2.this, 19090);
						}
					}, 6500);

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// Drehschmetterschlag #2
							if (isHome.equals(true) || isAlreadyDead()) {
								taskDivineGraspStop();
								return;
							}
							AI2Actions.useSkill(CaptainLakharaAI2.this, 19090);
						}
					}, 8500);

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							// normal weiter
							if (isHome.equals(true) || isAlreadyDead()) {
								taskDivineGraspStop();
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
					}, 10500);
				}
			}
		}, 60000, 60000);
	}

	private void taskDivineGraspStop() {
		if ((taskDivineGrasp != null) && !taskDivineGrasp.isDone()) {
			taskDivineGrasp.cancel(true);
		}
	}

	private void taskRageStop() {
		if ((taskRage != null) && !taskRage.isDone()) {
			taskRage.cancel(true);
		}
	}

	private void taskRageStart() {
		taskRage = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					taskRageStop();
				}
				else {
					int rnd = Rnd.get(1, 2);
					if (rnd == 2) {
						AI2Actions.useSkill(CaptainLakharaAI2.this, 18891);
					}
				}
			}
		}, 25000, 25000);
	}
}
