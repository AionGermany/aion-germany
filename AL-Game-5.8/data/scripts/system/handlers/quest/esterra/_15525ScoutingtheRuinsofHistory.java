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
public class _15525ScoutingtheRuinsofHistory extends QuestHandler {

	public _15525ScoutingtheRuinsofHistory() {
		super(questId);
	}

	private final static int questId = 15525;
	int[] mobs = { 240443, 240444, 241601, 241602, 241603, 240445, 240446, 241604, 241605, 241606, 241187, 241188, 243278, 243279, 243280, 237522, 237523, 237524, 237525, 237526, 237527, 237528, 242204, 242205, 242206, 242207, 237529, 237530, 237531, 237532, 237533, 237534, 237535, 242224, 242225, 242226, 242227, 237536, 237537, 237538, 237539, 237540, 237541, 237542, 242244, 242245, 242246, 242247, 237543, 237544, 237545, 237546, 237547, 237548, 237549, 242264, 242265, 242266, 242267, 238294, 238295, 238296, 238297, 238298, 238299, 238300, 238301, 238302, 238303, 238304, 238305, 238306, 238307, 238308, 238309, 238310, 238311, 238312, 238313, 238314, 238315, 238316, 238317, 238318, 238319, 238320, 238321, 238322, 238323, 238324, 238325, 238326, 238327, 238328, 238329, 238330, 238331, 238332, 238333, 238334, 238335, 242208, 242209, 242210, 242211, 242212, 242213, 242214, 242215, 242216, 242217, 242218, 242219, 242220, 242221, 242222, 242223, 238336, 238337, 238338, 238339, 238340, 238341, 238342, 238343, 238344, 238345, 238346, 238347, 238348, 238349, 238350, 238351, 238352, 238353, 238354, 238355, 238356, 238357, 238358, 238359, 238360, 238361, 238362, 238363, 238364, 238365,
		238366, 238367, 238368, 238369, 238370, 238371, 238372, 238373, 238374, 238375, 238376, 238377, 242228, 242229, 242230, 242231, 242232, 242233, 242234, 242235, 242236, 242237, 242238, 242239, 242240, 242241, 242242, 242243, 238378, 238379, 238380, 238381, 238382, 238383, 238384, 238385, 238386, 238387, 238388, 238389, 238390, 238391, 238392, 238393, 238394, 238395, 238396, 238397, 238398, 238399, 238400, 238401, 238402, 238403, 238404, 238405, 238406, 238407, 238408, 238409, 238410, 238411, 238412, 238413, 238414, 238415, 238416, 238417, 238418, 238419, 242248, 242249, 242250, 242251, 242252, 242253, 242254, 242255, 242256, 242257, 242258, 242259, 242260, 242261, 242262, 242263, 238420, 238421, 238422, 238423, 238424, 238425, 238426, 238427, 238428, 238429, 238430, 238431, 238432, 238433, 238434, 238435, 238436, 238437, 238438, 238439, 238440, 238441, 238442, 238443, 238444, 238445, 238446, 238447, 238448, 238449, 238450, 238451, 238452, 238453, 238454, 238455, 238456, 238457, 238458, 238459, 238460, 238461, 242268, 242269, 242270, 242271, 242272, 242273, 242274, 242275, 242276, 242277, 242278, 242279, 242280, 242281, 242282, 242283 };

	@Override
	public void register() {
		qe.registerQuestNpc(806097).addOnQuestStart(questId); // Libran
		qe.registerQuestNpc(806097).addOnTalkEvent(questId);
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
			if (targetId == 806097) { // Libran
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806097) { // Libran
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
			if (targetId == 806097) { // Libran
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
