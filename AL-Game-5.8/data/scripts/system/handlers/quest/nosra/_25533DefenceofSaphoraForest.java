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
package quest.nosra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ghost_KNA
 */
public class _25533DefenceofSaphoraForest extends QuestHandler {

	private final static int questId = 25533;
	int[] mobs = { 240467, 240468, 241646, 241647, 241648, 240469, 240470, 241649, 241650, 241651, 237613, 237614, 237615, 237616, 237617, 242484, 242485, 242486, 242487, 237618, 237619, 237620, 237621, 237622, 242504, 242505, 242506, 242507, 237623, 237624, 237625, 237626, 237627, 242524, 242525, 242526, 242527, 238840, 238841, 238842, 238843, 238844, 242488, 242489, 242490, 242491, 238845, 238846, 238847, 238848, 238849, 238850, 238851, 238852, 238853, 238854, 242492, 242493, 242494, 242495, 238855, 238856, 238857, 238858, 238859, 242496, 242497, 242498, 242499, 238860, 238861, 238862, 238863, 238864, 242500, 242501, 242502, 242503, 238865, 238866, 238867, 238868, 238869, 238870, 238871, 238872, 238873, 238874, 242508, 242509, 242510, 242511, 238875, 238876, 238877, 238878, 238879, 238880, 238881, 238882, 238883, 238884, 242512, 242513, 242514, 242515, 238885, 238886, 238887, 238888, 238889, 242516, 242517, 242518, 242519, 238890, 238891, 238892, 238893, 238894, 242520, 242521, 242522, 242523, 238895, 238896, 238897, 238898, 238899, 238900, 238901, 238902, 238903, 238904, 242528, 242529, 242530, 242531, 238905, 238906, 238907, 238908, 238909, 238910, 238911, 238912, 238913,
		238914, 242532, 242533, 242534, 242535, 238915, 238916, 238917, 238918, 238919, 242536, 242537, 242538, 242539, 238920, 238921, 238922, 238923, 238924, 242540, 242541, 242542, 242543, 238925, 238926, 238927, 238928, 238929 };

	public _25533DefenceofSaphoraForest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806112).addOnQuestStart(questId); // Phiorni
		qe.registerQuestNpc(806112).addOnTalkEvent(questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806112) { // Phiorni
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806112) { // Phiorni
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806112) { // Phiorni
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 59) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 59) {
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
