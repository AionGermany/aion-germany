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

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("kuhara_bomb")
// 282396
public class KuharaBombAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isDestroyed = new AtomicBoolean(false);
	private Npc boss;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		this.setStateIfNot(AIState.FOLLOWING);
		boss = getPosition().getWorldMapInstance().getNpc(217311);
	}

	@Override
	protected void handleMoveArrived() {
		if (isDestroyed.compareAndSet(false, true)) {
			if (boss != null && !CreatureActions.isAlreadyDead(boss)) {
				SkillEngine.getInstance().getSkill(getOwner(), 19659, 60, boss).useNoAnimationSkill();
			}
		}
	}
}
