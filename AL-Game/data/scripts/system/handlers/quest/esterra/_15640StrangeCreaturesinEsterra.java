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
public class _15640StrangeCreaturesinEsterra extends QuestHandler {

	private final static int questId = 15640;
	int[] mobs = { 238969, 238970, 238971, 238972, 238973, 242664, 242665, 242666, 242667, 238974, 238975, 238976, 238977, 238978, 242684, 242685, 242686, 242687, 238979, 238980, 238981, 238982, 238983, 242704, 242705, 242706, 242707, 239008, 239009, 239010, 239011, 239012, 242804, 242805, 242806, 242807, 239013, 239014, 239015, 239016, 239017, 242824, 242825, 242826, 242827, 239018, 239019, 239020, 239021, 239022, 242844, 242845, 242846, 242847, 239069, 239070, 239071, 239072, 239073, 243004, 243005, 243006, 243007, 239074, 239075, 239076, 239077, 239078, 243024, 243025, 243026, 243027, 239079, 239080, 239081, 239082, 239083, 243044, 243045, 243046, 243047, 239120, 239121, 239122, 239123, 239124, 243204, 243205, 243206, 243207, 239125, 239126, 239127, 239128, 239129, 243224, 243225, 243226, 243227, 239130, 239131, 239132, 239133, 239134, 243244, 243245, 243246, 243247 };

	public _15640StrangeCreaturesinEsterra() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806089).addOnQuestStart(questId); // Aquaris
		qe.registerQuestNpc(806089).addOnTalkEvent(questId);
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
			if (targetId == 806089) { // Aquaris
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806089) { // Aquaris
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
			if (targetId == 806089) { // Aquaris
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
