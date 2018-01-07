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
public class _25537ScoutingTarhaVillage extends QuestHandler {

	private final static int questId = 25537;
	int[] mobs = { 243752, 243753, 243754, 243844, 243845, 243846, 243709, 243710, 243711, 243712, 243713, 243714, 243715, 243716, 243995, 243996, 243717, 243718, 243719, 243720, 243997, 243998, 243721, 243722, 243723, 243724, 243999, 244000, 243725, 243726, 243727, 243728, 244001, 244002, 243729, 243730, 243731, 243732, 243733, 243734, 243735, 243736, 244003, 244004, 243737, 243738, 243739, 243740, 244005, 244006, 243741, 243742, 243743, 243744, 244007, 244008, 243745, 243746, 243747, 243748, 244009, 244010, 243824, 243825, 243826, 243827, 243828, 243829, 243830, 243831, 244011, 244012, 243832, 243833, 243834, 243835, 244013, 244014, 243836, 243837, 243838, 243839, 244015, 244016, 243840, 243841, 243842, 243843, 244017, 244018 };

	public _25537ScoutingTarhaVillage() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806255).addOnQuestStart(questId); // Sinuka
		qe.registerQuestNpc(806255).addOnTalkEvent(questId);
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
			if (targetId == 806255) { // Sinuka
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806255) { // Sinuka
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
			if (targetId == 806255) { // Sinuka
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
