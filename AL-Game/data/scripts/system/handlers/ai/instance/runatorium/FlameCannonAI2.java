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
package ai.instance.runatorium;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("flame_cannon")
// 702405, 855010, 702387, 702388
public class FlameCannonAI2 extends NpcAI2 {

	private Future<?> task;
	private final int FlameCannonSpawnTime = 1 * 60 * 1000;// Every 1-minute the Flame Cannons must be despawnd (Na Retail v4.7.5.17).

	@Override
	protected void handleSpawned() {

		super.handleSpawned();
		task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getOwner().getController().onDelete();
			}
		}, FlameCannonSpawnTime);// After 1-minute the flames will be disappeared.
	}

	@Override
	public void handleDespawned() {
		cancelTask();
		super.handleDespawned();
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(this, creature);
	}

	private void checkDistance(NpcAI2 ai, Creature creature) {
		if (!creature.getLifeStats().isAlreadyDead()) {
			if (MathUtil.isInSphere(creature, 294.73016f, 324.3288f, 79.23065f, 10f) || MathUtil.isInSphere(creature, 234.40262f, 194.1135f, 79.23065f, 10f)) {
				if (!creature.getEffectController().hasAbnormalEffect(21648)) {
					AI2Actions.targetCreature(this, creature);
					FlameDeathSkill();
				}
			}
		}
	}

	private void FlameDeathSkill() {
		/* SkillId:21648 (Flame Death) */
		SkillEngine.getInstance().getSkill(getOwner(), 21648, 56, getOwner().getTarget()).useNoAnimationSkill();
	}

	private void cancelTask() {
		if (task != null && !task.isDone()) {
			task.cancel(true);
		}
	}
}
