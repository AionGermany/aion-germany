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
package ai.quests;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;

import ai.AggressiveNpcAI2;

/**
 * @author FrozenKiller
 */
@AIName("examscarecrow") // NPC: 836530 QUEST:60003
public class ExamScareCrowAI2 extends AggressiveNpcAI2 {

	int rewardCount = Rnd.get(3, 10);
	int attackCount = 0;

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleAttack(Creature creature) {
		if (creature instanceof Player) {
			Player player = (Player) creature;
			if (player.getQuestStateList().hasQuest(60003) && player.getInventory().getItemCountByItemId(182216247) < 1) {
				attackCount++;
				if (attackCount == rewardCount) {
					ItemService.addItem(player, 182216247, 1);
					rewardCount = Rnd.get(3, 10);
					attackCount = 0;
				}
			}
		}
	}
}
