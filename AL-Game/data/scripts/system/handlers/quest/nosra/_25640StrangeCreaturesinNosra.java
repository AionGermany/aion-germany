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
 * @author Phantom_KNA
 */
public class _25640StrangeCreaturesinNosra extends QuestHandler {

	private final static int questId = 25640;
	int[] mobs = { 237455, 237456, 237457, 237458, 237459, 241964, 241965, 241966, 241967, 237460, 237461, 237462, 237463, 237464, 241984, 241985, 241986, 241987, 237450, 237451, 237452, 237453, 237454, 241944, 241945, 241946, 241947, 237494, 237495, 237496, 237497, 237498, 238136, 238137, 238138, 238139, 238140, 242104, 242105, 242106, 242107, 242112, 242113, 242114, 242115, 237499, 237500, 237501, 237502, 237503, 242124, 242125, 242126, 242127, 237489, 237490, 237491, 237492, 237493, 242084, 242085, 242086, 242087, 237560, 237561, 237562, 237563, 237564, 242324, 242325, 242326, 242327, 237555, 237556, 237557, 237558, 237559, 242304, 242305, 242306, 242307, 237550, 237551, 237552, 237553, 237554, 242284, 242285, 242286, 242287, 237618, 237619, 237620, 237621, 237622, 242504, 242505, 242506, 242507, 237623, 237624, 237625, 237626, 237627, 242524, 242525, 242526, 242527, 237613, 237614, 237615, 237616, 237617, 242484, 242485, 242486, 242487 };

	public _25640StrangeCreaturesinNosra() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806101).addOnQuestStart(questId); // Badory
		qe.registerQuestNpc(806101).addOnTalkEvent(questId);
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
			if (targetId == 806101) { // Badory
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806101) { // Badory
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
			if (targetId == 806101) { // Badory
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
				if (var1 >= 0 && var1 < 29) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 29) {
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
