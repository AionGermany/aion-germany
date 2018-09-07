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
public class _25536DefenceoftheZenzenTerritory extends QuestHandler {

	private final static int questId = 25536;
	int[] mobs = { 240793, 240794, 241637, 241638, 241639, 237595, 237596, 237597, 237598, 237599, 237600, 242424, 242425, 242426, 242427, 238732, 238733, 238734, 238735, 238736, 238737, 238738, 238739, 238740, 238741, 238742, 238743, 238744, 238745, 238746, 238747, 238748, 238749, 238750, 238751, 238752, 238753, 238754, 238755, 238756, 238757, 238758, 238759, 238760, 238761, 238762, 238763, 238764, 238765, 238766, 238767, 242428, 242429, 242430, 242431, 242432, 242433, 242434, 242435, 242436, 242437, 242438, 242439, 242440, 242441, 242442, 242443, 237601, 237602, 237603, 237604, 237605, 237606, 242444, 242445, 242446, 242447, 238768, 238769, 238770, 238771, 238772, 238773, 238774, 238775, 238776, 238777, 238778, 238779, 238780, 238781, 238782, 238783, 238784, 238785, 238786, 238787, 238788, 238789, 238790, 238791, 238792, 238793, 238794, 238795, 238796, 238797, 238798, 238799, 238800, 238801, 238802, 238803, 242448, 242449, 242450, 242451, 242452, 242453, 242454, 242455, 242456, 242457, 242458, 242459, 242460, 242461, 242462, 242463, 237607, 237608, 237609, 237610, 237611, 237612, 242464, 242465, 242466, 242467, 238804, 238805, 238806, 238807, 238808, 238809, 238810, 238811,
		238812, 238813, 238814, 238815, 238816, 238817, 238818, 238819, 238820, 238821, 238822, 238823, 238824, 238825, 238826, 238827, 238828, 238829, 238830, 238831, 238832, 238833, 238834, 238835, 238836, 238837, 238838, 238839, 242468, 242469, 242470, 242471, 242472, 242473, 242474, 242475, 242476, 242477, 242478, 242479, 242480, 242481, 242482, 242483, 243758, 243759, 243760, 243761, 243762, 243763, 244019, 244020, 244021, 244022 };

	public _25536DefenceoftheZenzenTerritory() {
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
