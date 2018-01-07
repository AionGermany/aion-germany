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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author FrozenKiller
 */
public class _14152SpoilerAlert extends QuestHandler {

	private final static int questId = 14152;

	public _14152SpoilerAlert() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204504).addOnQuestStart(questId); // Sofne
		qe.registerQuestNpc(204504).addOnTalkEvent(questId);// Sofne
		qe.registerQuestNpc(204574).addOnTalkEvent(questId); // Finn
		qe.registerQuestNpc(203705).addOnTalkEvent(questId); // Jumentis
		qe.registerQuestNpc(212151).addOnKillEvent(questId); // Chairman Garnis
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204504) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case ASK_QUEST_ACCEPT: {
						return sendQuestDialog(env, 4);
					}
					case QUEST_ACCEPT_1: {
						QuestService.startQuest(env);
						qs.setQuestVarById(5, 1);
						updateQuestStatus(env);
						return sendQuestDialog(env, 1003);
					}
					case QUEST_REFUSE_1: {
						return sendQuestDialog(env, 1004);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204574) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1352);
					}
					case SETPRO1: {
						qs.setQuestVarById(5, 2);
						updateQuestStatus(env);
						giveQuestItem(env, 182215481, 1);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 203705) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1693);
					}
					case SETPRO2: {
						removeQuestItem(env, 182215481, 1);
						qs.setQuestVarById(5, 0);
						qs.setQuestVarById(0, 0);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204504) {
				int var = qs.getQuestVarById(0);
				if (env.getDialog() == DialogAction.USE_OBJECT && var == 1) {
					return sendQuestDialog(env, 2375);
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
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (targetId == 212151 && var == 0) { // Chairman Garnis
			qs.setQuestVarById(0, 1);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
