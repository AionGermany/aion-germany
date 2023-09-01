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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("shallow_water")
// 855009
public class ShallowWaterAI2 extends NpcAI2 {

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(this, creature);
	}

	/*
	 * Way #1 (More Efficient and Accurate using new mathematics formula)
	 */
	private void checkDistance(NpcAI2 ai, Creature creature) {
		if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
			if (MathUtil.isInAnnulus(creature, new Point3D(264.4382f, 258.58527f, 85.81963f), 30.04288f, 16.70f)) {
				if (creature.getZ() < 86) {
					if (!creature.getEffectController().hasAbnormalEffect(8853)) {
						applyEffect(8853, creature);// SkillId:8853 (Contaminated Ide Pool)
					}
				}
			}
		}
	}

	private void applyEffect(int skillId, Creature creature) {
		SkillEngine.getInstance().applyEffectDirectly(skillId, getOwner(), creature, 0);
	}
}
