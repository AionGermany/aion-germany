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
package quest.kroban_base;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _28996TheUnstableFragment extends QuestHandler {

	public static final int questId = 28996;

	public _28996TheUnstableFragment() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806079).addOnQuestStart(questId); // Peregran
		qe.registerQuestNpc(806253).addOnTalkEvent(questId); // Viente
		qe.registerQuestNpc(834035).addOnTalkEvent(questId); // Lorako
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); // Peregran
		qe.registerQuestNpc(703290).addOnKillEvent(questId); // Barricade on Darkspore Road
		qe.registerQuestNpc(703292).addOnKillEvent(questId); // Barricade at Timolia Mine
		qe.registerQuestNpc(243683).addOnKillEvent(questId); // Brigade General Tahabata
		qe.registerQuestNpc(243684).addOnKillEvent(questId); // Artefact Guardian Kroban
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
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
			int var = qs.getQuestVarById(0);
			if (targetId == 806253) { // Viente
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
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
			else if (targetId == 834035) { // Lorako
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO2: {
						qs.setQuestVar(2);
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
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		int var1 = qs.getQuestVarById(1);
		int var2 = qs.getQuestVarById(2);

		if (var == 2) {
			switch (targetId) {
				case 703290: {
					if (var1 < 0) {
						return defaultOnKillEvent(env, 703290, 0, 0, 1);
					}
					else if (var1 == 0) {
						if (var2 == 1) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return true;
						}
						else {
							return defaultOnKillEvent(env, 703290, 0, 1, 1);
						}
					}
					break;
				}
				case 703292: {
					if (var2 < 0) {
						return defaultOnKillEvent(env, 703292, 0, 0, 2);
					}
					else if (var2 == 0) {
						if (var1 == 1) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return true;
						}
						else {
							return defaultOnKillEvent(env, 703292, 0, 1, 2);
						}
					}
					break;
				}
			}
		}
		else if (var == 3) {
			if (var1 >= 0 && var1 < 0) {
				return defaultOnKillEvent(env, 243683, var1, var1 + 1, 1);
			}
			else if (var1 == 0) {
				qs.setQuestVar(4);
				updateQuestStatus(env);
				return true;
			}
		}
		else if (var == 4) {
			if (var1 >= 0 && var1 < 0) {
				return defaultOnKillEvent(env, 243684, var1, var1 + 1, 1);
			}
			else if (var1 == 0) {
				qs.setQuestVar(5);
				qs.setStatus(QuestStatus.REWARD); // reward
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
