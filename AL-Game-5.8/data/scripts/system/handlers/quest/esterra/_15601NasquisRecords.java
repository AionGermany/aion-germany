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
package quest.esterra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _15601NasquisRecords extends QuestHandler {

	public static final int questId = 15601;

	public _15601NasquisRecords() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806158).addOnQuestStart(questId); // Ruabo
		qe.registerQuestNpc(806193).addOnTalkEvent(questId); // Hardens Soul
		qe.registerQuestNpc(703135).addOnTalkEvent(questId); // Pirate Chest
		qe.registerQuestNpc(806158).addOnTalkEvent(questId); // Ruabo
		qe.registerQuestNpc(241150).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("MYSTERIOUS_SHIPWRECK_DISCOVERY_SITE_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("PIRATE_CAVE_210100000"), questId);

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806158) { // Ruabo
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
			if (targetId == 806193) { // Hardens Soul
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SETPRO2: {
						qs.setQuestVar(2);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					case SETPRO5: {
						qs.setQuestVar(5);
						updateQuestStatus(env);

						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 703135) { // Pirate Chest
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 3) {
							return defaultCloseDialog(env, 3, 4);
						}
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806158) { // Ruabo
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
			if (var == 0 && zoneName == ZoneName.get("MYSTERIOUS_SHIPWRECK_DISCOVERY_SITE_210100000")) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			else if (var == 2 && zoneName == ZoneName.get("PIRATE_CAVE_210100000")) {
				changeQuestStep(env, 2, 3, false);
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
			case 241150:
				if (qs.getQuestVarById(1) != 0) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 6);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
