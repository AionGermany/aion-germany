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
package quest.eltnen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mariella
 */
public class _1323LostJewelBox extends QuestHandler {

	private final static int questId = 1323;

	public _1323LostJewelBox() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182201309, questId);
		qe.registerQuestNpc(730032).addOnTalkEvent(questId); // Jewel Box
		qe.registerQuestNpc(730019).addOnTalkEvent(questId); // Lodas
		qe.registerQuestNpc(203939).addOnTalkEvent(questId); // Justachys
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		int targetId = env.getTargetId();
		int var = 0;

		if (qs != null)
			var = qs.getQuestVarById(0);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			// quest is not active
			if (targetId == 0) { // Item 182201309
				switch (dialog) {
					case QUEST_ACCEPT_1: {
						QuestService.startQuest(env);
						return closeDialogWindow(env);
					}
					default: {
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 730032) { // Jewel Box
				switch (dialog) {
					case USE_OBJECT: {
						return giveQuestItem(env, 182201309, 1);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			// quest is active (accepted)
			if (targetId == 730019) { // Lodas
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1352);
					}
					case SETPRO1: {
						qs.setQuestVarById(0, 1); // 1
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					default:
						break;
				}
			}
			else if (targetId == 203939) { // Justachys
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							qs.setQuestVarById(0, 2); // 2
							updateQuestStatus(env);
							return sendQuestDialog(env, 2375);
						}
						break;
					}
					case SELECT_QUEST_REWARD: {
						removeQuestItem(env, 182201309, 1);
						return defaultCloseDialog(env, 2, 3, true, false);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) { // Reward
			if (targetId == 203939) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}
