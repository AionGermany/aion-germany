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
public class _26822TheEarthJotun extends QuestHandler {

	public _26822TheEarthJotun() {
		super(questId);
	}

	private final static int questId = 26822;

	@Override
	public void register() {
		qe.registerQuestNpc(220588).addOnTalkEvent(questId); // Fighting Earth Jotun
		qe.registerQuestNpc(220590).addOnTalkEvent(questId); // Earth Jotun
		qe.registerQuestNpc(806288).addOnTalkEvent(questId); // Stiget
		qe.registerOnEnterZone(ZoneName.get("ALRAS_GARDEN_1_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("CONNECTING_HALL_IN_THE_LIBRARY_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("VIDS_HIDDEN_LIBRARY_1_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("INTERIOR_OF_THE_ALL-KNOWING_TREE_301550000"), questId);
		qe.registerQuestNpc(220534).addOnKillEvent(questId); // Fallen Water Jotun
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
			if (targetId == 220588) { // Fighting Earth Jotun
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
			else if (targetId == 220590) { // Earth Jotun
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO4: {
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806288) { // Stiget
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
			if (var == 5) {
				if (var1 >= 0 && var1 < 0) {
					return defaultOnKillEvent(env, 220534, var1, var1 + 1, 1);
				}
				else if (var1 == 0) {
					qs.setQuestVar(6);
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
			if (var == 0 && zoneName == ZoneName.get("ALRAS_GARDEN_1_301550000")) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			if (var == 2 && zoneName == ZoneName.get("CONNECTING_HALL_IN_THE_LIBRARY_301550000")) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
			if (var == 4 && zoneName == ZoneName.get("VIDS_HIDDEN_LIBRARY_1_301550000")) {
				changeQuestStep(env, 4, 5, false);
				return true;
			}
			if (var == 6 && zoneName == ZoneName.get("INTERIOR_OF_THE_ALL-KNOWING_TREE_301550000")) {
				changeQuestStep(env, 6, 7, false);
				qs.setStatus(QuestStatus.REWARD); // reward
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
