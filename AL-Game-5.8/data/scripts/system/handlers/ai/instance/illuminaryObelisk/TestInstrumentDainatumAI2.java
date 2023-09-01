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
package ai.instance.illuminaryObelisk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author Alcapwnd
 */

@AIName("dainatum")
public class TestInstrumentDainatumAI2 extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();
	private Future<?> TimerTasks;
	protected NpcAI2 ai2;
	private boolean isCancelled;
	private Future<?> skillTask;
	private Future<?> natiskTask;
	private Future<?> portaltask;
	private AtomicBoolean isHome = new AtomicBoolean(true);

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			startSkillTask();
			PortalDestroy();
			startnatiskTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage > 95 && percents.size() < 6) {
			addPercent();
		}

		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 95:
						BombTimer();
						break;
					case 70:
						spawnSupport();
						break;
					case 60:
						spawnSupportB();
						break;
					case 50:
						catching();
						break;
					case 35:
						spawnHealers();
						break;
					case 10:
						Gazz();
						break;
				}

				percents.remove(percent);
				break;
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 95, 70, 60, 50, 35, 10 });
	}

	private void Boss_Timer_01() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402143);
	}

	private void Boss_Timer_02() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402144);
	}

	private void Boss_Timer_03() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402146);
	}

	private void Boss_Portal_Destroy() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402212);
		Npc Portal = getPosition().getWorldMapInstance().getNpc(702216);
		getOwner().setTarget(Portal);
		if (Portal != null) {
			SkillEngine.getInstance().getSkill(getOwner(), 21527, 1, Portal).useNoAnimationSkill();
			Portal.getController().onDelete();
		}
	}

	private void DainatumDestroy(int skillId) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, getOwner()).useNoAnimationSkill();
	}

	private void BombTimer() {

		TimerTasks = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead() && isCancelled == true) {
					CancelTask();
				}
				else {
					Boss_Timer_01();
				}
			}
		}, 1000);

		TimerTasks = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead() && isCancelled == true) {
					CancelTask();
				}
				else {
					Boss_Timer_02();
				}
			}
		}, 60000);

		TimerTasks = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Boss_Timer_03();
				DainatumDestroy(21275);
			}
		}, 360000);

		TimerTasks = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead() && isCancelled == true) {
					CancelTask();
				}
			}
		}, 365000);
	}

	private void DespawnDainatum() {
		CancelTask();
		isCancelled = true;
		cancelnatiskTask();
		cancelportalTask();
		cancelskillTask();

	}

	private void CancelTask() {
		if (TimerTasks != null && !TimerTasks.isCancelled()) {
			TimerTasks.cancel(true);
		}
	}

	private void spawnSupport() {
		float direction = Rnd.get(0, 199) / 100f;
		int distance = Rnd.get(0, 10);
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		spawn(284859, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284859, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284859, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284859, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
	}

	private void spawnSupportB() {
		float direction = Rnd.get(0, 199) / 100f;
		int distance = Rnd.get(0, 10);
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		spawn(284860, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284860, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284860, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		spawn(284860, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
	}

	private void spawnHealers() {
		spawn(284861, 265.34f, 254.80f, 455.12f, (byte) 60);
		spawn(284861, 255.59f, 264.44f, 455.12f, (byte) 89);
		spawn(284861, 248.42f, 247.52f, 455.12f, (byte) 15);
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private List<Npc> getNpcs(int npcId) {
		return getPosition().getWorldMapInstance().getNpcs(npcId);
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		CancelTask();
		isCancelled = true;
		super.handleDespawned();
		cancelnatiskTask();
		cancelportalTask();
		cancelskillTask();
	}

	@Override
	protected void handleDied() {
		DespawnDainatum();
		percents.clear();
		CancelTask();
		isCancelled = true;
		super.handleDied();
		deleteNpcs(getNpcs(284861));
		cancelnatiskTask();
		cancelportalTask();
		cancelskillTask();
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		isHome.set(true);
		super.handleBackHome();
		cancelnatiskTask();
		cancelportalTask();
		cancelskillTask();
	}

	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				}
				if (getOwner().isCasting()) {

				}
				else {
					Vnez();
				}
			}
		}, 20000, Rnd.get(10000, 40000));
	}

	private void PortalDestroy() {
		portaltask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelportalTask();
				}
				if (getOwner().isCasting()) {
					getOwner().getController().cancelCurrentSkill();
				}
				else {
					Boss_Portal_Destroy();
				}
			}
		}, 60000);
	}

	private void startnatiskTask() {

		natiskTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				}

				else {
					if (getOwner().isCasting() && getOwner().getCastingSkillId() != 21530) {
						getOwner().getController().cancelCurrentSkill();
					}
					else if (getOwner().isCasting() && getOwner().getCastingSkillId() == 21530) {

					}
					else
						Natisk();
				}
			}
		}, 2000, Rnd.get(25000, 60000));
	}

	private void Vnez() {
		int[] rndskl = { 21526, 21525, 21536, 21528 };
		Player target = getRandomTarget();
		if (target == null) {
		}
		SkillEngine.getInstance().getSkill(getOwner(), RndArray.get(rndskl), 1, target).useNoAnimationSkill();
	}

	private void Gazz() {
		Player target = getRandomTarget();
		if (target == null) {
		}
		SkillEngine.getInstance().getSkill(getOwner(), 21533, 1, target).useNoAnimationSkill();
	}

	private void Natisk() {
		int variant = Rnd.get(1, 2);
		Player target = getRandomTarget();
		if (target == null) {
		}
		if (variant == 1) {
			SkillEngine.getInstance().getSkill(getOwner(), 21531, 1, getOwner()).useNoAnimationSkill();
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (isAlreadyDead()) {
						cancelskillTask();
					}
					else {
						if (getOwner().isCasting()) {
							getOwner().getController().cancelCurrentSkill();
						}
						SkillEngine.getInstance().getSkill(getOwner(), 21532, 1, getOwner()).useNoAnimationSkill();
					}
				}
			}, 4000);
		}
		else {
		}
		SkillEngine.getInstance().getSkill(getOwner(), 21532, 1, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				}

				else {
					if (getOwner().isCasting()) {
						getOwner().getController().cancelCurrentSkill();
					}
					SkillEngine.getInstance().getSkill(getOwner(), 21531, 1, getOwner()).useNoAnimationSkill();
				}
			}
		}, 4000);
	}

	private void catching() {
		Player target = getRandomTarget();
		if (target == null) {
		}
		SkillEngine.getInstance().getSkill(getOwner(), 21529, 1, target).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelskillTask();
				}
				else {
					SkillEngine.getInstance().getSkill(getOwner(), 21530, 1, getOwner().getTarget()).useNoAnimationSkill();
				}
			}
		}, 4000);
	}

	private void cancelskillTask() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}

	}

	private void cancelportalTask() {
		if (portaltask != null && !portaltask.isCancelled()) {
			portaltask.cancel(true);
		}
	}

	private void cancelnatiskTask() {
		if (natiskTask != null && !natiskTask.isCancelled()) {
			natiskTask.cancel(true);
		}
	}

}
