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
package ai.instance.tiamatStrongHold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

/**
 * @author Antraxx
 */
@AIName("adjutantanuhart")
public class AdjutantAnuhartAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> task1;
	private Future<?> task2;
	private Future<?> task3;
	private Future<?> task4;
	private Future<?> task5;
	private Future<?> task6;
	protected List<Integer> percents = new ArrayList<Integer>();

	private void startTask(int taskId) {
		switch (taskId) {
			case 1:
				// Schmerzwelle
				task1 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						AI2Actions.useSkill(AdjutantAnuhartAI2.this, 20745);
						startTask(2);
					}
				}, 12000);
				break;
			case 2:
				// Ausladender Angriff
				task2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						AI2Actions.useSkill(AdjutantAnuhartAI2.this, 20746);
						startTask(3);
					}
				}, 10000);
				break;
			case 3:
				// Adjutanten-Schlag
				task2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						AI2Actions.useSkill(AdjutantAnuhartAI2.this, 20744);
						startTask(4);
					}
				}, 10000);
				break;
			case 4:
				// Wave of Pain
				task3 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						AI2Actions.useSkill(AdjutantAnuhartAI2.this, 20745);
						startTask(5);
					}
				}, 3000);
				break;
			case 5:
				// Wirbelklinge
				task4 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// Schild
						AI2Actions.targetSelf(AdjutantAnuhartAI2.this);
						AI2Actions.useSkill(AdjutantAnuhartAI2.this, 20749);
						// Wirbelklinge
						SkillEngine.getInstance().getSkill(getOwner(), 20747, 55, getOwner()).useNoAnimationSkill();
						spawn(283234, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
						startTask(6);
					}
				}, 4000);
				break;
			case 6:
				// Wave of Pain
				task5 = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// fallback to task1
						startTask(1);
					}
				}, 8000);
				break;
			default:
				break;
		}
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			// starte mit Wirbelklinge
			startTask(5);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void cancelTasks() {
		if (task1 != null && !task1.isCancelled()) {
			task1.cancel(true);
		}
		if (task2 != null && !task2.isCancelled()) {
			task2.cancel(true);
		}
		if (task3 != null && !task3.isCancelled()) {
			task3.cancel(true);
		}
		if (task4 != null && !task4.isCancelled()) {
			task4.cancel(true);
		}
		if (task5 != null && !task5.isCancelled()) {
			task5.cancel(true);
		}
		if (task6 != null && !task6.isCancelled()) {
			task6.cancel(true);
		}
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						chooseBuff(20938);
						break;
					case 25:
						chooseBuff(20939);
						break;
					case 10:
						chooseBuff(20940);
						cancelTasks();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void chooseBuff(int buff) {
		AI2Actions.targetSelf(this);
		AI2Actions.useSkill(this, buff);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50, 25, 10 });
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
		cancelTasks();
		getOwner().getEffectController().removeAllEffects();
		isHome.set(true);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTasks();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
		cancelTasks();
	}
}
