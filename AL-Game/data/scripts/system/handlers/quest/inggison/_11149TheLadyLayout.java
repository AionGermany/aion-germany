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
package quest.inggison;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _11149TheLadyLayout extends QuestHandler {

	private final static int questId = 11149;

	public _11149TheLadyLayout() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(296491).addOnQuestStart(questId);
		qe.registerQuestNpc(296491).addOnTalkEvent(questId);
		qe.registerQuestNpc(206155).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(206156).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(206157).addOnAtDistanceEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 296491) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 296491) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(1);
			int var1 = qs.getQuestVarById(2);
			int var2 = qs.getQuestVarById(3);
			if (targetId == 206155 && var == 0) {
				qs.setQuestVarById(1, 1);
				updateQuestStatus(env);
				cheakReward(env);
				return true;
			}
			else if (targetId == 206156 && var1 == 0) {
				qs.setQuestVarById(2, 1);
				updateQuestStatus(env);
				cheakReward(env);
				return true;
			}
			else if (targetId == 206157 && var2 == 0) {
				qs.setQuestVarById(3, 1);
				updateQuestStatus(env);
				cheakReward(env);
				return true;
			}
		}
		return false;
	}

	private void cheakReward(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(1);
		int var1 = qs.getQuestVarById(2);
		int var2 = qs.getQuestVarById(3);
		if (var == 1 && var1 == 1 && var2 == 1) {
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}
	}
}
