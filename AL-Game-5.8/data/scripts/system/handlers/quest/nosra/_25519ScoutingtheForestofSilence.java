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
public class _25519ScoutingtheForestofSilence extends QuestHandler {

	private final static int questId = 25519;
	int[] mobs = { 240537, 240538, 241743, 241744, 241745, 240539, 240540, 241746, 241747, 241748, 240779, 240780, 241749, 241750, 241751, 239008, 239009, 239010, 239011, 239012, 242804, 242805, 242806, 242807, 239013, 239014, 239015, 239016, 239017, 242824, 242825, 242826, 242827, 239018, 239019, 239020, 239021, 239022, 242844, 242845, 242846, 242847, 239603, 239604, 239605, 239606, 239607, 242808, 242809, 242810, 242811, 239608, 239609, 239610, 239611, 239612, 239613, 239614, 239615, 239616, 239617, 242812, 242813, 242814, 242815, 239618, 239619, 239620, 239621, 239622, 242816, 242817, 242818, 242819, 239623, 239624, 239625, 239626, 239627, 242820, 242821, 242822, 242823, 239628, 239629, 239630, 239631, 239632, 239633, 239634, 239635, 239636, 239637, 242828, 242829, 242830, 242831, 239638, 239639, 239640, 239641, 239642, 239643, 239644, 239645, 239646, 239647, 242832, 242833, 242834, 242835, 239648, 239649, 239650, 239651, 239652, 242836, 242837, 242838, 242839, 239653, 239654, 239655, 239656, 239657, 242840, 242841, 242842, 242843, 239658, 239659, 239660, 239661, 239662, 239663, 239664, 239665, 239666, 239667, 242848, 242849, 242850, 242851, 239668, 239669, 239670, 239671,
		239672, 239673, 239674, 239675, 239676, 239677, 242852, 242853, 242854, 242855, 239678, 239679, 239680, 239681, 239682, 242856, 242857, 242858, 242859, 239683, 239684, 239685, 239686, 239687, 242860, 242861, 242862, 242863, 239688, 239689, 239690, 239691, 239692 };

	public _25519ScoutingtheForestofSilence() {
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
