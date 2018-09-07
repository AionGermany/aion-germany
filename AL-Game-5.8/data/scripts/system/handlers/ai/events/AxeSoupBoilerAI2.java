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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author FrozenKiller
 */

@AIName("AxeSoupBoiler")
public class AxeSoupBoilerAI2 extends NpcAI2 {

	private final static int[] ingredients = { 164002369, 164002370, 164002371, 164002372 };
	private int count = 0;

	@Override
	protected void handleDialogStart(final Player player) {
		if (player.getController().hasScheduledTask(TaskId.ACTION_ITEM_NPC)) {
			return;
		}

		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 3000, 1));
		PacketSendUtility.sendPacket(player, new SM_EMOTION(player.getObjectId(), EmotionType.START_QUESTLOOT, 1));
		player.getController().addTask(TaskId.ACTION_ITEM_NPC, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.sendPacket(player, new SM_EMOTION(player.getObjectId(), EmotionType.END_QUESTLOOT, 1));
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 3000, 2));
				// check why not have look animation.
				PacketSendUtility.sendPacket(player, new SM_LOOKATOBJECT(getOwner()));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
			}
		}, 3000));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		Storage inventory = player.getInventory();
		switch (dialogId) {
			case 10000: {
				if (inventory.getItemCountByItemId(164002369) >= 1) {
					inventory.decreaseByItemId(164002369, 1);
					ItemService.addItem(player, 164002373, 1);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
				}
				break;
			}
			case 10001: {
				for (int items : ingredients) {
					if (inventory.getItemCountByItemId(items) >= 1) {
						count++;
					}
				}
				if (count == 4) {
					for (int items : ingredients) {
						inventory.decreaseByItemId(items, 1);
					}
					ItemService.addItem(player, 164002374, 1);
					count = 0;
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
				}
				break;
			}
			default:
				break;
		}
		return true;
	}
}
