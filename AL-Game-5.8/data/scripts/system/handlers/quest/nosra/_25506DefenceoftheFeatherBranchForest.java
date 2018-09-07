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
public class _25506DefenceoftheFeatherBranchForest extends QuestHandler {

	private final static int questId = 25506;
	int[] mobs = { 240395, 240396, 241523, 241524, 241525, 240397, 240398, 241526, 241527, 241528, 240399, 240400, 241529, 241530, 241531, 241179, 241180, 243266, 243267, 243268, 237411, 237412, 237413, 237414, 237415, 237416, 241824, 241825, 241826, 241827, 237417, 237418, 237419, 237420, 237421, 237422, 241844, 241845, 241846, 241847, 237423, 237424, 237425, 237426, 237427, 237428, 241864, 241865, 241866, 241867, 237628, 237629, 237630, 237631, 237632, 237633, 241828, 241829, 241830, 241831, 237634, 237635, 237636, 237637, 237638, 237639, 237640, 237641, 237642, 237643, 237644, 237645, 241832, 241833, 241834, 241835, 237646, 237647, 237648, 237649, 237650, 237651, 241836, 241837, 241838, 241839, 237652, 237653, 237654, 237655, 237656, 237657, 241840, 241841, 241842, 241843, 237658, 237659, 237660, 237661, 237662, 237663, 237664, 237665, 237666, 237667, 237668, 237669, 241848, 241849, 241850, 241851, 237670, 237671, 237672, 237673, 237674, 237675, 237676, 237677, 237678, 237679, 237680, 237681, 241852, 241853, 241854, 241855, 237682, 237683, 237684, 237685, 237686, 237687, 241856, 241857, 241858, 241859, 237688, 237689, 237690, 237691, 237692, 237693, 241860, 241861, 241862,
		241863, 237694, 237695, 237696, 237697, 237698, 237699, 237700, 237701, 237702, 237703, 237704, 237705, 241868, 241869, 241870, 241871, 237706, 237707, 237708, 237709, 237710, 237711, 237712, 237713, 237714, 237715, 237716, 237717, 241872, 241873, 241874, 241875, 237718, 237719, 237720, 237721, 237722, 237723, 241876, 241877, 241878, 241879, 237724, 237725, 237726, 237727, 237728, 237729, 241880, 241881, 241882, 241883, 237730, 237731, 237732, 237733, 237734, 237735 };

	public _25506DefenceoftheFeatherBranchForest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806103).addOnQuestStart(questId); // Vider
		qe.registerQuestNpc(806103).addOnTalkEvent(questId);
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
			if (targetId == 806103) { // Vider
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806103) { // Vider
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
			if (targetId == 806103) { // Vider
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
