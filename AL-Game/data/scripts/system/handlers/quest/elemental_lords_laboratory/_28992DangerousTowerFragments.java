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
package quest.elemental_lords_laboratory;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author FrozenKiller
 */
public class _28992DangerousTowerFragments extends QuestHandler {

	private final static int questId = 28992;
	private final static int[] mobs = { 220424, 220425, 220426 };

	public _28992DangerousTowerFragments() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806079).addOnQuestStart(questId);
		qe.registerQuestNpc(806079).addOnTalkEvent(questId);// Peregran
		qe.registerQuestNpc(806217).addOnTalkEvent(questId);// Gura
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806079) { // Peregran
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806217) { // Gura
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case SETPRO1: {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806079) { // Peregran
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int targetId = env.getTargetId();
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			switch (targetId) {
				case 220424: // Arachne
				case 220425: // Beautiful Galateia
					if (var == 1) {
						if (var1 >= 0 && var1 < 1) {
							qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
							updateQuestStatus(env);
							return true;
						}
						else if (var1 >= 1 && var1 < 2) {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return true;
						}
					}
				case 220426: // Elemental Lord
					if (var == 2) {
						qs.setQuestVar(3);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
			}
		}
		return false;
	}
}
