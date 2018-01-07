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
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 */
public class _20501WhatTheRuinsSay extends QuestHandler {

	public static final int questId = 20501;

	public _20501WhatTheRuinsSay() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804720, 804721, 731538, 731539, 731540, 804722 };
		qe.registerOnEnterZone(ZoneName.get("DREG_FINDSPOT_220080000"), questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 20500, true);
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final Npc npc = (Npc) env.getVisibleObject();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804720) {
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
			if (targetId == 804721) {
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
			if (targetId == 731538) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					case SETPRO4:
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 731539) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					case SETPRO5:
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 731540) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					case SETPRO6:
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804722) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					case SET_SUCCEED:
						QuestService.addNewSpawn(220080000, 1, 219935, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						changeQuestStep(env, 6, 6, true);
						return closeDialogWindow(env);
					default:
						break;
				}
			}

		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804721) {
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
