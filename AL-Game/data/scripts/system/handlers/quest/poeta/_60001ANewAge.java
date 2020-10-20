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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author QuestGenerator by Mariella
 */
public class _60001ANewAge extends QuestHandler {

	private final static int questId = 60001;

	public _60001ANewAge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(790001).addOnTalkEvent(questId); // Pernos
		qe.registerQuestNpc(820134).addOnTalkEvent(questId); // Guide to Rare Poisons
		qe.registerQuestNpc(820135).addOnTalkEvent(questId); // Day of the Storm
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
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 790001) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SELECT_ACTION_1012:
					return sendQuestDialog(env, 1012);
				case SETPRO1:
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820134) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SELECT_ACTION_1353:
					return sendQuestDialog(env, 1353);
				case SELECT_ACTION_1354:
					return sendQuestDialog(env, 1354);
				case SELECT_ACTION_1355:
					return sendQuestDialog(env, 1355);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820135) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SELECT_ACTION_1694:
					return sendQuestDialog(env, 1694);
				case SELECT_ACTION_1695:
					return sendQuestDialog(env, 1695);
				case SELECT_ACTION_1696:
					return sendQuestDialog(env, 1696);
				case SELECT_ACTION_1697:
					return sendQuestDialog(env, 1697);
				case SELECT_ACTION_1698:
					return sendQuestDialog(env, 1698);
				case SET_SUCCEED:
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 790001) {
				switch (action) {
				case USE_OBJECT:
					return sendQuestDialog(env, 10002);
				case SELECT_QUEST_REWARD:
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_NOREWARD:
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
