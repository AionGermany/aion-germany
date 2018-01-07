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
package quest.inggison;

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
 * @author VladimirZ
 */
public class _11031CanIEatIt extends QuestHandler {

	private final static int questId = 11031;

	public _11031CanIEatIt() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 798959 };
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestItem(182206724, questId);
		qe.registerQuestNpc(798959).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		if (sendQuestNoneDialog(env, 798959, 4762)) {
			return true;
		}

		final Player player = env.getPlayer();

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (env.getTargetId() == 798959) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
			return false;
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (env.getTargetId()) {
				case 798959:
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case CHECK_USER_HAS_QUEST_ITEM:
							if (var == 0) {
								if (QuestService.collectItemCheck(env, true)) {
									qs.setQuestVarById(0, var + 1);
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
									return true;
								}
								else {
									return sendQuestDialog(env, 10001);
								}
							}
						case SETPRO2:
							if (var == 1) {
								if (!giveQuestItem(env, 182206724, 1)) {
									return true;
								}
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
							}
							return true;
						default:
							break;
					}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182206724) {
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
				removeQuestItem(env, 182206724, 1);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}, 1000);
		return HandlerResult.SUCCESS;
	}
}
