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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("brigade_general_vasharti")
// 217313, 236300
public class BrigadeGeneralVashartiAI2 extends AggressiveNpcAI2 {

	private Future<?> airTask;
	private List<Integer> percents = new ArrayList<Integer>();
	private AtomicBoolean isHome = new AtomicBoolean(true);
	private boolean canThink = true;
	private Future<?> flameBuffTask;
	private Future<?> flameSmashTask;
	private List<Point3D> blueFlameSmashs = new ArrayList<Point3D>();
	private List<Point3D> redFlameSmashs = new ArrayList<Point3D>();
	private int flameSmashCount = 1;

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(70).setOpen(false);
			startFlameBuffEvent();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				percents.remove(percent);
				canThink = false;
				EmoteManager.emoteStopAttacking(getOwner());
				cancelFlameBuffEvent();
				SkillEngine.getInstance().getSkill(getOwner(), 20532, 60, getOwner()).useNoAnimationSkill();
				startAirEvent(this, percent);
				break;
			}
		}
	}

	private void startAirEvent(final NpcAI2 ai, final int percent) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {

				if (!isAlreadyDead()) {
					getSpawnTemplate().setWalkerId(null);
					WalkManager.stopWalking(ai);
					SkillEngine.getInstance().getSkill(getOwner(), 20534, 60, getOwner()).useNoAnimationSkill();
					int npcId1 = 0;
					int npcId2 = 0;
					switch (percent) {
						case 80:
							npcId1 = 283010;
							npcId2 = 283002;
							break;
						case 70:
							npcId1 = 283011;
							npcId2 = 283003;
							break;
						case 50:
							npcId1 = 283011;
							npcId2 = 283004;
							break;
						case 40:
							npcId1 = 283012;
							npcId2 = 283004;
							break;
						case 25:
							npcId1 = 283012;
							npcId2 = 283006;
							break;
					}

					spawn(npcId2, 188.16568f, 414.03534f, 260.75488f, (byte) 0);
					spawn(npcId1, 188.33f, 414.61f, 260.61f, (byte) 244);
					final Npc buffNpc = (Npc) spawn(283007, 188.33f, 414.61f, 260.61f, (byte) 0);

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!buffNpc.getLifeStats().isAlreadyDead()) {
								startFlameSmashEvent(percent);
								SkillEngine.getInstance().getSkill(buffNpc, 20538, 60, buffNpc).useNoAnimationSkill();
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										buffNpc.getController().onDelete();
									}
								}, 4000);
							}
						}
					}, 1000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							cancelFlameSmashTask();
							cancelAirEvent();
							startFlameBuffEvent();
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
					}, 40000);
				}
			}
		}, 4000);
	}

	private void cancelFlameSmashTask() {
		flameSmashCount = 1;
		if (flameSmashTask != null && !flameSmashTask.isDone()) {
			flameSmashTask.cancel(true);
		}
	}

	private void startFlameSmashEvent(final int percent) {
		flameSmashTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelFlameSmashTask();
				}
				else {
					List<Point3D> redFlameSmashs = getRedFlameSmashs(283008);
					List<Point3D> blueFlameSmashs = getRedFlameSmashs(283009);
					WorldMapInstance instance = getPosition().getWorldMapInstance();
					if (instance != null) {
						if (percent > 40 && flameSmashCount == 1) {
							flameSmashCount++;
							spawnFlameSmash(redFlameSmashs, 283008);
							spawnFlameSmash(blueFlameSmashs, 283009);
						}
						else {
							if (instance.getNpc(283010) != null) {
								flameSmashCount = 1;
								spawnFlameSmash(redFlameSmashs, 283008);
								spawnFlameSmash(redFlameSmashs, 283008);
								spawnFlameSmash(redFlameSmashs, 283008);
							}
							else if (instance.getNpc(283011) != null) {
								flameSmashCount = 1;
								spawnFlameSmash(blueFlameSmashs, 283009);
								spawnFlameSmash(blueFlameSmashs, 283009);
								spawnFlameSmash(blueFlameSmashs, 283009);
							}
							else if (instance.getNpc(283012) != null) {
								if (flameSmashCount == 1) {
									flameSmashCount++;
									spawnFlameSmash(redFlameSmashs, 283008);
									spawnFlameSmash(redFlameSmashs, 283008);
									spawnFlameSmash(blueFlameSmashs, 283009);
									spawnFlameSmash(blueFlameSmashs, 283009);
								}
								else if (flameSmashCount == 2) {
									flameSmashCount++;
									spawnFlameSmash(redFlameSmashs, 283008);
									spawnFlameSmash(redFlameSmashs, 283008);
									spawnFlameSmash(redFlameSmashs, 283008);
								}
								else {
									flameSmashCount = 1;
									spawnFlameSmash(blueFlameSmashs, 283009);
									spawnFlameSmash(blueFlameSmashs, 283009);
									spawnFlameSmash(blueFlameSmashs, 283009);
								}
							}
						}
					}
					redFlameSmashs.clear();
					blueFlameSmashs.clear();
				}
			}
		}, 3000, 3000);
	}

	private void spawnFlameSmash(List<Point3D> flameSmashs, int npcId) {
		if (!flameSmashs.isEmpty()) {
			Point3D spawn = flameSmashs.remove(Rnd.get(flameSmashs.size()));
			spawn(npcId, spawn.getX(), spawn.getY(), spawn.getZ(), (byte) 0);
		}
	}

	private boolean isSpawned(int npcId, Point3D position) {
		for (Npc npc : getPosition().getWorldMapInstance().getNpcs(npcId)) {
			if (npc.getX() == position.getX() && npc.getY() == position.getY()) {
				return true;
			}
		}
		return false;
	}

	private List<Point3D> getRedFlameSmashs(int npcId) {
		List<Point3D> flameSmashs = new ArrayList<Point3D>();
		for (Point3D flameSmash : (npcId == 283008 ? redFlameSmashs : blueFlameSmashs)) {
			if (!isSpawned(npcId, flameSmash)) {
				flameSmashs.add(flameSmash);
			}
		}
		return flameSmashs;
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void cancelAirEvent() {
		canThink = true;
		if (!isAlreadyDead()) {
			getOwner().getEffectController().removeEffect(20534);
		}
		if (getPosition() != null) {
			WorldMapInstance instance = getPosition().getWorldMapInstance();
			if (instance != null) {
				deleteNpcs(instance.getNpcs(283002));
				deleteNpcs(instance.getNpcs(283003));
				deleteNpcs(instance.getNpcs(283004));
				deleteNpcs(instance.getNpcs(283005));
				deleteNpcs(instance.getNpcs(283006));
				deleteNpcs(instance.getNpcs(283007));
				deleteNpcs(instance.getNpcs(283010));
				deleteNpcs(instance.getNpcs(283011));
				deleteNpcs(instance.getNpcs(283012));
				deleteNpcs(instance.getNpcs(283000));
				deleteNpcs(instance.getNpcs(283001));
			}
		}
		if (airTask != null && !airTask.isDone()) {
			airTask.cancel(true);
		}
	}

	private void cancelFlameBuffEvent() {
		if (flameBuffTask != null && !flameBuffTask.isDone()) {
			flameBuffTask.cancel(true);
		}
		if (!isAlreadyDead()) {
			SkillEngine.getInstance().getSkill(getOwner(), 20532, 60, getOwner()).useNoAnimationSkill();
		}
	}

	private void startFlameBuffEvent() {
		flameBuffTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelFlameBuffEvent();
				}
				else {
					WorldMapInstance instance = getPosition().getWorldMapInstance();
					if (instance != null) {
						SkillEngine.getInstance().getSkill(getOwner(), Rnd.get(0, 1) == 0 ? 20530 : 20531, 60, getOwner()).useNoAnimationSkill();
						if (instance.getNpc(283000) == null && instance.getNpc(283001) == null) {
							VisibleObject ice = spawn(283001, 205.280f, 410.53f, 261f, (byte) 56);
							VisibleObject fire = spawn(283000, 171.330f, 417.57f, 261f, (byte) 116);
							if (ice != null) {
								useKissBuff((Npc) ice);
							}
							if (fire != null) {
								useKissBuff((Npc) fire);
							}
						}
					}
				}
			}
		}, 0, 40000);
	}

	private void useKissBuff(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (npc != null && !npc.getLifeStats().isAlreadyDead()) {
					SkillEngine.getInstance().getSkill(npc, npc.getNpcId() == 283001 ? 19346 : 19345, 60, npc).useNoAnimationSkill();
				}
			}
		}, 1000);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 80, 70, 50, 40, 25 });
	}

	@Override
	protected void handleSpawned() {
		addPercent();
		cancelFlameBuffEvent();
		cancelAirEvent();
		super.handleSpawned();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500405, getObjectId(), 0, 2000);
		blueFlameSmashs.add(new Point3D(176.184f, 415.782f, 260.572f));
		blueFlameSmashs.add(new Point3D(159.480f, 412.495f, 260.555f));
		blueFlameSmashs.add(new Point3D(183.784f, 413.475f, 260.755f));
		blueFlameSmashs.add(new Point3D(211.497f, 398.355f, 260.550f));
		blueFlameSmashs.add(new Point3D(173.654f, 419.605f, 260.571f));
		blueFlameSmashs.add(new Point3D(168.923f, 397.403f, 260.571f));
		blueFlameSmashs.add(new Point3D(189.839f, 385.524f, 260.571f));
		blueFlameSmashs.add(new Point3D(209.488f, 398.908f, 260.552f));
		blueFlameSmashs.add(new Point3D(171.938f, 419.449f, 260.571f));
		blueFlameSmashs.add(new Point3D(202.292f, 403.402f, 260.559f));
		blueFlameSmashs.add(new Point3D(184.120f, 384.201f, 260.571f));
		blueFlameSmashs.add(new Point3D(178.429f, 415.372f, 260.572f));
		redFlameSmashs.add(new Point3D(177.106f, 413.678f, 260.569f));
		redFlameSmashs.add(new Point3D(167.271f, 418.102f, 260.721f));
		redFlameSmashs.add(new Point3D(188.180f, 406.594f, 260.572f));
		redFlameSmashs.add(new Point3D(181.117f, 402.296f, 260.571f));
		redFlameSmashs.add(new Point3D(176.960f, 411.328f, 260.550f));
		redFlameSmashs.add(new Point3D(196.692f, 408.827f, 260.564f));
		redFlameSmashs.add(new Point3D(204.483f, 390.333f, 260.565f));
		redFlameSmashs.add(new Point3D(205.820f, 412.985f, 260.571f));
		redFlameSmashs.add(new Point3D(167.999f, 416.711f, 260.721f));
		redFlameSmashs.add(new Point3D(192.086f, 419.873f, 260.572f));
		redFlameSmashs.add(new Point3D(173.963f, 412.215f, 260.557f));
		redFlameSmashs.add(new Point3D(175.762f, 422.974f, 260.572f));
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		blueFlameSmashs.clear();
		redFlameSmashs.clear();
		cancelFlameBuffEvent();
		cancelAirEvent();
		cancelFlameSmashTask();
		super.handleDespawned();
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		addPercent();
		cancelFlameBuffEvent();
		cancelFlameSmashTask();
		cancelAirEvent();
		isHome.set(true);
		super.handleBackHome();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		blueFlameSmashs.clear();
		redFlameSmashs.clear();
		cancelFlameBuffEvent();
		cancelFlameSmashTask();
		cancelAirEvent();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500410, getObjectId(), 0, 0);
		super.handleDied();
	}
}
