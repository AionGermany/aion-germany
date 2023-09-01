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
public class _25525ScoutingtheAetherTemple extends QuestHandler {

	private final static int questId = 25525;
	int[] mobs = { 240549, 240550, 241767, 241768, 241769, 240551, 240552, 241770, 241771, 241772, 239041, 239042, 239043, 239044, 239045, 239046, 239047, 242924, 242925, 242926, 242927, 239048, 239049, 239050, 239051, 239052, 239053, 239054, 242944, 242945, 242946, 242947, 239055, 239056, 239057, 239058, 239059, 239060, 239061, 242964, 242965, 242966, 242967, 239062, 239063, 239064, 239065, 239066, 239067, 239068, 242984, 242985, 242986, 242987, 239801, 239802, 239803, 239804, 239805, 239806, 239807, 239808, 239809, 239810, 239811, 239812, 239813, 239814, 239815, 239816, 239817, 239818, 239819, 239820, 239821, 239822, 239823, 239824, 239825, 239826, 239827, 239828, 239829, 239830, 239831, 239832, 239833, 239834, 239835, 239836, 239837, 239838, 239839, 239840, 239841, 239842, 242928, 242929, 242930, 242931, 242932, 242933, 242934, 242935, 242936, 242937, 242938, 242939, 242940, 242941, 242942, 242943, 239843, 239844, 239845, 239846, 239847, 239848, 239849, 239850, 239851, 239852, 239853, 239854, 239855, 239856, 239857, 239858, 239859, 239860, 239861, 239862, 239863, 239864, 239865, 239866, 239867, 239868, 239869, 239870, 239871, 239872, 239873, 239874, 239875, 239876, 239877,
		239878, 239879, 239880, 239881, 239882, 239883, 239884, 242948, 242949, 242950, 242951, 242952, 242953, 242954, 242955, 242956, 242957, 242958, 242959, 242960, 242961, 242962, 242963, 239885, 239886, 239887, 239888, 239889, 239890, 239891, 239892, 239893, 239894, 239895, 239896, 239897, 239898, 239899, 239900, 239901, 239902, 239903, 239904, 239905, 239906, 239907, 239908, 239909, 239910, 239911, 239912, 239913, 239914, 239915, 239916, 239917, 239918, 239919, 239920, 239921, 239922, 239923, 239924, 239925, 239926, 242968, 242969, 242970, 242971, 242972, 242973, 242974, 242975, 242976, 242977, 242978, 242979, 242980, 242981, 242982, 242983, 239927, 239928, 239929, 239930, 239931, 239932, 239933, 239934, 239935, 239936, 239937, 239938, 239939, 239940, 239941, 239942, 239943, 239944, 239945, 239946, 239947, 239948, 239949, 239950, 239951, 239952, 239953, 239954, 239955, 239956, 239957, 239958, 239959, 239960, 239961, 239962, 239963, 239964, 239965, 239966, 239967, 239968, 242988, 242989, 242990, 242991, 242992, 242993, 242994, 242995, 242996, 242997, 242998, 242999, 243000, 243001, 243002, 243003 };

	public _25525ScoutingtheAetherTemple() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806109).addOnQuestStart(questId); // Valanchia
		qe.registerQuestNpc(806109).addOnTalkEvent(questId);
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
			if (targetId == 806109) { // Valanchia
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806109) { // Valanchia
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
			if (targetId == 806109) { // Valanchia
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
