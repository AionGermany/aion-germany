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
package quest.pandaemonium;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author Falke_34
 */
public class _70100DaevaCertification extends QuestHandler {

	private final static int questId = 70100;
	private final static int[] onTalkNpc = {};

	public _70100DaevaCertification() {
		super(questId);
	}

	@Override
	public void register() {
		for (int npc : onTalkNpc) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(203679).addOnTalkEvent(questId);
		qe.registerQuestNpc(204182).addOnTalkEvent(questId);
		qe.registerQuestNpc(204075).addOnTalkEvent(questId);
		qe.registerQuestNpc(798800).addOnTalkEvent(questId);
		qe.registerQuestNpc(730268).addOnTalkEvent(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			env.setQuestId(questId);
			QuestService.startQuest(env);
		} else if (qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() == 120010000) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 204182) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SELECT_ACTION_1694:
					return sendQuestDialog(env, 1694);
				case SELECT_ACTION_1695:
					return sendQuestDialog(env, 1695);
				case SETPRO3:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 204075) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case SELECT_ACTION_2035:
					return sendQuestDialog(env, 2035);
				case SET_SUCCEED:
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 730268) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case SELECT_ACTION_2035:
					return sendQuestDialog(env, 2035);
				case SET_SUCCEED:
					qs.setQuestVar(4);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					TeleportService2.teleportTo(player, 120020000, 1563.0f, 1424.0f, 265.70996f, (byte) 70, TeleportAnimation.BEAM_ANIMATION);
					return closeDialogWindow(env);
				default:
					break;
				}
			}

		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798800) {
				switch (action) {
				case USE_OBJECT:
					return sendQuestDialog(env, 10002);
				case SELECT_QUEST_REWARD:
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_REWARD1:
				case SELECTED_QUEST_REWARD2:
					return sendQuestEndDialog(env);
				default:
					break;
				}
			}

		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
