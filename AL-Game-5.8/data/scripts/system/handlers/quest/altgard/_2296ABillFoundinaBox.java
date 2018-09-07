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
package quest.altgard;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Majka Ajural
 */
public class _2296ABillFoundinaBox extends QuestHandler {

	private final static int questId = 2296;
	private final static int questNpc1Id = 203656; // Urnir
	private final static int questNpc2Id = 798036; // Mabrunerk
	private final static int questStartItemId = 182203263; // A Bill Found in a Box

	public _2296ABillFoundinaBox() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(questNpc1Id).addOnTalkEvent(questId); // Urnir
		qe.registerQuestNpc(questNpc2Id).addOnTalkEvent(questId); // Mabrunerk
		qe.registerQuestItem(questStartItemId, questId); // A Bill Found in a Box
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (targetId == 0) {
			if (env.getDialog() == DialogAction.QUEST_ACCEPT_1) {
				QuestService.startQuest(env);
				closeDialogWindow(env);
			}

			if (env.getDialog() == DialogAction.QUEST_REFUSE_1) {
				closeDialogWindow(env);
			}
		}
		else if (targetId == questNpc1Id) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1); // 1
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == questNpc2Id) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					removeQuestItem(env, questStartItemId, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
			}
			else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();

		if (id != questStartItemId) {
			return HandlerResult.UNKNOWN;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			sendQuestDialog(env, 4);
		}
		return HandlerResult.SUCCESS;
	}
}
