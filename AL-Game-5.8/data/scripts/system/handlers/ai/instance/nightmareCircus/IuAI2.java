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
package ai.instance.nightmareCircus;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@AIName("iu")
public class IuAI2 extends NpcAI2 {

	@Override
	public void handleSpawned() {
		startSchedule();
	}

	private void startSchedule() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				checkForHeal();
			}
		}, 10000);
	}

	private void checkForHeal() {
		if (!isAlreadyDead() && getPosition().isSpawned()) {
			for (VisibleObject object : getKnownList().getKnownObjects().values()) {
				Creature creature = (Creature) object;
				CreatureLifeStats<?> lifeStats = creature.getLifeStats();
				if (isInRange(creature, 10) && !creature.getEffectController().hasAbnormalEffect(21363) && !lifeStats.isAlreadyDead() && (lifeStats.getCurrentHp() < lifeStats.getMaxHp())) {
					if (creature instanceof Player) {
						doHeal(creature);
						break;
					}
				}
			}
			startSchedule();
		}
	}

	private void doHeal(Creature creature) {
		AI2Actions.targetSelf(this);
		AI2Actions.useSkill(this, 21363);
		apllyEffect(creature, 21363);
	}

	public void playerSkillUse(Player player, int skillId) {
		apllyEffect(player, skillId);
	}

	private void apllyEffect(Creature creature, int skillId) {
		SkillEngine.getInstance().applyEffectDirectly(skillId, getOwner(), creature, 0);
	}
}
