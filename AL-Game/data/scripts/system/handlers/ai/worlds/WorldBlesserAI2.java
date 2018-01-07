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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI2;

/**
 * @author xTz, modified bobobear
 */
@AIName("world_blesser")
public class WorldBlesserAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		switch (getNpcId()) {
			case 831024: // Renniah
			case 831027: // Karzanke
			case 831028: // Erdat
			case 831029: // Edandos
			case 831030: // Netalion
			case 831031: // Nebrith
				super.handleDialogStart(player);
				break;
			default:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env)) {
			return true;
		}
		if (dialogId == 10000) {
			// int chance = Rnd.get(1, 2);
			// 951: Blessing of Health I, 955: Blessing of Rock I : 3.9
			// 20950 : Blessing of Growth : 4.0
			SkillEngine.getInstance().getSkill(getOwner(), 20950, 1, player).useWithoutPropSkill();
		}
		else if (dialogId == 26 && questId != 80487) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}
