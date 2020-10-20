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
public class _60004TheAttitudeToBecomeAGuardian extends QuestHandler {

	private final static int questId = 60004;

	public _60004TheAttitudeToBecomeAGuardian() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820006).addOnTalkEvent(questId); // Kasis
		qe.registerQuestNpc(820134).addOnTalkEvent(questId);
		qe.registerQuestNpc(820135).addOnTalkEvent(questId);
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
		@SuppressWarnings("unused")
		int var = qs.getQuestVarById(0);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 820006) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SELECT_ACTION_1012:
					return sendQuestDialog(env, 1012);
				case SELECT_ACTION_1013:
					return sendQuestDialog(env, 1013);
				case SELECT_ACTION_1034:
					return sendQuestDialog(env, 1034);
				case SELECT_ACTION_1097:
					return sendQuestDialog(env, 1097);
				case SELECT_ACTION_1182:
					return sendQuestDialog(env, 1182);
				case SELECT_ACTION_1183:
					return sendQuestDialog(env, 1183);
				case SELECT_ACTION_1098:
					return sendQuestDialog(env, 1098);
				case SELECT_ACTION_1119:
					return sendQuestDialog(env, 1119);
				case SELECT_QUEST_REWARD:
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 820006) {
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
