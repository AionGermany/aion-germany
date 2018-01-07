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
package quest.artefact_of_knowledge;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _26802TheProtectorsOfThe2stHall extends QuestHandler {

	public static final int questId = 26802; // Lv 68

	public _26802TheProtectorsOfThe2stHall() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 806149 };
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(857450).addOnKillEvent(questId);
		qe.registerQuestNpc(857452).addOnKillEvent(questId);
		qe.registerQuestNpc(857454).addOnKillEvent(questId);
		qe.registerQuestNpc(857456).addOnKillEvent(questId);
		qe.registerQuestNpc(857458).addOnKillEvent(questId);
		qe.registerQuestNpc(857459).addOnKillEvent(questId);
		qe.registerQuestNpc(220315).addOnKillEvent(questId);
		qe.registerQuestNpc(220318).addOnKillEvent(questId);
		qe.registerQuestNpc(220324).addOnKillEvent(questId);
		qe.registerQuestNpc(220327).addOnKillEvent(questId);
		qe.registerQuestNpc(220330).addOnKillEvent(questId);
		qe.registerQuestNpc(220306).addOnKillEvent(questId);
		qe.registerQuestNpc(220309).addOnKillEvent(questId);
		qe.registerQuestNpc(220312).addOnKillEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 26801);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		// int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806149) { // Peregran
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
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
				int[] protect2 = { 857450, 857452, 857454, 857456, 857458, 857459 };
				int[] protect = { 220315, 220318, 220324, 220327, 220330, 220306, 220309, 220312 };
				int targetId = env.getTargetId();
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 220306:
					case 220309:
					case 220312:
					case 220315:
					case 220318:
					case 220324:
					case 220327:
					case 220330: {
						if (var1 < 29) {
							return defaultOnKillEvent(env, protect, 0, 29, 1);
						}
						else if (var1 == 29) {
							if (var2 == 2) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, protect, 29, 30, 1);
							}
						}
						break;
					}
					case 857450:
					case 857452:
					case 857454:
					case 857456:
					case 857458:
					case 857459: {
						if (var2 < 1) {
							return defaultOnKillEvent(env, protect2, 0, 1, 2);
						}
						else if (var2 == 1) {
							if (var1 == 30) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, protect2, 1, 2, 2);
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
}
