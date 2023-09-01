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
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Maestros, Falke_34
 */
@AIName("coinfountainreshanta") // 730142, 730143
public class CoinFountainReshantaAI2 extends NpcAI2 {

	// Quest 1717 = Silber to Gold - dialogId=10000
	// Quest 1889 - Gold to ???? - dialogId=10001

	@Override
	protected void handleDialogStart(final Player player) {

		if (player.getController().hasScheduledTask(TaskId.ACTION_ITEM_NPC)) {
			return;
		}

		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 1000, 1));
		player.getController().addTask(TaskId.ACTION_ITEM_NPC, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT));
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 1000, 2));
				// check why not have look animation.
				PacketSendUtility.sendPacket(player, new SM_LOOKATOBJECT(getOwner()));

				if (player.getCommonData().getLevel() < 25 || !hasItem(player, 186000031)) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10, 0));
					return;
				}

				if (player.getRace() == Race.ASMODIANS) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 2717));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 1717));
				}
			}
		}, 1000));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		switch (dialogId) {
			case 10000: // Silver Medal to Gold Medal
				if (player.getRace() == Race.ASMODIANS) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 2717));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 1717));
				}
				break;
			// case 10001:
			// if (player.getRace() == Race.ASMODIANS) {
			// PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 2889));
			// } else {
			// PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, 1889));
			// }
			// break;
			case 23:
				if (!player.getInventory().decreaseByItemId(186000031, 1)) {
					return true;
				}
				giveItem(player);
				// player.getCommonData().addExp(CoinFountainConfig.MEDAL_EXP_RESHANTA, RewardType.QUEST); // TODO - EXP Reward Config?
				player.getCommonData().addExp(104390, RewardType.QUEST);
				if (player.getRace() == Race.ASMODIANS) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 2717));
				}
				else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, 1717));
				}
				break;
		}
		return true;
	}

	private boolean hasItem(Player player, int itemId) {
		return player.getInventory().getItemCountByItemId(itemId) > 0;
	}

	private void giveItem(Player player) {
		int rnd = Rnd.get(0, 100);
		if (rnd < 15) {
			ItemService.addItem(player, 186000030, 1); // Gold Medal
		}
		else {
			ItemService.addItem(player, 182005205, 1); // Rusted Medal
		}
	}
}
