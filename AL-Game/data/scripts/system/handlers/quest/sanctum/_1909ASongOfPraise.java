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
package quest.sanctum;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke
 * @modified Nephis
 */
public class _1909ASongOfPraise extends QuestHandler {

	private final static int questId = 1909;

	public _1909ASongOfPraise() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203739).addOnQuestStart(questId);
		qe.registerQuestNpc(203739).addOnTalkEvent(questId);
		qe.registerQuestNpc(203726).addOnTalkEvent(questId);
		qe.registerQuestNpc(203099).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 203739) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (env.getTargetId() == 203726) {
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					defaultCloseDialog(env, 0, 1, 182206001, 1, 0, 0);
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (env.getTargetId() == 203099) {
			if (qs != null) {
				if (env.getDialog() == DialogAction.QUEST_SELECT && qs.getStatus() == QuestStatus.START) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id() && qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
					return defaultCloseDialog(env, 1, 2, true, true, 0, 0, 0, 182206001, 1);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
