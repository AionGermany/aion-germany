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
public class _15510ScoutingoutSphirosTerritory extends QuestHandler {

	public _15510ScoutingoutSphirosTerritory() {
		super(questId);
	}

	private final static int questId = 15510;
	int[] mobs = { 240403, 240404, 241535, 241536, 241537, 240405, 240406, 241538, 241539, 241540, 240407, 240408, 241541, 241542, 241543, 237429, 237430, 237431, 237432, 237433, 237434, 237435, 241884, 241885, 241886, 241887, 237436, 237437, 237438, 237439, 237440, 237441, 237442, 241904, 241905, 241906, 241907, 237443, 237444, 237445, 237446, 237447, 237448, 237449, 237834, 237835, 237836, 237837, 237838, 237839, 237840, 241924, 241925, 241926, 241927, 241932, 241933, 241934, 241935, 237736, 237737, 237738, 237739, 237740, 237741, 237742, 241888, 241889, 241890, 241891, 237743, 237744, 237745, 237746, 237747, 237748, 237749, 237750, 237751, 237752, 237753, 237754, 237755, 237756, 241892, 241893, 241894, 241895, 237757, 237758, 237759, 237760, 237761, 237762, 237763, 241896, 241897, 241898, 241899, 237764, 237765, 237766, 237767, 237768, 237769, 237770, 241900, 241901, 241902, 241903, 237771, 237772, 237773, 237774, 237775, 237776, 237777, 237778, 237779, 237780, 237781, 237782, 237783, 237784, 241908, 241909, 241910, 241911, 237785, 237786, 237787, 237788, 237789, 237790, 237791, 237792, 237793, 237794, 237795, 237796, 237797, 237798, 241912, 241913, 241914, 241915, 237799,
		237800, 237801, 237802, 237803, 237804, 237805, 241916, 241917, 241918, 241919, 237806, 237807, 237808, 237809, 237810, 237811, 237812, 241920, 241921, 241922, 241923, 237813, 237814, 237815, 237816, 237817, 237818, 237819, 237820, 237821, 237822, 237823, 237824, 237825, 237826, 241928, 241929, 241930, 241931, 237827, 237828, 237829, 237830, 237831, 237832, 237833, 237841, 237842, 237843, 237844, 237845, 237846, 237847, 237848, 237849, 237850, 237851, 237852, 237853, 237854, 241936, 241937, 241938, 241939, 241940, 241941, 241942, 241943, 237855, 237856, 237857, 237858, 237859, 237860, 237861 };

	@Override
	public void register() {
		qe.registerQuestNpc(806092).addOnQuestStart(questId); // Tauron
		qe.registerQuestNpc(806092).addOnTalkEvent(questId);
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
			if (targetId == 806092) { // Tauron
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806092) { // Tauron
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
			if (targetId == 806092) { // Tauron
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
