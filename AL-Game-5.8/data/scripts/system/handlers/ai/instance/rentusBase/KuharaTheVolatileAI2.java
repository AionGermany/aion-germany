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
package ai.instance.rentusBase;

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
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("kuhara_the_volatile")
// 217311
public class KuharaTheVolatileAI2 extends AggressiveNpcAI2 {

	private Future<?> activeEventTask;
	private Future<?> barrelEventTask;
	private Future<?> bombEventTask;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Phase phase = Phase.ACTIVE;
	private boolean canThink = true;

	private enum Phase {
		ACTIVE,
		BOMBS,
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(43).setOpen(true);
			getPosition().getWorldMapInstance().getDoors().get(150).setOpen(false);
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500393, getObjectId(), 0, 0);
			startActivEvent();
			startBarrelEvent();
		}
	}

	private void cancelActiveEventTask() {
		if (activeEventTask != null && !activeEventTask.isDone()) {
			activeEventTask.cancel(true);
		}
	}

	private void cancelBarrelEventTask() {
		if (barrelEventTask != null && !barrelEventTask.isDone()) {
			barrelEventTask.cancel(true);
		}
	}

	private void cancelBombEventTask() {
		if (bombEventTask != null && !bombEventTask.isDone()) {
			bombEventTask.cancel(true);
		}
	}

	private void startBarrelEvent() {
		barrelEventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelBarrelEventTask();
				}
				else {
					switch (Rnd.get(1, 4)) {
						case 1:
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(126.528755f, 274.48883f, 209.81859f));
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(126.528755f, 274.48883f, 209.81859f));
							break;
						case 2:
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(162.22261f, 263.89288f, 209.81859f));
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(162.22261f, 263.89288f, 209.81859f));
							break;
						case 3:
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(156.32321f, 235.733f, 209.81859f));
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(156.32321f, 235.733f, 209.81859f));
							break;
						case 4:
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(119.23888f, 245.8903f, 209.81859f));
							rndSpawnInRange(282394, Rnd.get(1, 4), new Point3D(119.23888f, 245.8903f, 209.81859f));
							break;
					}
					startBombEvent();
				}
			}
		}, 15000, 15000);
	}

	private void startBombEvent() {
		bombEventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
					phase = Phase.BOMBS;
					cancelActiveEventTask();
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500394, getObjectId(), 0, 0);
					canThink = false;
					EmoteManager.emoteStopAttacking(getOwner());
					setStateIfNot(AIState.WALKING);
					SkillEngine.getInstance().getSkill(getOwner(), 19703, 60, getOwner()).useNoAnimationSkill();
					spawnBombEvent();
					bombEventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.BOMBS)) {
								phase = Phase.ACTIVE;
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
									SkillEngine.getInstance().getSkill(getOwner(), 19375, 60, getOwner()).useNoAnimationSkill();
								}
								deleteNpcs(getPosition().getWorldMapInstance().getNpcs(282396)); // Kuhara Bomb.
							}
						}
					}, 11000);
				}
			}
		}, 14000);
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void spawnBombEvent() {
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(126.528755f, 274.48883f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(126.528755f, 274.48883f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(162.22261f, 263.89288f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(162.22261f, 263.89288f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(156.32321f, 235.733f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(156.32321f, 235.733f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(119.23888f, 245.8903f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(119.23888f, 245.8903f, 209.81859f)));
	}

	private void MoveBombToBoss(final Npc npc) {
		if (!isAlreadyDead() && !isHome.get()) {
			npc.setTarget(getOwner());
			npc.getMoveController().moveToTargetObject();
		}
	}

	private Npc rndSpawnInRange(int npcId, float distance, Point3D position) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		return (Npc) spawn(npcId, position.getX() + x1, position.getY() + y1, position.getZ(), (byte) 0);
	}

	private void startActivEvent() {
		activeEventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelActiveEventTask();
				}
				else {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500395, getObjectId(), 0, 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
								SkillEngine.getInstance().getSkill(getOwner(), 19704, 60, getOwner()).useNoAnimationSkill();
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
											SkillEngine.getInstance().getSkill(getOwner(), 19705, 60, getOwner()).useNoAnimationSkill();
										}
									}
								}, 3500);
							}
						}
					}, 1000);
				}
			}
		}, 8000, 14000);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
	}

	@Override
	protected void handleBackHome() {
		isHome.set(true);
		phase = Phase.ACTIVE;
		canThink = true;
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
		getPosition().getWorldMapInstance().getDoors().get(43).setOpen(false);
		getPosition().getWorldMapInstance().getDoors().get(150).setOpen(true);
		super.handleBackHome();
	}

	@Override
	protected void handleDied() {
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282394)); // Oil Cask.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282395)); // Spilled Oil.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282396)); // Kuhara Bomb.
			spawn(219215, p.getX(), p.getY(), p.getZ(), p.getHeading()); // Kuhara Treasure Box.
			p.getWorldMapInstance().getDoors().get(43).setOpen(false);
			p.getWorldMapInstance().getDoors().get(150).setOpen(true);
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
}
