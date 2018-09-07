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
public class _15522ScoutingtheForestofIllusions extends QuestHandler {

	public _15522ScoutingtheForestofIllusions() {
		super(questId);
	}

	private final static int questId = 15522;
	int[] mobs = { 240435, 240436, 241589, 241590, 241591, 240437, 240438, 241592, 241593, 241594, 240439, 240440, 241595, 241596, 241597, 241183, 241184, 243272, 243273, 243274, 237504, 237505, 237506, 237507, 237508, 237509, 242144, 242145, 242146, 242147, 237510, 237511, 237512, 237513, 237514, 237515, 242164, 242165, 242166, 242167, 237516, 237517, 237518, 237519, 237520, 237521, 242184, 242185, 242186, 242187, 238186, 238187, 238188, 238189, 238190, 238191, 242148, 242149, 242150, 242151, 238192, 238193, 238194, 238195, 238196, 238197, 238198, 238199, 238200, 238201, 238202, 238203, 242152, 242153, 242154, 242155, 238204, 238205, 238206, 238207, 238208, 238209, 242156, 242157, 242158, 242159, 238210, 238211, 238212, 238213, 238214, 238215, 242160, 242161, 242162, 242163, 238216, 238217, 238218, 238219, 238220, 238221, 238222, 238223, 238224, 238225, 238226, 238227, 242168, 242169, 242170, 242171, 238228, 238229, 238230, 238231, 238232, 238233, 238234, 238235, 238236, 238237, 238238, 238239, 242172, 242173, 242174, 242175, 238240, 238241, 238242, 238243, 238244, 238245, 242176, 242177, 242178, 242179, 238246, 238247, 238248, 238249, 238250, 238251, 242180, 242181, 242182,
		242183, 238252, 238253, 238254, 238255, 238256, 238257, 238258, 238259, 238260, 238261, 238262, 238263, 242188, 242189, 242190, 242191, 238264, 238265, 238266, 238267, 238268, 238269, 238270, 238271, 238272, 238273, 238274, 238275, 242192, 242193, 242194, 242195, 238276, 238277, 238278, 238279, 238280, 238281, 242196, 242197, 242198, 242199, 238282, 238283, 238284, 238285, 238286, 238287, 242200, 242201, 242202, 242203, 238288, 238289, 238290, 238291, 238292, 238293 };

	@Override
	public void register() {
		qe.registerQuestNpc(806096).addOnQuestStart(questId); // Pardenos
		qe.registerQuestNpc(806096).addOnTalkEvent(questId);
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
			if (targetId == 806096) { // Pardenos
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806096) { // Pardenos
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
			if (targetId == 806096) { // Pardenos
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
