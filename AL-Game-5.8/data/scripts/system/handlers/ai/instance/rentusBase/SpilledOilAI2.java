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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("spilled_oil")
// 282395
public class SpilledOilAI2 extends AggressiveNpcAI2 {

	private int count;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startEventTask();
	}

	private void startEventTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					count++;
					if (count < 1) {
						SkillEngine.getInstance().getSkill(getOwner(), 19658, 60, getOwner()).useNoAnimationSkill();
						startEventTask();
					}
					else {
						delete();
					}
				}

			}
		}, 1000);
	}

	private void delete() {
		AI2Actions.deleteOwner(this);
	}
}
