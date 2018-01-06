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
package com.aionemu.gameserver.taskmanager.tasks;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;
import com.aionemu.gameserver.taskmanager.FIFOSimpleExecutableQueue;
import com.aionemu.gameserver.world.zone.ZoneUpdateService;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author ATracer
 * @reworked Rolandas, parallelized by using Fork/Join framework
 */
public class MoveTaskManager extends AbstractPeriodicTaskManager {

	private final FastMap<Integer, Creature> movingCreatures = new FastMap<Integer, Creature>().shared();
	private final TargetReachedManager targetReachedManager = new TargetReachedManager();
	private final TargetTooFarManager targetTooFarManager = new TargetTooFarManager();

	private MoveTaskManager() {
		super(100);
	}

	public void addCreature(Creature creature) {
		movingCreatures.put(creature.getObjectId(), creature);
	}

	public void removeCreature(Creature creature) {
		movingCreatures.remove(creature.getObjectId());
	}

	@Override
	public void run() {
		final FastList<Creature> arrivedCreatures = FastList.newInstance();
		final FastList<Creature> followingCreatures = FastList.newInstance();

		for (FastMap.Entry<Integer, Creature> e = movingCreatures.head(), mapEnd = movingCreatures.tail(); (e = e.getNext()) != mapEnd;) {
			Creature creature = e.getValue();
			creature.getMoveController().moveToDestination();
			if (creature.getAi2().poll(AIQuestion.DESTINATION_REACHED)) {
				movingCreatures.remove(e.getKey());
				arrivedCreatures.add(e.getValue());
			}
			else {
				followingCreatures.add(e.getValue());
			}
		}
		targetReachedManager.executeAll(arrivedCreatures);
		targetTooFarManager.executeAll(followingCreatures);
		FastList.recycle(arrivedCreatures);
		FastList.recycle(followingCreatures);

	}

	public static MoveTaskManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private final class TargetReachedManager extends FIFOSimpleExecutableQueue<Creature> {

		@Override
		protected void removeAndExecuteFirst() {
			final Creature creature = removeFirst();
			try {
				creature.getAi2().onGeneralEvent(AIEventType.MOVE_ARRIVED);
				ZoneUpdateService.getInstance().add(creature);
			}
			catch (RuntimeException e) {
				log.warn("", e);
			}
		}
	}

	private final class TargetTooFarManager extends FIFOSimpleExecutableQueue<Creature> {

		@Override
		protected void removeAndExecuteFirst() {
			final Creature creature = removeFirst();
			try {
				creature.getAi2().onGeneralEvent(AIEventType.MOVE_VALIDATE);
			}
			catch (RuntimeException e) {
				log.warn("", e);
			}
		}
	}

	private static final class SingletonHolder {

		private static final MoveTaskManager INSTANCE = new MoveTaskManager();
	}
}
