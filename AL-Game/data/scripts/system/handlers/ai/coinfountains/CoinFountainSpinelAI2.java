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
package ai.coinfountains;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Maestros, Falke_34
 */
@AIName("coinfountainspinel") // 805778, 805753
public class CoinFountainSpinelAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		if (player.getCommonData().getLevel() >= 65 && (player.getRace() == Race.ASMODIANS)) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 25667));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 15667));
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		switch (dialogId) {
			case 10000:
				if (hasItem(player, 186000242) && (player.getRace() == Race.ASMODIANS)) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 25667));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 15667));
				}

				break;
			case 23:
				if (!player.getInventory().decreaseByItemId(186000242, 1)) { // Ceramium Medal
					return true;
				}
				giveItem(player);
				// player.getCommonData().addExp(CoinFountainConfig.MEDAL_EXP_ESTERRA_NOSRA, RewardType.QUEST); // TODO - EXP Reward Config?
				player.getCommonData().addExp(1043900, RewardType.QUEST);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 25667));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 15667));
				break;
		}
		return true;
	}

	private boolean hasItem(Player player, int itemId) {
		return player.getInventory().getItemCountByItemId(itemId) > 0;
	}

	private void giveItem(Player player) {
		int rnd = Rnd.get(0, 100);
		if (rnd < 10) {
			ItemService.addItem(player, 186000454, 1); // Spinel Medal
		}
		else {
			ItemService.addItem(player, 182005205, 1); // Rusted Medal
		}
	}
}
