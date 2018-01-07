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
package quest.esterra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _15531ScoutingtheBlackManeMountains extends QuestHandler {

	public _15531ScoutingtheBlackManeMountains() {
		super(questId);
	}

	private final static int questId = 15531;
	int[] mobs = { 240459, 240460, 241625, 241626, 241627, 240791, 240792, 241628, 241629, 241630, 240795, 240796, 241631, 241632, 241633, 237565, 237566, 237567, 237568, 237569, 237570, 242344, 242345, 242346, 242347, 238552, 238553, 238554, 238555, 238556, 238557, 238558, 238559, 238560, 238561, 238562, 238563, 238564, 238565, 238566, 238567, 238568, 238569, 238570, 238571, 238572, 238573, 238574, 238575, 238576, 238577, 238578, 238579, 238580, 238581, 238582, 238583, 238584, 238585, 238586, 238587, 242348, 242349, 242350, 242351, 242352, 242353, 242354, 242355, 242356, 242357, 242358, 242359, 242360, 242361, 242362, 242363, 237571, 237572, 237573, 237574, 237575, 237576, 242364, 242365, 242366, 242367, 238588, 238589, 238590, 238591, 238592, 238593, 238594, 238595, 238596, 238597, 238598, 238599, 238600, 238601, 238602, 238603, 238604, 238605, 238606, 238607, 238608, 238609, 238610, 238611, 238612, 238613, 238614, 238615, 238616, 238617, 238618, 238619, 238620, 238621, 238622, 238623, 242368, 242369, 242370, 242371, 242372, 242373, 242374, 242375, 242376, 242377, 242378, 242379, 242380, 242381, 242382, 242383, 237577, 237578, 237579, 237580, 237581, 237582, 242384, 242385,
		242386, 242387, 238624, 238625, 238626, 238627, 238628, 238629, 238630, 238631, 238632, 238633, 238634, 238635, 238636, 238637, 238638, 238639, 238640, 238641, 238642, 238643, 238644, 238645, 238646, 238647, 238648, 238649, 238650, 238651, 238652, 238653, 238654, 238655, 238656, 238657, 238658, 238659, 242388, 242389, 242390, 242391, 242392, 242393, 242394, 242395, 242396, 242397, 242398, 242399, 242400, 242401, 242402, 242403, 237583, 237584, 237585, 237586, 237587, 237588, 242404, 242405, 242406, 242407, 238660, 238661, 238662, 238663, 238664, 238665, 238666, 238667, 238668, 238669, 238670, 238671, 238672, 238673, 238674, 238675, 238676, 238677, 238678, 238679, 238680, 238681, 238682, 238683, 238684, 238685, 238686, 238687, 238688, 238689, 238690, 238691, 238692, 238693, 238694, 238695, 242408, 242409, 242410, 242411, 242412, 242413, 242414, 242415, 242416, 242417, 242418, 242419, 242420, 242421, 242422, 242423 };

	@Override
	public void register() {
		qe.registerQuestNpc(806099).addOnQuestStart(questId); // Satel
		qe.registerQuestNpc(806099).addOnTalkEvent(questId);
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
			if (targetId == 806099) { // Satel
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806099) { // Satel
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
			if (targetId == 806099) { // Satel
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
