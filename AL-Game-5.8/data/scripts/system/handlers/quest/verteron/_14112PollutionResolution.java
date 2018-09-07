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
package quest.verteron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author FrozenKiller
 */

public class _14112PollutionResolution extends QuestHandler {

	private final static int questId = 14112;

	public _14112PollutionResolution() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203149).addOnQuestStart(questId);
		qe.registerQuestNpc(203149).addOnTalkEvent(questId); // Geolus
		qe.registerQuestNpc(203148).addOnTalkEvent(questId); // Lepios
		qe.registerQuestNpc(210318).addOnKillEvent(questId); // Poisonus Bubblegut
		qe.registerQuestNpc(203195).addOnKillEvent(questId); // Kato
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203149) { // Geolus
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, -1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 203148) { // Lepios
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO1: {
						qs.setQuestVar(0);
						updateQuestStatus(env);
						giveQuestItem(env, 182215455, 1);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 203195) { // Kato
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SELECT_QUEST_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203195) { // Kato
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					removeQuestItem(env, 182215455, 1);
					return sendQuestDialog(env, 4080);
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
				int targetId = env.getTargetId();
				if (targetId == 210318) {
					QuestService.addNewSpawn(210030000, player.getInstanceId(), 203195, player.getX() + 2, player.getY() + 2, player.getZ() + 1, (byte) 0);
					return defaultOnKillEvent(env, 210318, 0, 1); // 1
				}
			}
		}
		return false;
	}
}
