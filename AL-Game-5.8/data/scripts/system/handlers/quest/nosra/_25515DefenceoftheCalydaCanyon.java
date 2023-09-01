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
public class _25515DefenceoftheCalydaCanyon extends QuestHandler {

	private final static int questId = 25515;
	int[] mobs = { 240419, 240420, 241559, 241560, 241561, 240421, 240422, 241562, 241563, 241564, 240423, 240424, 241565, 241566, 241567, 240425, 240426, 241568, 241569, 241570, 240787, 240788, 241571, 241572, 241573, 237465, 237466, 237467, 237468, 237469, 237470, 242004, 242005, 242006, 242007, 243559, 243560, 243561, 243562, 237471, 237472, 237473, 237474, 237475, 237476, 242024, 242025, 242026, 242027, 243579, 243580, 243581, 243582, 237477, 237478, 237479, 237480, 237481, 237482, 242044, 242045, 242046, 242047, 243599, 243600, 243601, 243602, 237483, 237484, 237485, 237486, 237487, 237488, 242064, 242065, 242066, 242067, 243619, 243620, 243621, 243622, 237952, 237953, 237954, 237955, 237956, 237957, 237958, 237959, 237960, 237961, 237962, 237963, 237964, 237965, 237966, 237967, 237968, 237969, 237970, 237971, 237972, 237973, 237974, 237975, 237976, 237977, 237978, 237979, 237980, 237981, 237982, 237983, 237984, 237985, 237986, 237987, 242008, 242009, 242010, 242011, 242012, 242013, 242014, 242015, 242016, 242017, 242018, 242019, 242020, 242021, 242022, 242023, 243563, 243564, 243565, 243566, 243567, 243568, 243569, 243570, 243571, 243572, 243573, 243574, 243575, 243576,
		243577, 243578, 237988, 237989, 237990, 237991, 237992, 237993, 237994, 237995, 237996, 237997, 237998, 237999, 238000, 238001, 238002, 238003, 238004, 238005, 238006, 238007, 238008, 238009, 238010, 238011, 238012, 238013, 238014, 238015, 238016, 238017, 238018, 238019, 238020, 238021, 238022, 238023, 242028, 242029, 242030, 242031, 242032, 242033, 242034, 242035, 242036, 242037, 242038, 242039, 242040, 242041, 242042, 242043, 243583, 243584, 243585, 243586, 243587, 243588, 243589, 243590, 243591, 243592, 243593, 243594, 243595, 243596, 243597, 243598, 238024, 238025, 238026, 238027, 238028, 238029, 238030, 238031, 238032, 238033, 238034, 238035, 238036, 238037, 238038, 238039, 238040, 238041, 238042, 238043, 238044, 238045, 238046, 238047, 238048, 238049, 238050, 238051, 238052, 238053, 238054, 238055, 238056, 238057, 238058, 238059, 242048, 242049, 242050, 242051, 242052, 242053, 242054, 242055, 242056, 242057, 242058, 242059, 242060, 242061, 242062, 242063, 243603, 243604, 243605, 243606, 243607, 243608, 243609, 243610, 243611, 243612, 243613, 243614, 243615, 243616, 243617, 243618, 238060, 238061, 238062, 238063, 238064, 238065, 238066, 238067, 238068, 238069, 238070,
		238071, 238072, 238073, 238074, 238075, 238076, 238077, 238078, 238079, 238080, 238081, 238082, 238083, 238084, 238085, 238086, 238087, 238088, 238089, 238090, 238091, 238092, 238093, 238094, 238095, 242068, 242069, 242070, 242071, 242072, 242073, 242074, 242075, 242076, 242077, 242078, 242079, 242080, 242081, 242082, 242083, 243623, 243624, 243625, 243626, 243627, 243628, 243629, 243630, 243631, 243632, 243633, 243634, 243635, 243636, 243637, 243638 };

	public _25515DefenceoftheCalydaCanyon() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806106).addOnQuestStart(questId); // Kenkar
		qe.registerQuestNpc(806106).addOnTalkEvent(questId);
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
			if (targetId == 806106) { // Kenkar
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806106) { // Kenkar
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
			if (targetId == 806106) { // Kenkar
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
