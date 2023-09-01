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
public class _25518DefenceofthePlateauofRetribution extends QuestHandler {

	private final static int questId = 25518;
	int[] mobs = { 240431, 240432, 241580, 241581, 241582, 240789, 240790, 241583, 241584, 241585, 240881, 240882, 241464, 241465, 243380, 243381, 243382, 241466, 241467, 243383, 243384, 243385, 241468, 241469, 243386, 243387, 243388, 237489, 237490, 237491, 237492, 237493, 242084, 242085, 242086, 242087, 237494, 237495, 237496, 237497, 237498, 238136, 238137, 238138, 238139, 238140, 242104, 242105, 242106, 242107, 242112, 242113, 242114, 242115, 237499, 237500, 237501, 237502, 237503, 242124, 242125, 242126, 242127, 238096, 238097, 238098, 238099, 238100, 242088, 242089, 242090, 242091, 238101, 238102, 238103, 238104, 238105, 238106, 238107, 238108, 238109, 238110, 242092, 242093, 242094, 242095, 238111, 238112, 238113, 238114, 238115, 242096, 242097, 242098, 242099, 238116, 238117, 238118, 238119, 238120, 242100, 242101, 242102, 242103, 238121, 238122, 238123, 238124, 238125, 238126, 238127, 238128, 238129, 238130, 242108, 242109, 242110, 242111, 238131, 238132, 238133, 238134, 238135, 238141, 238142, 238143, 238144, 238145, 238146, 238147, 238148, 238149, 238150, 242116, 242117, 242118, 242119, 242120, 242121, 242122, 242123, 238151, 238152, 238153, 238154, 238155, 238156,
		238157, 238158, 238159, 238160, 242128, 242129, 242130, 242131, 238161, 238162, 238163, 238164, 238165, 238166, 238167, 238168, 238169, 238170, 242132, 242133, 242134, 242135, 238171, 238172, 238173, 238174, 238175, 242136, 242137, 242138, 242139, 238176, 238177, 238178, 238179, 238180, 242140, 242141, 242142, 242143, 238181, 238182, 238183, 238184, 238185, 241490, 241491, 243389, 243390, 243391 };

	public _25518DefenceofthePlateauofRetribution() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806107).addOnQuestStart(questId); // Roebe
		qe.registerQuestNpc(806107).addOnTalkEvent(questId);
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
			if (targetId == 806107) { // Roebe
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806107) { // Roebe
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
			if (targetId == 806107) { // Roebe
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
