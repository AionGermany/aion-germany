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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _25606AWomanSadMemories extends QuestHandler {

	public static final int questId = 25606;

	public _25606AWomanSadMemories() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806174).addOnQuestStart(questId); // Shardeil
		qe.registerQuestNpc(806178).addOnTalkEvent(questId); // Wandering Soul
		qe.registerQuestNpc(806156).addOnTalkEvent(questId); // Amnesiac Woman
		qe.registerQuestNpc(806157).addOnTalkEvent(questId); // Lini
		qe.registerQuestNpc(241220).addOnKillEvent(questId); // Ghost
		qe.registerOnEnterZone(ZoneName.get("SEA_OF_LIFE_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("HEROES_SQUARE_220110000"), questId);

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806174) { // Shardeil
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 806178) { // Wandering Soul
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
			else if (targetId == 806156) { // Amnesiac Woman
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
			else if (targetId == 806157) { // Lini
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					}
					case SETPRO7: {
						qs.setQuestVar(7);
						updateQuestStatus(env);

						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806174) { // Shardeil
				if (dialog == DialogAction.USE_OBJECT) {
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
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0 && zoneName == ZoneName.get("SEA_OF_LIFE_220110000")) {
				changeQuestStep(env, 0, 1, false);
				QuestService.addNewSpawn(220110000, 1, 806178, (float) 633.5589, (float) 1348.8346, (float) 287.25, (byte) 4);
				return true;
			}
			else if (var == 2 && zoneName == ZoneName.get("SEA_OF_LIFE_220110000")) { // TODO Original Zone
				changeQuestStep(env, 2, 3, false);
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("SEA_OF_LIFE_220110000")) { // TODO Original Zone
				changeQuestStep(env, 4, 5, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 241220:
				if (qs.getQuestVarById(1) != 0) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVar(6);
					// qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
