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
package quest.gelkmaros_armor;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 */
public class _21052DragonHuntin extends QuestHandler {

	private final static int questId = 21052;

	public _21052DragonHuntin() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799268).addOnQuestStart(questId);
		qe.registerQuestNpc(799268).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 799268) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case SELECT_ACTION_1012: {
						return sendQuestDialog(env, 1012);
					}
					case ASK_QUEST_ACCEPT: {
						return sendQuestDialog(env, 4);
					}
					case QUEST_ACCEPT_1: {
						return sendQuestStartDialog(env);
					}
					case QUEST_REFUSE_1: {
						return sendQuestDialog(env, 1004);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 799268) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 2375);
					}
					case SELECT_ACTION_2034: {
						return sendQuestDialog(env, 2034);
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, var, var, true, 5, 2716);
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
			if (targetId == 799268) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
