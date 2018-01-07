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
public class _25510ScoutingtheCoastoftheDoomed extends QuestHandler {

	private final static int questId = 25510;
	int[] mobs = { 240509, 240510, 241695, 241696, 241697, 240511, 240512, 241698, 241699, 241700, 240513, 240514, 241701, 241702, 241703, 238948, 238949, 238950, 238951, 238952, 238953, 238954, 242604, 242605, 242606, 242607, 238955, 238956, 238957, 238958, 238959, 238960, 238961, 242624, 242625, 242626, 242627, 238962, 238963, 238964, 238965, 238966, 238967, 238968, 239341, 239342, 239343, 239344, 239345, 239346, 239347, 242644, 242645, 242646, 242647, 242652, 242653, 242654, 242655, 239243, 239244, 239245, 239246, 239247, 239248, 239249, 242608, 242609, 242610, 242611, 239250, 239251, 239252, 239253, 239254, 239255, 239256, 239257, 239258, 239259, 239260, 239261, 239262, 239263, 242612, 242613, 242614, 242615, 239264, 239265, 239266, 239267, 239268, 239269, 239270, 242616, 242617, 242618, 242619, 239271, 239272, 239273, 239274, 239275, 239276, 239277, 242620, 242621, 242622, 242623, 239278, 239279, 239280, 239281, 239282, 239283, 239284, 239285, 239286, 239287, 239288, 239289, 239290, 239291, 242628, 242629, 242630, 242631, 239292, 239293, 239294, 239295, 239296, 239297, 239298, 239299, 239300, 239301, 239302, 239303, 239304, 239305, 242632, 242633, 242634, 242635, 239306,
		239307, 239308, 239309, 239310, 239311, 239312, 242636, 242637, 242638, 242639, 239313, 239314, 239315, 239316, 239317, 239318, 239319, 242640, 242641, 242642, 242643, 239320, 239321, 239322, 239323, 239324, 239325, 239326, 239327, 239328, 239329, 239330, 239331, 239332, 239333, 242648, 242649, 242650, 242651, 239334, 239335, 239336, 239337, 239338, 239339, 239340, 239348, 239349, 239350, 239351, 239352, 239353, 239354, 239355, 239356, 239357, 239358, 239359, 239360, 239361, 242656, 242657, 242658, 242659, 242660, 242661, 242662, 242663, 239362, 239363, 239364, 239365, 239366, 239367, 239368 };

	public _25510ScoutingtheCoastoftheDoomed() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806104).addOnQuestStart(questId); // Chelles
		qe.registerQuestNpc(806104).addOnTalkEvent(questId);
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
			if (targetId == 806104) { // Chelles
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806104) { // Chelles
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
			if (targetId == 806104) { // Chelles
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
