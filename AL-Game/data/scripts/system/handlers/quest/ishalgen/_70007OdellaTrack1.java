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
package quest.ishalgen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Falke_34
 */
public class _70007OdellaTrack1 extends QuestHandler {

	private final static int questId = 70007;

	public _70007OdellaTrack1() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203553).addOnTalkEvent(questId);
		qe.registerQuestNpc(203554).addOnTalkEvent(questId);
		qe.registerQuestNpc(806813).addOnTalkEvent(questId);
		qe.registerQuestNpc(806812).addOnTalkEvent(questId);
		qe.registerQuestNpc(806811).addOnTalkEvent(questId);
		qe.registerQuestNpc(703485).addOnTalkEvent(questId);
		qe.registerQuestNpc(651783).addOnKillEvent(questId);
		qe.registerQuestNpc(651785).addOnKillEvent(questId);
		qe.registerQuestItem(182216251, questId);
		qe.registerQuestItem(182216252, questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
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
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 806813) {
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
			} 
			else if (targetId == 806811) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SELECT_ACTION_1353:
					return sendQuestDialog(env, 1353);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} 
			else if (targetId == 806812) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case CHECK_USER_HAS_QUEST_ITEM:
					return checkQuestItems(env, 2, 3, false, 10000, 10001);
				default:
					break;
				}
			} 
			else if (targetId == 703485) {
				switch (action) {
				case USE_OBJECT:
					changeQuestStep(env, 0, 4, true);
				default:
					break;
				}
			}
		} 
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806812) {
				switch (action) {
				case USE_OBJECT:
					return sendQuestDialog(env, 10002);
				case SELECT_QUEST_REWARD:
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_NOREWARD:
					return sendQuestEndDialog(env);
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
}
