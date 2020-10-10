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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Falke_34
 * @author FrozenKiller
 */
public class _70003ArchonSoldierExam extends QuestHandler {

	private final static int questId = 70003;

	public _70003ArchonSoldierExam() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806814).addOnTalkEvent(questId); // Marko
		qe.registerQuestNpc(856022).addOnTalkEvent(questId);
		qe.registerQuestNpc(820134).addOnTalkEvent(questId);
		qe.registerQuestNpc(820135).addOnTalkEvent(questId);
		qe.registerQuestNpc(836532).addOnAttackEvent(questId);
		qe.registerQuestNpc(836533).addOnAttackEvent(questId);
		qe.registerQuestItem(182216395, questId);
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
	public boolean onAttackEvent(QuestEnv env) {
		Player player = env.getPlayer();
		@SuppressWarnings("unused")
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (targetId == 836532) {
			giveQuestItem(env, 182216395, 1);
			changeQuestStep(env, 0, 1, false);
			return true;
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
			if (targetId == 806814) {
				int var = qs.getQuestVarById(0);
				if (var == 0) {
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
				} else if (var == 1) {
					switch (action) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1352);
					case CHECK_USER_HAS_QUEST_ITEM:
						qs.setStatus(QuestStatus.REWARD);
						return checkQuestItems(env, 1, 2, false, 10000, 10001);
					default:
						break;
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806814) {
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

}