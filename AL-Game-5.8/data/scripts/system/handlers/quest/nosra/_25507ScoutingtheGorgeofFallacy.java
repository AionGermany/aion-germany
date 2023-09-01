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
public class _25507ScoutingtheGorgeofFallacy extends QuestHandler {

	private final static int questId = 25507;
	int[] mobs = { 240501, 240502, 241683, 241684, 241685, 240503, 240504, 241686, 241687, 241688, 240505, 240506, 241689, 241690, 241691, 238930, 238931, 238932, 238933, 238934, 238935, 242544, 242545, 242546, 242547, 238936, 238937, 238938, 238939, 238940, 238941, 242564, 242565, 242566, 242567, 238942, 238943, 238944, 238945, 238946, 238947, 239219, 239220, 239221, 239222, 239223, 239224, 242584, 242585, 242586, 242587, 242592, 242593, 242594, 242595, 239135, 239136, 239137, 239138, 239139, 239140, 242548, 242549, 242550, 242551, 239141, 239142, 239143, 239144, 239145, 239146, 239147, 239148, 239149, 239150, 239151, 239152, 242552, 242553, 242554, 242555, 239153, 239154, 239155, 239156, 239157, 239158, 242556, 242557, 242558, 242559, 239159, 239160, 239161, 239162, 239163, 239164, 242560, 242561, 242562, 242563, 239165, 239166, 239167, 239168, 239169, 239170, 239171, 239172, 239173, 239174, 239175, 239176, 242568, 242569, 242570, 242571, 239177, 239178, 239179, 239180, 239181, 239182, 239183, 239184, 239185, 239186, 239187, 239188, 242572, 242573, 242574, 242575, 239189, 239190, 239191, 239192, 239193, 239194, 242576, 242577, 242578, 242579, 239195, 239196, 239197, 239198,
		239199, 239200, 242580, 242581, 242582, 242583, 239201, 239202, 239203, 239204, 239205, 239206, 239207, 239208, 239209, 239210, 239211, 239212, 242588, 242589, 242590, 242591, 239213, 239214, 239215, 239216, 239217, 239218, 239225, 239226, 239227, 239228, 239229, 239230, 239231, 239232, 239233, 239234, 239235, 239236, 242596, 242597, 242598, 242599, 242600, 242601, 242602, 242603, 239237, 239238, 239239, 239240, 239241, 239242 };

	public _25507ScoutingtheGorgeofFallacy() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806103).addOnQuestStart(questId); // Vider
		qe.registerQuestNpc(806103).addOnTalkEvent(questId);
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
			if (targetId == 806103) { // Vider
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806103) { // Vider
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
			if (targetId == 806103) { // Vider
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
