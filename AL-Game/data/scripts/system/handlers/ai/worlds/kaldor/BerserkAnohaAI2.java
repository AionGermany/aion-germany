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
package ai.worlds.kaldor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

@AIName("anoha")
// 855263
public class BerserkAnohaAI2 extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private Future<?> phaseTask;
	private Future<?> thinkTask;
	private Future<?> specialSkillTask;
	private boolean think = true;
	private int curentPercent = 100;
	public Player player;

	@Override
	public boolean canThink() {
		return think;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			startSpecialSkillTask();
			sendMsg(1501153);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), false, 0, 0);
	}

	private synchronized void checkPercentage(int hpPercentage) {
		curentPercent = hpPercentage;
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 90:
					case 70:
					case 44:
					case 23:
						cancelspecialSkillTask();
						think = false;
						EmoteManager.emoteStopAttacking(getOwner());
						SkillEngine.getInstance().getSkill(getOwner(), 21765, 60, getOwner()).useNoAnimationSkill();
						sendMsg(1501154);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								if (!isAlreadyDead()) {
									SkillEngine.getInstance().getSkill(getOwner(), 21767, 60, getOwner()).useNoAnimationSkill();
									startThinkTask();
									int total = numberOfServants(855262); // maxHp=2382, lvl=65
									if (total == 0 || (6 - total) != 0) {
										for (int i = 0; i < (6 - total); i++) {
											rndSpawn(855262);
										}
									}
								}
							}
						}, 3500);
						break;
					case 84:
					case 79:
					case 75:
					case 72:
					case 67:
					case 63:
					case 59:
					case 53:
					case 47:
					case 43:
					case 39:
					case 35:
					case 30:
					case 26:
					case 21:
					case 16:
					case 11:
					case 6:
						startPhaseTask();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void startThinkTask() {
		thinkTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					think = true;
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
						cancelspecialSkillTask();
						startSpecialSkillTask();
					}
				}
			}

		}, 20000);
	}

	private void startPhaseTask() {
		SkillEngine.getInstance().getSkill(getOwner(), 21755, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1501155);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					deleteNpcs(282746);
					// reassess even though it's already being deleted
					int total = numberOfServants(282746);
					if (total == 0 || (8 - total) != 0) {
						for (int i = 0; i < (8 - total); i++) {
							rndSpawn(282746);
						}
					}
					cancelspecialSkillTask();
					startSpecialSkillTask();
				}
			}

		}, 4000);
	}

	private void startSpecialSkillTask() {
		specialSkillTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					SkillEngine.getInstance().getSkill(getOwner(), 21761, 60, getOwner()).useNoAnimationSkill();
					specialSkillTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isAlreadyDead()) {
								SkillEngine.getInstance().getSkill(getOwner(), 21762, 60, getOwner()).useNoAnimationSkill();
								specialSkillTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										if (!isAlreadyDead()) {
											SkillEngine.getInstance().getSkill(getOwner(), 21763, 60, getOwner()).useNoAnimationSkill();
											if (curentPercent <= 63) {
												specialSkillTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

													@Override
													public void run() {
														if (!isAlreadyDead()) {
															SkillEngine.getInstance().getSkill(getOwner(), 21764, 60, getOwner()).useNoAnimationSkill();
															sendMsg(1501156);
															ThreadPoolManager.getInstance().schedule(new Runnable() {

																@Override
																public void run() {
																	if (!isAlreadyDead()) {
																		deleteNpcs(282747);
																		rndSpawn(282747);
																		rndSpawn(282747);
																	}
																}

															}, 2000);
														}
													}

												}, 21000);
											}
										}
									}

								}, 3500);
							}
						}

					}, 1500);
				}
			}

		}, 12000);
	}

	private void deleteNpcs(final int npcId) {
		if (getKnownList() != null) {
			getKnownList().doOnAllNpcs(new Visitor<Npc>() {

				@Override
				public void visit(Npc npc) {
					if (npc.getNpcId() == npcId) {
						CreatureActions.delete(npc);
					}
				}

			});
		}
	}

	private int numberOfServants(final int npcId) {
		final AtomicInteger total = new AtomicInteger();
		if (getKnownList() != null) {
			getKnownList().doOnAllNpcs(new Visitor<Npc>() {

				@Override
				public void visit(Npc npc) {
					if (npc.getNpcId() == npcId) {
						total.incrementAndGet();
					}
				}
			});
		}
		return total.get();
	}

	private void cancelspecialSkillTask() {
		if (specialSkillTask != null && !specialSkillTask.isDone()) {
			specialSkillTask.cancel(true);
		}
	}

	private void cancelPhaseTask() {
		if (phaseTask != null && !phaseTask.isDone()) {
			phaseTask.cancel(true);
		}
	}

	private void cancelThinkTask() {
		if (thinkTask != null && !thinkTask.isDone()) {
			thinkTask.cancel(true);
		}
	}

	private void rndSpawn(int npcId) {
		float direction = Rnd.get(0, 199) / 100f;
		int distance = Rnd.get(1, 25);
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		spawn(npcId, 1191.4962f + x1, 360.13733f + y1, 128.5f, p.getHeading());
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, 90, 84, 79, 75, 72, 70, 67, 63, 59, 53, 47, 44, 43, 39, 35, 30, 26, 23, 21, 16, 11, 6);
	}

	@Override
	protected void handleDespawned() {
		cancelspecialSkillTask();
		cancelThinkTask();
		cancelPhaseTask();
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		sendMsg(1501159);
		cancelspecialSkillTask();
		cancelThinkTask();
		cancelPhaseTask();
		percents.clear();
		deleteNpcs(855262);
		deleteNpcs(282746);
		deleteNpcs(282747);

		// TODO after death spawns Commander Anoha
		// Asmodians: 804594
		// Elyos: 804595
		Collection<AggroInfo> lastAttackers = this.getAggroList().getList();
		if (lastAttackers.size() > 0) {
			for (AggroInfo ai : lastAttackers) {
				if (((Player) ai.getAttacker()).getRace() == Race.ELYOS) {
					// spawn npcs for Elyos
					spawn(804595, 788.97375f, 456.78577f, 144.01175f, (byte) 25);
					break;
				}
				else if (((Player) ai.getAttacker()).getRace() == Race.ASMODIANS) {
					// spawn npcs for Asmo
					spawn(804594, 788.97375f, 456.78577f, 144.01175f, (byte) 25);
					break;
				}
			}
		}

		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		think = true;
		cancelspecialSkillTask();
		cancelThinkTask();
		cancelPhaseTask();
		addPercent();
		curentPercent = 100;
		deleteNpcs(855262);
		deleteNpcs(282746);
		deleteNpcs(282747);
		isAggred.set(false);
		super.handleBackHome();
	}

}
