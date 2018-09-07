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
package ai.worlds;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.NoActionAI2;

/**
 * @author Steve
 */
@AIName("sacred_image")
// 258280, 258281, 258313, 258314
public class SacredImageAI2 extends NoActionAI2 {

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(creature);
	}

	private void checkDistance(Creature creature) {
		int spellid = getOwner().getNpcId() == 258281 ? 20373 : 20374;
		if (creature instanceof Player) {
			Player player = (Player) creature;
			if ((player.getRace().equals(Race.ASMODIANS) && getOwner().getNpcId() == 258281) || (player.getRace().equals(Race.ELYOS) && getOwner().getNpcId() == 258280)) {
				return;
			}
			if (MathUtil.isIn3dRangeLimited(getOwner(), creature, 0, 25)) {
				SkillEngine.getInstance().getSkill(getOwner(), spellid, 65, player).useNoAnimationSkill();
			}
		}
	}

	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}
