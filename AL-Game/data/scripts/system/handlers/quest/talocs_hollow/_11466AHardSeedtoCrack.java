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
package quest.talocs_hollow;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author mr.madison
 */
public class _11466AHardSeedtoCrack extends QuestHandler {

	private final static int questId = 11466;

	public _11466AHardSeedtoCrack() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182209524, questId);
		qe.registerQuestNpc(279000).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (targetId == 0) {
			if (env.getDialogId() == DialogAction.QUEST_ACCEPT_1.id()) {
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
			else if (env.getDialogId() == DialogAction.QUEST_REFUSE_1.id()) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return false;
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 279000) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 2375);
					case SELECT_QUEST_REWARD: {
						removeQuestItem(env, 182209524, 1);
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 279000) {
				return sendQuestEndDialog(env);
			}

		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182209524) {
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
				sendQuestDialog(env, 4);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}
