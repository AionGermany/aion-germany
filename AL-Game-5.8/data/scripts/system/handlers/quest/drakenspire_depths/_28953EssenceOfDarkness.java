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
package quest.drakenspire_depths;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _28953EssenceOfDarkness extends QuestHandler {

	private static final int questId = 28953;

	public _28953EssenceOfDarkness() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(209743).addOnQuestStart(questId);
		qe.registerQuestNpc(209743).addOnTalkEvent(questId);
		qe.registerQuestNpc(804738).addOnTalkEvent(questId);
		qe.registerQuestNpc(702769).addOnTalkEvent(questId); // Ominous Darkness.
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 209743) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
					case QUEST_REFUSE_SIMPLE:
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (targetId == 702769) { // Ominous Darkness.
			if (dialog == DialogAction.USE_OBJECT) {
				closeDialogWindow(env);
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804738) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 0, 0, true, 5, 2716);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804738) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
