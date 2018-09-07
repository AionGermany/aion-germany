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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _26821TheLostPeregran extends QuestHandler {

	public _26821TheLostPeregran() {
		super(questId);
	}

	private final static int questId = 26821;
	int[] mobs = { 220476, 220474, 220475, 220477, 220479, 220467, 220465, 220466, 220469, 220459, 220460, 220462, 220458, 220621, 220619, 220620, 220622, 220627, 220628, 220629, 220630, 220626, 220631, 220632, 220633, 220635, 220636, 220637, 220638, 220670, 220676, 220675, 220674, 220671 };

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(806286).addOnTalkEvent(questId); // Zangrik
		qe.registerQuestNpc(806427).addOnTalkEvent(questId); // Bregat
		qe.registerQuestNpc(220587).addOnTalkEvent(questId); // Liberated Earth Jotun
		qe.registerOnEnterZone(ZoneName.get("REFUGE_OF_RESTRAINT_301550000"), questId);
		qe.registerQuestNpc(220480).addOnKillEvent(questId); // Fallen Jotun Warrior
		qe.registerQuestNpc(220526).addOnKillEvent(questId); // All-Seeing Eye
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (player.getWorldId() == 301550000) {
				QuestService.startQuest(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 806286) { // Zangrik
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					}
					case SETPRO1: {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 806427) { // Bregat
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO2: {
						qs.setQuestVar(2);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 220587) { // Liberated Earth Jotun
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
			int var1 = qs.getQuestVarById(1);
			if (var == 2) {
				if (var1 >= 0 && var1 < 34) {
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 34) {
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 3) {
				if (var1 >= 0 && var1 < 0) {
					return defaultOnKillEvent(env, 220480, var1, var1 + 1, 1);
				}
				else if (var1 == 0) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 5) {
				if (var1 >= 0 && var1 < 0) {
					return defaultOnKillEvent(env, 220526, var1, var1 + 1, 1);
				}
				else if (var1 == 0) {
					qs.setQuestVar(6);
					qs.setStatus(QuestStatus.REWARD); // reward
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3 && zoneName == ZoneName.get("REFUGE_OF_RESTRAINT_301550000")) { // Check
				changeQuestStep(env, 3, 4, false);
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("REFUGE_OF_RESTRAINT_301550000")) {
				changeQuestStep(env, 4, 5, false);
				return true;
			}
		}
		return false;
	}
}
