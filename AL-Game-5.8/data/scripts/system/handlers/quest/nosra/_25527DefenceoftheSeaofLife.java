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
public class _25527DefenceoftheSeaofLife extends QuestHandler {

	private final static int questId = 25527;
	int[] mobs = { 240449, 240450, 241610, 241611, 241612, 240451, 240452, 241613, 241614, 241615, 240453, 240454, 241616, 241617, 241618, 241189, 241190, 243281, 243282, 243283, 237550, 237551, 237552, 237553, 237554, 242284, 242285, 242286, 242287, 237555, 237556, 237557, 237558, 237559, 242304, 242305, 242306, 242307, 237560, 237561, 237562, 237563, 237564, 242324, 242325, 242326, 242327, 238462, 238463, 238464, 238465, 238466, 242288, 242289, 242290, 242291, 238467, 238468, 238469, 238470, 238471, 238472, 238473, 238474, 238475, 238476, 242292, 242293, 242294, 242295, 238477, 238478, 238479, 238480, 238481, 242296, 242297, 242298, 242299, 238482, 238483, 238484, 238485, 238486, 242300, 242301, 242302, 242303, 238487, 238488, 238489, 238490, 238491, 238492, 238493, 238494, 238495, 238496, 242308, 242309, 242310, 242311, 238497, 238498, 238499, 238500, 238501, 238502, 238503, 238504, 238505, 238506, 242312, 242313, 242314, 242315, 238507, 238508, 238509, 238510, 238511, 242316, 242317, 242318, 242319, 238512, 238513, 238514, 238515, 238516, 242320, 242321, 242322, 242323, 238517, 238518, 238519, 238520, 238521, 238522, 238523, 238524, 238525, 238526, 242328, 242329, 242330,
		242331, 238527, 238528, 238529, 238530, 238531, 238532, 238533, 238534, 238535, 238536, 242332, 242333, 242334, 242335, 238537, 238538, 238539, 238540, 238541, 242336, 242337, 242338, 242339, 238542, 238543, 238544, 238545, 238546, 242340, 242341, 242342, 242343, 238547, 238548, 238549, 238550, 238551 };

	public _25527DefenceoftheSeaofLife() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806110).addOnQuestStart(questId); // Scorvio
		qe.registerQuestNpc(806110).addOnTalkEvent(questId);
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
			if (targetId == 806110) { // Scorvio
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806110) { // Scorvio
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
			if (targetId == 806110) { // Scorvio
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
