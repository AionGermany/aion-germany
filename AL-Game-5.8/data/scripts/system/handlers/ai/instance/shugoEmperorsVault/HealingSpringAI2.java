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
package ai.instance.shugoEmperorsVault;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author Falke_34
 */
@AIName("healingSpring")
// 832918, 832919
public class HealingSpringAI2 extends NpcAI2 {

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(this, creature);
	}

	protected void checkDistance(NpcAI2 ai, Creature creature) {
		Npc owner = ai.getOwner();
		if (creature == null || creature.getLifeStats() == null) {
			return;
		}
		if (creature.getLifeStats().isAlreadyDead()) {
			return;
		}
		if (!owner.canSee(creature)) {
			return;
		}
		if (!owner.getActiveRegion().isMapRegionActive()) {
			return;
		}
		if (!(creature instanceof Player)) {
			return;
		}
		if (MathUtil.isIn3dRange(owner, creature, 2.0F)) {
			if (GeoService.getInstance().canSee(owner, creature)) {
				if (!creature.getEffectController().hasAbnormalEffect(21837)) {
					AI2Actions.targetCreature(this, creature);
					AI2Actions.useSkill(this, 21837); // Healing Aura
					AI2Actions.targetCreature(this, null);
				}
			}
		}
	}
}
