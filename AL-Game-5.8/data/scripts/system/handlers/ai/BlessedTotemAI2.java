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
package ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author boscar
 */
@AIName("blessed")
// 831990
public class BlessedTotemAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		switch (getNpcId()) {
			case 831990:
				super.handleDialogStart(player);
				break;
			default:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		long badgeCount = (player.getInventory().getFirstItemByItemId(186000343) != null ? player.getInventory().getFirstItemByItemId(186000343).getItemCount() : 0);
		boolean blessedbuff = false;
		DialogAction finalAction = DialogAction.getActionByDialogId(dialogId);

		switch (finalAction) {
			case SETPRO1:
				if (badgeCount >= 1) {
					blessedbuff = true;
				}
				break;
			default:
				break;
		}

		if (blessedbuff) {
			switch (getNpcId()) {
				case 831990:
					SkillEngine.getInstance().getSkill(getOwner(), 21650, 1, player).useWithoutPropSkill(); // Prestigious Blessing
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
					break;
			}
		}
		else {
			PacketSendUtility.sendMessage(player, "Not enough Prestige Badge ...");
		}

		return true;
	}

}
