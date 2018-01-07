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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _11227EasyAs_4_3_2_1 extends QuestHandler {

	private final static int questId = 11227;
	private final static int[] mob_ids = { 217068, 217069, 217070, 217071 };

	public _11227EasyAs_4_3_2_1() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799076).addOnQuestStart(questId);
		qe.registerQuestNpc(799076).addOnTalkEvent(questId);
		for (int mob_id : mob_ids) {
			qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 799076) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
				if (env.getDialogId() == 26) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
			else if (qs.getStatus() == QuestStatus.START) {
				if (env.getDialogId() == 26) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 4);
				}
			}
			else if (qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		Npc npc = null;
		if (env.getVisibleObject() instanceof Npc) {
			npc = (Npc) env.getVisibleObject();
			targetId = npc.getNpcId();
		}
		switch (targetId) {
			case 217071:
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			case 217070:
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			case 217069:
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			case 217068:
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
		}
		return false;
	}
}
