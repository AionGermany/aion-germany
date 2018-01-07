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
package quest.gelkmaros;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author VladimirZ
 */
public class _21004VillageStatusReport extends QuestHandler {

	private final static int questId = 21004;

	public _21004VillageStatusReport() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 799227, 799268, 799269 };
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(799227).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		if (sendQuestNoneDialog(env, 799227)) {
			return true;
		}

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 799268) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					case SETPRO1:
						return defaultCloseDialog(env, 0, 1);
					default:
						break;
				}
			}
			else if (env.getTargetId() == 799269) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO2:
						return defaultCloseDialog(env, 1, 2, false, false);
					default:
						break;
				}
			}
			else if (env.getTargetId() == 799227) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 2375);
						}
					case SELECT_QUEST_REWARD:
						return defaultCloseDialog(env, 2, 2, true, true);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (env.getTargetId() == 799227) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
