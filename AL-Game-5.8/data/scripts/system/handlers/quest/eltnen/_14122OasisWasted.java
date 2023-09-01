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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author FrozenKiller
 */
public class _14122OasisWasted extends QuestHandler {

	private final static int questId = 14122;

	public _14122OasisWasted() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203917).addOnQuestStart(questId);
		qe.registerQuestNpc(203917).addOnTalkEvent(questId); // Gaia
		qe.registerQuestNpc(203992).addOnTalkEvent(questId); // Ophelos
		qe.registerQuestNpc(203987).addOnTalkEvent(questId); // Heratos
		qe.registerQuestNpc(203934).addOnTalkEvent(questId); // Sirink
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203917) { // Gaia
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 182215480, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203992) { // Ophelos
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1); // 1
				}
			}
			else if (targetId == 203987) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == DialogAction.SETPRO2) {
					return defaultCloseDialog(env, 1, 2); // 2
				}
			}
			else if (targetId == 203934) { // Sirink
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					return checkQuestItems(env, 2, 2, true, 5, 2716); // reward
				}
				else if (env.getDialog() == DialogAction.FINISH_DIALOG) {
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203934) { // Sirink
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
