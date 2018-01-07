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
public class _15533DefenceoftheMushroomGorge extends QuestHandler {

	public _15533DefenceoftheMushroomGorge() {
		super(questId);
	}

	private final static int questId = 15533;
	int[] mobs = { 240573, 240574, 241815, 241816, 241817, 240575, 240576, 241818, 241819, 241820, 240577, 240578, 241821, 241822, 241823, 239120, 239121, 239122, 239123, 239124, 243204, 243205, 243206, 243207, 239125, 239126, 239127, 239128, 239129, 243224, 243225, 243226, 243227, 239130, 239131, 239132, 239133, 239134, 243244, 243245, 243246, 243247, 240275, 240276, 240277, 240278, 240279, 243208, 243209, 243210, 243211, 240280, 240281, 240282, 240283, 240284, 240285, 240286, 240287, 240288, 240289, 243212, 243213, 243214, 243215, 240290, 240291, 240292, 240293, 240294, 243216, 243217, 243218, 243219, 240295, 240296, 240297, 240298, 240299, 243220, 243221, 243222, 243223, 240300, 240301, 240302, 240303, 240304, 240305, 240306, 240307, 240308, 240309, 243228, 243229, 243230, 243231, 240310, 240311, 240312, 240313, 240314, 240315, 240316, 240317, 240318, 240319, 243232, 243233, 243234, 243235, 240320, 240321, 240322, 240323, 240324, 243236, 243237, 243238, 243239, 240325, 240326, 240327, 240328, 240329, 243240, 243241, 243242, 243243, 240330, 240331, 240332, 240333, 240334, 240335, 240336, 240337, 240338, 240339, 243248, 243249, 243250, 243251, 240340, 240341, 240342, 240343,
		240344, 240345, 240346, 240347, 240348, 240349, 243252, 243253, 243254, 243255, 240350, 240351, 240352, 240353, 240354, 243256, 243257, 243258, 243259, 240355, 240356, 240357, 240358, 240359, 243260, 243261, 243262, 243263, 240360, 240361, 240362, 240363, 240364 };

	@Override
	public void register() {
		qe.registerQuestNpc(806100).addOnQuestStart(questId); // Karnus
		qe.registerQuestNpc(806100).addOnTalkEvent(questId);
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
			if (targetId == 806100) { // Karnus
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806100) { // Karnus
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
			if (targetId == 806100) { // Karnus
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
