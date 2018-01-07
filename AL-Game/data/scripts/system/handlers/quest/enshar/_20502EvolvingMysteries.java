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
package quest.enshar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author pralinka
 */
public class _20502EvolvingMysteries extends QuestHandler {

	public static final int questId = 20502;

	public _20502EvolvingMysteries() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804723, 804724, 804725, 804726, 804727 };
		qe.registerOnEnterWorld(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 20501, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804723) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					case SETPRO1:
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804724) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					case SETPRO2:
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804725) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					case SETPRO3:
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					case CHECK_USER_HAS_QUEST_ITEM:
						if (QuestService.collectItemCheck(env, true)) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10000);
						}
						else {
							return sendQuestDialog(env, 10001);
						}
					case SETPRO5:
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804726) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					case SETPRO6:
						giveQuestItem(env, 182215638, 1);
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804727) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					case SET_SUCCEED:
						removeQuestItem(env, 182215638, 1);
						qs.setQuestVar(7);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804723) {
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
}
