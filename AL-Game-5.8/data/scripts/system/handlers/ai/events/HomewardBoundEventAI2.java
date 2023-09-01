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
package ai.events;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.GeneralNpcAI2;

/**
 * @author FrozenKiller
 */
@AIName("homeward_bound_event")
public class HomewardBoundEventAI2 extends GeneralNpcAI2 {

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {

		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 833671: {
					if (player.getRace() == Race.ELYOS) {
						SkillEngine.getInstance().getSkill(player, 11047, 1, player).useWithoutPropSkill();
					}
					break;
				}
				case 833672: {
					if (player.getRace() == Race.ELYOS) {
						SkillEngine.getInstance().getSkill(player, 11049, 1, player).useWithoutPropSkill();
					}
					break;
				}
				case 833673: {
					if (player.getRace() == Race.ASMODIANS) {
						SkillEngine.getInstance().getSkill(player, 11048, 1, player).useWithoutPropSkill();
					}
					break;
				}
				case 833674: {
					if (player.getRace() == Race.ASMODIANS) {
						SkillEngine.getInstance().getSkill(player, 11050, 1, player).useWithoutPropSkill();
					}
					break;
				}
				default:
					break;
			}
		}
		else {
			QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
			env.setExtendedRewardIndex(extendedRewardIndex);
			if (QuestEngine.getInstance().onDialog(env)) {
				return true;
			}
		}
		return true;
	}
}
