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
package quest.cygnea;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author pralinka
 */
public class _15204SendingAMessage extends QuestHandler {

	private final static int questId = 15204;

	public _15204SendingAMessage() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804704).addOnQuestStart(questId);
		qe.registerQuestNpc(804704).addOnTalkEvent(questId);
		qe.registerOnKillInWorld(210070000, questId);
	}

	@Override
	public boolean onKillInWorldEvent(QuestEnv env) {
		return defaultOnKillRankedEvent(env, 0, 5, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (env.getTargetId() == 804704) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 4762);
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
					default:
						break;
				}
			}
			else if (qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
