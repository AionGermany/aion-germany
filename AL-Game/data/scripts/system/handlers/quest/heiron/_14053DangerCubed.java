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
package quest.heiron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _14053DangerCubed extends QuestHandler {

	private final static int questId = 14053;

	public _14053DangerCubed() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204602).addOnTalkEvent(questId); // Atalante
		qe.registerQuestNpc(204020).addOnTalkEvent(questId); // Mabangtah
		qe.registerQuestNpc(212614).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204602) { // Atalante
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO1: {
						changeQuestStep(env, 0, 1, false);
						TeleportService2.teleportTo(player, 210020000, 1598f, 1529f, 317.412f, (byte) 118, TeleportAnimation.BEAM_ANIMATION);
						return closeDialogWindow(env);
					}
					case SETPRO3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 3, 4, false, 10000, 10001); // 4
					}
					case FINISH_DIALOG: {
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 204020) { // Mabangtah
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO2: {
						changeQuestStep(env, 1, 2, false);
						TeleportService2.teleportTo(player, 210040000, 2013f, 1397f, 118f, (byte) 59, TeleportAnimation.BEAM_ANIMATION);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204602) { // Atalante
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		if (var == 4) {
			return defaultOnKillEvent(env, 212614, 4, true); // REWARD
		}
		else {
			return false;
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 14050, true);
	}
}
