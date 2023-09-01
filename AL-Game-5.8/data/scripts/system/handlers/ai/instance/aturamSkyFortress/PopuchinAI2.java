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
package ai.instance.aturamSkyFortress;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("popuchin")
public class PopuchinAI2 extends AggressiveNpcAI2 {

	private boolean isHome = true;
	private Future<?> bombTask;

	private void startBombTask() {
		if (!isAlreadyDead() && !isHome) {
			bombTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isAlreadyDead() && !isHome) {
						VisibleObject target = getTarget();
						if (target != null && target instanceof Player) {
							SkillEngine.getInstance().getSkill(getOwner(), 19413, 49, target).useNoAnimationSkill();
						}
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								if (!isAlreadyDead() && !isHome) {
									SkillEngine.getInstance().getSkill(getOwner(), 19412, 49, getOwner()).useNoAnimationSkill();
									ThreadPoolManager.getInstance().schedule(new Runnable() {

										@Override
										public void run() {
											if (!isAlreadyDead() && !isHome && getOwner().isSpawned()) {
												if (getLifeStats().getHpPercentage() > 50) {
													WorldPosition p = getPosition();
													if (p != null && p.getWorldMapInstance() != null) {
														spawn(217374, p.getX(), p.getY(), p.getZ(), p.getHeading());
														spawn(217374, p.getX(), p.getY(), p.getZ(), p.getHeading());
														startBombTask();
													}
												}
												else {
													spawnRndBombs();
													startBombTask();
												}
											}
										}
									}, 1500);
								}
							}
						}, 3000);
					}
				}
			}, 15500);
		}
	}

	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome) {
			isHome = false;
			getPosition().getWorldMapInstance().getDoors().get(68).setOpen(false);
			startBombTask();
		}
	}

	private void rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		spawn(npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), (byte) 0);
	}

	@Override
	protected void handleBackHome() {
		isHome = true;
		super.handleBackHome();
		getPosition().getWorldMapInstance().getDoors().get(68).setOpen(true);
		if (bombTask != null && !bombTask.isDone()) {
			bombTask.cancel(true);
		}
	}

	private void spawnRndBombs() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead() && !isHome) {
					for (int i = 0; i < 10; i++) {
						rndSpawnInRange(217375, Rnd.get(1, 12));
					}
				}
			}
		}, 1500);

	}

	@Override
	public AIAnswer ask(AIQuestion question) {
		switch (question) {
			case CAN_RESIST_ABNORMAL:
				return AIAnswers.POSITIVE;
			default:
				return AIAnswers.NEGATIVE;
		}
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		getPosition().getWorldMapInstance().getDoors().get(68).setOpen(true);
	}
}
