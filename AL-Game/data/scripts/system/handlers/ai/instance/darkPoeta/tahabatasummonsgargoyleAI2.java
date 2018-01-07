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
package ai.instance.darkPoeta;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.GeneralNpcAI2;

/**
 * @author Ritsu
 */
@AIName("summonsgargoyle")
public class tahabatasummonsgargoyleAI2 extends GeneralNpcAI2 {

	private Future<?> eventTask;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startTimer();
	}

	@Override
	protected void handleDied() {
		cancelEventTask();
		super.handleDied();
	}

	@Override
	protected void handleDespawned() {
		cancelEventTask();
		super.handleDespawned();
	}

	private void startTimer() {
		eventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 18219, 50, getTarget()).useSkill();
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						getOwner().getController().onDelete();
					}
				}, 5000);
			}
		}, 10000);
	}

	private void cancelEventTask() {
		if (eventTask != null && !eventTask.isDone()) {
			eventTask.cancel(true);
		}
	}
}
