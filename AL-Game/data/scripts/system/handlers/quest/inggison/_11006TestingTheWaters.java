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

/**
 * @author Cheatkiller
 */
public class _11006TestingTheWaters extends QuestHandler {

	private final static int questId = 11006;

	public _11006TestingTheWaters() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182206704, questId);
		qe.registerQuestItem(182206705, questId);
		qe.registerQuestNpc(798940).addOnQuestStart(questId);
		qe.registerQuestNpc(798940).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798940) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env, 182206704, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798940) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == DialogAction.SETPRO2) {
					giveQuestItem(env, 182206705, 1);
					removeQuestItem(env, 182206706, 1);
					return defaultCloseDialog(env, 1, 2);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798940) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				removeQuestItem(env, 182206707, 1);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (item.getItemId() == 182206704) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 0, 1, false, 182206706, 1));
			}
			else if (item.getItemId() == 182206705) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 2, true, 182206707, 1));
			}
		}
		return HandlerResult.FAILED;
	}
}
