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
package quest.ascension;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author Mr. Poke, modified Bobobear
 * @rework FrozenKiller
 */
public class _19071DispatchtoVerteron extends QuestHandler {

	private final static int questId = 19071;

	public _19071DispatchtoVerteron() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(801213).addOnQuestStart(questId);
		qe.registerQuestNpc(801213).addOnTalkEvent(questId);
		qe.registerQuestNpc(203726).addOnTalkEvent(questId);
		qe.registerQuestNpc(203097).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 801213) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203726: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, 210030000, player.getInstanceId(), 1643f, 1500f, 120f);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 203097:
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						default:
							break;
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203097) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
