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
package com.aionemu.gameserver.skillengine.task;

import java.util.concurrent.Future;

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 * @author Antraxx
 */
public abstract class AbstractInteractionTask {

	protected Future<?> task;
	protected Player requestor;
	protected VisibleObject responder;

	/**
	 * @param requestor
	 * @param responder
	 */
	public AbstractInteractionTask(Player requestor, VisibleObject responder) {
		this.requestor = requestor;
		if (responder == null) {
			this.responder = requestor;
		}
		else {
			this.responder = responder;
		}
	}

	/**
	 * Called on each interaction
	 *
	 * @return
	 */
	protected abstract boolean onInteraction();

	/**
	 * Called when interaction is complete
	 */
	protected abstract void onInteractionFinish();

	/**
	 * Called before interaction is started
	 */
	protected abstract void onInteractionStart();

	/**
	 * Called when interaction is not complete and need to be aborted
	 */
	protected abstract void onInteractionAbort();

	/**
	 * Interaction scheduling method
	 */
	public void start() {
		onInteractionStart();
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!validateParticipants()) {
					stop(true);
				}
				boolean stopTask = onInteraction();
				if (stopTask) {
					stop(false);
				}
			}
		}, CraftConfig.CRAFT_TIMER_DELAY, CraftConfig.CRAFT_TIMER_PERIOD);
	}

	/**
	 * Stop current interaction
	 */
	public void stop(boolean participantNull) {
		if (!participantNull) {
			onInteractionFinish();
		}
		if (task != null && !task.isCancelled()) {
			task.cancel(false);
			task = null;
		}
	}

	/**
	 * Abort current interaction
	 */
	public void abort() {
		onInteractionAbort();
		stop(false);
	}

	/**
	 * @return true or false
	 */
	public boolean isInProgress() {
		return task != null && !task.isCancelled();
	}

	/**
	 * @return true or false
	 */
	public boolean validateParticipants() {
		return requestor != null;
	}
}
