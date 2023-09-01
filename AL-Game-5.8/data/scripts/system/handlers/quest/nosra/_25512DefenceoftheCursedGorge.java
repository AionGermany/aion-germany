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
public class _25512DefenceoftheCursedGorge extends QuestHandler {

	private final static int questId = 25512;
	int[] mobs = { 240411, 240412, 241547, 241548, 241549, 240413, 240414, 241550, 241551, 241552, 241181, 241182, 243269, 243270, 243271, 237450, 237451, 237452, 237453, 237454, 241944, 241945, 241946, 241947, 237455, 237456, 237457, 237458, 237459, 241964, 241965, 241966, 241967, 237460, 237461, 237462, 237463, 237464, 241984, 241985, 241986, 241987, 237862, 237863, 237864, 237865, 237866, 241948, 241949, 241950, 241951, 237867, 237868, 237869, 237870, 237871, 237872, 237873, 237874, 237875, 237876, 241952, 241953, 241954, 241955, 237877, 237878, 237879, 237880, 237881, 241956, 241957, 241958, 241959, 237882, 237883, 237884, 237885, 237886, 241960, 241961, 241962, 241963, 237887, 237888, 237889, 237890, 237891, 237892, 237893, 237894, 237895, 237896, 241968, 241969, 241970, 241971, 237897, 237898, 237899, 237900, 237901, 237902, 237903, 237904, 237905, 237906, 241972, 241973, 241974, 241975, 237907, 237908, 237909, 237910, 237911, 241976, 241977, 241978, 241979, 237912, 237913, 237914, 237915, 237916, 241980, 241981, 241982, 241983, 237917, 237918, 237919, 237920, 237921, 237922, 237923, 237924, 237925, 237926, 241988, 241989, 241990, 241991, 237927, 237928, 237929, 237930,
		237931, 237932, 237933, 237934, 237935, 237936, 241992, 241993, 241994, 241995, 237937, 237938, 237939, 237940, 237941, 241996, 241997, 241998, 241999, 237942, 237943, 237944, 237945, 237946, 242000, 242001, 242002, 242003, 237947, 237948, 237949, 237950, 237951 };

	public _25512DefenceoftheCursedGorge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806105).addOnQuestStart(questId); // Bline
		qe.registerQuestNpc(806105).addOnTalkEvent(questId);
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
			if (targetId == 806105) { // Bline
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806105) { // Bline
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
			if (targetId == 806105) { // Bline
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
