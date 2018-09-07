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

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.GeneralNpcAI2;

/**
 * @author Alcapwnd
 */
@AIName("dainatum_healers")
public class DainatumHealersAI2 extends GeneralNpcAI2 {

	private Future<?> SkillTasks;
	private boolean isCancelled;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillActive();
	}

	private void HealDainatum(int skillId) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, getOwner()).useNoAnimationSkill();
	}

	private void SkillActive() {

		SkillTasks = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead() && isCancelled == true) {
					CancelTask();
				}
				else {
					HealDainatum(21535);
				}
			}
		}, 1000, 10000);
	}

	private void CancelTask() {
		if (SkillTasks != null && !SkillTasks.isCancelled()) {
			SkillTasks.cancel(true);
		}
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		CancelTask();
		isCancelled = true;
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
	}
}
