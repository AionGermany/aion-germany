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
package quest.brusthonin;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author FrozenKiller
 */

public class _24203SecretofContamination extends QuestHandler {

	private final static int questId = 24203;

	public _24203SecretofContamination() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205150).addOnQuestStart(questId);
		qe.registerQuestNpc(205150).addOnTalkEvent(questId); // Surt
		qe.registerQuestNpc(205192).addOnTalkEvent(questId); // Sahnu
		qe.registerQuestNpc(205155).addOnTalkEvent(questId); // Heintz
		qe.registerQuestNpc(204057).addOnTalkEvent(questId); // Sigyn
		qe.registerQuestNpc(214700).addOnKillEvent(questId); // Suspicious Boy
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205150) { // Surt
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 205150) { // Surt
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case SETPRO1: {
						return defaultCloseDialog(env, 0, 1);
					}
					default:
						break;
				}
			}
			else if (targetId == 205192) { // Sahnu
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO2: {
						TeleportService2.teleportTo(player, 220040000, 569.95056f, 2845.857f, 231.00928f, (byte) 114, TeleportAnimation.BEAM_ANIMATION);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						if (var == 2) {
							return checkQuestItems(env, 2, 3, false, 10000, 10001); // 3
						}
					}
					case FINISH_DIALOG: {
						if (var == 3) {
							return defaultCloseDialog(env, 3, 3); // 3
						}
					}
					case SETPRO4: {
						if (var == 3) {
							return defaultCloseDialog(env, 3, 4); // 4
						}
					}
					default:
						break;
				}
			}
			else if (targetId == 205155) { // Heintz
				if (var == 5) {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2716);
						}
						case SETPRO6: {
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204057) { // Sigyn
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					default: {
						return sendQuestEndDialog(env);
					}
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

		if (targetId == 214700) {
			if (var == 4) {
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
