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
public class _16823ReunionwithWeda extends QuestHandler {

	public _16823ReunionwithWeda() {
		super(questId);
	}

	private final static int questId = 16823;
	int[] archors = { 220610, 220609, 220608, 220607 };
	int[] pitons = { 220539, 220541 };

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(806284).addOnTalkEvent(questId); // Weda
		qe.registerQuestNpc(806285).addOnTalkEvent(questId); // Weda
		qe.registerOnEnterZone(ZoneName.get("PATH_OF_MEMORY_301550000"), questId);
		qe.registerQuestNpc(220540).addOnKillEvent(questId); // Piton
		for (int mob : archors) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : pitons) {
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
			if (targetId == 806284) { // Weda
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					}
					case SETPRO3: {
						qs.setQuestVar(3);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806285) { // Weda
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
			if (var == 0) {
				if (var1 >= 0 && var1 < 4) {
					return defaultOnKillEvent(env, archors, var1, var1 + 1, 1);
				}
				else if (var1 == 4) {
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 3) {
				if (var1 >= 0 && var1 < 1) {
					return defaultOnKillEvent(env, pitons, var1, var1 + 1, 1);
				}
				else if (var1 == 1) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 4) {
				if (var1 >= 0 && var1 < 0) {
					return defaultOnKillEvent(env, 220540, var1, var1 + 1, 1);
				}
				else if (var1 == 0) {
					qs.setQuestVar(5);
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
			if (var == 1 && zoneName == ZoneName.get("PATH_OF_MEMORY_301550000")) {
				changeQuestStep(env, 1, 2, false);
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
