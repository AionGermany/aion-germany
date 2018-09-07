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
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _25608SugarSweet extends QuestHandler {

	public static final int questId = 25608;

	public _25608SugarSweet() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(805964).addOnQuestStart(questId); // Vindar
		qe.registerQuestNpc(805964).addOnTalkEvent(questId); // Vindar
		qe.registerQuestNpc(806177).addOnTalkEvent(questId); // Monis
		qe.registerQuestNpc(806197).addOnTalkEvent(questId); // Zezen Beekeeper
		qe.registerQuestNpc(241235).addOnKillEvent(questId); // Kurubi Laborer
		qe.registerOnEnterZone(ZoneName.get("SAPHORA_FOREST_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ISLAND_OF_FREEDOM_210100000"), questId); // Nedd Teritory cucurro

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 805964) { // Vindar
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
			if (targetId == 806177) { // Monis
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
			if (targetId == 806197) { // Zezen Beekeeper
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
			if (targetId == 805964) { // Zezen Beekeeper
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 6, 7, true, 10000, 10001);
					}

					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805964) { // Vindar
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
			int time = GameTimeManager.getGameTime().getHour();

			if (var == 2 && zoneName == ZoneName.get("SAPHORA_FOREST_220110000") && time > 21) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
			if (var == 5 && zoneName == ZoneName.get("SAPHORA_FOREST_220110000") && time > 21) {
				changeQuestStep(env, 5, 6, false);
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
			case 241235:
				if (qs.getQuestVarById(1) != 9) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 4);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
