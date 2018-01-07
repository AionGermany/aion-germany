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
package ai.instance.dragonLordsRefuge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

/**
 * @author Cheatkiller
 * @reworked Luzien
 */
@AIName("tiamat")
// 219361
public class TiamatAI2 extends AggressiveNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();
	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> skillTask;
	private Future<?> painTask;
	private Future<?> addTask;

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			startSkillTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void startPainTask() {
		painTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTasks();
				}
				else {
					spawnInfinitePain();
				}
			}
		}, 25000, 80000);
	}

	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTasks();
				}
				else {
					atrocityEvent();
				}
			}
		}, 10000, 25000);
	}

	private void cancelTasks() {
		cancelSkillTask();
		if (addTask != null && !addTask.isCancelled()) {
			addTask.cancel(true);
		}
		if (painTask != null && !painTask.isCancelled()) {
			painTask.cancel(true);
		}
	}

	private void cancelSkillTask() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						cancelSkillTask();
						spawnDivisiveCreation();
						break;
					case 30:
						spawnGravityCrusher();
						break;
					case 25:
						startPainTask();
						spawnGravityCrusher();
						break;
					case 20:
					case 15:
					case 10:
						spawnGravityCrusher();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	/*
	 * sinking sand 283330 skill -> 20965 TODO
	 */

	private void atrocityEvent() {
		int var = Rnd.get(3);
		int skill = 20922 + (var * 2); // 20922/20924/20926, left,central,right
		spawnAtrocityNPCs(var);
		SkillEngine.getInstance().getSkill(getOwner(), skill, 60, getOwner()).useNoAnimationSkill(); // Animation without damage
	}

	private void spawnAtrocityNPCs(int var) {
		switch (var) {
			case 0:
				spawn(283237, 445.0000f, 550.7000f, 417.4000f, (byte) 0);
				break;
			case 2:
				spawn(283244, 454.1000f, 474.9000f, 417.4000f, (byte) 0);
				break;
			case 1:
				spawn(283241, 457.8000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 462.3000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 469.7000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 466.6000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 473.6000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 479.3000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 475.8000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 491.2000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 482.7000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 485.2000f, 514.6000f, 417.4000f, (byte) 0);
				spawn(283241, 488.1000f, 514.6000f, 417.4000f, (byte) 0);
				break;
		}
	}

	private void spawnDivisiveCreation() {
		addTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelTasks();
				}
				else {
					despawnAdds();
					spawn(283139, 464.24f, 462.26f, 417.4f, (byte) 18);
					spawn(283139, 542.79f, 465.03f, 417.4f, (byte) 43);
					spawn(283139, 541.79f, 563.71f, 417.4f, (byte) 74);
					spawn(283139, 465.79f, 565.43f, 417.4f, (byte) 100);
				}
			}
		}, 80000, 45000);
	}

	private void spawnGravityCrusher() {
		despawnAdds();
		spawn(283141, 464.24f, 462.26f, 417.4f, (byte) 18);
		spawn(283141, 542.79f, 465.03f, 417.4f, (byte) 43);
		spawn(283141, 541.79f, 563.71f, 417.4f, (byte) 74);
		spawn(283141, 465.79f, 565.43f, 417.4f, (byte) 100);
	}

	private void spawnInfinitePain() {
		despawnAdds();
		spawn(283143, 508.32f, 515.18f, 417.4f, (byte) 0);
		spawn(283144, 508.32f, 515.18f, 417.4f, (byte) 0);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50, 30, 25, 20, 15, 10 });
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
		despawnAdds();
		cancelTasks();
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
		despawnAdds();
		cancelTasks();
		isHome.set(true);
	}

	private void despawnAdds() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(283141));
		deleteNpcs(instance.getNpcs(283139));
		deleteNpcs(instance.getNpcs(283140));
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleDied() {
		percents.clear();
		despawnAdds();
		super.handleDied();
		cancelTasks();
	}
}
