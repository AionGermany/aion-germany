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
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Cheatkiller
 */
public class _11143BabyShulackJourney extends QuestHandler {

	private final static int questId = 11143;

	public _11143BabyShulackJourney() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182206866, questId);
		qe.registerQuestNpc(700909).addOnTalkEvent(questId);
		qe.registerQuestNpc(798985).addOnTalkEvent(questId);
		qe.registerQuestNpc(798984).addOnTalkEvent(questId);
		qe.registerQuestNpc(798976).addOnTalkEvent(questId);
		qe.registerQuestNpc(798948).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0) {
				if (dialog == DialogAction.QUEST_ACCEPT_1) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
			else if (targetId == 700909) {
				return giveQuestItem(env, 182206866, 1);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798985) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (dialog == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1);
				}
			}
			else if (targetId == 798984) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1693);
				}
				else if (dialog == DialogAction.SETPRO2) {
					return defaultCloseDialog(env, 1, 2);
				}
			}
			else if (targetId == 798976) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2034);
				}
				else if (dialog == DialogAction.SETPRO3) {
					qs.setQuestVar(3);
					return defaultCloseDialog(env, 3, 3, true, false);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798948) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 2375);
					}
					default: {
						removeQuestItem(env, 182206866, 1);
						return sendQuestEndDialog(env);
					}
				}
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
