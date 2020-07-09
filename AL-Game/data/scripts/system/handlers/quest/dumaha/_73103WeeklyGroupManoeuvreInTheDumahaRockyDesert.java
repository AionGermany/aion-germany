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
package quest.dumaha;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator 1.15 by Mariella
 */
public class _73103WeeklyGroupManoeuvreInTheDumahaRockyDesert extends QuestHandler {

	private final static int questId = 73103;
	private final static int[] mobs = { 886596, 886599, 886602, 886605, 886713, 886716, 886719, 886722, 886791, 886794, 886797, 886800, 886830, 886833, 886836, 886839, 886908, 886911, 886914, 886917, 886597, 886600, 886603, 886606, 886714, 886717, 886720, 886723, 886792, 886795, 886798, 886801, 886831, 886834, 886837, 886840, 886909, 886912, 886915, 886918 };

	public _73103WeeklyGroupManoeuvreInTheDumahaRockyDesert() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(807053).addOnTalkEvent(questId);  // Zella

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1000, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 807053: {
					switch (dialog) {
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		}

		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var  = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 0, Count: 5, Mobs : 886596, 886599, 886602, 886605, 886713, 886716, 886719, 886722, 886791, 886794, 886797, 886800, 886830, 886833, 886836, 886839, 886908, 886911, 886914, 886917, 886597, 886600, 886603, 886606, 886714, 886717, 886720, 886723, 886792, 886795, 886798, 886801, 886831, 886834, 886837, 886840, 886909, 886912, 886915, 886918

			if (var == 0 && var1 < 4) {
				return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
			} else {
				qs.setQuestVar(1);
				updateQuestStatus(env);
			}
		}
		return false;
	}
}
