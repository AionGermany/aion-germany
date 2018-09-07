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
public class _25522ScoutingtheForestofLife extends QuestHandler {

	private final static int questId = 25522;
	int[] mobs = { 240543, 240544, 241755, 241756, 241757, 240545, 240546, 241758, 241759, 241760, 239023, 239024, 239025, 239026, 239027, 239028, 242864, 242865, 242866, 242867, 239029, 239030, 239031, 239032, 239033, 239034, 242884, 242885, 242886, 242887, 239035, 239036, 239037, 239038, 239039, 239040, 242904, 242905, 242906, 242907, 239693, 239694, 239695, 239696, 239697, 239698, 242868, 242869, 242870, 242871, 239699, 239700, 239701, 239702, 239703, 239704, 239705, 239706, 239707, 239708, 239709, 239710, 242872, 242873, 242874, 242875, 239711, 239712, 239713, 239714, 239715, 239716, 242876, 242877, 242878, 242879, 239717, 239718, 239719, 239720, 239721, 239722, 242880, 242881, 242882, 242883, 239723, 239724, 239725, 239726, 239727, 239728, 239729, 239730, 239731, 239732, 239733, 239734, 242888, 242889, 242890, 242891, 239735, 239736, 239737, 239738, 239739, 239740, 239741, 239742, 239743, 239744, 239745, 239746, 242892, 242893, 242894, 242895, 239747, 239748, 239749, 239750, 239751, 239752, 242896, 242897, 242898, 242899, 239753, 239754, 239755, 239756, 239757, 239758, 242900, 242901, 242902, 242903, 239759, 239760, 239761, 239762, 239763, 239764, 239765, 239766, 239767,
		239768, 239769, 239770, 242908, 242909, 242910, 242911, 239771, 239772, 239773, 239774, 239775, 239776, 239777, 239778, 239779, 239780, 239781, 239782, 242912, 242913, 242914, 242915, 239783, 239784, 239785, 239786, 239787, 239788, 242916, 242917, 242918, 242919, 239789, 239790, 239791, 239792, 239793, 239794, 242920, 242921, 242922, 242923, 239795, 239796, 239797, 239798, 239799, 239800 };

	public _25522ScoutingtheForestofLife() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806108).addOnQuestStart(questId); // Berju
		qe.registerQuestNpc(806108).addOnTalkEvent(questId);
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
			if (targetId == 806108) { // Berju
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806108) { // Berju
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
			if (targetId == 806108) { // Berju
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
