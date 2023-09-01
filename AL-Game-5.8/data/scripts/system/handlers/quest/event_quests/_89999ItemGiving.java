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
package quest.event_quests;

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas
 */
public class _89999ItemGiving extends QuestHandler {

	private final static int questId = 89999;

	public _89999ItemGiving() {
		super(questId);
	}

	@Override
	public void register() {
		// Juice
		qe.registerQuestNpc(799702).addOnTalkEvent(questId); // Laylin (elyos)
		qe.registerQuestNpc(799703).addOnTalkEvent(questId); // Ronya (asmodian)
		// Cakes
		qe.registerQuestNpc(798414).addOnTalkEvent(questId); // Brios (elyos)
		qe.registerQuestNpc(798416).addOnTalkEvent(questId); // Bothen (asmodian)
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		int itemId = 0;
		Player player = env.getPlayer();

		if (env.getTargetId() == 799703 || env.getTargetId() == 799702) {
			itemId = EventsConfig.EVENT_GIVEJUICE;
		}
		else if (env.getTargetId() == 798416 || env.getTargetId() == 798414) {
			itemId = EventsConfig.EVENT_GIVECAKE;
		}

		if (itemId == 0) {
			return false;
		}

		int targetId = env.getVisibleObject().getObjectId();
		switch (env.getDialog()) {
			case USE_OBJECT:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1011, 0));
				return true;
			case SELECT_ACTION_1012: {
				Storage inventory = player.getInventory();
				if (inventory.getItemCountByItemId(itemId) > 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1097, 0));
					return true;
				}
				else {
					if (giveQuestItem(env, itemId, 1)) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1012, 0));
					}
					return true;
				}
			}
			default:
				break;
		}
		return false;

	}
}
