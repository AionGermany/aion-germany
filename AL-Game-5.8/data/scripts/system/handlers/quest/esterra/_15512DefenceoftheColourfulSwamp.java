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
public class _15512DefenceoftheColourfulSwamp extends QuestHandler {

	public _15512DefenceoftheColourfulSwamp() {
		super(questId);
	}

	private final static int questId = 15512;
	int[] mobs = { 240517, 240518, 241707, 241708, 241709, 240519, 240520, 241710, 241711, 241712, 240521, 240522, 241713, 241714, 241715, 241236, 241237, 241716, 241717, 241718, 238969, 238970, 238971, 238972, 238973, 242664, 242665, 242666, 242667, 238974, 238975, 238976, 238977, 238978, 242684, 242685, 242686, 242687, 238979, 238980, 238981, 238982, 238983, 242704, 242705, 242706, 242707, 239369, 239370, 239371, 239372, 239373, 242668, 242669, 242670, 242671, 239374, 239375, 239376, 239377, 239378, 239379, 239380, 239381, 239382, 239383, 242672, 242673, 242674, 242675, 239384, 239385, 239386, 239387, 239388, 242676, 242677, 242678, 242679, 239389, 239390, 239391, 239392, 239393, 242680, 242681, 242682, 242683, 239394, 239395, 239396, 239397, 239398, 239399, 239400, 239401, 239402, 239403, 242688, 242689, 242690, 242691, 239404, 239405, 239406, 239407, 239408, 239409, 239410, 239411, 239412, 239413, 242692, 242693, 242694, 242695, 239414, 239415, 239416, 239417, 239418, 242696, 242697, 242698, 242699, 239419, 239420, 239421, 239422, 239423, 242700, 242701, 242702, 242703, 239424, 239425, 239426, 239427, 239428, 239429, 239430, 239431, 239432, 239433, 242708, 242709, 242710,
		242711, 239434, 239435, 239436, 239437, 239438, 239439, 239440, 239441, 239442, 239443, 242712, 242713, 242714, 242715, 239444, 239445, 239446, 239447, 239448, 242716, 242717, 242718, 242719, 239449, 239450, 239451, 239452, 239453, 242720, 242721, 242722, 242723, 239454, 239455, 239456, 239457, 239458 };

	@Override
	public void register() {
		qe.registerQuestNpc(806093).addOnQuestStart(questId); // Hemelos
		qe.registerQuestNpc(806093).addOnTalkEvent(questId);
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
			if (targetId == 806093) { // Hemelos
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806093) { // Hemelos
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
			if (targetId == 806093) { // Hemelos
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
