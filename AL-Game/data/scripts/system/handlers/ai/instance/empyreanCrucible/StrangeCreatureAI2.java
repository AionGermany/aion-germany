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
package ai.instance.empyreanCrucible;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.GeneralNpcAI2;

/**
 * @author Luzien
 */
@AIName("strange_creature")
public class StrangeCreatureAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startEventTask();
		startLifeTask();
	}

	private void startEventTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 341444, getObjectId(), 0, 0);
					SkillEngine.getInstance().getSkill(getOwner(), 17914, 34, getOwner()).useNoAnimationSkill();
				}
			}
		}, 500);
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(StrangeCreatureAI2.this);
			}
		}, 6500);
	}
}
